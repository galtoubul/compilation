package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;

public class AST_STMT_FUNC extends AST_STMT {
    public String id;
    public AST_EXP_LIST pl;

    public AST_STMT_FUNC(String id, AST_EXP_LIST pl) {
        this.id = id;
        this.pl = pl;
    }

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_STMT_FUNC SemantMe");
        TYPE_FUNCTION funcType = null;
        try {
            funcType = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(this.id);
        } catch (ClassCastException exc) {
            System.out.format(">> ERROR [line:col] class extends\n");
            System.exit(-1);
        }

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line:col] function use before define\n");
            System.exit(-1);
        } else {
            System.out.println("-- AST_STMT_FUNC funcType isnt null");
        }

        return null;
    }
}
