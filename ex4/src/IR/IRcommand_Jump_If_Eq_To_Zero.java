package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Jump_If_Eq_To_Zero implements IRcommand_Jump {

	TEMP t;
	String label;

	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label) {
		this.t = t;
		this.label = label;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Jump_If_Eq_To_Zero MIPSme");

		MIPSGenerator.getInstance().beqz(tempMap.get(t), label);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.t);
		return in;
	}

	@Override
	public String jumpLabel() {
		return this.label;
	}
}
