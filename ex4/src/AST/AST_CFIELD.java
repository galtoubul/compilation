package AST;

import java.util.Optional;

import TYPES.TYPE;

public abstract class AST_CFIELD extends AST_Node {
    public abstract TYPE SemantMe(Optional<String> classId, Optional<Integer> fieldInd);

    public abstract void IRme();
}
