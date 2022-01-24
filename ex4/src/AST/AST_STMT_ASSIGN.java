package AST;

import java.util.Optional;

import TYPES.TYPE;
import ast_annotation.AstAnnotation;
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

		Optional<SymbolTableEntry> entry = SYMBOL_TABLE.getInstance().findEntry(((AST.AST_VAR_SIMPLE) var).name);
		varName = ((AST.AST_VAR_SIMPLE) var).name;
		setNotation(Optional.of(entry.get().position));

		return null;
	}

	private void setNotation(Optional<Integer> localVarInd) {
		System.out.println("-- AST_STMT_ASSIGN setNotation");
		ScopeType scopeType = SYMBOL_TABLE.getInstance().getScopeTypeByEntryName(varName);
		System.out.println("\t\tvariable scope type = " + scopeType);

		if (scopeType == ScopeType.Global) {
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.GLOBAL_VAR, localVarInd);
			System.out.format("\t\t%s is a global variable\n", varName);
		} else { // local
			astAnnotation = new AstAnnotation(AstAnnotation.TYPE.LOCAL_VAR, localVarInd);
			int ind = localVarInd.orElse(-1);
			System.out.format("\t\t%s is a local variable | its index = %s\n", varName, ind);
		}
	}

	public TEMP IRme() {
		System.out.println("-- AST_STMT_ASSIGN IRme");

		TEMP tmpRvalue = exp.IRme();

		if (astAnnotation.type == AstAnnotation.TYPE.GLOBAL_VAR) {
			System.out.format("\t\t%s is a global variable\n", varName);

			String globalVarLabel = GlobalVariables.getGlobalVarLabel(varName);
			String globalVarType = GlobalVariables.getGlobalVarType(((AST.AST_VAR_SIMPLE) var).name);

			IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Global_Var(globalVarLabel, tmpRvalue));
		} else { // local variable
			System.out.format("\t\t%s is a local variable\n", varName);

			int localVarInd = astAnnotation.ind.orElse(-1);
			IR.getInstance().Add_IRcommand(new IRcommand_Assign_To_Local_Var(localVarInd, tmpRvalue));
		}

		return null;
	}
}
