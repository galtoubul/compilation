package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_NEW_EXP extends AST_Node {
    public AST_TYPE type;
    public Optional<AST_EXP> subscript;

    public AST_NEW_EXP(AST_TYPE type, Optional<AST_EXP> subscript) {
        this.type = type;
        this.subscript = subscript;
    }

    public TYPE SemantMe() {
        // Get the new type
        TYPE t = SYMBOL_TABLE.getInstance().find(type.name());

        // Check that the type exists
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            System.exit(0);
        }

        // Check that the type can be allocated (i.e., class or array)
        if (!t.isClass() && !t.isArray()) {
            System.out.format(">> ERROR [%d:%d] type %s cannot be allocated\n", 2, 2, type.name());
            System.exit(0);
        }

        // Validate subscript for arrays
        if (this.subscript.isPresent()) {
            if (!t.isArray()) {
                System.out.format(">> ERROR [%d:%d] type %s is not an array typedef\n", 2, 2, type.name());
                System.exit(0);
            }
            if (subscript.get().SemantMe() != TYPE_INT.getInstance()) {
                System.out.format(">> ERROR [%d:%d] non integral array length\n", 2, 2);
                System.exit(0);
            }
        }

        return t;
    }
}
