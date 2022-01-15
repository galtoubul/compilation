package AST;

import java.util.Optional;

import TYPES.TYPE;
import TEMP.*;

public abstract class AST_VAR extends AST_Node {
    public abstract TYPE SemantMe(Optional<String> classId);
    public abstract TEMP IRme();

    public AST_VAR_SIMPLE getSimple() {
        AST_VAR v = this;
        while (!(v instanceof AST_VAR_SIMPLE)) {
            if (v instanceof AST_VAR_FIELD) {
                v = ((AST_VAR_FIELD) v).var;
            } else {
                v = ((AST_VAR_SUBSCRIPT) v).var;
            }
        }

        return (AST_VAR_SIMPLE) v;
    }
}
