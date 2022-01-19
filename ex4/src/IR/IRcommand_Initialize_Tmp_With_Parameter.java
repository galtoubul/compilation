package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Parameter extends IRcommand {
    public TEMP tmp;
    public int paramInd;

    public IRcommand_Initialize_Tmp_With_Parameter(TEMP tmp, int paramInd) {
        this.tmp = tmp;
        this.paramInd = paramInd;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Parameter MIPSme");
        MIPSGenerator.getInstance().loadFromParameters(tmp, paramInd);
    }
}

