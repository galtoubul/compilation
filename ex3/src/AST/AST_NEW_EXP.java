package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_NEW_EXP extends AST_Node {
    public AST_TYPE type;
    public Optional<AST_EXP> subscript;

    public AST_NEW_EXP(AST_TYPE type, Optional<AST_EXP> subscript) {
        this.type = type;
        this.subscript = subscript;
    }

    public TYPE SemantMe() {
        return null;
    }
}
