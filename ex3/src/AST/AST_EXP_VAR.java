package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_NONE;

public class AST_EXP_VAR extends AST_EXP {
	public AST_VAR var;

	public AST_EXP_VAR(AST_VAR var) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> var\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
	}

	public void PrintMe() {
		System.out.print("AST NODE EXP VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null)
			var.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"EXP\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);

	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.println("-- AST_EXP_VAR SemantMe");
		TYPE t = this.var.SemantMe(classId);
		if (t == null) {
			return new TYPE_NONE();
		}
		System.out.println(t);
		return t;
	}
}
