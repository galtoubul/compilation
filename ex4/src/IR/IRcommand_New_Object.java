package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;
import TYPES.*;

public class IRcommand_New_Object implements IRcommand {
	TYPE_CLASS objectType;
	TEMP dstTempReg;

	public IRcommand_New_Object(TEMP dstTempReg, TYPE_CLASS objectType) {
		this.objectType = objectType;
		this.dstTempReg = dstTempReg;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		MIPSGenerator.getInstance().createNewObject(tempMap.get(dstTempReg), objectType);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dstTempReg);
		return in;
	}
}
