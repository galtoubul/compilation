package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_VOID;
import AstNotationType.AstNotationType;
import IR.*;
import TEMP.*;


public class AST_VAR_INITIALIZATION extends AST_VAR_DEC {
    public AST_EXP initialValue;

    public AST_VAR_INITIALIZATION(AST_TYPE type, String id, AST_EXP initialValue) {
        super(type, id);
        this.initialValue = initialValue;
    }

    /********************************************************/
    /* The printing message for a declaration list AST node */
    /********************************************************/
    public void PrintMe() {
        /********************************/
        /* AST NODE TYPE = AST DEC LIST */
        /********************************/
        if (initialValue != null)
            System.out.format("VAR-DEC(%s):%s := initialValue\n", id, type.name());
        if (initialValue == null)
            System.out.format("VAR-DEC(%s):%s                \n", id, type.name());

        /**************************************/
        /* RECURSIVELY PRINT initialValue ... */
        /**************************************/
        if (initialValue != null)
            initialValue.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR\nDEC(%s)\n:%s", id, type.name()));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (initialValue != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, initialValue.SerialNumber);

    }

    public TEMP IRme() {
        System.out.println("-- AST_VAR_INITIALIZATION IRme");
        IR.getInstance().Add_IRcommand(new IRcommand_Allocate(id));
        IR.getInstance().Add_IRcommand(new IRcommand_Store(id,initialValue.IRme()));
        return null;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId, Optional<Integer> localVarIndexOpt) {
        System.out.println("-- AST_VAR_INITIALIZATION SemantMe");

        // Check If Type exists
        // Optional<SymbolTableEntry> entry =
        // SYMBOL_TABLE.getInstance().findEntry(type.name());
        // if (!entry.isPresent()) {
        // System.out.format(">> ERROR [%d] non existing type '%s'\n", lineNum,
        // type.name());
        // throw new SemanticErrorException(String.valueOf(lineNum));
        // }
        // if (!entry.get().isType) {
        // System.out.format(">> ERROR [%d] '%s' is not a type\n", lineNum,
        // type.name());
        // throw new SemanticErrorException(String.valueOf(lineNum));
        // }

        TYPE t = this.type.getTYPE(lineNum);

        // Check that the type isn't void
        if (t == TYPE_VOID.getInstance()) {
            System.out.format(">> ERROR [%d:%d] cannot declare a variable as void\n", 2, 2, this.type.name());
            throw new SemanticErrorException("" + lineNum);
        }

        // Check that id does NOT exist at the same scope
        if (SYMBOL_TABLE.getInstance().isInScope(id)) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",
                    2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        /******************************************************/
        /* [3] Match the variable type with the initial value */
        /******************************************************/
        TYPE tInitial = initialValue.SemantMe(classId);
        if (!TYPE.isSubtype(tInitial, t)) {
            System.out.format(">> ERROR [%d:%d] type mismatch\n", 2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        // If the initialization is of a class field, it must be initialized with a
        // constant
        if (SYMBOL_TABLE.getInstance().currentScopeType() == ScopeType.Class &&
                !(initialValue instanceof AST_EXP_INT) &&
                !(initialValue instanceof AST_EXP_STRING) &&
                !(initialValue instanceof AST_EXP_NIL)) {
            System.out.format(">> ERROR [%d:%d] class fields can only be initialized with constant values\n", 2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        /***************************************************/
        /* [4] Enter the variable name to the Symbol Table */
        /***************************************************/
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
        String callerClassName = e.getClassName();
        System.out.println("\t\tcaller's class = " + callerClassName);

        AstNotationType astNotationType = callerClassName == "AST.AST_STMT_VAR_DEC" ? AstNotationType.localVariable : AstNotationType.globalVariable;
        Optional<AstNotationType> astNotationTypeOpt = Optional.of(astNotationType);
        System.out.println("\t\tlocalVarIndexOpt = " + localVarIndexOpt + " | astNotationTypeOpt = " + astNotationTypeOpt);
        SYMBOL_TABLE.getInstance().enter(id, t, false, localVarIndexOpt, astNotationTypeOpt);

        return new TYPE_CLASS_VAR_DEC(t, id);
    }
}
