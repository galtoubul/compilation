package IR;

import TEMP.*;

import MIPS.*;

public class IRcommand_Binop_Sub_Integers extends IRcommand_Binop {

    public IRcommand_Binop_Sub_Integers(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        MIPSGenerator.getInstance().sub(dst, t1, t2);
    }
}
