package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Initialize_Tmp_With_Global_Var extends IRcommand {
    public TEMP tmp;
    public String globalVarLabel;

    public IRcommand_Initialize_Tmp_With_Global_Var(TEMP tmp, String globalVarLabel) {
        this.tmp = tmp;
        this.globalVarLabel = globalVarLabel;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Tmp_With_Global_Var MIPSme");
        MIPSGenerator.getInstance().loadFromGlobal(tmp, globalVarLabel);
    }
}

