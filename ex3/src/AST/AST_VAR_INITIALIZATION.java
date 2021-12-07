package AST;

import TYPES.TYPE;

public class AST_VAR_INITIALIZATION extends AST_VAR_DEC {
    public AST_TYPE type;
    public String id;
    public AST_EXP exp;

    public AST_VAR_INITIALIZATION(AST_TYPE type, String id, AST_EXP exp) {
        super(type, id);
        this.exp = exp;
    }

    public TYPE SemantMe() {
        // System.out.format("ASSIGN");
        // TYPE expType = null;

        // if (exp != null)
        // expType = exp.SemantMe();

        // if (type != expType.name) {
        // System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
        // }
        return null;
    }
}
