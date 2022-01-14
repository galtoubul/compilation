package AST;

import java.util.Optional;

import TYPES.TYPE;

public abstract class AST_VAR_DEC extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_VAR_DEC(AST_TYPE type, String id) {
        this.type = type;
        this.id = id;
    }

    public abstract TYPE SemantMe(Optional<String> classId);
}
