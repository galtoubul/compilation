package AST;

public class AST_STMT_RETURN_EXP extends AST_STMT {
    public AST_EXP exp;

    public AST_STMT_RETURN_EXP(AST_EXP exp) {
        this.exp = exp;
    }
}
