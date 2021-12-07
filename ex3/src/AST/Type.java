package AST;

public enum Type {
    TYPE_INT("int"),
    TYPE_STRING("string"),
    TYPE_VOID("void");

    public String typeName;

    Type(String typeName) {
        this.typeName = typeName;
    }
}
