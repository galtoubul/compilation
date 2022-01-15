package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_VOID;
import TEMP.*;

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
        // TYPE t = SYMBOL_TABLE.getInstance().find(type.name());
        TYPE t = this.type.getTYPE(lineNum);

        if (t == TYPE_VOID.getInstance()) {
            System.out.format(">> ERROR [%d] parameters cannot be of type 'void'", lineNum);
            throw new SemanticErrorException(String.valueOf(lineNum));
        }

        // Check that the typedef does not exist yet
        TYPE typedef = SYMBOL_TABLE.getInstance().find(id);
        if (typedef != null) {
            System.out.format(">> ERROR [%d] non existing type %s\n", lineNum, type.name());
            throw new SemanticErrorException(String.valueOf(lineNum));
        }

        // Add new array type to the symbol table
        TYPE_ARRAY arrayType = new TYPE_ARRAY(t, id);
        SYMBOL_TABLE.getInstance().enter(id, arrayType, true);

        return null;
    }

    // TODO
    public TEMP IRme() {
        System.out.println("-- AST_ARRAY_TYPEDEF IRme");
        return null;
    }
}
