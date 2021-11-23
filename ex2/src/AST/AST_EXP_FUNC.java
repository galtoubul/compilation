package AST;

public class AST_EXP_FUNC extends AST_EXP{
    public String id;
    public AST_EXP_LIST pl;

    public AST_EXP_FUNC(String id, AST_EXP_LIST pl) {
        this.id = id;
        this.pl = pl;
        System.out.println("|dfdf");
    }
}
