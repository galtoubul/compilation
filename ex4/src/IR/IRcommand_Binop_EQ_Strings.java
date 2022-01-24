package IR;

import TEMP.*;
import labels.Labels;
import MIPS.*;

public class IRcommand_Binop_EQ_Strings extends IRcommand_Binop {

    public IRcommand_Binop_EQ_Strings(TEMP dst, TEMP t1, TEMP t2) {
        super(dst, t1, t2);
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        System.out.println("-- IRcommand_Binop_EQ_Strings MIPSme");

        // allocate 3 fresh labels
        String labelNeq = Labels.getAvialableLabel("neq");
        String labelStrEqLoop = Labels.getAvialableLabel("str_eq_loop");
        String labelStrEqEnd = Labels.getAvialableLabel("str_eq_end");

        // ------------ init ------------ //

        // resultTmp = 1
        MIPSGenerator.getInstance().liTemp(dst, 1);

        // stringOneTmp = t1
        TEMP stringOneTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().movFromTmpToTmp(stringOneTmp, t1);

        // stringTwoTmp = t2
        TEMP stringTwoTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().movFromTmpToTmp(stringTwoTmp, t2);

        // ------------ str_eq_loop ------------ //

        // str_eq_loop:
        MIPSGenerator.getInstance().label(labelStrEqLoop);

        // get the first byte of the strings

        // byteOfStringOne = 0(stringOneTmp)
        TEMP byteOfStringOne = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().lbFromTmpToTmp(byteOfStringOne, stringOneTmp, 0);

        // byteOfStringTwo = 0(stringTwoTmp)
        TEMP byteOfStringTwo = TEMP_FACTORY.getInstance().getFreshTEMP();
        MIPSGenerator.getInstance().lbFromTmpToTmp(byteOfStringTwo, stringTwoTmp, 0);

        // ------------ compare bytes ------------ //

        // bne byteOfStringOne, byteOfStringTwo, neq
        MIPSGenerator.getInstance().bne(byteOfStringOne, byteOfStringTwo, labelNeq);

        // beq byteOfStringOne, 0, str_eq_end
        MIPSGenerator.getInstance().beqz(byteOfStringOne, labelStrEqEnd);

        // ------------ advance strings by one ------------ //

        // addu stringOneTmp, stringOneTmp, 1
        MIPSGenerator.getInstance().addConstIntToTmp(stringOneTmp, 1);

        // addu stringTwoTmp, stringTwoTmp, 1
        MIPSGenerator.getInstance().addConstIntToTmp(stringTwoTmp, 1);

        // j str_eq_loop
        MIPSGenerator.getInstance().jump(labelStrEqLoop);

        // ------------ neq ------------ //

        // neq:
        MIPSGenerator.getInstance().label(labelNeq);

        // resultTmp = 0
        MIPSGenerator.getInstance().liTemp(dst, 0);

        // ------------ str_eq_end ------------ //

        // str_eq_end:
        MIPSGenerator.getInstance().label(labelStrEqEnd);
    }
}
