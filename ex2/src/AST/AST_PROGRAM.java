package AST;

public class AST_PROGRAM extends AST_Node{
    public AST_DEC_LIST dl;

    public AST_PROGRAM(AST_DEC_LIST dl) {
        this.dl = dl;
    }
}
