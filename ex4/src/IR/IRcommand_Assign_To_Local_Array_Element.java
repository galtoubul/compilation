package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

import MIPS.*;

public class IRcommand_Assign_To_Local_Array_Element extends IRcommand {
    public int varIndex;
    public TEMP offsetTmp;
    public TEMP tmpRvalue;

    public IRcommand_Assign_To_Local_Array_Element(int varIndex, TEMP offsetTmp, TEMP tmpRvalue) {
        this.varIndex = varIndex;
        this.offsetTmp = offsetTmp;
        this.tmpRvalue = tmpRvalue;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Assign_To_Local_Array_Element MIPSme");
        MIPSGenerator.getInstance().localVarAssignment(varIndex, offsetTmp, tmpRvalue);
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.add(this.tmpRvalue);
        return in;
    }
}
