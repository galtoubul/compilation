package AST;

import TYPES.TYPE;

public class AST_TYPE_WITH_VALUE extends AST_TYPE {
    public String val;

    public AST_TYPE_WITH_VALUE(String val) {
        this.val = val;
    }

    @Override
    public String name() {
        return this.val;
    }

    public TYPE SemantMe() {
        return null; // TODO
    }
}
