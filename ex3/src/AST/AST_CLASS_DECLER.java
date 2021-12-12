package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

public class AST_CLASS_DECLER extends AST_CLASS_DEC {
    public AST_CLASS_DECLER(String id, AST_CFIELD_LIST fields) {
        super(id, fields);
    }

    public void PrintMe() {

        System.out.format("CLASS DECLER = %s\n", id);
        if (fields != null)
            fields.PrintMe();

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

    @Override
    public TYPE SemantMe() {
        System.out.println("-- AST_CLASS_DECLER SemantMe");
        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Class, id);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS t = new TYPE_CLASS(null, id, fields.SemantMe(Optional.empty()));

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
