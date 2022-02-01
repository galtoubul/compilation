package AST;

import java.util.Optional;

import IR.IR;
import IR.IRcommand_Return;
import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeEntry;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_VOID;

public class AST_STMT_RETURN extends AST_STMT {
    public Optional<AST_EXP> exp;
    String funcName;

    public AST_STMT_RETURN(Optional<AST_EXP> exp) {
        this.exp = exp;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************************/
    /* The printing message for a function declaration AST node */
    /************************************************************/
    public void PrintMe() {
        /*************************************/
        /* AST NODE TYPE = AST SUBSCRIPT VAR */
        /*************************************/
        System.out.print("AST NODE STMT RETURN EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT exp ... */
        /*****************************/
        if (exp.isPresent()) {
            exp.get().PrintMe();
        }

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "RETURN EXP");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp.isPresent()) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.get().SerialNumber);
        }
    }

    @Override
    public TYPE SemantMe(Optional<String> classId, int localVarIndex) {
        // It is guaranteed syntacticly thet return statements are only present in
        // funciton declarations
        ScopeEntry scope = SYMBOL_TABLE.getInstance().findScopeType(ScopeType.Function).get();
        TYPE returnType = ((TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(scope.scopeName.get())).returnType;
        funcName = scope.scopeName.orElse("no_name");
        Optional<ScopeEntry> classScope = SYMBOL_TABLE.getInstance().findScopeType(ScopeType.Class);
        if (classScope.isPresent()) {
            // Update the function name to bear the class name
            funcName += "_" + classScope.get().scopeName.get();
        }

        if (this.exp.isPresent()) {
            if (returnType == TYPE_VOID.getInstance()) {
                System.out.format(">> ERROR [%d:%d] cannot return a value, since the return type is void\n", 2, 2);
                throw new SemanticErrorException("" + lineNum);
            }
            TYPE valueType = this.exp.get().SemantMe(classId);
            if (!TYPE.isSubtype(valueType, returnType)) {
                System.out.format(">> ERROR [%d:%d] %s must return %s\n", 2, 2, scope.scopeName, returnType.name);
                throw new SemanticErrorException("" + lineNum);
            }
        } else {
            if (returnType != TYPE_VOID.getInstance()) {
                System.out.format(">> ERROR [%d:%d] %s must return %s\n", 2, 2, scope.scopeName, returnType.name);
                throw new SemanticErrorException("" + lineNum);
            }
        }
        return null;
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_STMT_RETURN IRme");
        IR.getInstance().Add_IRcommand(new IRcommand_Return(this.exp.map(AST_EXP::IRme), funcName));
    }
}
