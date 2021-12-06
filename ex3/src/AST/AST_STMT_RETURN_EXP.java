package AST;

public class AST_STMT_RETURN_EXP extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RETURN_EXP(AST_EXP exp) {
        this.exp = exp;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************************/
    /* The printing message for a function declaration AST node */
    /************************************************************/
    public void PrintMe()
    {
        /*************************************/
        /* AST NODE TYPE = AST SUBSCRIPT VAR */
        /*************************************/
        System.out.print("AST NODE STMT RETURN EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT exp ... */
        /*****************************/
        if (exp != null) exp.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "RETURN EXP");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }
}
