package AST;

import TYPES.TYPE;

public class AST_PROGRAM extends AST_Node {
    public AST_DEC_LIST decList;

    public AST_PROGRAM(AST_DEC_LIST decList) {
        this.decList = decList;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe() {
        /*******************************/
        /* AST NODE TYPE = AST PROGRAM */
        /*******************************/
        String className = this.getClass().getName();
        // System.out.format("AST NODE %s( %s, %s)\n", className, type, id);
        System.out.format("AST NODE PROGRAM");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("%s", decList));
    }

    public TYPE SemantMe() {
        return decList.SemantMe();
    }
}
