package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Create_Global_Var extends IRcommand
{
	String varLabel;
	String varType;

	public IRcommand_Create_Global_Var(String varLabel, String varType) {
		this.varLabel = varLabel;
		this.varType = varType;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().declareGlobalVar(varLabel, varType);
	}
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
