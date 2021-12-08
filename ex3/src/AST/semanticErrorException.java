package AST;

public class semanticErrorException extends IllegalStateException {
    public semanticErrorException(String errorMessage) {
        super(errorMessage);
    }
}
