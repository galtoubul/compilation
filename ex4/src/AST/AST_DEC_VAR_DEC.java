package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_DEC_VAR_DEC extends AST_DEC {
    public AST_VAR_DEC varDec;

    public AST_DEC_VAR_DEC(AST_VAR_DEC v) {
        this.varDec = v;
    }

    public TYPE SemantMe() {
        if (varDec != null) {
            varDec.SemantMe(Optional.empty());
        }
        return null;
    }
}
