package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Assign_To_Local_Var extends IRcommand
{
	public int varIndex;
	public TEMP tmpRvalue;

	public IRcommand_Assign_To_Local_Var(int varIndex, TEMP tmpRvalue) {
		this.varIndex = varIndex;
		this.tmpRvalue = tmpRvalue;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Assign_To_Local_Var MIPSme");
		MIPSGenerator.getInstance().localVarAssignment(varIndex, tmpRvalue);
	}

}
