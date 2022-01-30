package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class IRcommand_Call_PrintString implements IRcommand {
    TEMP_LIST argsTempList;

    public IRcommand_Call_PrintString(TEMP_LIST argsTempList) {
        this.argsTempList = argsTempList;
    }

    @Override
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().PrintString(tempMap.get(argsTempList.head));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        for (TEMP temp : this.argsTempList) {
            in.add(temp);
        }
        return in;
    }

    @Override
    public String toString() {
        return String.format("PrintString(%s)",
                String.join(", ",
                        this.argsTempList.stream().map(temp -> temp.toString()).collect(Collectors.toList())));
    }
}
