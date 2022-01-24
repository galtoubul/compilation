package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;

public class IRcommand_Return extends IRcommand {
	TEMP expRetReg;

	public IRcommand_Return(TEMP expRetReg) {
		this.expRetReg = expRetReg;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		System.out.println("-- IRcommand_Return MIPSme");
		MIPSGenerator.getInstance().doReturn(expRetReg);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.add(this.expRetReg);
		return in;
	}
}
