package AST;

public class AST_STMT_FUNC extends AST_STMT{
    public String id;
    public AST_PARM_LIST pl;
    public AST_VAR var;

    public AST_STMT_FUNC(String id, AST_PARM_LIST pl, AST_VAR var) {
        this.id = id;
        this.pl = pl;
        this.var = var;
    }
}
