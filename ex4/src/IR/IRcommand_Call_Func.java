package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

abstract class IRcommand_Call_Func extends IRcommand {
    TEMP dst;
    String funcName;
    TEMP_LIST argsTempList;

    public IRcommand_Call_Func(TEMP dst, String funcName, TEMP_LIST argsTempList) {
        this.dst = dst;
        this.funcName = funcName;
        this.argsTempList = argsTempList;
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.remove(this.dst);
        for (TEMP temp : this.argsTempList) {
            in.add(temp);
        }
        return in;
    }
}
