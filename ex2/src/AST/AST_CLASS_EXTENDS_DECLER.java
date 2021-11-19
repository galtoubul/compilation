package AST;

public class AST_CLASS_EXTENDS_DECLER extends AST_CLASS_DEC{
    public String id;
    public String id_extends;
    public AST_CFIELD cf;
    public AST_STMT_LIST sl;

    public AST_CLASS_EXTENDS_DECLER(String id, String id_extends, AST_CFIELD cf, AST_STMT_LIST sl) {
        this.id = id;
        this.id_extends = id_extends;
        this.cf = cf;
        this.sl = sl;
    }
}
