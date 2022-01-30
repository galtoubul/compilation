package IR;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import MIPS.MIPSGenerator;
import TEMP.TEMP;
import TEMP.TEMP_LIST;

public class IRcommand_Call_Method_Stmt extends IRcommand_Call_Func {
    private TEMP objectTmp;
    private int methodOffset;

    public IRcommand_Call_Method_Stmt(TEMP objectTmp, int offset, TEMP_LIST argsTempList) {
        super(String.valueOf(offset), argsTempList); // We don't really care about the name
        this.objectTmp = objectTmp;
        this.methodOffset = offset;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Call_Method_Stmt MIPSme");
        MIPSGenerator.getInstance().callMethodStmt(tempMap.get(objectTmp), methodOffset,
                this.argsTempList.mapTempsToRegs(tempMap));
    }
}
