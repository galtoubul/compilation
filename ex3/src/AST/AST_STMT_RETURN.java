package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SYMBOL_TABLE_ENTRY;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_FOR_SCOPE_BOUNDARIES;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_VOID;

public class AST_STMT_RETURN extends AST_STMT {
    public Optional<AST_EXP> exp;

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

    public TYPE SemantMe(Optional<String> classId) {
        // It is guaranteed syntacticly thet return statements are only present in
        // funciton declarations
        TYPE_FOR_SCOPE_BOUNDARIES scope = SYMBOL_TABLE.getInstance().findScopeType(ScopeType.Function).get();
        TYPE returnType = ((TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(scope.scopeName)).returnType;

        if (this.exp.isPresent()) {
            if (returnType == TYPE_VOID.getInstance()) {
                System.out.format(">> ERROR [%d:%d] cannot return a value, since the return type is void\n", 2, 2);
                System.exit(0);
            }
            TYPE valueType = this.exp.get().SemantMe(classId);
            if (!TYPE.isSubtype(valueType, returnType)) {
                System.out.format(">> ERROR [%d:%d] %s must return %s\n", 2, 2, scope.scopeName, returnType.name);
                System.exit(0);
            }
        } else {
            if (returnType != TYPE_VOID.getInstance()) {
                System.out.format(">> ERROR [%d:%d] %s must return %s\n", 2, 2, scope.scopeName, returnType.name);
                System.exit(0);
            }
        }
        return null;
    }

}
