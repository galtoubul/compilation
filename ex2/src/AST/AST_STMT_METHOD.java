package AST;

public class AST_STMT_METHOD extends AST_STMT{
    public String id;
    public AST_EXP_LIST pl;
    public AST_VAR var;

    public AST_STMT_METHOD(AST_VAR v, String id, AST_EXP_LIST pl) {
        this.id = id;
        this.pl = pl;
        this.var = var;
    }
}
