package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Return implements IRcommand {
	TEMP expRetReg;
	String funcName;
	public IRcommand_Return(TEMP expRetReg, String funcName) {
		this.expRetReg = expRetReg;
		this.funcName = funcName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Return MIPSme");
		MIPSGenerator.getInstance().doReturn(tempMap.get(expRetReg), funcName);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.expRetReg);
		return in;
	}
}
