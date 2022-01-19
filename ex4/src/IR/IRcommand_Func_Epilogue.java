/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;
import SYMBOL_TABLE.*;
import TYPES.*;

public class IRcommand_Func_Epilogue extends IRcommand {
	String funcName;

	public IRcommand_Func_Epilogue(String funcName) {
		System.out.println("-- IRcommand_Func_Epilogue");
		this.funcName = funcName;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().funcEpilogue(funcName);
	}
}
