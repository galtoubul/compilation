package AST;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD{
    public AST_VAR var;

    public AST_CFIELD_VAR_DEC(AST_VAR var) {
        this.var = var;
    }
}
