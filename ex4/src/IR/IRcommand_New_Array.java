package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;
import TYPES.*;

public class IRcommand_New_Array extends IRcommand {
	public TEMP dstTempReg;
	public TYPE arrayType;
	public TEMP subscriptTemp;

	public IRcommand_New_Array(TEMP dstTempReg, TYPE arrayType, TEMP subscriptTemp) {
		this.dstTempReg = dstTempReg;
		this.arrayType = arrayType;
		this.subscriptTemp = subscriptTemp;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		MIPSGenerator.getInstance().createNewArray(tempMap.get(dstTempReg), tempMap.get(subscriptTemp));
		// MIPSGenerator.getInstance().localVarAssignment(varIndex, dstTempReg);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dstTempReg);
		in.add(this.subscriptTemp);
		return in;
	}
}
