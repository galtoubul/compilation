package AstNotationType;

public enum AstNotationType {
    parameter,
    localVariable,
    globalVariable,
    field;

    public static AstNotationType getAstNotationType(String callerClassName) {
        AstNotationType astNotationType = null;
        switch (callerClassName) {
            case "AST.AST_STMT_VAR_DEC":
                astNotationType = AstNotationType.localVariable;
                break;
            case "AST.AST_CFIELD_VAR_DEC":
                astNotationType = AstNotationType.field;
                break;
            default:
                astNotationType = AstNotationType.globalVariable;
        }
        return astNotationType;
    }
}

