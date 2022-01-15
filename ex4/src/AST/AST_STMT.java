package AST;

import java.util.Optional;

import TYPES.TYPE;
import TEMP.*;

public abstract class AST_STMT extends AST_Node {
	public void PrintMe() {
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	abstract public TYPE SemantMe(Optional<String> classId);
	abstract public TEMP IRme();
}
