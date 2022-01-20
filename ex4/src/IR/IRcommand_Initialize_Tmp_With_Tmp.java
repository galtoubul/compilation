package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Tmp extends IRcommand {
    public TEMP dstTmp;
    public TEMP srcTmp;

    public IRcommand_Initialize_Tmp_With_Tmp(TEMP dstTmp, TEMP srcTmp) {
        this.dstTmp = dstTmp;
        this.srcTmp = srcTmp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Tmp MIPSme");
        MIPSGenerator.getInstance().movFromTmpToTmp(dstTmp, srcTmp);
    }
}

