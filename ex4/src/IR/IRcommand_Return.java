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
		System.out.println("-- IRcommand_Return MIPSme");
		MIPSGenerator.getInstance().doReturn(expRetReg);
	}
}
