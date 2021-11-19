package AST;

public class AST_PROGRM extends AST_Node{
    public AST_DEC_LIST dl;

    public AST_PROGRM(AST_DEC_LIST dl) {
        this.dl = dl;
    }
}
