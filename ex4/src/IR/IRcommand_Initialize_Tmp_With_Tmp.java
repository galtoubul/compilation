package IR;

import TEMP.*;

import java.util.HashSet;
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
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Tmp MIPSme");
        MIPSGenerator.getInstance().movFromTmpToTmp(this.dst, this.src);
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = super.transform(liveTemps);
        in.add(this.src);
        return in;
    }
}
