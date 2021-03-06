package AST;

import java.util.Optional;

import TYPES.TYPE;
import TEMP.*;

public class AST_EXP_VAR extends AST_EXP {
	public AST_VAR var;
	public String varTypeString;

	public AST_EXP_VAR(AST_VAR var) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== exp -> var\n");

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

		TYPE varType = this.var.SemantMe(classId);

		// won't reach this point if varType = null since the above SemantMe should
		// throw an error
		if (varType == null) {
			System.out.format(">> ERROR [" + lineNum + "] parameter use before define\n");
			throw new SemanticErrorException("" + lineNum);
		}

		System.out.println("-- AST_EXP_VAR\n\t\tvar type = " + varType.name);

		this.varTypeString = varType.name;
		return varType;
	}

	public TEMP IRme() {
		System.out.println("-- AST_EXP_VAR IRme");
		TEMP varTemp = var.IRme();
		return varTemp;
	}
}
