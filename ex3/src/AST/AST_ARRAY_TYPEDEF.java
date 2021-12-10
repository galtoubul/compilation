package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;

public class AST_ARRAY_TYPEDEF extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_ARRAY_TYPEDEF(String id, AST_TYPE type) {
        this.type = type;
        this.id = id;

        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    /************************************************/
    /* The printing message for an INT EXP AST node */
    /************************************************/
    public void PrintMe() {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        // String className = this.getClass().getName();
        // System.out.format("AST NODE %s( %s, %s)\n", className, type, id);
        System.out.format("AST NODE ARRAY TYPEDEF( %s, %s)\n", type, id);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s, %s", type, id));
    }

    public TYPE SemantMe() {
        // Check that the type of the array exists
        TYPE t = SYMBOL_TABLE.getInstance().find(type.name());
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        // Check that the typedef does not exist yet
        TYPE typedef = SYMBOL_TABLE.getInstance().find(id);
        if (typedef != null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        // Add new array type to the symbol table
        TYPE_ARRAY arrayType = new TYPE_ARRAY(t, id);
        SYMBOL_TABLE.getInstance().enter(id, arrayType);

        return null;
    }
}
