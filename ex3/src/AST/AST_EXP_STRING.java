package AST;

import TYPES.TYPE;
import TYPES.TYPE_STRING;

public class AST_EXP_STRING extends AST_EXP {
    public String s;

    public AST_EXP_STRING(String s) {
        SerialNumber = AST_Node_Serial_Number.getFresh();

        System.out.format("====================== exp -> STRING( %s )\n", s);
        this.s = s;
    }

    public void PrintMe() {
        /*******************************/
        /* AST NODE TYPE = AST STRING EXP */
        /*******************************/
        System.out.format("AST NODE STRING( %s )\n", s);

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STRING\n%s", s.replace('"', '\'')));
    }

    public TYPE SemantMe() {
        return TYPE_STRING.getInstance();
    }

}
