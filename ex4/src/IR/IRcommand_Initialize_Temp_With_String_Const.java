package IR;

import TEMP.*;
import MIPS.*;
import GlobalVariables.*;

public class IRcommand_Initialize_Temp_With_String_Const extends IRcommand
{
	public TEMP dst;
	public String stringConst;

	public IRcommand_Initialize_Temp_With_String_Const(TEMP dst, String stringConst) {
		this.dst = dst;
		this.stringConst = stringConst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Initialize_Temp_With_String_Const MIPSme");
		String stringConstLabel = GlobalVariables.getStringConstLabel();
		MIPSGenerator.getInstance().initializeGlobalVar(stringConstLabel, stringConst);
		MIPSGenerator.getInstance().loadAddress(dst, stringConstLabel);
	}
}
