package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_EXP_PAREN extends AST_EXP {
    public AST_EXP exp;

    public AST_EXP_PAREN(AST_EXP exp) {
        this.exp = exp;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return this.exp.SemantMe(classId);
    }
}
