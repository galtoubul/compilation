package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;

public class AST_VAR_SIMPLE extends AST_VAR {

	public String name;

	public AST_VAR_SIMPLE(String name) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n", name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
	}

	public void PrintMe() {
		System.out.format("AST NODE SIMPLE VAR( %s )\n", name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("SIMPLE\nVAR\n(%s)", name));
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.format("-- AST_VAR_SIMPLE SemantMe, var name = %s\n", name);
		TYPE t = SYMBOL_TABLE.getInstance().find(name);
		return t;
	}
}
