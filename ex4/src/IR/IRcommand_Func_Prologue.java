/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.*;
import SYMBOL_TABLE.*;
import TYPES.*;

public class IRcommand_Func_Prologue extends IRcommand_IDTransform {
	int localVarsNum;
	String funcName;

	public IRcommand_Func_Prologue(String funcName) {
		System.out.println("-- IRcommand_Func_Prologue");
		TYPE_FUNCTION t = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(funcName);
		this.localVarsNum = t.localVarsNum;
		System.out.println("\t\tlocalVarsNum = " + localVarsNum);
		this.funcName = funcName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Func_Prologue MIPSme");
		MIPSGenerator.getInstance().funcPrologue(localVarsNum, funcName);
	}
}
