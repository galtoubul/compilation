package AST;

public class AST_CLASS_DECLER extends AST_CLASS_DEC{
    public String id;
    public AST_CFIELD cf;
    public AST_STMT_LIST sl;

    public AST_CLASS_DECLER(String id, AST_CFIELD cf, AST_STMT_LIST sl) {
        this.id = id;
        this.cf = cf;
        this.sl = sl;
    }
}
