package AST;

import TYPES.TYPE;

public class AST_EXP_PAREN extends AST_EXP {
    public AST_EXP exp;

    public AST_EXP_PAREN(AST_EXP exp) {
        this.exp = exp;
    }

    public TYPE SemantMe() {
        return this.exp.SemantMe();
    }
}
