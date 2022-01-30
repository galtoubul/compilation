package AST;

import TYPES.TYPE;

public class AST_DEC_ARRAY_TYPEDEF extends AST_DEC {
    public AST_ARRAY_TYPEDEF typedef;

    public AST_DEC_ARRAY_TYPEDEF(AST_ARRAY_TYPEDEF typedef) {
        this.typedef = typedef;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe() {
        if (typedef != null) {
            typedef.PrintMe();
        }
    }

    public TYPE SemantMe() {
        System.out.println("-- AST_DEC_ARRAY_TYPEDEF SemantMe");
        this.typedef.SemantMe();
        return null;
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_DEC_ARRAY_TYPEDEF IRme");
        this.typedef.IRme();
    }
}
