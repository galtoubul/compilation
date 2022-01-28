package register_alloc;

public class RegisterAllocationErrorException extends IllegalStateException {
    public RegisterAllocationErrorException() {
        super("Cannot allocate enough registers for the program");
    }
}
