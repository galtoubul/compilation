package IR;

import TEMP.*;
import MIPS.*;
import TYPES.*;

public class IRcommand_New_Object extends IRcommand
{
	TYPE objectType;
	TEMP dstTempReg;

	public IRcommand_New_Object(TEMP dstTempReg, TYPE objectType) {
		this.objectType = objectType;
		this.dstTempReg = dstTempReg;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewObject(dstTempReg, objectType);
	}
}
