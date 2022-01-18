package IR;

import TEMP.*;
import MIPS.*;

public class IRcommand_Create_Global_Var extends IRcommand
{
	String varLabel;
	String varType;
	String stringConst;
	int intConst;
	Object objConst; // null

	// declaring without initializing
	public IRcommand_Create_Global_Var(String varLabel, String varType) {
		this.varLabel = varLabel;
		this.varType = varType;
	}

	// initializing with a string const
	public IRcommand_Create_Global_Var(String varLabel, String varType, String value) {
		this.varLabel = varLabel;
		this.varType = varType;
		this.stringConst = value;
	}

	// initializing with an int const
	public IRcommand_Create_Global_Var(String varLabel, String varType, int value) {
		this.varLabel = varLabel;
		this.varType = varType;
		this.intConst = value;
	}

	// initializing with a null
	public IRcommand_Create_Global_Var(String varLabel, String varType, Object obj) {
		this.varLabel = varLabel;
		this.varType = varType;
		this.objConst = obj;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (varType == "string") {

		}
		else if (varType == "int") {
			MIPSGenerator.getInstance().initializeGlobalVar(varLabel, intConst);
		}
		else { // global var was only declared (without initialization) or it was initialized with null
			MIPSGenerator.getInstance().declareGlobalVar(varLabel, varType);
		}
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
