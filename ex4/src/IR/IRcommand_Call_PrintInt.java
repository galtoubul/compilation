package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Call_PrintInt implements IRcommand {
    TEMP_LIST argsTempList;

    public IRcommand_Call_PrintInt(TEMP_LIST argsTempList) {
        this.argsTempList = argsTempList;
    }

    @Override
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().PrintInt(tempMap.get(argsTempList.head));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        for (TEMP temp : this.argsTempList) {
            in.add(temp);
        }
        return in;
    }
}
