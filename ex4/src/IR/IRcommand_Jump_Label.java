/***********/
/* PACKAGE */
/***********/
package IR;

import java.util.Map;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import MIPS.*;
import TEMP.TEMP;

public class IRcommand_Jump_Label extends IRcommand_IDTransform implements IRcommand_Jump {
	String label_name;

	public IRcommand_Jump_Label(String label_name) {
		this.label_name = label_name;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		MIPSGenerator.getInstance().jump(label_name);
	}

	@Override
	public String jumpLabel() {
		return this.label_name;
	}

	@Override
	public String toString() {
		return String.format("jump %s", this.label_name);
	}
}
