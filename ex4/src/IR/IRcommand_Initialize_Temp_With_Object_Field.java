package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Initialize_Temp_With_Object_Field implements IRcommand {
    public TEMP objectTmp;
    public TEMP dst;
    public int fieldOffset;

    public IRcommand_Initialize_Temp_With_Object_Field(TEMP dst, TEMP objectTmp, int fieldOffset) {
        this.objectTmp = objectTmp;
        this.fieldOffset = fieldOffset;
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Field_Access MIPSme");
        MIPSGenerator.getInstance().fieldAccess(tempMap.get(dst), tempMap.get(objectTmp), fieldOffset);
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.remove(this.dst);
        in.add(this.objectTmp);
        return in;
    }
}
