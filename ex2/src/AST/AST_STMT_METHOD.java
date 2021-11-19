package AST;

public class AST_STMT_METHOD extends AST_STMT{
    public String id;
    public AST_PARM_LIST pl;
    public

    public AST_STMT_METHOD(String id, AST_PARM_LIST pl) {
        this.id = id;
        this.pl = pl;
    }
}
