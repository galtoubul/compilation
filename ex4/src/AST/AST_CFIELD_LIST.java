package AST;

import java.util.Optional;

import TYPES.TYPE_LIST;
import TYPES.TYPE;

public class AST_CFIELD_LIST extends AST_Node {
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;
	public int fieldIndex = 0;

	public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null)
			System.out.print("====================== CFIELDs -> CFIELD CFIELDs\n");
		if (tail == null)
			System.out.print("====================== CFIELDs -> CFIELD      \n");

		this.head = head;
		this.tail = tail;
	}

	public void PrintMe() {
		System.out.print("AST NODE CFIELD LIST\n");

		if (head != null)
			head.PrintMe();
		if (tail != null)
			tail.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"CFIELD\nLIST\n");

		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
	}

	public TYPE_LIST SemantMe(Optional<String> classId) {
		System.out.format("-- AST_CFIELD_LIST SemantMe %s\n",
				classId.isPresent() ? "of class that extends class" + classId.get() : "");

		TYPE headType = null;
		if (head != null) {
			// head can be a field or a method
			headType = head instanceof AST.AST_CFIELD_VAR_DEC ? head.SemantMe(classId,
					Optional.of(fieldIndex + 1))
					: head.SemantMe(classId, Optional.empty());
		}

		TYPE_LIST tailType = null;
		if (tail != null) {
			tail.fieldIndex = fieldIndex + 1;
			tailType = tail.SemantMe(classId);
		}

		return new TYPE_LIST(headType, tailType);
	}
}