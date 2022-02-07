package IR;

import TEMP.*;

import java.util.HashSet;
import java.util.Set;

abstract class IRcommand_Call_Func implements IRcommand {
    String funcName;
    TEMP_LIST argsTempList;

    public IRcommand_Call_Func(String funcName, TEMP_LIST argsTempList) {
        this.funcName = funcName;
        this.argsTempList = argsTempList;
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        return new HashSet<>(liveTemps);
    }
}
