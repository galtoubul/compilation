package AST;

public class AST_NEW_EXP_WITH_SUBSCRIPT extends AST_NEW_EXP{
    public String id;
    public AST_EXP exp;

    public AST_NEW_EXP_WITH_SUBSCRIPT(String id, AST_EXP exp) {
        this.id = id;
        this.exp = exp;
    }
}
