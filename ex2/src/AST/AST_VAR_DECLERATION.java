package AST;

public class AST_VAR_DECLERATION extends AST_VAR_DEC{
    public AST_TYPE type;
    public String id;

    public AST_VAR_DECLERATION(AST_TYPE type, String id) {
        this.type = type;
        this.id = id;
    }
}
