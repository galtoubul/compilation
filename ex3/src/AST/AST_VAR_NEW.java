package AST;

import TYPES.TYPE;

public class AST_VAR_NEW extends AST_VAR_DEC {
    public AST_TYPE type;
    public String id;
    public AST_NEW_EXP exp;

    public AST_VAR_NEW(AST_TYPE type, String id, AST_NEW_EXP exp) {
        super(type, id);
        this.exp = exp;
    }

    public TYPE SemantMe() {
        return null;
    }
}
