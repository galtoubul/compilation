package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Call_Func_Stmt extends IRcommand {
	TEMP dst;
	String funcName;
	TEMP_LIST argsTempList;
	
	public IRcommand_Call_Func_Stmt(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		this.dst = dst;
		this.funcName = funcName;
		this.argsTempList = argsTempList;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Call_Func_Stmt MIPSme");
		MIPSGenerator.getInstance().callFuncStmt(dst, funcName, argsTempList);
	}
}
