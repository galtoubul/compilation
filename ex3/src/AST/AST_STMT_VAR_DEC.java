package AST;

import TYPES.TYPE;

public class AST_STMT_VAR_DEC extends AST_STMT {
    public AST_VAR_DEC varDec;

    public AST_STMT_VAR_DEC(AST_VAR_DEC v) {
        this.varDec = v;
    }

    public TYPE SemantMe() {
        return this.varDec.SemantMe();
    }
}
