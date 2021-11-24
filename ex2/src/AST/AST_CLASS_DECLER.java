package AST;

public class AST_CLASS_DECLER extends AST_DEC_CLASS_DEC{
    public String id;
    public AST_CFIELD_LIST cf;

    public AST_CLASS_DECLER(String id, AST_CFIELD_LIST cf) {
        this.id = id;
        this.cf = cf;
    }
}
