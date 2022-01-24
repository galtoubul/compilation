package IR;

import java.util.HashSet;
import java.util.Set;

import TEMP.TEMP;

abstract class IRcommand_Initialize_Tmp extends IRcommand {
    TEMP dst;

    public IRcommand_Initialize_Tmp(TEMP dst) {
        this.dst = dst;
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.remove(this.dst);
        return in;
    }
}
