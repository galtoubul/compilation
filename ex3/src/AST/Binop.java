package AST;

public enum Binop {
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    DIVIDE("/"),
    LT("<"),
    GT(">"),
    EQ("=");

    public String type;
    Binop(String s) {
        type = s;
    }
}
