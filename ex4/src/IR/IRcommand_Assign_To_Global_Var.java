package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Assign_To_Global_Var implements IRcommand {
	public String varLabel;
	public TEMP tmpRvalue;

	public IRcommand_Assign_To_Global_Var(String varLabel, TEMP tmpRvalue) {
		this.varLabel = varLabel;
		this.tmpRvalue = tmpRvalue;
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Assign_To_Global_Var MIPSme");
		MIPSGenerator.getInstance().globalVarAssignment(varLabel, tempMap.get(tmpRvalue));
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.tmpRvalue);
		return in;
	}
}
