package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_STMT_VAR_DEC extends AST_STMT {
    public AST_VAR_DEC varDec;

    public AST_STMT_VAR_DEC(AST_VAR_DEC v) {
        this.varDec = v;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        return this.varDec.SemantMe(classId);
    }
}
