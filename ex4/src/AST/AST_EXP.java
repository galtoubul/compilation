package AST;

import java.util.Optional;

import TYPES.*;

public abstract class AST_EXP extends AST_Node {
    public abstract TYPE SemantMe(Optional<String> classId);
}
