package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;

public class IRcommand_Assign_To_Global_Var extends IRcommand {
	public String varLabel;
	public TEMP tmpRvalue;

	public IRcommand_Assign_To_Global_Var(String varLabel, TEMP tmpRvalue) {
		this.varLabel = varLabel;
		this.tmpRvalue = tmpRvalue;
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Assign_To_Global_Var MIPSme");
		MIPSGenerator.getInstance().globalVarAssignment(varLabel, tmpRvalue);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.tmpRvalue);
		return in;
	}
}
