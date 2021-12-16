package SYMBOL_TABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Stack;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.IntStream;

import TYPES.*;

public class SYMBOL_TABLE {

	// Singelton
	private static SYMBOL_TABLE instance = null;

	private HashMap<String, Stack<SymbolTableEntry>> table = new HashMap<>();
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
			instance.enter("int", TYPE_INT.getInstance(), true);
			instance.enter("string", TYPE_STRING.getInstance(), true);
			instance.enter("void", TYPE_VOID.getInstance(), true);

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
									null)),
					false);
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintString",
							new TYPE_LIST(TYPE_STRING.getInstance(), null)),
					false);
			instance.enter(
					"PrintTrace",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintTrace",
							new TYPE_LIST(
									null,
									null)),
					false);

		}
		return instance;
	}

	private int currentScopeIndex() {
		return this.scopes.size();
	}

	/**
	 * Find `name` in the inner-most scope it appears in.
	 * 
	 * @param name
	 * @return The symbol table entry of `name` in the inner-most scope it appears
	 *         in, if any; otherwise null.
	 */
	public Optional<SymbolTableEntry> findEntry(String name) {
		Stack<SymbolTableEntry> idStack = this.table.get(name);
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
	public void enter(String name, TYPE type, boolean isType) {
		Stack<SymbolTableEntry> idStack = this.table.get(name);
		if (idStack == null) {
			idStack = new Stack<>();
			this.table.put(name, idStack);
		}
		if (idStack.empty() || idStack.peek().index < this.currentScopeIndex()) {
			idStack.push(new SymbolTableEntry(this.currentScopeIndex(), name, type, isType));
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
		Optional<SymbolTableEntry> entry = this.findEntry(name);
		if (!entry.isPresent()) {
			return null;
		}
		return entry.get().type;
	}

	public boolean isInScope(String name) {
		Optional<SymbolTableEntry> entry = this.findEntry(name);
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

	private ScopeEntry currentScope() {
		return this.scopes.peek().getKey();
	}

	/**
	 * Retreive the type of the current scope in the symbol table.
	 */
	public ScopeType currentScopeType() {
		return this.currentScope().scopeType;
	}

	/**
	 * Find the closest scope with the type `scopeType`.
	 */
	public Optional<ScopeEntry> findScopeType(ScopeType scopeType) {
		return this.scopes.stream()
				.map(SimpleEntry::getKey)
				.filter(scope -> scope.scopeType == scopeType)
				.findFirst();
	}

	private ScopeEntry getScope(int index) {
		return this.scopes.get(this.currentScopeIndex() - index).getKey();
	}

	/**
	 * Find the closest scope and its index with the type `scopeType`.
	 */
	private Optional<SimpleEntry<Integer, ScopeEntry>> findIndexedScopeType(ScopeType scopeType) {
		return IntStream
				.range(1, this.currentScopeIndex() + 1)
				.mapToObj(i -> new SimpleEntry<Integer, ScopeEntry>(i, this.getScope(i)))
				.filter(indexedScope -> indexedScope.getValue().scopeType == scopeType)
				.findFirst();
	}

	public Optional<TYPE> findPotentialMember(String name) {
		Optional<SymbolTableEntry> firstEntry = this.findEntry(name);

		Optional<SimpleEntry<Integer, ScopeEntry>> classScope = this.findIndexedScopeType(ScopeType.Class);

		if (!firstEntry.isPresent() || (classScope.isPresent() && firstEntry.get().index < classScope.get().getKey())) {
			TYPE_CLASS classType = (TYPE_CLASS) this.find(classScope.get().getValue().scopeName.get());
			Optional<TYPE> member = classType.lookupMemberInAncestors(name).map(type -> {
				if (type instanceof TYPE_CLASS_VAR_DEC) {
					return ((TYPE_CLASS_VAR_DEC) type).type;
				}
				return type;
			});
			if (member.isPresent()) {
				return member;
			}
		}

		return firstEntry.map(entry -> entry.type);
	}
}
