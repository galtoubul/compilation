package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Return extends IRcommand {
	TEMP expRetReg;
	
	public IRcommand_Return(TEMP expRetReg) {
		this.expRetReg = expRetReg;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().doReturn(expRetReg);
	}
}
