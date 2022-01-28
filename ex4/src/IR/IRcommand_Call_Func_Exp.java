package IR;

import TEMP.*;

import java.util.Map;

import MIPS.*;

public class IRcommand_Call_Func_Exp extends IRcommand_Call_Func {

	public IRcommand_Call_Func_Exp(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		super(dst, funcName, argsTempList);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Call_Func_Exp MIPSme");
		MIPSGenerator.getInstance().callFuncExp(tempMap.get(dst), funcName, argsTempList.mapTempsToRegs(tempMap));
	}
}
