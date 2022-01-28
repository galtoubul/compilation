package IR;

import TEMP.*;

import java.util.Map;

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
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Local_Var MIPSme");
        MIPSGenerator.getInstance().loadFromLocal(tempMap.get(this.dst), localVarInd);
    }
}
