package IR;

import TEMP.*;

import java.util.Map;

import MIPS.*;

public class IRcommand_Binop_Div_Integers extends IRcommand_Binop {

    public IRcommand_Binop_Div_Integers(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().div(tempMap.get(dst), tempMap.get(t1), tempMap.get(t2));
    }
}
