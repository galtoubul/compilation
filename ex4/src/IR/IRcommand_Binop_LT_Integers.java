package IR;

import TEMP.*;
import labels.Labels;

import java.util.Map;

import MIPS.*;

public class IRcommand_Binop_LT_Integers extends IRcommand_Binop {

	public IRcommand_Binop_LT_Integers(TEMP dst, TEMP t1, TEMP t2) {
		super(dst, t1, t2);
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme(Map<TEMP, Integer> tempMap) {
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end = Labels.getAvialableLabel("end");
		String label_AssignOne = Labels.getAvialableLabel("AssignOne");
		String label_AssignZero = Labels.getAvialableLabel("AssignZero");

		int dstId = tempMap.get(this.dst);
		int t1Id = tempMap.get(this.t1);
		int t2Id = tempMap.get(this.t2);

		/******************************************/
		/* [2] if (t1< t2) goto label_AssignOne; */
		/* if (t1>=t2) goto label_AssignZero; */
		/******************************************/
		MIPSGenerator.getInstance().blt(t1Id, t2Id, label_AssignOne);
		MIPSGenerator.getInstance().bge(t1Id, t2Id, label_AssignZero);

		/************************/
		/* [3] label_AssignOne: */
		/*                      */
		/* t3 := 1 */
		/* goto end; */
		/*                      */
		/************************/
		MIPSGenerator.getInstance().label(label_AssignOne);
		MIPSGenerator.getInstance().liTemp(dstId, 1);
		MIPSGenerator.getInstance().jump(label_end);

		/*************************/
		/* [4] label_AssignZero: */
		/*                       */
		/* t3 := 1 */
		/* goto end; */
		/*                       */
		/*************************/
		MIPSGenerator.getInstance().label(label_AssignZero);
		MIPSGenerator.getInstance().liTemp(dstId, 0);
		MIPSGenerator.getInstance().jump(label_end);

		/******************/
		/* [5] label_end: */
		/******************/
		MIPSGenerator.getInstance().label(label_end);
	}
}
