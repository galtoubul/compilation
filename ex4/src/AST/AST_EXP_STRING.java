package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_STRING;
import TEMP.*;
import IR.*;

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

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return TYPE_STRING.getInstance();
    }

    public TEMP IRme() {
        System.out.println("-- AST_EXP_STRING IRme");
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Temp_With_String_Const(dst, s));
        return dst;
    }
}
