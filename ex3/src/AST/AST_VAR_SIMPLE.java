package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeEntry;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_FUNCTION;

public class AST_VAR_SIMPLE extends AST_VAR {

	public String name;

	public AST_VAR_SIMPLE(String name) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> ID( %s )\n", name);

		this.name = name;
	}

	public void PrintMe() {
		System.out.format("AST NODE SIMPLE VAR( %s )\n", name);

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("SIMPLE\nVAR\n(%s)", name));
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.format("-- AST_VAR_SIMPLE SemantMe, var name = %s\n", name);

		Optional<TYPE> type = SYMBOL_TABLE.getInstance().findPotentialMember(name);
		if (type.isPresent()) {
			if (type.get().isFunction()) {
				System.out.format(">> ERROR [line] '%s' is a not a variable\n", name);
			} else {
				return type.get();
			}
		} else {
			System.out.format(">> ERROR [line] variable '%s' does not exist at the current scope/outer scopes\n", name);
		}

		throw new semanticErrorException("line");
	}
}
