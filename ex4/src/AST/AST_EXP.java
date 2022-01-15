package AST;

import java.util.Optional;

import TYPES.*;
import TEMP.*;

public abstract class AST_EXP extends AST_Node {
    public abstract TYPE SemantMe(Optional<String> classId);
    public abstract TEMP IRme();
}
