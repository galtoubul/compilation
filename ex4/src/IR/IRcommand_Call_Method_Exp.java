package IR;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

public class IRcommand_Call_Method_Exp extends IRcommand_Call_Func {
    private TEMP objectTmp;
    private int methodOffset;
    private TEMP dst;

    public IRcommand_Call_Method_Exp(TEMP dst, TEMP objectTmp, int offset, TEMP_LIST argsTempList) {
        super(String.valueOf(offset), argsTempList); // We don't really care about the name
        this.dst = dst;
        this.objectTmp = objectTmp;
        this.methodOffset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().callMethodExp(tempMap.get(dst), tempMap.get(objectTmp), methodOffset,
                this.argsTempList.mapTempsToRegs(tempMap));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.remove(this.dst);
        in = super.transform(in);
        in.add(this.objectTmp);
        return in;
    }
}
