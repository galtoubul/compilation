package IR;

import TEMP.*;
import labels.Labels;
import MIPS.*;

public class IRcommand_Binop_Concat_Strings extends IRcommand_Binop {

    public IRcommand_Binop_Concat_Strings(TEMP resultStringTmp, TEMP firstStringTmp, TEMP secondStringTmp) {
        super(resultStringTmp, firstStringTmp, secondStringTmp);
    }

    public void copyString(String loopLabel, String loopEndLabel, TEMP copiedString, TEMP dstTmp,
            boolean isSecondString) {

        if (!isSecondString) { // the label for the second string is the end label of the first string
            // loopLabel:
            MIPSGenerator.getInstance().label(loopLabel);
        }

        // byteOfString = 0(copiedString)
        TEMP byteOfString = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().lbFromTmpToTmp(byteOfString, copiedString, 0);

        // beq byteOfString, 0, loopEnd
        MIPSGenerator.getInstance().beqz(byteOfString, loopEndLabel);

        // store current byte in resultStringTmp
        MIPSGenerator.getInstance().sbFromTmpToTmp(byteOfString, dstTmp, 0);

        // addu dstTmp, dstTmp, 1
        MIPSGenerator.getInstance().addConstIntToTmp(dstTmp, 1);

        // addu byteOfString, byteOfString, 1
        MIPSGenerator.getInstance().addConstIntToTmp(byteOfString, 1);

        // j loopLabel
        MIPSGenerator.getInstance().jump(loopLabel);

        // loopEnd:
        MIPSGenerator.getInstance().label(loopEndLabel);

        if (isSecondString) {
            // adding null terminator
            MIPSGenerator.getInstance().sbFromTmpToTmp(byteOfString, dstTmp, 0);
        }
    }

    public void allocateSpaceForResult() {
        // ------------ calc the length of the concatenated result ------------ //

        // first string length
        TEMP firstStringLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().calcStringLengthIntoTmp(firstStringLenTmp, this.t1);

        // second string length
        TEMP secondStringLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        System.out.format("\t\tsecond string length\n\t\tsecondStringTmp = %s\n", this.t2);
        MIPSGenerator.getInstance().calcStringLengthIntoTmp(secondStringLenTmp, this.t2);

        // sum lengths
        TEMP conctenationLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().add(conctenationLenTmp, firstStringLenTmp, secondStringLenTmp, false);

        // add 1 for null terminator
        MIPSGenerator.getInstance().addConstIntToTmp(conctenationLenTmp, 1);

        // ------------ allocate space on heap for the result ------------ //

        MIPSGenerator.getInstance().malloc(this.dst, conctenationLenTmp);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Binop_Concat_Strings MIPSme");

        allocateSpaceForResult();

        // allocate labels
        String labelCopyFirstString = Labels.getAvialableLabel("copy_first_string_loop");
        String labelCopySecondString = Labels.getAvialableLabel("copy_second_string_loop");
        String labelAfterCopySecondString = Labels.getAvialableLabel("after_copy_second_string");

        // resultStringTmp = firstStringTmp + secondStringTmp
        copyString(labelCopyFirstString, labelCopySecondString, this.t1, this.dst, false);
        copyString(labelCopySecondString, labelAfterCopySecondString, this.t2, this.dst, true);
    }
}
