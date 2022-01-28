package IR;

import TEMP.*;
import MIPS.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Initialize_Temp_With_Array_Element extends IRcommand_Initialize_Tmp {

    public TEMP arrayTmp;
    public TEMP subscriptTmp;

    public IRcommand_Initialize_Temp_With_Array_Element(TEMP dst, TEMP arrayTmp, TEMP subscriptTmp) {
        super(dst);
        this.arrayTmp = arrayTmp;
        this.subscriptTmp = subscriptTmp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Initialize_Temp_With_Array_Element MIPSme");
        MIPSGenerator.getInstance().assignTmpWithArrayElement(tempMap.get(dst), tempMap.get(arrayTmp),
                tempMap.get(subscriptTmp));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = super.transform(liveTemps);
        in.add(this.arrayTmp);
        in.add(this.subscriptTmp);
        return in;
    }
}
