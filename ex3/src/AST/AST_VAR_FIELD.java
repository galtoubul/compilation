package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_LIST;

public class AST_VAR_FIELD extends AST_VAR {
	public AST_VAR var;
	public String fieldName;

	public AST_VAR_FIELD(AST_VAR var, String fieldName) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.format("====================== var -> var DOT ID( %s )\n", fieldName);

		this.var = var;
		this.fieldName = fieldName;
	}

	public void PrintMe() {

		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null)
			var.PrintMe();
		System.out.format("FIELD NAME( %s )\n", fieldName);

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("FIELD\nVAR\n...->%s", fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.println("-- AST_VAR_FIELD SemantMe");

		TYPE t = null;
		TYPE_CLASS tc = null;

		// Recursively semant var
		if (var != null)
			t = var.SemantMe(classId);
		System.out.println("-- AST_VAR_FIELD\n\t\tt.name = " + t.name);

		// Make sure type is a class
		if (t.isClass() == false) {
			System.out.format(">> ERROR [line] access %s field of a non-class variable\n", fieldName);
			throw new semanticErrorException("line");
		}
		else {
			tc = (TYPE_CLASS) t;
			System.out.println("-- AST_VAR_FIELD\n\t\ttc.name = " + tc.name);
		}

		// Look for fieldName inside tc
		for (TYPE_LIST it = tc.data_members; it != null; it = it.tail) {
			System.out.println("-- AST_VAR_FIELD\n\t\tit.name = " + it.head.name);
			System.out.println("-- AST_VAR_FIELD\n\t\tfieldName = " + fieldName);

			if (it.head.name.equals(fieldName)) {
				if (it.head instanceof TYPE_CLASS_VAR_DEC) {
					return ((TYPE_CLASS_VAR_DEC) it.head).t;
				}
				return it.head;
			}
		}

		// fieldName does not exist in class var
		System.out.format(">> ERROR [line] field %s does not exist in class\n", fieldName);
		throw new semanticErrorException("line");
	}
}
