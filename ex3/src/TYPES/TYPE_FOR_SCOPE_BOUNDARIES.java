package TYPES;

import SYMBOL_TABLE.ScopeType;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE {
	public final ScopeType scopeType;
	public final String scopeName;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, ScopeType scopeType, String scopeName) {
		this.name = name;
		this.scopeType = scopeType;
		this.scopeName = scopeName;
	}
}
