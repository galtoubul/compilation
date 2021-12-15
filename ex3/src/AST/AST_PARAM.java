package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
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
		Optional<TYPE> type = Optional.ofNullable(SYMBOL_TABLE.getInstance().find(this.type.name()));
		if (type.isPresent()) {
			if (type.get() == TYPE_VOID.getInstance()) {
				System.out.println(">> ERROR [line] parameters cannot be of type 'void'");
				throw new semanticErrorException("line");
			}
			if (SYMBOL_TABLE.getInstance().isInScope(this.id)) {
				System.out.format(">> ERROR [line] parameter '%s' already exists\n",
						this.id);
				throw new semanticErrorException("line");
			}
			SYMBOL_TABLE.getInstance().enter(this.id, type.get());
			return type.get();
		}
		System.out.format(">> ERROR [line] non existing type '%s' for parameter '%s'\n",
				this.type.name(), this.id);
		throw new semanticErrorException("line");
	}
}
