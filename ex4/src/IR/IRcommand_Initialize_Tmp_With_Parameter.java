package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Parameter extends IRcommand_Initialize_Tmp {
    public int paramInd;

    public IRcommand_Initialize_Tmp_With_Parameter(TEMP dst, int paramInd) {
        super(dst);
        this.paramInd = paramInd;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Parameter MIPSme");
        MIPSGenerator.getInstance().loadFromParameters(this.dst, paramInd);
    }
}
