package IR;

import TEMP.*;
import MIPS.*;
import global_variables.GlobalVariables;

public class IRcommand_Create_Global_Var extends IRcommand_IDTransform {

	public String varLabel;
	public String varType;
	public String stringConst = null;
	public int intConst = Integer.MAX_VALUE;
	public TEMP initExpTmp = null;

	// declaring without initializing or initializing with nill
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

	// initializing with a tmp (result of exp_binop/exp_paren)
	public IRcommand_Create_Global_Var(String varLabel, String varType, TEMP initExpTmp) {
		this.varLabel = varLabel;
		this.varType = varType;
		this.initExpTmp = initExpTmp;
		if (varType == "string") {
			stringConst = "\"\"";
		} else if (varType == "int") {
			intConst = 0;
		} else { // varType == "new" (maybe there are more types?)
			System.out.format("-- IRcommand_Create_Global_Var\n\t\tvarType = %s\n", varType);
			intConst = 0;
		}
	}

	public void MIPSme() {
		System.out.println("-- IRcommand_Create_Global_Var MIPSme");

		// global var was only declared (without initialization) or it was initialized
		// with nill/new *
		if (initExpTmp == null && stringConst == null && intConst == Integer.MAX_VALUE) {
			MIPSGenerator.getInstance().declareGlobalVar(varLabel, varType);
			return;
		}

		if (varType == "string") {
			/*
			 * string z := "abc"; --> str_const<num>: .asciiz "abc"
			 * globsl_z: .word str_const<num>
			 */

			// get string const label (str_const<num>)
			String stringConstLabel = GlobalVariables.getStringConstLabel();

			// initialize a string const label with a string const value (str_const<num>:
			// .asciiz "abc")
			MIPSGenerator.getInstance().initializeGlobalVar(stringConstLabel, stringConst);

			// initialize the global variable label with the string const label (globsl_z:
			// .word str_const<num>)
			MIPSGenerator.getInstance().initializeGlobalVarWithStringConstLabel(varLabel, stringConstLabel);
		} else if (varType == "int") {
			MIPSGenerator.getInstance().initializeGlobalVar(varLabel, intConst);
		} else { // varType == "new" (maybe there are more types?)
			MIPSGenerator.getInstance().initializeGlobalVar(varLabel, intConst);
		}

		// the global variable was initialized with
		// EXP_BINOP/EXP_PAREN/EXP_VAR/AST_VAR_NEW
		if (initExpTmp != null) {
			MIPSGenerator.getInstance().globalVarAssignment(varLabel, initExpTmp);
		}
	}
}