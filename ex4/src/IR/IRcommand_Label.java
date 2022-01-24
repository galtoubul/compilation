package IR;

import MIPS.*;

public class IRcommand_Label extends IRcommand_IDTransform {

	String label;

	public IRcommand_Label(String label) {
		this.label = label;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().label(label);
	}
}
