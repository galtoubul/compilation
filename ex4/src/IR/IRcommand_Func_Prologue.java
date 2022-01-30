/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.MIPSGenerator;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TEMP.TEMP;
import TYPES.TYPE_FUNCTION;

public class IRcommand_Func_Prologue implements IRcommand {
	int localVarsNum;
	String funcName;

	public IRcommand_Func_Prologue(String funcName, int localVarsNum) {
		System.out.println("-- IRcommand_Func_Prologue");
		TYPE_FUNCTION t = (TYPE_FUNCTION) SYMBOL_TABLE.getInstance().find(funcName);
		this.localVarsNum = localVarsNum;
		System.out.println("\t\tlocalVarsNum = " + localVarsNum);
		this.funcName = funcName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Func_Prologue MIPSme");
		MIPSGenerator.getInstance().funcPrologue(localVarsNum, funcName);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		return new HashSet<>();
	}
}
