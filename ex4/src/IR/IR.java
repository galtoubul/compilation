/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.ArrayList;

// import java.util.Optional;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IR {
	// private IRcommand head=null;
	// private IRcommandList commands = null;

	private ArrayList<IRcommand> commands = new ArrayList<>();

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd) {
		commands.add(cmd);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		for (IRcommand command : this.commands) {
			command.MIPSme();
		}

		// if (head != null)
		// head.MIPSme();
		// if (tail != null)
		// tail.MIPSme();
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance() {
		if (instance == null) {
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}

}
