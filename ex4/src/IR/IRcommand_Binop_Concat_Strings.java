package IR;

import TEMP.*;
import labels.Labels;

import java.util.Map;

import MIPS.*;

public class IRcommand_Binop_Concat_Strings extends IRcommand_Binop {

    public IRcommand_Binop_Concat_Strings(TEMP resultStringTmp, TEMP firstStringTmp, TEMP secondStringTmp) {
        super(resultStringTmp, firstStringTmp, secondStringTmp);
    }

    public void copyString(String loopLabel, String loopEndLabel, int copiedStringTemp, int dstTmp,
            boolean isSecondString) {

        // if (!isSecondString) { // the label for the second string is the end label of
        // the first string
        // // loopLabel:
        // MIPSGenerator.getInstance().label(loopLabel);
        // }

        // // byteOfString = 0(copiedString)
        // TEMP byteOfString = TEMP_FACTORY.getInstance().getFreshTEMP();
        // MIPSGenerator.getInstance().lbFromTmpToTmp(byteOfString, copiedStringTemp,
        // 0);

        // // beq byteOfString, 0, loopEnd
        // MIPSGenerator.getInstance().beqz(byteOfString, loopEndLabel);

        // // store current byte in resultStringTmp
        // MIPSGenerator.getInstance().sbFromTmpToTmp(byteOfString, dstTmp, 0);

        // // addu dstTmp, dstTmp, 1
        // MIPSGenerator.getInstance().addConstIntToTmp(dstTmp, 1);

        // // addu byteOfString, byteOfString, 1
        // MIPSGenerator.getInstance().addConstIntToTmp(byteOfString, 1);

        // // j loopLabel
        // MIPSGenerator.getInstance().jump(loopLabel);

        // // loopEnd:
        // MIPSGenerator.getInstance().label(loopEndLabel);

        // if (isSecondString) {
        // // adding null terminator
        // MIPSGenerator.getInstance().sbFromTmpToTmp(byteOfString, dstTmp, 0);
        // }

        throw new UnsupportedOperationException();
    }

    public void allocateSpaceForResult() {
        // // ------------ calc the length of the concatenated result ------------ //

        // // first string length
        // TEMP firstStringLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        // MIPSGenerator.getInstance().calcStringLengthIntoTmp(firstStringLenTmp,
        // this.t1);

        // // second string length
        // TEMP secondStringLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        // System.out.format("\t\tsecond string length\n\t\tsecondStringTmp = %s\n",
        // this.t2);
        // MIPSGenerator.getInstance().calcStringLengthIntoTmp(secondStringLenTmp,
        // this.t2);

        // // sum lengths
        // TEMP conctenationLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        // MIPSGenerator.getInstance().add(conctenationLenTmp, firstStringLenTmp,
        // secondStringLenTmp, false);

        // // add 1 for null terminator
        // MIPSGenerator.getInstance().addConstIntToTmp(conctenationLenTmp, 1);

        // // ------------ allocate space on the heap for the result ------------ //

        // MIPSGenerator.getInstance().malloc(this.dst, conctenationLenTmp);

        throw new UnsupportedOperationException();
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme(Map<TEMP, Integer> tempMap) {
        System.out.println("-- IRcommand_Binop_Concat_Strings MIPSme");

        allocateSpaceForResult();

        // allocate labels
        String labelCopyFirstString = Labels.getAvialableLabel("copy_first_string_loop");
        String labelCopySecondString = Labels.getAvialableLabel("copy_second_string_loop");
        String labelAfterCopySecondString = Labels.getAvialableLabel("after_copy_second_string");

        // resultStringTmp = firstStringTmp + secondStringTmp
        copyString(labelCopyFirstString, labelCopySecondString, tempMap.get(this.t1), tempMap.get(this.dst), false);
        copyString(labelCopySecondString, labelAfterCopySecondString, tempMap.get(this.t2), tempMap.get(this.dst),
                true);
    }
}
