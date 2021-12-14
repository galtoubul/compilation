package SYMBOL_TABLE;

import TYPES.TYPE;

class SYMBOL_TABLE_ENTRY {
	int index;
	String name;
	TYPE type;

	SYMBOL_TABLE_ENTRY(int index, String name, TYPE type) {
		this.index = index;
		this.name = name;
		this.type = type;
	}
}
