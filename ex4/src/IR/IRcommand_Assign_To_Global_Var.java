package IR;

import TEMP.*;
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
}

