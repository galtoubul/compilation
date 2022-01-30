package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_STMT_VAR_DEC extends AST_STMT {
    public AST_VAR_DEC varDec;

    public AST_STMT_VAR_DEC(AST_VAR_DEC v) {
        this.varDec = v;
    }

    @Override
    public TYPE SemantMe(Optional<String> classId) {
        System.out.println("-- AST_STMT_VAR_DEC SemantMe");
        return this.varDec.SemantMe(classId, Optional.empty());
    }

    public TYPE SemantMe(Optional<String> classId, Optional<Integer> localVarIndexOpt) {
        System.out.println("-- AST_STMT_VAR_DEC SemantMe\n\t\tlocalVarIndexOpt = " + localVarIndexOpt);
        return this.varDec.SemantMe(classId, localVarIndexOpt);
    }

    @Override
    public void IRme() {
        System.out.println("-- AST_STMT_VAR_DEC IRme");
        String callerClassName = (Thread.currentThread().getStackTrace())[2].getClassName();
        System.out.println("\t\tcaller = " + callerClassName);
        this.varDec.IRme();
    }
}
