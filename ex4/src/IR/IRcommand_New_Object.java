package IR;

import TEMP.*;
import MIPS.*;
import TYPES.*;

public class IRcommand_New_Object extends IRcommand
{
	TYPE objectType;

	public IRcommand_New_Object(TYPE objectType) {
		this.objectType = objectType;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewObject(objectType);
	}
}
