package IR;

import TEMP.*;
import MIPS.*;
import TYPES.*;

public class IRcommand_New_Array extends IRcommand
{
	public TEMP dstTempReg;
	public TYPE arrayType;
	public TEMP subscriptTemp;

	public IRcommand_New_Array(TEMP dstTempReg, TYPE arrayType, TEMP subscriptTemp) {
		this.dstTempReg = dstTempReg;
		this.arrayType = arrayType;
		this.subscriptTemp = subscriptTemp;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewArray(dstTempReg, subscriptTemp);
//		MIPSGenerator.getInstance().localVarAssignment(varIndex, dstTempReg);
	}
}
