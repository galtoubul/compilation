package AST;

import java.util.Optional;

import TYPES.TYPE;
import TEMP.*;

public class AST_STMT_LIST extends AST_Node {

	public AST_STMT head;
	public AST_STMT_LIST tail;
	public int localVarIndex = 0;

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
		String callerClassName = e.getClassName();
		System.out.println("-- AST_STMT_LIST SemantMe\n\t\tcaller's class = " + callerClassName);

		Boolean isPartOfFunc = (callerClassName == "AST.AST_FUNC_DEC") ? true : false;
		if (isPartOfFunc)
			System.out.println("\t\tstmts of function/method");

		if (head != null) {
			System.out.println("\t\thead != null");
			if (head instanceof AST.AST_STMT_VAR_DEC) {
				Optional<Integer> localVarIndexOpt = Optional.of(localVarIndex + 1);
				System.out.println("\t\thead is instance of AST.AST_STMT_VAR_DEC\n\t\tlocalVarIndexOpt = " + localVarIndexOpt);
				((AST.AST_STMT_VAR_DEC)head).SemantMe(classId, localVarIndexOpt);
			}
			else {
				head.SemantMe(classId);
			}
		}
		if (tail != null) {
			tail.localVarIndex = localVarIndex + 1;
			System.out.println("\t\ttail != null");
			tail.SemantMe(classId);
		}

		return null;
	}

	public TEMP IRme() {
		System.out.println("-- AST_STMT_LIST IRme");
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();

		return null;
	}
}
