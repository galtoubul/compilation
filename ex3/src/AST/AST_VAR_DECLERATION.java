package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS_VAR_DEC;

public class AST_VAR_DECLERATION extends AST_VAR_DEC {
    public AST_VAR_DECLERATION(AST_TYPE type, String id) {
        super(type, id);
    }

    public void PrintMe() {
        System.out.format("VAR-DEC(%s):%s                \n", id, type);

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR\nDEC(%s)\n:%s", id, type));
    }

    @Override
    public TYPE SemantMe(String classId) {
        System.out.println("-- AST_VAR_DECLERATION SemantMe extends");
        TYPE t;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That id does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, id);
        }

        SYMBOL_TABLE.getInstance().enter(id, t);

        TYPE var = new TYPE_CLASS_VAR_DEC(t, id);
        System.out.println(var.name);
        return var;
    }

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_VAR_DECLERATION SemantMe");
        TYPE t;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That id does NOT exist */
        /**************************************/
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, id);
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(id, t);
        TYPE var = SYMBOL_TABLE.getInstance().find(id);
        System.out.println(var.name);
        return var;
    }
}
