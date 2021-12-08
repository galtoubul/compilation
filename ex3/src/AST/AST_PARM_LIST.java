package AST;

public class AST_PARM_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_PARAM head;
	public AST_PARM_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PARM_LIST(AST_PARAM head, AST_PARM_LIST tail)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== PARMs -> PARM PARMs\n");
		if (tail == null) System.out.print("====================== PARMs -> PARM      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe()
	{
		System.out.print("AST NODE PARM LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PARM\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
}
