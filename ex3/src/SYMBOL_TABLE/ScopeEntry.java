package SYMBOL_TABLE;

public class ScopeEntry {
    public final ScopeType scopeType;
    public final String scopeName;

    public ScopeEntry(ScopeType scopeType, String scopeName) {
        this.scopeType = scopeType;
        this.scopeName = scopeName;
    }
}
