package IR;

import java.util.HashSet;
import java.util.Set;

import TEMP.TEMP;

abstract class IRcommand_Binop_Integers extends IRcommand {
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.remove(this.dst);
        in.add(this.t1);
        in.add(this.t2);
        return in;
    }
}
