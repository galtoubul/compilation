package AST;

public class AST_ARRAY_TYPEDEF extends AST_DEC{
    public AST_TYPE type;
    public String id;

    public AST_ARRAY_TYPEDEF(AST_TYPE type, String id) {
        this.type = type;
        this.id = id;
    }
}
