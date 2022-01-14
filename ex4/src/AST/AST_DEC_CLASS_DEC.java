package AST;

import TYPES.TYPE;

public class AST_DEC_CLASS_DEC extends AST_DEC {
    public AST_CLASS_DEC v;

    public AST_DEC_CLASS_DEC(AST_CLASS_DEC v) {
        this.v = v;
    }

    public void PrintMe() {
        if (v != null) {
            v.PrintMe();
        }
    }

    public TYPE SemantMe() {
        if (v != null) {
            v.SemantMe();
        }
        return null;
    }
}
