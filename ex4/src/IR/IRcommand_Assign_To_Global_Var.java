package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Assign_To_Global_Var extends IRcommand
{
	public String varLabel;
	public TEMP tmpRvalue;

//	public int intConst;
//	public TEMP tmpRvalue = null;

	public IRcommand_Assign_To_Global_Var(String varLabel, TEMP tmpRvalue) {
		this.varLabel = varLabel;
		this.tmpRvalue = tmpRvalue;
	}

//	// Assign a const int value
//	public IRcommand_Assign_To_Global_Var(String varLabel, int intConst) {
//		this.varLabel = varLabel;
//		this.intConst = intConst;
//	}
//
//	// Assign a func result/string/another variable
//	public IRcommand_Assign_To_Global_Var(String varLabel, TEMP tmpRvalue) {
//		this.varLabel = varLabel;
//		this.tmpRvalue = tmpRvalue;
//	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
			MIPSGenerator.getInstance().globalVarAssignment(varLabel, tmpRvalue);
	}

//	/***************/
//	/* MIPS me !!! */
//	/***************/
//	public void MIPSme()
//	{
//		if (tmpRvalue != null) {
//			MIPSGenerator.getInstance().globalVarAssignment(varLabel, tmpRvalue);
//		}
//		else {
//			MIPSGenerator.getInstance().globalVarAssignment(varLabel, intConst);
//		}
//	}
}

//package IR;
//
//		import TEMP.*;
//		import MIPS.*;
//
//public class IRcommand_Create_Global_Var extends IRcommand
//{
//	String globalLabel;
//	String type;
//
//	public IRcommand_Create_Global_Var(String globalLabel, String type) {
//		this.globalLabel = globalLabel;
//		this.type = type;
//	}
//
//	/***************/
//	/* MIPS me !!! */
//	/***************/
//	public void MIPSme()
//	{
//		MIPSGenerator.getInstance().declareGlobalVar(globalLabel, type);
//	}
//}
//
