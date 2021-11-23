package AST;

public class AST_STMT_VAR_DEC extends AST_STMT{
    public AST_VAR_DEC v;

    public AST_STMT_VAR_DEC(AST_VAR_DEC v) {
        this.v = v;
    }
}
