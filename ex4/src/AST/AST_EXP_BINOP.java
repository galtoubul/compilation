package AST;

import java.util.Optional;

import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP {
	Binop OP;
	public AST_EXP left;
	public AST_EXP right;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, Binop OP) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}

	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe() {

		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");
		System.out.format("BINOP EXP(%s)\n", OP.type);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null)
			left.PrintMe();
		if (right != null)
			right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("BINOP(%s)", OP.type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, left.SerialNumber);
		if (right != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, right.SerialNumber);
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		TYPE t1 = null;
		TYPE t2 = null;

		if (left != null) {
			t1 = left.SemantMe(classId);
		}
		if (right != null) {
			t2 = right.SemantMe(classId);
		}

		switch (OP) {
			case EQ:
				if (!TYPE.isSubtype(t2, t1) && !TYPE.isSubtype(t1, t2)) {
					System.out.format(">> ERROR [%d:%d] comparing values with different types\n",
							2, 2);
					throw new SemanticErrorException("" + lineNum);
				}
				break;
			case PLUS:
				if ((t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance())) {
					return TYPE_STRING.getInstance();
				}
				if (!(t1 == TYPE_INT.getInstance()) || !(t2 == TYPE_INT.getInstance())) {
					System.out.format(">> ERROR [%d:%d] invalid types for '%s' operation\n", 2, 2, OP.type);
					throw new SemanticErrorException("" + lineNum);
				}
				break;
			case DIVIDE:
				if (!(t1 == TYPE_INT.getInstance()) || !(t2 == TYPE_INT.getInstance())) {
					System.out.format(">> ERROR [%d:%d] invalid types for '%s' operation\n", 2, 2, OP.type);
					throw new SemanticErrorException("" + lineNum);
				}
				if (right instanceof AST_EXP_INT && ((AST_EXP_INT) right).value == 0) {
					System.out.format(">> ERROR [%d:%d] division by zero\n", 2, 2);
					throw new SemanticErrorException("" + lineNum);
				}
			default:
				if (!(t1 == TYPE_INT.getInstance()) || !(t2 == TYPE_INT.getInstance())) {
					System.out.format(">> ERROR [%d:%d] invalid types for '%s' operation\n", 2, 2, OP.type);
					throw new SemanticErrorException("" + lineNum);
				}
		}

		return TYPE_INT.getInstance();
	}

}
