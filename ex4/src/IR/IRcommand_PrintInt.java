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

public class IRcommand_PrintInt extends IRcommand {
	TEMP t;

	public IRcommand_PrintInt(TEMP t) {
		this.t = t;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().print_int(t);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.t);
		return in;
	}
}
