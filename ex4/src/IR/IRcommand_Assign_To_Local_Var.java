package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Assign_To_Local_Var implements IRcommand {
	public int varIndex;
	public TEMP tmpRvalue;

	public IRcommand_Assign_To_Local_Var(int varIndex, TEMP tmpRvalue) {
		this.varIndex = varIndex;
		this.tmpRvalue = tmpRvalue;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Assign_To_Local_Var MIPSme");
		MIPSGenerator.getInstance().localVarAssignment(varIndex, tempMap.get(tmpRvalue));
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.tmpRvalue);
		return in;
	}

	@Override
	public String toString() {
		return String.format("var%d := %s", this.varIndex, this.tmpRvalue);
	}
}
