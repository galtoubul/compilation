package AST;

import java.util.Optional;
import TEMP.*;

import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return TYPE_NIL.getInstance();
    }

    public TEMP IRme() {
        throw new UnsupportedOperationException();
    }
}
