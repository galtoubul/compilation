package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_VAR_DEC;
import TYPES.TYPE_LIST;
import TEMP.*;

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

	// look for the field name in the given TYPE_CLASS
	// if found -> returns null. OW, returns null
	public TYPE lookForFieldNameInClassDataMembers(TYPE_CLASS varTypeClass) {
		for (TYPE_LIST dataMembersList = varTypeClass.data_members; dataMembersList != null; dataMembersList = dataMembersList.tail) {
			System.out.println("-- AST_VAR_FIELD\n\t\tdata member name = " + dataMembersList.head.name);
			System.out.println("-- AST_VAR_FIELD\n\t\tfieldName = " + fieldName);

			if (dataMembersList.head.name.equals(fieldName)) {
				if (dataMembersList.head instanceof TYPE_CLASS_VAR_DEC) {
					return ((TYPE_CLASS_VAR_DEC) dataMembersList.head).type;
				}
				return dataMembersList.head;
			}
		}
		return null;
	}

	@Override
	public TYPE SemantMe(Optional<String> fatherClassId) {
		System.out.println("-- AST_VAR_FIELD SemantMe");

		TYPE varType = null;

		// Recursively semant var
		if (var != null) {
			varType = var.SemantMe(fatherClassId);
		}
		System.out.println("-- AST_VAR_FIELD\n\t\tvariable type = " + varType.name);

		// Make sure varType is a class
		if (!varType.isClass()) {
			System.out.format(">> ERROR [" + lineNum + "] access %s field of a non-class variable\n", fieldName);
			throw new SemanticErrorException("" + lineNum);
		}

		Optional<TYPE> field = ((TYPE_CLASS) varType).lookupMemberInAncestors(this.fieldName);
		if (field.isPresent()) {
			if (field.get() instanceof TYPE_CLASS_VAR_DEC) {
				return ((TYPE_CLASS_VAR_DEC) field.get()).type;
			}
			System.out.format(">> ERROR [" + lineNum + "] '%s' is a method, not a field variable\n", fieldName);
			throw new SemanticErrorException("" + lineNum);
		}

		// fieldName does not exist in class var
		System.out.format(">> ERROR [" + lineNum + "] there is no field named '%s' in class '%s'\n", fieldName, varType.name);
		throw new SemanticErrorException("" + lineNum);
	}

	// TODO
	public TEMP IRme() {
		System.out.println("-- AST_VAR_FIELD IRme");
		return null;
	}
}
