package AST;

import TYPES.TYPE;
import TEMP.*;

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
        System.out.println("-- AST_DEC_CLASS_DEC SemantMe");

        if (v != null) {
            v.SemantMe();
        }
        return null;
    }

    public TEMP IRme() {
        System.out.println("-- AST_DEC_CLASS_DEC IRme");

        if (v != null) {
            v.IRme();
        }
        return null;
    }
}
