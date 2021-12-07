package AST;

import TYPES.TYPE;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD {
    public AST_VAR_DEC var;

    public AST_CFIELD_VAR_DEC(AST_VAR_DEC var) {
        this.var = var;
    }

    /**************************************************************/
    /* The printing message for a cfield var declaration AST node */
    /**************************************************************/
    public void PrintMe() {
        /**************************************/
        /* AST NODE TYPE = AST CFIELD VAR DEC */
        /**************************************/
        System.out.print("AST NODE CFIELD VAR DEC\n");
        if (var != null)
            var.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "CFIELD\nVAR DEC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (var != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        return this.var.SemantMe();
    }
}
