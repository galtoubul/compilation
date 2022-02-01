package IR;

import TEMP.*;
import global_variables.GlobalVariables;
import MIPS.*;

import java.util.Map;

public class IRcommand_Initialize_Temp_With_String_Const extends IRcommand_Initialize_Tmp {
	public String stringConst;

	public IRcommand_Initialize_Temp_With_String_Const(TEMP dst, String stringConst) {
		super(dst);
		this.stringConst = stringConst;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Initialize_Temp_With_String_Const MIPSme");
		String stringConstLabel = GlobalVariables.getStringConstLabel();
		MIPSGenerator.getInstance().initializeGlobalVar(stringConstLabel, stringConst);
		MIPSGenerator.getInstance().loadAddressTemp(tempMap.get(dst), stringConstLabel);
	}

	@Override
	public String toString() {
		return String.format("%s := %s", this.dst, this.stringConst);
	}
}
