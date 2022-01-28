package IR;

import java.util.ArrayList;
import java.util.Map;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import pair.Pair;

public class IRcommand_ClassDec extends IRcommand_IDTransform {
    private ArrayList<Pair<String, String>> vtable;
    private String className;

    public IRcommand_ClassDec(ArrayList<Pair<String, String>> vtable, String className) {
        this.vtable = vtable;
        this.className = className;
    }

    @Override
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        MIPSGenerator.getInstance().addVtable(this.vtable, this.className);
    }

}
