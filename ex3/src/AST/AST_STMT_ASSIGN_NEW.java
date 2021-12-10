package AST;

import TYPES.TYPE;

public class AST_STMT_ASSIGN_NEW extends AST_STMT {
    public AST_VAR var;
    public AST_NEW_EXP exp;

    public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public TYPE SemantMe() {
        System.out.format("ASSIGN");
        TYPE t1 = null;
        TYPE t2 = null;

        // Retrieve children types
        if (var != null)
            t1 = var.SemantMe();
        if (exp != null)
            t2 = exp.SemantMe();

        // Compate the two types
        // TODO apply polymorphism
        if (t1 != t2) {
            System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
        }

        return null;
    }
}
