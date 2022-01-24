/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;

public class IRcommandConstInt extends IRcommand {
	TEMP t;
	int value;

	public IRcommandConstInt(TEMP t, int value) {
		this.t = t;
		this.value = value;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommandConstInt MIPSme");
		MIPSGenerator.getInstance().liTemp(t, value);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.t);
		return in;
	}
}
