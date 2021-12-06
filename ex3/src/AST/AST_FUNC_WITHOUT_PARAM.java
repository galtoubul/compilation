package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;

public class AST_FUNC_WITHOUT_PARAM extends AST_FUNC_DEC {

    public AST_FUNC_WITHOUT_PARAM(AST_TYPE returnType, String id, AST_STMT st, AST_STMT_LIST body) {
        super(returnType, id, st, body);
    }

    /************************************************************/
    /* The printing message for a function declaration AST node */
    /************************************************************/
    public void PrintMe() {
        /*************************************************/
        /* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
        /*************************************************/
        System.out.format("FUNC(%s):%s\n", id, returnType);

        /******************************/
        /* RECURSIVELY PRINT body ... */
        /******************************/
        if (body != null)
            body.PrintMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC(%s)\n:%s\n", id, returnType));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (body != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public TYPE SemantMe() {
        TYPE t;
        TYPE returnType = null;
        TYPE_LIST type_list = null;

        /*******************/
        /* [0] return type */
        /*******************/
        returnType = SYMBOL_TABLE.getInstance().find(returnType);
        if (returnType == null) {
            System.out.format(">> ERROR [%d:%d] non existing return type %s\n", 6, 6, returnType);
        }

        /****************************/
        /* [1] Begin Function Scope */
        /****************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /*******************/
        /* [3] Semant Body */
        /*******************/
        body.SemantMe();

        /*****************/
        /* [4] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(id, new TYPE_FUNCTION(returnType, id, type_list));

        /*********************************************************/
        /* [6] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }
}
