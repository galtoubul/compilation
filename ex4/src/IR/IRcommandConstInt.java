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
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommandConstInt implements IRcommand {
	TEMP t;
	int value;

	public IRcommandConstInt(TEMP t, int value) {
		this.t = t;
		this.value = value;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommandConstInt MIPSme");
		MIPSGenerator.getInstance().liTemp(tempMap.get(t), value);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.t);
		return in;
	}

	@Override
	public String toString() {
		return String.format("%s := %d", this.t, value);
	}
}
