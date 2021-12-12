package AST;

import java.util.Optional;

import SYMBOL_TABLE.SYMBOL_TABLE;
import SYMBOL_TABLE.ScopeType;
import TYPES.TYPE;
import TYPES.TYPE_INT;

public class AST_STMT_WHILE extends AST_STMT {
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/* CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond, AST_STMT_LIST body) {
		this.cond = cond;
		this.body = body;
	}

	public TYPE SemantMe(Optional<String> classId) {
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe(classId) != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] condition inside WHILE is not integral\n", 2, 2);
		}

		/*************************/
		/* [1] Begin Loop Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope(ScopeType.Block, null);

		/***************************/
		/* [2] Semant Loop body */
		/***************************/
		body.SemantMe(classId);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/**************************************************/
		/* [4] Return value is irrelevant for while loops */
		/**************************************************/
		return null;
	}
}