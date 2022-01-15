package SYMBOL_TABLE;

import TYPES.TYPE;
import java.util.Optional;
import AstNotationType.AstNotationType;

public class SymbolTableEntry {
	final int index;
	public final String name;
	public final TYPE type;
	public final boolean isType;
	public int position = 0;
	public AstNotationType astNotationType = null;

	SymbolTableEntry(int index, String name, TYPE type, boolean isType) {
		System.out.format("\t\t---- SymbolTableEntry index = %d | " +
				"name = %s | type = %s | isType = %s | position = %d | type = %s\n",
				index, name, type, isType, this.position, this.astNotationType);
		this.index = index;
		this.name = name;
		this.type = type;
		this.isType = isType;
	}

	SymbolTableEntry(int index, String name, TYPE type, boolean isType, Optional<Integer> position,
					 Optional<AstNotationType> astNotationType) {
		this.index = index;
		this.name = name;
		this.type = type;
		this.isType = isType;
		this.position = position.orElse(null);
		this.astNotationType = astNotationType.orElse(null);
		System.out.format("\t\t---- SymbolTableEntry index = %d | " +
						"name = %s | type = %s | isType = %s | position = %d | type = %s\n",
				index, name, type, isType, this.position, this.astNotationType);
	}
}
