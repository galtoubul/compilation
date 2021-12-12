package AST;

import TYPES.TYPE_LIST;
import java.util.Optional;

public class AST_CFIELD_LIST extends AST_Node {
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;

	public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null)
			System.out.print("====================== CFIELDs -> CFIELD CFIELDs\n");
		if (tail == null)
			System.out.print("====================== CFIELDs -> CFIELD      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe() {
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

	public TYPE_LIST SemantMe(Optional<String> classId) {
		if (classId.isPresent())
			System.out.println("-- AST_CFIELD_LIST SemantMe of class that extends class" + classId);
		else
			System.out.println("-- AST_CFIELD_LIST SemantMe");

		if (tail == null) {
			System.out.println("-- AST_CFIELD_LIST SemantMe\n\t\tcfield list's tail is null");
			TYPE_LIST list = new TYPE_LIST(head.SemantMe(classId),null);
			return list;
		} else {
			System.out.println("-- AST_CFIELD_LIST SemantMe\n\t\tcfield list's tail isnt null");
			TYPE_LIST list = new TYPE_LIST(head.SemantMe(classId), tail.SemantMe(classId));
			return list;
		}
	}
}
//			System.out.println(list.head.name);
//			System.out.println("cfield list " + list.head.name);