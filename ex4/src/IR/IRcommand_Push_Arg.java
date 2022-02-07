package IR;

import MIPS.MIPSGenerator;
import TEMP.TEMP;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IRcommand_Push_Arg implements IRcommand{
    public TEMP temp;

    public IRcommand_Push_Arg(TEMP temp) {
        this.temp = temp;
    }

    @Override
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().pushTempReg(tempMap.get(temp));
    }

    @Override
    public HashSet<TEMP> transform(Set<TEMP> liveTemps) {
        HashSet<TEMP> in = new HashSet<>(liveTemps);
        in.add(this.temp);
        return in;

    }
}
