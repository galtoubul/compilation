package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand {

	TEMP t;
	String label;

	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label) {
		this.t = t;
		this.label = label;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Jump_If_Eq_To_Zero MIPSme");

		MIPSGenerator.getInstance().beqz(t, label);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.t);
		return in;
	}

	public String jumpLabel() {
		return this.label;
	}
}
