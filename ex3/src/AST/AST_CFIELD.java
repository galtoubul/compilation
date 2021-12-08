package AST;

import TYPES.TYPE;

public abstract class AST_CFIELD extends AST_Node {
    public abstract TYPE SemantMe();
    public abstract TYPE SemantMe(String classId);
}
