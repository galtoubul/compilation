package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import MIPS.*;

public class IRcommand_Return implements IRcommand {
	Optional<TEMP> expRetReg;
	String funcName;

	public IRcommand_Return(Optional<TEMP> expRetReg, String funcName) {
		this.expRetReg = expRetReg;
		this.funcName = funcName;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Return MIPSme");
		MIPSGenerator.getInstance().doReturn(this.expRetReg.map(temp -> tempMap.get(temp)), this.funcName);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		if (this.expRetReg.isPresent()) {
			in.add(this.expRetReg.get());
		}
		return in;
	}

	@Override
	public String toString() {
		return this.expRetReg.isPresent() ? String.format("return %s", this.expRetReg) : "return";
	}
}
