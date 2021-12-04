package AST;

public class AST_DEC_ARRAY_TYPEDEF extends AST_DEC {
    public AST_ARRAY_TYPEDEF v;

    public AST_DEC_ARRAY_TYPEDEF(AST_ARRAY_TYPEDEF v) {
        this.v = v;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }


    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        String className = this.getClass().getName();
        //System.out.format("AST NODE %s( %s, %s)\n", className, type, id);
        System.out.format("AST NODE");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
//        AST_GRAPHVIZ.getInstance().logNode(
//                SerialNumber,
//                String.format("%s, %s",type, id));
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("123"));
    }
}
