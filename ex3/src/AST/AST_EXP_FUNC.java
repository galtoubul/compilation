package AST;

public class AST_EXP_FUNC extends AST_EXP{
    public String id;
    public AST_EXP_LIST pl;

    public AST_EXP_FUNC(String id, AST_EXP_LIST pl) {
        SerialNumber = AST_Node_Serial_Number.getFresh();

        this.id = id;
        this.pl = pl;
    }
    public void PrintMe()
    {
        /*************************************************/
        /* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
        /*************************************************/
        System.out.format("CALL(%s)\nWITH:\n",id);

        /***************************************/
        /* RECURSIVELY PRINT params + body ... */
        /***************************************/
        if (pl != null) pl.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CALL(%s)\nWITH",id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,pl.SerialNumber);
    }
}
