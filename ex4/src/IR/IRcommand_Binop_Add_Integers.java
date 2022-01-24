package IR;

import TEMP.*;

import MIPS.*;

public class IRcommand_Binop_Add_Integers extends IRcommand_Binop_Integers {

	public IRcommand_Binop_Add_Integers(TEMP dst, TEMP t1, TEMP t2) {
		super(dst, t1, t2);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Binop_Add_Integers MIPSme");
		MIPSGenerator.getInstance().add(dst, t1, t2);
	}
}
