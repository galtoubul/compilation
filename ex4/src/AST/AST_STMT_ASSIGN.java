package AST;

import java.util.Optional;

import TYPES.TYPE;
import ast_annotation.AstAnnotation;
import ast_notation_type.AstNotationType;
import global_variables.GlobalVariables;
import TEMP.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN extends AST_STMT {
	/***************/
	/* var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public AstAnnotation astAnnotation;
	public String varName;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var, AST_EXP exp) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe() {
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null)
			var.PrintMe();
		if (exp != null)
			exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"ASSIGN\nleft := right\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
	}

	public TYPE SemantMe(Optional<String> classId) {
		System.out.println("-- AST_STMT_ASSIGN SemantMe");
		TYPE t1 = var.SemantMe(classId);
		TYPE t2 = exp.SemantMe(classId);

		// Compare the two types
		if (!TYPE.isSubtype(t2, t1)) {
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n", 6, 6);
			throw new SemanticErrorException("" + lineNum);
		}

		AST_VAR_SIMPLE varSimple;
		if (var instanceof AST.AST_VAR_SIMPLE) {
			varSimple = ((AST.AST_VAR_SIMPLE) var);
		} else if (var instanceof AST.AST_VAR_SUBSCRIPT) {
			varSimple = ((AST.AST_VAR_SUBSCRIPT) var).var.getSimple();
		} else { // instanceof AST.AST_VAR_FIELD
			varSimple = ((AST.AST_VAR_FIELD) var).var.getSimple();
		}

		Optional<SymbolTableEntry> entry = SYMBOL_TABLE.getInstance().findEntry(varSimple.name);
		varName = varSimple.name;
		setNotation(Optional.of(entry.get().position));

		return null;
	}

	private void setNotation(Optional<Integer> offset) {
		System.out.println("-- AST_STMT_ASSIGN setNotation");
		ScopeType scopeType = SYMBOL_TABLE.getInstance().getScopeTypeByEntryName(varName);
		AstNotationType astNotationType = SYMBOL_TABLE.getInstance().findEntry(varName).get().astNotationType;

		System.out.println("\t\tvariable scope type = " + scopeType);

		if (scopeType == ScopeType.Global) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, Optional.empty());
			System.out.format("\t\t%s is a global variable\n", varName);
		} else if (astNotationType == AstNotationType.parameter) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.PARAMETER, offset);
			System.out.format("\t\t%s is a parameter | its index = %s\n", varName, offset);
		} else { // local
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, offset);
			System.out.format("\t\t%s is a local variable | its index = %s\n", varName, offset);
		}

		// if (scopeType == ScopeType.Global) {
		// astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, offset);
		// System.out.format("\t\t%s is a global variable\n", varName);
		// } else { // local
		// astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, offset);
		// int ind = offset.orElse(-1);
		// System.out.format("\t\t%s is a local variable | its index = %s\n", varName,
		// ind);
		// }
	}

	@Override
	public void IRme() {
		System.out.println("-- AST_STMT_ASSIGN IRme");

		TEMP rValueTmp = exp.IRme();
		if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR) {
			System.out.format("\t\t%s is a global variable\n", varName);

			String globalVarLabel = GlobalVariables.getGlobalVarLabel(varName);

			IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Global_Var(globalVarLabel, rValueTmp));
		} else {
			// local variable
			System.out.format("\t\t%s is a local variable\n", varName);

			int localVarInd = astAnnotation.ind.orElse(-1);
			if (var instanceof AST.AST_VAR_SUBSCRIPT) {
				TEMP arrayTmp = ((AST_VAR_SUBSCRIPT) var).var.IRme();
				AST_EXP subscript = ((AST_VAR_SUBSCRIPT) var).subscript;
				// IR.getInstance().Add_IRcommand(new
				// IRcommand_Initialize_Tmp_With_Local_Var(arrayTmp, localVarInd));
				TEMP offsetTmp = subscript.IRme();
				IR.getInstance()
						.Add_IRcommand(new IRcommand_Assign_To_Local_Array_Element(arrayTmp, offsetTmp, rValueTmp));
			} else if (var instanceof AST.AST_VAR_FIELD) {
				TEMP varOfTmp = ((AST_VAR_FIELD) var).var.IRme();
				int fieldInd = ((AST_VAR_FIELD) var).astAnnotation.ind.orElse(-1);
				IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Field(varOfTmp, fieldInd, rValueTmp));

			} else {
				// instanceof AST.AST_VAR_SIMPLE
				IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Local_Var(localVarInd, rValueTmp));
			}
		}
	}
}
