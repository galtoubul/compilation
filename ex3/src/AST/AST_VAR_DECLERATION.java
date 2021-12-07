package AST;

import TYPES.TYPE;

public class AST_VAR_DECLERATION extends AST_VAR_DEC {
    public AST_VAR_DECLERATION(AST_TYPE type, String id) {
        super(type, id);
    }

    public TYPE SemantMe() {
        return null;
    }
}
