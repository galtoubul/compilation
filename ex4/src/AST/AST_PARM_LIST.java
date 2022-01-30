package AST;

import TYPES.TYPE_LIST;

public class AST_PARM_LIST extends AST_Node {
	public AST_PARAM head;
	public AST_PARM_LIST tail;

	public AST_PARM_LIST(AST_PARAM head, AST_PARM_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null)
			System.out.print("====================== PARMs -> PARM PARMs\n");
		if (tail == null)
			System.out.print("====================== PARMs -> PARM      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe() {
		System.out.print("AST NODE PARM LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null)
			head.PrintMe();
		if (tail != null)
			tail.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"PARM\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public TYPE_LIST SemantMe(int index) {
		System.out.println("-- AST_PARM_LIST SemantMe");

		if (this.head == null) {
			return new TYPE_LIST(null, null);
		} else if (tail == null) {
			System.out.println("\t\tparam index = " + index);
			return new TYPE_LIST(head.SemantMe(index + 1), null);
		} else {
			System.out.println("\t\tparam index = " + index);
			return new TYPE_LIST(head.SemantMe(index + 1), tail.SemantMe(index + 1));
		}
	}

	// TODO
	public void IRme() {
		System.out.println("-- AST_PARM_LIST IRme");
	}
}
