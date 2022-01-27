package IR;

import TEMP.*;
import MIPS.*;
import java.util.HashSet;
import java.util.Set;

public class IRcommand_Initialize_Temp_With_Array_Element extends IRcommand {

    public TEMP dstTmp;
    public TEMP arrayTmp;
    public TEMP subscriptTmp;

    public IRcommand_Initialize_Temp_With_Array_Element(TEMP dstTmp, TEMP arrayTmp, TEMP subscriptTmp) {
        this.dstTmp = dstTmp;
        this.arrayTmp = arrayTmp;
        this.subscriptTmp = subscriptTmp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Initialize_Temp_With_Array_Element MIPSme");
        MIPSGenerator.getInstance().assignTmpWithArrayElement(dstTmp, arrayTmp, subscriptTmp);
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
