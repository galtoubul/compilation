package IR;

import TEMP.*;
import MIPS.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Initialize_Temp_With_Offset extends IRcommand {

    public TEMP dstTmp;
    public TEMP arrayTmp;
    public TEMP subscriptTmp;

    public IRcommand_Initialize_Temp_With_Offset(TEMP dstTmp, TEMP arrayTmp, TEMP subscriptTmp) {
        this.dstTmp = dstTmp;
        this.arrayTmp = arrayTmp;
        this.subscriptTmp = subscriptTmp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Initialize_Temp_With_Offset MIPSme");
        MIPSGenerator.getInstance().assignTmpWithOffset(tempMap.get(dstTmp), tempMap.get(arrayTmp),
                tempMap.get(subscriptTmp));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
