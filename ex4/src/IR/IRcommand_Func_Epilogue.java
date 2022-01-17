/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;
import SYMBOL_TABLE.*;
import TYPES.*;

public class IRcommand_Func_Epilogue extends IRcommand {

	public IRcommand_Func_Epilogue() {
		System.out.println("-- IRcommand_Func_Epilogue");
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().funcEpilogue();
	}
}
