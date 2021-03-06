package AST;

import java.util.Optional;
import java.util.stream.IntStream;

import IR.*;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;
import TYPES.TYPE_CLASS_VAR_DEC;
import TEMP.*;
import ast_annotation.AstAnnotation;

public class AST_VAR_FIELD extends AST_VAR {
	public AST_VAR var;
	public String fieldName;
	public AstAnnotation astAnnotation;

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
		for (TYPE member : varTypeClass.data_members) {
			System.out.println("-- AST_VAR_FIELD\n\t\tdata member name = " + member.name);
			System.out.println("-- AST_VAR_FIELD\n\t\tfieldName = " + fieldName);

			if (member.name.equals(fieldName)) {
				if (member instanceof TYPE_CLASS_VAR_DEC) {
					return ((TYPE_CLASS_VAR_DEC) member).type;
				}
				return member;
			}
		}
		return null;
	}

	@Override
	public TYPE SemantMe(Optional<String> fatherClassId) {
		System.out.println("-- AST_VAR_FIELD SemantMe");

		TYPE_CLASS varType = null;

		// Recursively semant var
		if (var != null) {
			varType = (TYPE_CLASS) var.SemantMe(fatherClassId);
		}
		System.out.println("-- AST_VAR_FIELD\n\t\tvariable type = " + varType.name);

		// Make sure varType is a class
		if (!varType.isClass()) {
			System.out.format(">> ERROR [" + lineNum + "] access %s field of a non-class variable\n", fieldName);
			throw new SemanticErrorException("" + lineNum);
		}

		Optional<TYPE> field = varType.lookupMemberInAncestors(this.fieldName);
		if (field.isPresent()) {
			if (field.get() instanceof TYPE_CLASS_VAR_DEC) {
				this.astAnnotation = new AstAnnotation(AstAnnotation.TYPE.FIELD, this.fieldOffset(varType));
				return ((TYPE_CLASS_VAR_DEC) field.get()).type;
			}
			System.out.format(">> ERROR [" + lineNum + "] '%s' is a method, not a field variable\n", fieldName);
			throw new SemanticErrorException("" + lineNum);
		}
		// fieldName does not exist in class var
		System.out.format(">> ERROR [" + lineNum + "] there is no field named '%s' in class '%s'\n", fieldName,
				varType.name);
		System.out.format(">> ERROR [" + lineNum + "] there is no field named '%s' in class '%s'\n", fieldName,
				varType.name);
		throw new SemanticErrorException("" + lineNum);
	}

	private Optional<Integer> fieldOffset(TYPE_CLASS classToSearch) {
		return IntStream.range(0, classToSearch.initialValues.size())
				.boxed()
				.filter(index -> classToSearch.initialValues.get(index).getKey().equals(this.fieldName))
				.findFirst();
	}

	// TODO
	public TEMP IRme() {
		System.out.println("-- AST_VAR_FIELD IRme");
		TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP varTmp = var.IRme();
		IR.getInstance().Add_IRcommand(
				new IRcommand_Initialize_Temp_With_Object_Field(tmp, varTmp, astAnnotation.ind.orElse(-1)));
		return tmp;
	}
}
