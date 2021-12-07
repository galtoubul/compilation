package AST;

public class AST_TYPE_WITHOUT_VALUE extends AST_TYPE {
    public Type type;

    public AST_TYPE_WITHOUT_VALUE(Type type) {
        this.type = type;
    }

    @Override
    public String name() {
        return this.type.name();
    }
}
