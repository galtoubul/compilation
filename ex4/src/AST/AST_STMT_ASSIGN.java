package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_STMT_ASSIGN extends AST_STMT {
	/***************/
	/* var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe() {
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"ASSIGN\nleft := right\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
	}

	public TYPE SemantMe(Optional<String> classId) {
		System.out.println("-- AST_STMT_ASSIGN SemantMe");
		TYPE t1 = var.SemantMe(classId);
		TYPE t2 = exp.SemantMe(classId);

		// Compare the two types
		if (!TYPE.isSubtype(t2, t1)) {
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
			throw new SemanticErrorException("" + lineNum);
		}

		return null;
	}
}
