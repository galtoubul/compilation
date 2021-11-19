package AST;

public class AST_EXP_METHOD extends AST_EXP{
    public AST_VAR var;
    public String id;
    public AST_PARM_LIST pl;

    public AST_EXP_METHOD(AST_VAR var, String id, AST_PARM_LIST pl) {
        this.var = var;
        this.id = id;
        this.pl = pl;
    }
}
