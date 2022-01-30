package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_DEC_FUNC_DEC extends AST_DEC {
    public AST_FUNC_DEC v;

    public AST_DEC_FUNC_DEC(AST_FUNC_DEC v) {
        this.v = v;
    }

    public TYPE SemantMe() {
        if (v != null) {
            v.SemantMe(Optional.empty());
        }
        return null;
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_DEC_FUNC_DEC IRme");
        if (v != null) {
            v.IRme();
        }
    }
}
