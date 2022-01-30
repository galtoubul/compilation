package AST;

import TYPES.TYPE;

public class AST_DEC_LIST extends AST_Node {
	public AST_DEC head;
	public AST_DEC_LIST tail;

	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== DECs -> DEC DECs\n");
		if (tail == null)
			System.out.print("====================== DECs -> DEC      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public TYPE SemantMe() {
		/*************************************/
		/* RECURSIVELY semant HEAD + TAIL ... */
		/*************************************/
		System.out.println("-- AST_DEC_LIST SemantMe");
		if (head != null)
			head.SemantMe();
		if (tail != null)
			tail.SemantMe();

		return null; // won't reach this
	}

	public void PrintMe() {
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		System.out.print("AST NODE DEC LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null)
			head.PrintMe();
		if (tail != null)
			tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"DEC\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public void IRme() {
		System.out.println("-- AST_DEC_LIST IRme");
		if (head != null) {
			head.IRme();
		}
		if (tail != null) {
			tail.IRme();
		}
	}

}
