package AST;

public class AST_STMT_ASSIGN_NEW extends AST_STMT{
    public AST_VAR var;
    public AST_NEW_EXP exp;

    public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp) {
        this.var = var;
        this.exp = exp;
    }
}
