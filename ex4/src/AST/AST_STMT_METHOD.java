package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_STMT_METHOD extends AST_STMT {
    public String id;
    public AST_EXP_LIST pl;
    public AST_VAR var;

    public AST_STMT_METHOD(AST_VAR var, String id, AST_EXP_LIST pl) {
        this.id = id;
        this.pl = pl;
        this.var = var;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        // TODO Auto-generated method stub
        return null;
    }

}
