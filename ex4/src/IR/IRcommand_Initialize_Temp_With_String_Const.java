package IR;

import TEMP.*;
import global_variables.GlobalVariables;
import MIPS.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Initialize_Temp_With_String_Const extends IRcommand {
	public TEMP dst;
	public String stringConst;

	public IRcommand_Initialize_Temp_With_String_Const(TEMP dst, String stringConst) {
		this.dst = dst;
		this.stringConst = stringConst;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Initialize_Temp_With_String_Const MIPSme");
		String stringConstLabel = GlobalVariables.getStringConstLabel();
		MIPSGenerator.getInstance().initializeGlobalVar(stringConstLabel, stringConst);
		MIPSGenerator.getInstance().loadAddress(tempMap.get(dst), stringConstLabel);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dst);
		return in;
	}
}
