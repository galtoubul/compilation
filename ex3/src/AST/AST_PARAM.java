package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;
import TYPES.TYPE_VOID;

public class AST_PARAM extends AST_Node {
	public AST_TYPE type;
	public String id;

	public AST_PARAM(AST_TYPE type, String id) {
		this.type = type;
		this.id = id;
	}

	public TYPE SemantMe() {
		TYPE t = this.type.getTYPE(lineNum);

		if (t == TYPE_VOID.getInstance()) {
			System.out.format(">> ERROR [%d] parameters cannot be of type 'void'", lineNum);
			throw new SemanticErrorException(String.valueOf(lineNum));
		}

		if (SYMBOL_TABLE.getInstance().isInScope(this.id)) {
			System.out.format(">> ERROR [%d] parameter '%s' already exists\n",
					lineNum, this.id);
			throw new SemanticErrorException(String.valueOf(lineNum));
		}
		SYMBOL_TABLE.getInstance().enter(this.id, t, false);
		return t;
	}
}
