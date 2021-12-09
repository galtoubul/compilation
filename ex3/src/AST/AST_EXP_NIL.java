package AST;

import TYPES.TYPE;
import TYPES.TYPE_NIL;

public class AST_EXP_NIL extends AST_EXP {
    public TYPE SemantMe() {
        return TYPE_NIL.getInstance();
    }
}
