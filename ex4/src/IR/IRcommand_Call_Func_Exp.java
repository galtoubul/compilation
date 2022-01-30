package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Call_Func_Exp extends IRcommand_Call_Func {

	private TEMP dst;

	public IRcommand_Call_Func_Exp(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		super(funcName, argsTempList);
		this.dst = dst;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Call_Func_Exp MIPSme");
		MIPSGenerator.getInstance().callFuncExp(tempMap.get(dst), funcName, argsTempList.mapTempsToRegs(tempMap));
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dst);
		return super.transform(in);
	}
}
