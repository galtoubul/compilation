package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Assign_To_Parameter implements IRcommand {
	public int paramIndex;
	public TEMP tmpRvalue;

	public IRcommand_Assign_To_Parameter(int paramIndex, TEMP tmpRvalue) {
		this.paramIndex = paramIndex;
		this.tmpRvalue = tmpRvalue;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Assign_To_Local_Var MIPSme");
		MIPSGenerator.getInstance().parameterAssignment(paramIndex, tempMap.get(tmpRvalue));
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.tmpRvalue);
		return in;
	}

	@Override
	public String toString() {
		return String.format("param%d := %s", this.paramIndex, this.tmpRvalue);
	}
}
