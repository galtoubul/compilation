package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.SymbolTableEntry;
import TYPES.TYPE;
import TYPES.TYPE_VOID;
import java.util.Optional;
import AstNotationType.AstNotationType;

public class AST_PARAM extends AST_Node {
	public AST_TYPE type;
	public String id;

	public AST_PARAM(AST_TYPE type, String id) {
		System.out.println("-- AST_PARAM\n\tctor");
		this.type = type;
		this.id = id;
	}

	public TYPE SemantMe(int index) {
		System.out.println("-- AST_PARAM SemantMe\n\t\tid = " + id + " | index = " + index);
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
		Optional<Integer> indexOpt = Optional.of(index);
		AstNotationType astNotationType = AstNotationType.parameter;
		Optional<AstNotationType> astNotationTypeOpt = Optional.of(astNotationType);
		SYMBOL_TABLE.getInstance().enter(this.id, t, false, indexOpt, astNotationTypeOpt);
		return t;
	}
}
