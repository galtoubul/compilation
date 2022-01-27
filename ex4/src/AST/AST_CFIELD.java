package AST;

import java.util.Optional;

import TEMP.TEMP;
import TYPES.TYPE;

public abstract class AST_CFIELD extends AST_Node {
    public abstract TYPE SemantMe(Optional<String> classId, Optional<Integer> fieldInd);
    public abstract TEMP IRme();
}
