package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Local_Var extends IRcommand_Initialize_Tmp {
    public int localVarInd;

    public IRcommand_Initialize_Tmp_With_Local_Var(TEMP dst, int localVarInd) {
        super(dst);
        this.localVarInd = localVarInd;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Local_Var MIPSme");
        MIPSGenerator.getInstance().loadFromLocal(this.dst, localVarInd);
    }
}
