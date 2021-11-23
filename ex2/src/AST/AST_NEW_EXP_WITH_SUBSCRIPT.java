package AST;

public class AST_NEW_EXP_WITH_SUBSCRIPT extends AST_NEW_EXP{
    public AST_TYPE type;
    public AST_EXP exp;

    public AST_NEW_EXP_WITH_SUBSCRIPT(AST_TYPE type, AST_EXP exp) {
        this.type = type;
        this.exp = exp;
    }
}
