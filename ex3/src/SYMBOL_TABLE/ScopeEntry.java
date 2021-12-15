package SYMBOL_TABLE;

import java.util.Optional;

public class ScopeEntry {
    public final ScopeType scopeType;
    public final Optional<String> scopeName;

    public ScopeEntry(ScopeType scopeType, String scopeName) {
        this.scopeType = scopeType;
        this.scopeName = Optional.ofNullable(scopeName);
    }
}
