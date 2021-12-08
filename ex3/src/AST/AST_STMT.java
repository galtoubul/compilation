package AST;

import TYPES.TYPE;

public abstract class AST_STMT extends AST_Node {
	public void PrintMe() {
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	abstract public TYPE SemantMe();
}
