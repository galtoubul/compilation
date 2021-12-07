package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_FUNCTION;
import TYPES.TYPE_LIST;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE returnType;
    public String id;
    public AST_STMT_LIST body;
    public Optional<AST_PARM_LIST> params;

    public AST_FUNC_DEC(AST_TYPE returnType, String id, AST_STMT_LIST body, Optional<AST_PARM_LIST> params) {
        this.returnType = returnType;
        this.id = id;
        this.body = body;
        this.params = params;
    }

    /************************************************************/
    /* The printing message for a function declaration AST node */
    /************************************************************/
    public void PrintMe() {
        /*************************************************/
        /* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
        /*************************************************/
        System.out.format("FUNC(%s):%s\n", id, returnType);

        /***************************************/
        /* RECURSIVELY PRINT params + body ... */
        /***************************************/
        if (params.isPresent())
            params.get().PrintMe();
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
        if (params.isPresent())
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, params.get().SerialNumber);
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

        /***************************/
        /* [2] Semant Input Params */
        /***************************/
        if (params.isPresent()) {
            for (AST_PARM_LIST it = params.get(); it != null; it = it.tail) {
                t = SYMBOL_TABLE.getInstance().find(it.head.type);
                if (t == null) {
                    System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, it.head.type);
                } else {
                    type_list = new TYPE_LIST(t, type_list);
                    SYMBOL_TABLE.getInstance().enter(it.head.id, t);
                }
            }
        }

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
