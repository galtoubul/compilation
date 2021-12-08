package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_LIST;

public class AST_VAR_FIELD extends AST_VAR {
	public AST_VAR var;
	public String fieldName;

	public AST_VAR_FIELD(AST_VAR var, String fieldName) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n", fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe() {
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null)
			var.PrintMe();
		System.out.format("FIELD NAME( %s )\n", fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FIELD\nVAR\n...->%s", fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
	}

	public TYPE SemantMe() {
		TYPE t = null;
		TYPE_CLASS tc = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null)
			t = var.SemantMe();
		System.out.println("t.name = " + t.name);
		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (t.isClass() == false) {
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n", 6, 6, fieldName);
			System.exit(0);
		} else {
			tc = (TYPE_CLASS) t;
			System.out.println("tc.name = " + tc.name);

		}

		/************************************/
		/* [3] Look for fiedlName inside tc */
		/************************************/

		for (TYPE_LIST it = tc.data_members; it != null; it = it.tail) {
			System.out.println("it.name = " + it.head.name);
			System.out.println("fieldName = " + fieldName);
			if (it.head.name.equals(fieldName)) {
				return it.head;
			}
		}



		/*********************************************/
		/* [4] fieldName does not exist in class var */
		/*********************************************/
		System.out.format(">> ERROR [%d:%d] field %s does not exist in class\n", 6, 6, fieldName);
		System.exit(0);
		return null;
	}
}
