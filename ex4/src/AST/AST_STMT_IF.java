package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_INT;
import labels.Labels;
import TEMP.*;
import IR.*;

public class AST_STMT_IF extends AST_STMT {
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body) {
		this.cond = cond;
		this.body = body;
	}

	public void PrintMe() {
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE STMT IF\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (cond != null)
			cond.PrintMe();
		if (body != null)
			body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"IF (left)\nTHEN right");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
		if (body != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
	}

	@Override
	public TYPE SemantMe(Optional<String> classId, int localVarIndex) {
		System.out.println("-- AST_STMT_IF\n\n\t line num = " + lineNum);

		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe(classId) != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n", 2, 2);
			throw new SemanticErrorException(String.valueOf(lineNum));
		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope(ScopeType.Block, null);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantMe(classId, localVarIndex);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

	@Override
	public void IRme() {
		System.out.println("-- AST_STMT_IF IRme");

		TEMP condTmp = cond.IRme();
		String label = Labels.getAvialableLabel("after_if_block");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(condTmp, label));
		body.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label));
	}
}