package IR;

import java.util.Map;

import MIPS.*;
import TEMP.TEMP;

public class IRcommand_Label extends IRcommand_IDTransform {

	String label;

	public IRcommand_Label(String label) {
		this.label = label;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		MIPSGenerator.getInstance().label(label);
	}

	public String label() {
		return this.label;
	}
}
