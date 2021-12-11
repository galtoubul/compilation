package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.*;

public class AST_FUNC_DEC extends AST_Node {
    public AST_TYPE returnTypeName;
    public String id;
    public AST_STMT_LIST body;
    public Optional<AST_PARM_LIST> params;

    public AST_FUNC_DEC(AST_TYPE returnTypeName, String id, AST_STMT_LIST body, Optional<AST_PARM_LIST> params) {
        this.returnTypeName = returnTypeName;
        this.id = id;
        this.body = body;
        this.params = params;
    }

    public void PrintMe() {
        System.out.format("FUNC(%s):%s\n", id, returnTypeName);

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
                String.format("FUNC(%s)\n:%s\n", id, returnTypeName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (params.isPresent())
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, params.get().SerialNumber);
        if (body != null)
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.format("-- AST_FUNC_DEC SemantMe%s\n", fatherClassId.isPresent() ? " extends" : "");

        if (fatherClassId.isPresent()) {
            TYPE_CLASS fatherType = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherClassId.get());
            System.out.println(fatherType.name);
            TYPE_LIST dataMembers = fatherType.data_members;

            while (dataMembers != null && dataMembers.head != null) {
                System.out.println("-- AST_FUNC_DEC SemantMe extends while (dataMembers.head != null)");
                if (dataMembers.head instanceof TYPE_FUNCTION ||
                        dataMembers.head instanceof TYPE_VOID ||
                        dataMembers.head instanceof TYPE_CLASS_VAR_DEC) {

                    System.out.println("is instance of TYPE_...");
                    TYPE dm = dataMembers.head;

                    System.out.format("id = %s\n", id);
                    System.out.format("dm.name = %s\n", dm.name);

                    if (dm.name.equals(id)) {
                        if (dataMembers.head instanceof TYPE_CLASS_VAR_DEC) {
                            System.out.println(">> ERROR [line] overloading var and func names isnt allowed");
                            throw new semanticErrorException("line");
                        } else if (dataMembers.head instanceof TYPE_VOID
                                && !this.returnTypeName.name().equals("void")) {
                            System.out.println(">> ERROR [line] overloading func names isnt allowed");
                            throw new semanticErrorException("line");
                        } else if (dataMembers.head instanceof TYPE_FUNCTION &&
                                !((TYPE_FUNCTION) dm).returnType.name.equals(this.returnTypeName.name())) {
                            System.out.format("this.returnTypeName.name() = %s\n", this.returnTypeName.name());
                            System.out.format("dm.returnType.name = %s\n", ((TYPE_FUNCTION) dm).returnType.name);
                            System.out.println(">> ERROR [line] overloading isnt allowed");
                            throw new semanticErrorException("line");
                        }
                    }
                }
                dataMembers = dataMembers.tail;
            }
        }

        TYPE t;
        TYPE returnType = null;
        TYPE_LIST type_list = null;

        /*******************/
        /* [0] return type */
        /*******************/
        returnType = SYMBOL_TABLE.getInstance().find(this.returnTypeName.name());
        if (returnType == null) {
            System.out.format(">> ERROR [%d:%d] non existing return type %s\n", 6, 6, returnType);
        }

        // Check That id does NOT exist
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [line] function %s already exists in scope\n", id);
            throw new semanticErrorException("line");
        }

        /****************************/
        /* [1] Begin Function Scope */
        /****************************/
        SYMBOL_TABLE.getInstance().beginScope(ScopeType.Function);

        /***************************/
        /* [2] Semant Input Params */
        /***************************/
        if (params.isPresent()) {
            for (AST_PARM_LIST it = params.get(); it != null; it = it.tail) {
                t = SYMBOL_TABLE.getInstance().find(it.head.type.name());
                if (t == null) {
                    System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, it.head.type.name());
                } else {
                    type_list = new TYPE_LIST(t, type_list);
                    SYMBOL_TABLE.getInstance().enter(it.head.id, t);
                }
            }
        }

        /*******************/
        /* [3] Semant Body */
        /*******************/
        body.SemantMe(Optional.empty());

        /*****************/
        /* [4] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /***************************************************/
        /* [5] Enter the Function Type to the Symbol Table */
        /***************************************************/
        TYPE_FUNCTION funcType = new TYPE_FUNCTION(returnType, id, type_list);
        SYMBOL_TABLE.getInstance().enter(id, funcType);

        return funcType;
    }
}
