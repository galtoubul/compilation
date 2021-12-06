package AST;

import TYPES.TYPE_LIST;

public class AST_CFIELD_LIST extends AST_Node {
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== CFIELDs -> CFIELD CFIELDs\n");
		if (tail == null)
			System.out.print("====================== CFIELDs -> CFIELD      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a cfield list AST node */
	/******************************************************/
	public void PrintMe() {
		/**************************************/
		/* AST NODE TYPE = AST CFIELD LIST */
		/**************************************/
		System.out.print("AST NODE CFIELD LIST\n");

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
				"CFIELD\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public TYPE_LIST SemantMe() {
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
