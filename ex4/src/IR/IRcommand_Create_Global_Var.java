package IR;

import global_variables.GlobalVariables;
import MIPS.*;

public class IRcommand_Create_Global_Var extends IRcommand_IDTransform {
	String varLabel;
	String varType;
	String stringConst = null;
	int intConst = Integer.MAX_VALUE;
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
	public void MIPSme() {
		System.out.println("-- IRcommand_Create_Global_Var MIPSme");

		if (varType == "string" && stringConst != null) {
			/*
			 * string z := "abc"; --> str_const<num>: .asciiz "abc"
			 * globsl_z: .word str_const<num>
			 */

			// get string const label (str_const<num>)
			String stringConstLabel = GlobalVariables.getStringConstLabel();

			// initialize a string const label with the string const value (str_const<num>:
			// .asciiz "abc")
			MIPSGenerator.getInstance().initializeGlobalVar(stringConstLabel, stringConst);

			// initialize the global variable label with the string const label (globsl_z:
			// .word str_const<num>)
			MIPSGenerator.getInstance().initializeGlobalVarWithStringConstLabel(varLabel, stringConstLabel);
		} else if (varType == "int" && intConst != Integer.MAX_VALUE) {
			MIPSGenerator.getInstance().initializeGlobalVar(varLabel, intConst);
		} else { // global var was only declared (without initialization) or it was initialized
					// with null
			MIPSGenerator.getInstance().declareGlobalVar(varLabel, varType);
		}
	}
}
