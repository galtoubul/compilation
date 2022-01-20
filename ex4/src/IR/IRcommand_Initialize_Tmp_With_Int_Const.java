package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Int_Const extends IRcommand {
    public TEMP dstTmp;
    public int constInt;

    public IRcommand_Initialize_Tmp_With_Int_Const(TEMP dstTmp, int constInt) {
        this.dstTmp = dstTmp;
        this.constInt = constInt;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Int_Const MIPSme");
        MIPSGenerator.getInstance().liTemp(dstTmp, constInt);
    }
}

