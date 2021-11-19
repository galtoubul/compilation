package AST;

public class AST_FUNC_DECLER_PARML extends AST_FUNC_DEC{
    public AST_TYPE type;
    public String id;
    public AST_STMT st;
    public AST_STMT_LIST sl;

    public AST_FUNC_DECLER_PARML(AST_TYPE type, String id, AST_STMT st, AST_STMT_LIST sl) {
        this.type = type;
        this.id = id;
        this.st = st;
        this.sl = sl;
    }
}
