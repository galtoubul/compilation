package AST;

import java.util.Optional;

import MIPS.MIPSGenerator;
import TYPES.*;
import TEMP.*;
import IR.IRcommand_Binop_Add_Integers;
import IR.IRcommand_Binop_Sub_Integers;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP {
	Binop OP;
	public AST_EXP left;
	public AST_EXP right;
	public TYPE leftType;
	public TYPE rightType;

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
		System.out.println("-- AST_EXP_BINOP SemantMe");
		TYPE t1 = null;
		TYPE t2 = null;

		if (left != null) {
			t1 = left.SemantMe(classId);
		}
		if (right != null) {
			t2 = right.SemantMe(classId);
		}
		this.leftType = t1;
		this.rightType = t2;
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

	public TEMP IRme() {
		System.out.println("-- AST_EXP_BINOP IRme");

		System.out.format("\t\tleftType = %s | rightType = %s\n", leftType.name, rightType.name);

		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		if (left != null) {
			t1 = left.IRme();
		}
		if (right != null) {
			t2 = right.IRme();
		}

		switch (OP) {
			case PLUS:
				if (leftType.name.equals("string") && rightType.name.equals("string")) {
					System.out.format("\t\t-- strings concatenation");
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Concat_Strings(dst, t1, t2));
				}
				else {
					System.out.println("\t\t-- summing integers");
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, t1, t2));
				}
				break;
			case MINUS:
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst, t1, t2));
				break;
			case TIMES:
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst, t1, t2));
				break;
			case DIVIDE:
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Div_Integers(dst, t1, t2));
				break;
			case EQ:
				if (leftType.name.equals("string") && rightType.name.equals("string") ) {
					System.out.println("\t\t-- equality between strings");
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, t1, t2));
				}
				else {
					System.out.println("\t\t-- equality between integers");
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, t1, t2));
				}
				break;
			case LT:
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst, t1, t2));
				break;
			case GT:
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst, t1, t2));
				break;
		}

		return dst;
	}
}
