package AST;

public class AST_CFIELD_FUNC_DEC extends AST_CFIELD{
    public AST_FUNC_DEC var;

    public AST_CFIELD_FUNC_DEC(AST_FUNC_DEC var) {
        this.var = var;
    }
}
