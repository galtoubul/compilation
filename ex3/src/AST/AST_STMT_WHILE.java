package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
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

	public TYPE SemantMe() {
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe() != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] condition inside WHILE is not integral\n", 2, 2);
		}

		/*************************/
		/* [1] Begin Loop Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Loop body */
		/***************************/
		body.SemantMe();

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