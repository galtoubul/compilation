package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SYMBOL_TABLE_ENTRY;

import TYPES.*;

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
    public TYPE SemantMe(Optional<String> fatherClassId) {
        System.out.format("-- AST_VAR_DECLERATION SemantMe%s\n", fatherClassId.isPresent() ? " extends" : "");

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
                        System.out.println(">> ERROR [line] overloading var and func names isnt allowed");
                        throw new semanticErrorException("line");
                    }
                }
                dataMembers = dataMembers.tail;
            }
        }

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        TYPE t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        /**************************************/
        /* [2] Check That id does NOT exist */
        /**************************************/
        for (SYMBOL_TABLE_ENTRY e : SYMBOL_TABLE.scopes.get(SYMBOL_TABLE.currentScope)) {
            System.out.println("e.name = " + e.name);
            System.out.println("id = " + id);

            if (e.name.equals(id))
                System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n", 2, 2, id);
        }

        /***************************************************/
        /* [3] Enter the Function Type to the Symbol Table */
        /***************************************************/
        SYMBOL_TABLE.getInstance().enter(id, t);
        TYPE_CLASS_VAR_DEC var = new TYPE_CLASS_VAR_DEC(t, id);
        System.out.println("var.name = " + var.name);
        return var;

    }
}
