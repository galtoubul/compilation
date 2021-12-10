package AST;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_INT;

public class AST_VAR_SUBSCRIPT extends AST_VAR {
	public AST_VAR var;
	public AST_EXP subscript;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null)
			var.PrintMe();
		if (subscript != null)
			subscript.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"SUBSCRIPT\nVAR\n...[...]");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (subscript != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, subscript.SerialNumber);
	}

	public TYPE SemantMe() {
		TYPE type = this.var.SemantMe();

		// Check that the variable is an array
		if (!type.isArray()) {
			System.out.format(">> ERROR [%d:%d] not an array\n", 2, 2);
			System.exit(0);
		}

		// Validate the subscript to be integral
		if (subscript.SemantMe() != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] non integral array length\n", 2, 2);
			System.exit(0);
		}

		return ((TYPE_ARRAY) type).type;
	}
}
