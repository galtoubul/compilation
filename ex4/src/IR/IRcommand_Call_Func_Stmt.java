package IR;

import TEMP.*;

import java.util.Map;

import MIPS.*;

public class IRcommand_Call_Func_Stmt extends IRcommand_Call_Func {

	public IRcommand_Call_Func_Stmt(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		super(dst, funcName, argsTempList);
		System.out.println("-- IRcommand_Call_Func_Stmt");
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Call_Func_Stmt MIPSme");
		MIPSGenerator.getInstance().callFuncStmt(tempMap.getOrDefault(dst, dst.getSerialNumber()), funcName, argsTempList.mapTempsToRegs(tempMap));
	}
}
