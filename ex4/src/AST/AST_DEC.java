package AST;

import TYPES.TYPE;
import TEMP.*;

public abstract class AST_DEC extends AST_Node {
    public abstract TYPE SemantMe();
    public abstract TEMP IRme();
}
