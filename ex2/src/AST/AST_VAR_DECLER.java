package AST;

public class AST_VAR_DECLER extends AST_VAR_DEC{
    public AST_TYPE type;
    public String id;

    public AST_VAR_DECLER(AST_TYPE type, String id) {
        this.type = type;
        this.id = id;
    }
}
