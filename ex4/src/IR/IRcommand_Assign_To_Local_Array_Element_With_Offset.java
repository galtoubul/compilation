package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Assign_To_Local_Array_Element_With_Offset implements IRcommand {
    public TEMP arrayTmp;
    public int ind;
    public TEMP tmpRvalue;

    public IRcommand_Assign_To_Local_Array_Element_With_Offset(TEMP arrayTmp, int ind,
                                                               TEMP tmpRvalue) {
        this.arrayTmp = arrayTmp;
        this.ind = ind;
        this.tmpRvalue = tmpRvalue;

    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Assign_To_Local_Array_Element_With_Offset MIPSme");
        MIPSGenerator.getInstance().assignToArrayElementWithOffset(tempMap.get(arrayTmp), ind,
                tempMap.get(tmpRvalue));
        for (TEMP t : tempMap.keySet()) {
            System.out.println(t.getSerialNumber()+" "+tempMap.get(t));
        }
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.add(this.tmpRvalue);
        in.add(this.arrayTmp);
        return in;
    }
}
