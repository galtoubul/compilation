package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Load extends IRcommand {
	TEMP dst;
	String var_name;

	public IRcommand_Load(TEMP dst, String var_name) {
		this.dst = dst;
		this.var_name = var_name;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		System.out.println("-- IRcommand_Load MIPSme");
		MIPSGenerator.getInstance().load(tempMap.get(dst), var_name);
	}

	@Override
	public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
		HashSet<TEMP> in = new HashSet<>(liveTemps);
		in.remove(this.dst);
		return in;
	}
}
