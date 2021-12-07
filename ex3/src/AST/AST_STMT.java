package AST;

import TYPES.TYPE;

public abstract class AST_STMT extends AST_Node {
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe() {
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	abstract public TYPE SemantMe();
}
