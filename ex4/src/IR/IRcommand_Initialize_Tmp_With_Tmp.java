package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Tmp extends IRcommand_Initialize_Tmp {
    public TEMP src;

    public IRcommand_Initialize_Tmp_With_Tmp(TEMP dst, TEMP src) {
        super(dst);
        this.src = src;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Tmp MIPSme");
        MIPSGenerator.getInstance().movFromTmpToTmp(tempMap.get(this.dst), tempMap.get(this.src));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = super.transform(liveTemps);
        in.add(this.src);
        return in;
    }
}
