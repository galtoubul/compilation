package AST;

import TYPES.TYPE;

public class AST_EXP_METHOD extends AST_EXP {
    public AST_VAR var;
    public String id;
    public AST_EXP_LIST pl;

    public AST_EXP_METHOD(AST_VAR var, String id, AST_EXP_LIST pl) {
        this.var = var;
        this.id = id;
        this.pl = pl;
    }

    public TYPE SemantMe() {
        return null;
    }
}
