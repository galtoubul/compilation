package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_INT;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP extends AST_Node {
    public AST_TYPE type;
    public Optional<AST_EXP> subscript;

    public AST_NEW_EXP(AST_TYPE type, Optional<AST_EXP> subscript) {
        this.type = type;
        this.subscript = subscript;
    }

    public TYPE SemantMe(Optional<String> classId) {
        System.out.println("-- AST_NEW_EXP SemantMe");

        // Get the new type
        TYPE t = SYMBOL_TABLE.getInstance().find(type.name());

        // Check that the type exists
        if (t == null) {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n", 2, 2, type.name());
            throw new SemanticErrorException("" + lineNum);
        }

        // Check that the type can be allocated (i.e., class or array)
        if (!t.isClass() && !this.subscript.isPresent()) {
            System.out.format(">> ERROR [%d:%d] type %s cannot be allocated\n", 2, 2, type.name());
            throw new SemanticErrorException("" + lineNum);
        }

        // Validate subscript for arrays
        if (this.subscript.isPresent()) {
            // Validate integral subscript value
            if (this.subscript.get().SemantMe(classId) != TYPE_INT.getInstance()) {
                System.out.format(">> ERROR [%d:%d] non integral array length\n", 2, 2);
                throw new SemanticErrorException("" + lineNum);
            }

            // Validate positive constant subscript
            if (this.subscript.get() instanceof AST_EXP_INT &&
                    ((AST_EXP_INT) this.subscript.get()).value <= 0) {
                System.out.format(">> ERROR [%d:%d] non-positive constant array length\n", 2, 2);
                throw new SemanticErrorException("" + lineNum);
            }
        }

        return t;
    }

    public TEMP IRme() {
        System.out.println("-- AST_NEW_EXP IRme");

        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

        if (this.subscript.isPresent()) {
            // array
            TEMP subscriptTemp = this.subscript.get().IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_New_Array(dst, this.type.getTYPE(lineNum), subscriptTemp));
        } else {
            // object
            IR.getInstance().Add_IRcommand(new IRcommand_New_Object(dst, this.type.getTYPE(lineNum)));
        }

        return dst;
    }
}
