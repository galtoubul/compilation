package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Global_Var extends IRcommand_Initialize_Tmp {
    public String globalVarLabel;

    public IRcommand_Initialize_Tmp_With_Global_Var(TEMP dst, String globalVarLabel) {
        super(dst);
        this.globalVarLabel = globalVarLabel;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Global_Var MIPSme");
        MIPSGenerator.getInstance().loadFromGlobal(this.dst, globalVarLabel);
    }
}
