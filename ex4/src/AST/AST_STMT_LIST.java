package AST;

import java.util.Optional;

import TYPES.TYPE;

public class AST_STMT_LIST extends AST_Node {

	public AST_STMT head;
	public AST_STMT_LIST tail;

	public AST_STMT_LIST(AST_STMT head, AST_STMT_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null)
			System.out.print("====================== stmts -> stmt stmts\n");
		if (tail == null)
			System.out.print("====================== stmts -> stmt      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe() {
		System.out.print("AST NODE STMT LIST\n");

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
				"STMT\nLIST\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public TYPE SemantMe(Optional<String> classId) {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
		String methodName = e.getClassName();
//		String callerClassName = new Exception().getStackTrace()[1].getClassName();
		System.out.println("-- AST_STMT_LIST SemantMe\ncaller's class = " + methodName);
		if (head != null) {
			System.out.println("\t\thead != null");
			if (head instanceof AST_STMT_ASSIGN)
				System.out.println("instanceof AST_STMT_ASSIGN");
			head.SemantMe(classId);
		}
		if (tail != null) {
			System.out.println("\t\ttail != null");
			tail.SemantMe(classId);
		}

		return null;
	}
}
