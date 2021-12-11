package TYPES;

import SYMBOL_TABLE.ScopeType;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE {
	public final ScopeType scopeType;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, ScopeType scopeType) {
		this.name = name;
		this.scopeType = scopeType;
	}
}
