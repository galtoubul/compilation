package AST;

import java.util.Optional;

import IR.IR;
import IR.IRcommandConstInt;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;
import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return TYPE_NIL.getInstance();
    }

    public TEMP IRme() {
        final int NIL_VALUE = 0;
        TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommandConstInt(temp, NIL_VALUE));
        return temp;
    }
}
