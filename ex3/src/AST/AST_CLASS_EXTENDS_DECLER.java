package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_CLASS_EXTENDS_DECLER extends AST_CLASS_DEC {
    public String id_extends;

    public AST_CLASS_EXTENDS_DECLER(String id, String id_extends, AST_CFIELD_LIST fields) {
        super(id, fields);
        this.id_extends = id_extends;
    }

    public void PrintMe() {
        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        System.out.format("CLASS DECLER = %s EXTENDS %s\n", id, id_extends);
        if (fields != null) {
            fields.PrintMe();
        }

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASS\n%s", id));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, fields.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("-- AST_CLASS_EXTENDS_DECLER SemantMe");

        TYPE_CLASS fatherType;
        try {
            fatherType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(this.id_extends);
        }catch (ClassCastException exc) {
            System.out.format(">> ERROR [line:col] class extends\n");
            return null;
        }

        // Didn't find father in the lookup
        if (fatherType == null) {
            System.out.format(">> ERROR [line:col] class extends\n");
            return null;
        } else {
            System.out.println("-- AST_CLASS_EXTENDS_DECLER fatherType isnt null");
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS t = new TYPE_CLASS(fatherType, id, fields.SemantMe());

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(id, t);

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }
}
