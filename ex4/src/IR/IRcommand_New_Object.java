package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;
import TYPES.*;

public class IRcommand_New_Object extends IRcommand {
	TYPE objectType;
	TEMP dstTempReg;

	public IRcommand_New_Object(TEMP dstTempReg, TYPE objectType) {
		this.objectType = objectType;
		this.dstTempReg = dstTempReg;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		MIPSGenerator.getInstance().createNewObject(dstTempReg, objectType);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dstTempReg);
		return in;
	}
}
