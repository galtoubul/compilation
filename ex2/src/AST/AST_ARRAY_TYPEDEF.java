package AST;

public class AST_ARRAY_TYPEDEF extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_ARRAY_TYPEDEF(String id, AST_TYPE type) {
        this.type = type;
        this.id = id;
    }
}
