package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_VOID;
import AstNotationType.AstNotationType;
import TEMP.*;
import AstAnnotation.*;
import GlobalVariables.*;
import IR.*;


public class AST_VAR_NEW extends AST_VAR_DEC {

    public AST_NEW_EXP initialValue;
    public AstAnnotation astAnnotation;
    public String varType;

    public AST_VAR_NEW(AST_TYPE type, String id, AST_NEW_EXP initialValue) {
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

    @Override
    public TYPE SemantMe(Optional<String> classId, Optional<Integer> localVarIndexOpt) {
        System.out.println("-- AST_VAR_NEW SemantMe");

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
        if (!initialValue.subscript.isPresent() && !TYPE.isSubtype(tInitial, t)) {
            System.out.format(">> ERROR [%d:%d] type mismatch\n", 2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        // Validate arrays
        if (initialValue.subscript.isPresent()
                && (!t.isArray() || (t.isArray() && !TYPE.isSubtype(tInitial, ((TYPE_ARRAY) t).type)))) {
            System.out.format(">> ERROR [%d:%d] type mismatch: type %s is not an array of %s\n",
                    2, 2, t.name, tInitial.name);
            throw new SemanticErrorException("" + lineNum);
        }

        // If the initialization is of a class field, it must be initialized with a
        // constant; and a new-expression is not a constant
        if (SYMBOL_TABLE.getInstance().currentScopeType() == ScopeType.Class) {
            System.out.format(">> ERROR [%d:%d] classs fields can only be initialized with constant values\n", 2, 2, id);
            throw new SemanticErrorException("" + lineNum);
        }

        AstNotationType astNotationType = AstNotationType.localVariable;
        Optional<AstNotationType> astNotationTypeOpt = Optional.of(astNotationType);
        SYMBOL_TABLE.getInstance().enter(id, t, false, localVarIndexOpt, astNotationTypeOpt);

        TYPE_CLASS_VAR_DEC typeClassVarDec = new TYPE_CLASS_VAR_DEC(t, id);
        this.varType = typeClassVarDec.type.name;

        setNotation(localVarIndexOpt);

        return typeClassVarDec;
    }

    private void setNotation(Optional<Integer> localVarInd) {
        System.out.println("-- AST_VAR_NEW setNotation");

        ScopeType scopeType = SYMBOL_TABLE.getInstance().currentScopeType();
        if (scopeType == scopeType.Global) {
            astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, localVarInd);
            System.out.format("\t\t%s is a global variable\n", id);
        }
        else { // local
            astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, localVarInd);
            int ind = localVarInd.orElse(-1);
            System.out.format("\t\t%s is a local variable | its index = %s\n", id, ind);
        }
    }

    public TEMP IRme() {
        System.out.println("-- AST_VAR_NEW IRme");

        TEMP rValueTmp = initialValue.IRme();

        if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR){
            System.out.println("\t\tglobal variable");

            String globalVarLabel = GlobalVariables.insertGlobal(id, varType);
            IR.getInstance().Add_IRcommand(new IRcommand_Create_Global_Var(globalVarLabel, "new", rValueTmp));
        }

        else { // local variable
            System.out.println("\t\tlocal variable");

            int localVarInd = astAnnotation.ind.orElse(-1);
            IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Local_Var(localVarInd, rValueTmp));
        }

        return null;
    }
}
