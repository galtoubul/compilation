package AST;

import TYPES.TYPE;

public class AST_PROGRAM extends AST_Node {
    public AST_DEC_LIST decList;

    public AST_PROGRAM(AST_DEC_LIST decList) {
        this.decList = decList;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    public void PrintMe() {
        // String className = this.getClass().getName();
        // System.out.format("AST NODE %s( %s, %s)\n", className, type, id);
        System.out.format("AST NODE PROGRAM\n");

        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("%s", decList));
    }

    public TYPE SemantMe() {
        System.out.println("-- AST_PROGRAM SemantMe");
        return decList.SemantMe();
    }

    public void IRme() {
        System.out.println("-- AST_PROGRAM IRme");
        decList.IRme();
    }
}
