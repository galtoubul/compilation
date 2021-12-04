package AST;

public class AST_PROGRAM extends AST_Node {
    public AST_DEC_LIST dl;

    public AST_PROGRAM(AST_DEC_LIST dl) {
        this.dl = dl;

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
        System.out.format("AST NODE PROGRAM");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("%s", dl));
    }
}
