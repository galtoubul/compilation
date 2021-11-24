package AST;

public class AST_FUNC_VAR_DEC extends AST_DEC{
    public AST_VAR_DEC v;

    public AST_FUNC_VAR_DEC(AST_VAR_DEC v) {
        this.v = v;
    }
}
