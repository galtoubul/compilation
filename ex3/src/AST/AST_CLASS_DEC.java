package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_CLASS_DEC extends AST_Node {
    public String id;
    public AST_CFIELD_LIST fields;

    public AST_CLASS_DEC(String id, AST_CFIELD_LIST fields) {
        this.id = id;
        this.fields = fields;
    }

    public TYPE SemantMe() {
        return null;
    }
}
