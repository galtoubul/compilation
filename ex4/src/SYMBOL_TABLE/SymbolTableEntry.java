package SYMBOL_TABLE;

import TYPES.TYPE;

public class SymbolTableEntry {
	final int index;

	public final String name;
	public final TYPE type;
	public final boolean isType;

	SymbolTableEntry(int index, String name, TYPE type, boolean isType) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.isType = isType;
	}
}
