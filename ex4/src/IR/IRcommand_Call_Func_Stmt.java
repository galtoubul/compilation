package IR;

import TEMP.*;

import java.util.Map;

import MIPS.*;

public class IRcommand_Call_Func_Stmt extends IRcommand_Call_Func {

	public IRcommand_Call_Func_Stmt(String funcName, TEMP_LIST argsTempList) {
		super(funcName, argsTempList);
		System.out.println("-- IRcommand_Call_Func_Stmt");
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Call_Func_Stmt MIPSme");
		MIPSGenerator.getInstance().callFuncStmt(funcName, argsTempList.mapTempsToRegs(tempMap));
	}
}
