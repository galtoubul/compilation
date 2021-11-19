package AST;

public class AST_VAR_INT extends AST_VAR_DEC{
    public AST_TYPE type;
    public String id;
    public AST_EXP exp;


    public AST_VAR_INT(AST_TYPE type, String id, AST_EXP exp) {
        this.type = type;
        this.id = id;
        this.exp = exp;
    }
}
