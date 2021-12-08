package AST;

import TYPES.TYPE;

public class AST_CFIELD_FUNC_DEC extends AST_CFIELD {
    public AST_FUNC_DEC func;

    public AST_CFIELD_FUNC_DEC(AST_FUNC_DEC func) {
        this.func = func;
    }

    public void PrintMe() {
        System.out.print("AST NODE CFIELD FUNC DEC\n");
        if (func != null)
            func.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "CFIELD\nFUNC DEC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (func != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);
    }

    @Override
    public TYPE SemantMe() {
        return this.func.SemantMe();
    }
}
