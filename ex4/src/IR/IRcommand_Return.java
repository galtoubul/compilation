package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Return implements IRcommand {
	TEMP expRetReg;

	public IRcommand_Return(TEMP expRetReg) {
		this.expRetReg = expRetReg;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Return MIPSme");
		MIPSGenerator.getInstance().doReturn(tempMap.get(expRetReg));
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.expRetReg);
		return in;
	}
}
