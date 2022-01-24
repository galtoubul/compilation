package AST;

import java.util.Optional;

import TYPES.TYPE;
import TYPES.TYPE_ARRAY;
import TYPES.TYPE_INT;
import TEMP.*;
import IR.*;
import ast_annotation.*;
import SYMBOL_TABLE.*;
import global_variables.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR {
	public AST_VAR var;
	public String varName;
	public AST_EXP subscript;
	public AstAnnotation astAnnotation;

	public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript) {

		SerialNumber = AST_Node_Serial_Number.getFresh();

		System.out.print("====================== var -> var [ exp ]\n");

		this.var = var;
		this.subscript = subscript;
	}

	public void PrintMe() {

		System.out.print("AST NODE SUBSCRIPT VAR\n");

		if (var != null)
			var.PrintMe();
		if (subscript != null)
			subscript.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"SUBSCRIPT\nVAR\n...[...]");

		if (var != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		if (subscript != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, subscript.SerialNumber);
	}

	@Override
	public TYPE SemantMe(Optional<String> classId) {
		System.out.println("-- AST_VAR_SUBSCRIPT SemantMe");
		TYPE type = this.var.SemantMe(classId);

		// Check that the variable is an array
		if (!type.isArray()) {
			System.out.format(">> ERROR [%d:%d] not an array\n", 2, 2);
			throw new SemanticErrorException("" + lineNum);
		}

		// Validate the subscript to be integral
		if (subscript.SemantMe(classId) != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] non integral array length\n", 2, 2);
			throw new SemanticErrorException("" + lineNum);
		}

		// Validate positive constant subscript
		if (this.subscript instanceof AST_EXP_INT &&
				((AST_EXP_INT) this.subscript).value < 0) {
			System.out.format(">> ERROR [%d:%d] negative constant array length\n", 2, 2);
			throw new SemanticErrorException("" + lineNum);
		}

		Optional<SymbolTableEntry> entry = SYMBOL_TABLE.getInstance().findEntry(((AST.AST_VAR_SIMPLE)var).name);
		varName = ((AST.AST_VAR_SIMPLE)var).name;
//		setNotation(Optional.of(entry.get().position));

		return ((TYPE_ARRAY) type).type;
	}

	private void setNotation(Optional<Integer> localVarInd) {
		System.out.println("-- AST_VAR_SUBSCRIPT setNotation");
		ScopeType scopeType = SYMBOL_TABLE.getInstance().getScopeTypeByEntryName(varName);
		System.out.println("\t\tvariable scope type = " + scopeType);

		if (scopeType == scopeType.Global) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, localVarInd);
			System.out.format("\t\t%s is a global variable\n", varName);
		}
		else { // local
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, localVarInd);
			int ind = localVarInd.orElse(-1);
			System.out.format("\t\t%s is a local variable | its index = %s\n", varName, ind);
		}
	}

	public TEMP IRme() {
		System.out.println("-- AST_VAR_SUBSCRIPT IRme");

		String callerClassName = (Thread.currentThread().getStackTrace())[2].getClassName();
		System.out.println("\t\tcaller's class = " + callerClassName);

		TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP arrayTmp = var.IRme();
		TEMP subscriptTmp = subscript.IRme();

		if (callerClassName == "AST.AST_STMT_ASSIGN") {
			IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Temp_With_Offset(tmp, arrayTmp, subscriptTmp));
		}
		else {
			IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Temp_With_Array_Element(tmp, arrayTmp, subscriptTmp));
		}

		return tmp;
	}

//	public TEMP IRme() {
//		System.out.println("-- AST_VAR_SUBSCRIPT IRme");
//
//		TEMP arrayElementTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
//		TEMP arrayTmp = var.IRme();
//		TEMP subscriptTmp = subscript.IRme();
//		IR.getInstance().Add_IRcommand(new IRcommand_Initialize_Temp_With_Array_Element(arrayElementTmp, arrayTmp, subscriptTmp));
//
//		return arrayElementTmp;
//	}
}
