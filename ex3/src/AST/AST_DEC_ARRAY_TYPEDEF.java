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
    public void PrintMe() {
        if (v != null) {
            v.PrintMe();
        }
    }
}
