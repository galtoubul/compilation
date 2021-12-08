package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;

public class AST_STMT_FUNC extends AST_STMT {
    public String id;
    public AST_EXP_LIST el;

    public AST_STMT_FUNC(String id, AST_EXP_LIST el) {
        this.id = id;
        this.el = el;
    }

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_STMT_FUNC SemantMe");

        // function name lookup
        TYPE_FUNCTION funcType = null;
        try {
            funcType = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(this.id);
        } catch (ClassCastException exc) {
            System.out.format(">> ERROR [line] class extends\n");
            throw new semanticErrorException("line");
        }

        // Didn't find function in the lookup
        if (funcType == null) {
            System.out.format(">> ERROR [line] function use before define\n");
            throw new semanticErrorException("line");
        } else {
            System.out.println("-- AST_STMT_FUNC funcType isnt null");
        }

        // this.el.SemantMe() is a TYPE_LIST -> need to check that head isnt null
        if (this.el.SemantMe().head == null) {
            System.out.format(">> ERROR [line] parameter use before define\n");
            throw new semanticErrorException("line");
        } else {
            System.out.println("-- AST_STMT_FUNC parameter isnt null");
        }

        return null;
    }
}
