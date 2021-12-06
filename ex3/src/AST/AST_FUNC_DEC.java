package AST;

import TYPES.TYPE;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE returnType;
    public String id;
    public AST_STMT st; // ???
    public AST_STMT_LIST body;

    public AST_FUNC_DEC(AST_TYPE returnType, String id, AST_STMT st, AST_STMT_LIST body) {
        this.returnType = returnType;
        this.id = id;
        this.st = st;
        this.body = body;
    }

    public TYPE SemantMe() {
        return null;
    }
}
