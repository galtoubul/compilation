package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Local_Var extends IRcommand {
    public TEMP tmp;
    public int localVarInd;

    public IRcommand_Initialize_Tmp_With_Local_Var(TEMP tmp, int localVarInd) {
        this.tmp = tmp;
        this.localVarInd = localVarInd;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Local_Var MIPSme");
        MIPSGenerator.getInstance().loadFromLocal(tmp, localVarInd);
    }
}

