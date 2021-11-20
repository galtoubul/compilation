package AST;

public class AST_STMT_FUNC extends AST_STMT{
    public String id;
    public AST_PARM_LIST pl;

    public AST_STMT_FUNC(String id, AST_PARM_LIST pl) {
        this.id = id;
        this.pl = pl;
    }
}
