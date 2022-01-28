package IR;

import java.util.HashSet;
import java.util.Set;

import TEMP.TEMP;

abstract class IRcommand_IDTransform implements IRcommand {
    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        return new HashSet<>(liveTemps);
    }
}
