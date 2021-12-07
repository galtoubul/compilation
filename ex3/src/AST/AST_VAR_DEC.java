package AST;

import TYPES.TYPE;

public class AST_VAR_DEC extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_VAR_DEC(AST_TYPE type, String id) {
        this.type = type;
        this.id = id;
    }

    public TYPE SemantMe() {
        return null;
    }
}
