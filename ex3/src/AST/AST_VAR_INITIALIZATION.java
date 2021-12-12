package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;

public class AST_VAR_INITIALIZATION extends AST_VAR_DEC {
    public AST_EXP initialValue;

    public AST_VAR_INITIALIZATION(AST_TYPE type, String id, AST_EXP initialValue) {
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
    public TYPE SemantMe(Optional<String> classId) {
        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        TYPE t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        /************************************/
        /* [2] Check That id does NOT exist */
        /************************************/
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, id);
            System.exit(0);
        }

        /******************************************************/
        /* [3] Match the variable type with the initial value */
        /******************************************************/
        TYPE tInitial = initialValue.SemantMe(classId);
        if (!TYPE.isSubtype(tInitial, t)) {
            System.out.format(">> ERROR [%d:%d] type mismatch\n", 2, 2, id);
            System.exit(0);
        }

        // If the initialization is of a class field, it must be initialized with a
        // constant
        if (SYMBOL_TABLE.getInstance().currentScope() == ScopeType.Class &&
                !(initialValue instanceof AST_EXP_INT) &&
                !(initialValue instanceof AST_EXP_STRING) &&
                !(initialValue instanceof AST_EXP_NIL)) {
            System.out.format(">> ERROR [%d:%d] class fields can only be initialized with constant values\n", 2, 2, id);
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
}
