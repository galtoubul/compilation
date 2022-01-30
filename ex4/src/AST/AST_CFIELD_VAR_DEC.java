package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_CFIELD_VAR_DEC extends AST_CFIELD {
    public AST_VAR_DEC var;

    public AST_CFIELD_VAR_DEC(AST_VAR_DEC var) {
        this.var = var;
    }

    public void PrintMe() {
        System.out.print("AST NODE CFIELD VAR DEC\n");
        if (var != null)
            var.PrintMe();

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
    public TYPE SemantMe(Optional<String> classId, Optional<Integer> fieldIndOpt) {
        System.out.println("-- AST_CFIELD_VAR_DEC SemantMe");
        return this.var.SemantMe(classId, fieldIndOpt);
    }

    @Override
    public void IRme() {
        var.IRme();
    }
}
