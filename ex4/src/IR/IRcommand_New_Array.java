package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;
import TYPES.*;

public class IRcommand_New_Array extends IRcommand {
	TEMP dstTempReg;
	TYPE arrayType;
	TEMP subscriptTemp;

	public IRcommand_New_Array(TEMP dstTempReg, TYPE arrayType, TEMP subscriptTemp) {
		this.dstTempReg = dstTempReg;
		this.arrayType = arrayType;
		this.subscriptTemp = subscriptTemp;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewArray(dstTempReg, arrayType, subscriptTemp);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dstTempReg);
		in.add(this.subscriptTemp);
		return in;
	}
}
