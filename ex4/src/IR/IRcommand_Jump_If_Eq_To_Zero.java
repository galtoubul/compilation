package IR;

import TEMP.*;
import MIPS.*;
import Labels.*;

public class IRcommand_Jump_If_Eq_To_Zero extends IRcommand {

	TEMP t;
	String label;
	
	public IRcommand_Jump_If_Eq_To_Zero(TEMP t, String label) {
		this.t          = t;
		this.label = label;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Jump_If_Eq_To_Zero MIPSme");

		MIPSGenerator.getInstance().beqz(t, label);
	}
}
