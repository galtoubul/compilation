package AST;

public class SemanticErrorException extends IllegalStateException {
    public SemanticErrorException(String errorMessage) {
        super(errorMessage);
    }
}
