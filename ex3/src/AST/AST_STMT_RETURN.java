package AST;

import java.util.Optional;

import TYPES.TYPE;

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

    public TYPE SemantMe() {
        if (this.exp.isPresent()) {
            return exp.get().SemantMe();
        }
        return null; // TODO return void type?
    }

}
