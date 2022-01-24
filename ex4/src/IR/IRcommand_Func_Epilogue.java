/***********/
/* PACKAGE */
/***********/
package IR;

import MIPS.*;

public class IRcommand_Func_Epilogue extends IRcommand_IDTransform {
	String funcName;

	public IRcommand_Func_Epilogue(String funcName) {
		System.out.println("-- IRcommand_Func_Epilogue");
		this.funcName = funcName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Func_Epilogue MIPSme");
		MIPSGenerator.getInstance().funcEpilogue(funcName);
	}
}
