package AST;

import TYPES.TYPE;

public abstract class AST_VAR extends AST_Node {
    public abstract TYPE SemantMe();

    public AST_VAR_SIMPLE getSimple() {
        AST_VAR v = this;
        while (!(v instanceof AST_VAR_SIMPLE)){
            if(v instanceof AST_VAR_FIELD) {
                v = ((AST_VAR_FIELD) v).var;
            }else {
                v = ((AST_VAR_SUBSCRIPT) v).var;
            }
        }

        return (AST_VAR_SIMPLE) v;
    }
}
