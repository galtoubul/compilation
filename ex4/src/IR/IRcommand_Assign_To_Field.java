package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Assign_To_Field implements IRcommand {
    public TEMP lValueTmp;
    public int fieldInd;
    public TEMP rValueTemp;

    public IRcommand_Assign_To_Field(TEMP lValueTmp, int fieldInd, TEMP rValueTemp) {
        this.lValueTmp = lValueTmp;
        this.fieldInd = fieldInd;
        this.rValueTemp = rValueTemp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Assign_To_Local_Array_Element MIPSme");
        MIPSGenerator.getInstance().fieldAssignment(tempMap.get(lValueTmp),
                tempMap.get(rValueTemp), fieldInd);
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.add(this.rValueTemp);
        in.add(this.lValueTmp);
        return in;
    }
}
