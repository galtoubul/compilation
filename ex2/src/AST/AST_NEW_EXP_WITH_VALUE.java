package AST;

public class AST_NEW_EXP_WITH_VALUE extends AST_NEW{
    public String id;
    public AST_EXP exp;

    public AST_NEW_EXP_WITH_VALUE(String id, AST_EXP exp) {
        this.id = id;
        this.exp = exp;
    }
}
