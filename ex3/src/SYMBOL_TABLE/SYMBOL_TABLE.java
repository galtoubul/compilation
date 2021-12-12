package SYMBOL_TABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.AbstractMap.SimpleEntry;

import TYPES.*;

public class SYMBOL_TABLE {

	// Singelton
	private static SYMBOL_TABLE instance = null;

	private HashMap<String, Stack<SYMBOL_TABLE_ENTRY>> table = new HashMap<>();
	private Stack<SimpleEntry<ScopeEntry, ArrayList<String>>> scopes = new Stack<>();

	public static SYMBOL_TABLE getInstance() {
		if (instance == null) {
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			// Create the global scope
			instance.beginScope(ScopeType.Global);

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int", TYPE_INT.getInstance());
			instance.enter("string", TYPE_STRING.getInstance());
			instance.enter("void", TYPE_VOID.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
					"PrintInt",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintInt",
							new TYPE_LIST(
									TYPE_INT.getInstance(),
									null)));
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintString",
							new TYPE_LIST(TYPE_STRING.getInstance(), null)));
			instance.enter(
					"PrintTrace",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintTrace",
							new TYPE_LIST(
									null,
									null)));

		}
		return instance;
	}

	private int currentScopeIndex() {
		return this.scopes.size();
	}

	private Optional<SYMBOL_TABLE_ENTRY> findEntry(String name) {
		Stack<SYMBOL_TABLE_ENTRY> idStack = this.table.get(name);
		if (idStack == null) {
			return Optional.empty();
		}
		if (idStack.empty()) {
			return Optional.empty();
		}
		return Optional.of(idStack.peek());
	}

	private SimpleEntry<ScopeEntry, ArrayList<String>> currentScopePair() {
		return this.scopes.peek();
	}

	/**
	 * If `name` is not in the current scope, insert it with type `type`.
	 */
	public void enter(String name, TYPE type) {
		Stack<SYMBOL_TABLE_ENTRY> idStack = this.table.get(name);
		if (idStack == null) {
			idStack = new Stack<>();
			this.table.put(name, idStack);
		}
		if (idStack.empty() || idStack.peek().index < this.currentScopeIndex()) {
			idStack.push(new SYMBOL_TABLE_ENTRY(this.currentScopeIndex(), name, type));
			this.currentScopePair().getValue().add(name);
		}
	}

	/**
	 * Find `name` in the inner-most scope it appears in.
	 * 
	 * @param name
	 * @return The type of `name` in the inner-most scope it appears in, if any;
	 *         otherwise null.
	 */
	public TYPE find(String name) {
		Optional<SYMBOL_TABLE_ENTRY> entry = this.findEntry(name);
		if (!entry.isPresent()) {
			return null;
		}
		return entry.get().type;
	}

	public boolean isInScope(String name) {
		Optional<SYMBOL_TABLE_ENTRY> entry = this.findEntry(name);
		return entry.isPresent() && entry.get().index == this.currentScopeIndex();
	}

	public void beginScope(ScopeType scopeType, String scopeName) {
		this.scopes.push(new SimpleEntry<ScopeEntry, ArrayList<String>>(new ScopeEntry(scopeType, scopeName),
				new ArrayList<>()));
	}

	public void beginScope(ScopeType scopeType) {
		beginScope(scopeType, null);
	}

	public void endScope() {
		for (String name : this.currentScopePair().getValue()) {
			this.table.get(name).pop();
		}
		this.scopes.pop();
	}

	private Optional<ScopeEntry> currentScope() {
		return Optional.ofNullable(this.scopes.peek()).map(SimpleEntry::getKey);
	}

	// Retreive the type of the current scope in the symbol table
	public ScopeType currentScopeType() {
		Optional<ScopeEntry> scope = this.currentScope();
		if (scope.isPresent()) {
			return scope.get().scopeType;
		}

		// There is only the global scope, so there are no scope entris to be found
		return ScopeType.Global;
	}

	// Find the closest scope with the type `scopeType`
	public Optional<ScopeEntry> findScopeType(ScopeType scopeType) {
		return ((List<SimpleEntry<ScopeEntry, ArrayList<String>>>) this.scopes).stream()
				.map(SimpleEntry::getKey)
				.filter(scope -> scope.scopeType == scopeType)
				.findFirst();
	}
}
