package AST;

import TYPES.TYPE_LIST;

public class AST_EXP_LIST extends AST_Node {
	public AST_EXP head;
	public AST_EXP_LIST tail;

	public AST_EXP_LIST(AST_EXP head, AST_EXP_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== EXPs -> EXP EXPs\n");
		if (tail == null)
			System.out.print("====================== EXPs -> EXP      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe() {
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE EXP LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null)
			head.PrintMe();
		if (tail != null)
			tail.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"EXP\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public TYPE_LIST SemantMe() {
		System.out.println("-- AST_EXP_LIST SemantMe");

		if (tail == null) {
			return new TYPE_LIST(
					head.SemantMe(),
					null);
		} else {
			return new TYPE_LIST(
					head.SemantMe(),
					tail.SemantMe());
		}
	}

}
