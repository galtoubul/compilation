package AST;

public class AST_FUNC_WITH_PARAM extends AST_FUNC_DEC{
    public AST_TYPE type;
    public String id;
    public AST_STMT st;
    public AST_STMT_LIST sl;
    public AST_PARM_LIST pl;

    public AST_FUNC_WITH_PARAM(AST_TYPE type, String id, AST_STMT st, AST_STMT_LIST sl, AST_PARM_LIST pl) {
        this.type = type;
        this.id = id;
        this.st = st;
        this.sl = sl;
        this.pl = pl;
    }
}
