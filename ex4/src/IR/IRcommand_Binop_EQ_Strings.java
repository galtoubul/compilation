package IR;

import TEMP.*;
import labels.Labels;

import java.util.Map;

import MIPS.*;

public class IRcommand_Binop_EQ_Strings extends IRcommand_Binop {

    public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Binop_EQ_Strings MIPSme");
        MIPSGenerator.getInstance().checkEqStrings(tempMap.get(dst), tempMap.get(t1), tempMap.get(t2));
    }
}
