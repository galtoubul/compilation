package AST;

public class AST_ARRAY_TYPEDEF extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_ARRAY_TYPEDEF(String id, AST_TYPE type) {
        this.type = type;
        this.id = id;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe() {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        // String className = this.getClass().getName();
        // System.out.format("AST NODE %s( %s, %s)\n", className, type, id);
        System.out.format("AST NODE ARRAY TYPEDEF( %s, %s)\n", type, id);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s, %s", type, id));
    }
}
