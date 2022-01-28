package IR;

import TEMP.*;

import java.util.Map;

import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Int_Const extends IRcommand_Initialize_Tmp {
    public int constInt;

    public IRcommand_Initialize_Tmp_With_Int_Const(TEMP dst, int constInt) {
        super(dst);
        this.constInt = constInt;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Int_Const MIPSme");
        MIPSGenerator.getInstance().liTemp(tempMap.get(this.dst), constInt);
    }
}
