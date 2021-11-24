package AST;

public class AST_CLASS_EXTENDS_DECLER extends AST_DEC_CLASS_DEC{
    public String id;
    public String id_extends;
    public AST_CFIELD_LIST cf;

    public AST_CLASS_EXTENDS_DECLER(String id, String id_extends, AST_CFIELD_LIST cf) {
        this.id = id;
        this.id_extends = id_extends;
        this.cf = cf;
    }
}
