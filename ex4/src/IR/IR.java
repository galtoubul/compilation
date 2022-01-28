/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import TEMP.TEMP;
import register_alloc.RegisterAllocation;
import register_alloc.RegisterAllocationErrorException;

// import java.util.Optional;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IR {
	/**
	 * The number of temporary registers (`$ti`) in the MIPS code
	 */
	private static final int MAX_TEMPS = 10;

	private ArrayList<IRcommand> commands = new ArrayList<>();

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd) {
		commands.add(cmd);
	}

	private HashMap<TEMP, Integer> allocateRegisters() {
		Optional<HashMap<TEMP, Integer>> tempMap = RegisterAllocation.allocateRegisters(this.commands, MAX_TEMPS);
		if (!tempMap.isPresent()) {
			throw new RegisterAllocationErrorException();
		}
		return tempMap.get();
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		HashMap<TEMP, Integer> tempMap = this.allocateRegisters();
		System.out.println("\n\n\n");
		for (IRcommand command : this.commands) {
			command.MIPSme(tempMap);
		}
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
