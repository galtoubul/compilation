/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;
import SYMBOL_TABLE.*;
import TYPES.*;

public class IRcommand_Func_Prologue extends IRcommand {
	int localVarsNum;
	String funcName;
	
	public IRcommand_Func_Prologue(String funcName) {
		System.out.println("-- IRcommand_Func_Prologue");
		TYPE_FUNCTION t = (TYPE_FUNCTION)SYMBOL_TABLE.getInstance().find(funcName);
		this.localVarsNum = t.localVarsNum;
		System.out.println("\t\tlocalVarsNum = " + localVarsNum);
		this.funcName = funcName;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().funcPrologue(localVarsNum, funcName);
	}
}
