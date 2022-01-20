package IR;

import TEMP.*;
import MIPS.*;
import Labels.*;

public class IRcommand_Binop_Concat_Strings extends IRcommand {

    public TEMP firstStringTmp;
    public TEMP secondStringTmp;
    public TEMP resultStringTmp;

    public IRcommand_Binop_Concat_Strings(TEMP resultStringTmp, TEMP firstStringTmp, TEMP secondStringTmp) {
        this.firstStringTmp = firstStringTmp;
        this.secondStringTmp = secondStringTmp;
        this.resultStringTmp = resultStringTmp;
    }

    public void copyString(String loopLabel, String loopEndLabel, TEMP copiedString, TEMP dstTmp, boolean isSecondString) {

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
        MIPSGenerator.getInstance().calcStringLengthIntoTmp(firstStringLenTmp, firstStringTmp);

        // second string length
        TEMP secondStringLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        System.out.format("\t\tsecond string length\n\t\tsecondStringTmp = %s\n", secondStringTmp);
        MIPSGenerator.getInstance().calcStringLengthIntoTmp(secondStringLenTmp, secondStringTmp);

        // sum lengths
        TEMP conctenationLenTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().add(conctenationLenTmp, firstStringLenTmp, secondStringLenTmp); // TODO: overflow?

        // add 1 for null terminator
        MIPSGenerator.getInstance().addConstIntToTmp(conctenationLenTmp, 1);

        // ------------ allocate space on heap for the result ------------ //

        MIPSGenerator.getInstance().malloc(resultStringTmp, conctenationLenTmp);
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
        copyString(labelCopyFirstString, labelCopySecondString, firstStringTmp, resultStringTmp, false);
        copyString(labelCopySecondString, labelAfterCopySecondString, secondStringTmp, resultStringTmp, true);
    }
}
