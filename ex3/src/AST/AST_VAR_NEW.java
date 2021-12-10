package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;

public class AST_VAR_NEW extends AST_VAR_DEC {
    public AST_NEW_EXP initialValue;

    public AST_VAR_NEW(AST_TYPE type, String id, AST_NEW_EXP initialValue) {
        super(type, id);
        this.initialValue = initialValue;
    }

    /********************************************************/
    /* The printing message for a declaration list AST node */
    /********************************************************/
    public void PrintMe() {
        /********************************/
        /* AST NODE TYPE = AST DEC LIST */
        /********************************/
        if (initialValue != null)
            System.out.format("VAR-DEC(%s):%s := initialValue\n", id, type.name());
        if (initialValue == null)
            System.out.format("VAR-DEC(%s):%s                \n", id, type.name());

        /**************************************/
        /* RECURSIVELY PRINT initialValue ... */
        /**************************************/
        if (initialValue != null)
            initialValue.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("VAR\nDEC(%s)\n:%s", id, type.name()));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (initialValue != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, initialValue.SerialNumber);

    }

    @Override
    public TYPE SemantMe() {
        TYPE t;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        /************************************/
        /* [2] Check That id does NOT exist */
        /************************************/
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, id);
        }

        /******************************************************/
        /* [3] Match the variable type with the initial value */
        /******************************************************/
        TYPE tInitial = initialValue.SemantMe();
        // TODO apply polymorphism
        if (initialValue.subscript.isEmpty() && t != tInitial) {
            System.out.format(">> ERROR [%d:%d] type mismatch\n", 2, 2, id);
        }

        // Validate arrays
        if (initialValue.subscript.isPresent()
                && (!t.isArray() || (t.isArray() && !tInitial.isSubtype(((TYPE_ARRAY) t).type)))) {
            System.out.format(">> ERROR [%d:%d] type mismatch: type %s is not an array of %s\n",
                    2, 2, t.name, tInitial.name);
            System.exit(0);
        }

        /***************************************************/
        /* [4] Enter the variable name to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(id, t);

        /************************************************************/
        /* [5] Return value is irrelevant for variable declarations */
        /************************************************************/
        return null;
    }

    @Override
    public TYPE SemantMe(String classId) {
        TYPE t;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        System.out.println("\n" + type.name());
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

        if (t != initialValue.SemantMe()) {
            System.out.format(">> ERROR [%d:%d] type mismatch\n", 2, 2, id);
        }

        /***************************************************/
        /* [3] Enter the variable name to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(id, t);

        /************************************************************/
        /* [4] Return value is irrelevant for variable declarations */
        /************************************************************/
        return null;
    }
}
