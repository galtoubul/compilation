package IR;

import TEMP.*;
import MIPS.*;
import TYPES.*;

public class IRcommand_New_Array extends IRcommand
{
	TEMP dstTempReg;
	TYPE arrayType;
	TEMP subscriptTemp;

	public IRcommand_New_Array(TEMP dstTempReg, TYPE arrayType, TEMP subscriptTemp) {
		this.dstTempReg = dstTempReg;
		this.arrayType = arrayType;
		this.subscriptTemp = subscriptTemp;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewArray(dstTempReg, arrayType, subscriptTemp);
	}
}
