package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_VOID;
import AstNotationType.*;
import IR.*;
import TEMP.*;
import GlobalVariables.*;
import AstAnnotation.*;


public class AST_VAR_INITIALIZATION extends AST_VAR_DEC {
    public AST_EXP initialValue;
    String id;
    String varType;
    AstAnnotation astAnnotation;

    public AST_VAR_INITIALIZATION(AST_TYPE type, String id, AST_EXP initialValue) {
        super(type, id);
        this.initialValue = initialValue;
        this.id = id;
    }

    public void PrintMe() {
        if (initialValue != null)
            System.out.format("VAR-DEC(%s):%s := initialValue\n", id, type.name());
        if (initialValue == null)
            System.out.format("VAR-DEC(%s):%s                \n", id, type.name());

        if (initialValue != null)
            initialValue.PrintMe();

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR\nDEC(%s)\n:%s", id, type.name()));

        if (initialValue != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, initialValue.SerialNumber);
    }

    @Override
    public TYPE SemantMe(Optional<String> classId, Optional<Integer> localVarIndexOpt) {
        System.out.println("-- AST_VAR_INITIALIZATION SemantMe");

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
        String callerClassName = (Thread.currentThread().getStackTrace())[2].getClassName();
        System.out.println("\t\tcaller's class = " + callerClassName);

        AstNotationType astNotationType = AstNotationType.getAstNotationType(callerClassName);
        Optional<AstNotationType> astNotationTypeOpt = Optional.of(astNotationType);

        TYPE_CLASS_VAR_DEC typeClassVarDec = new TYPE_CLASS_VAR_DEC(t, id);
        SYMBOL_TABLE.getInstance().enter(id, t, false, localVarIndexOpt, astNotationTypeOpt);
        this.varType = typeClassVarDec.type.name;
        setNotation(localVarIndexOpt);
        return typeClassVarDec;
    }

    private void setNotation(Optional<Integer> localVarInd) {
        System.out.println("-- AST_VAR_INITIALIZATION setNotation");

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
        System.out.format("-- AST_VAR_INITIALIZATION IRme\n\t\tid = %s\n", id);

        if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR){
            System.out.println("\t\tglobal variable");

            String globalVarLabel = GlobalVariables.insertGlobal(this.id, this.varType);

            IRcommand_Create_Global_Var irCmd;
            if (initialValue instanceof AST.AST_EXP_INT) {
                irCmd = new IRcommand_Create_Global_Var(globalVarLabel, varType, ((AST.AST_EXP_INT)initialValue).value);
            }
            else if (initialValue instanceof AST.AST_EXP_NIL){
                irCmd = new IRcommand_Create_Global_Var(globalVarLabel, varType, null);
            }
            else { // instanceof AST.AST_EXP_STRING
                irCmd = new IRcommand_Create_Global_Var(globalVarLabel, varType, ((AST.AST_EXP_STRING)initialValue).s);
            }
            IR.getInstance().Add_IRcommand(irCmd);
        }

        else { // local variable
            System.out.println("\t\tlocal variable");

            if (initialValue instanceof AST.AST_EXP_STRING) {
                System.out.println("\t\tis string");
            }
            TEMP rValueTmp = initialValue.IRme();
            int localVarInd = astAnnotation.ind.orElse(-1);
            IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Local_Var(localVarInd, rValueTmp));
        }

        return null;
    }
}
