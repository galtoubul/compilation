package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TEMP.*;
import IR.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT {
    public AST_VAR var;
    public AST_NEW_EXP exp;

    public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId, int localVarIndex) {
        System.out.format("-- AST_STMT_ASSIGN_NEW SemantMe");
        TYPE t1 = null;
        TYPE t2 = null;

        // Retrieve children types
        if (var != null) {
            t1 = var.SemantMe(classId);
        }
        if (exp != null) {
            t2 = exp.SemantMe(classId);
        }

        // Compate the two types
        // TODO apply polymorphism
        if (!exp.subscript.isPresent() && TYPE.isSubtype(t2, t1)) {
            System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
        }

        // Validate arrays
        if (exp.subscript.isPresent() &&
                (!t1.isArray() || (t1.isArray() && !TYPE.isSubtype(t2, ((TYPE_ARRAY) t1).type)))) {
            System.out.format(">> ERROR [%d:%d] type mismatch: type %s is not an array of %s\n",
                    2, 2, t1.name, t2.name);
            throw new SemanticErrorException("" + lineNum);
        }

        return null;
    }

    // TODO
    @Override
    public void IRme() {
        System.out.println("-- AST_STMT_ASSIGN_NEW IRme");
        TEMP src = exp.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Store(var.getSimple().name, src));

        throw new UnsupportedOperationException();
    }
}
