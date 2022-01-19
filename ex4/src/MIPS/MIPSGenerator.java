package MIPS;

import java.io.PrintWriter;

import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class MIPSGenerator {

	private int WORD_SIZE = 4;
	private int TMPS_BACKUP_SPACE = 40; // t0 - t9 backup
	private PrintWriter fileWriter;
	private String dataSegment = "";
	private String textSegment = "";

	public void finalizeFile() {
		textSegment += String.format("main:\n");
		textSegment += String.format("\tjal user_main\n");
		textSegment += String.format("\tli $v0,10\n");
		textSegment += String.format("\tsyscall\n");
		fileWriter.format(dataSegment);
		fileWriter.format(".text\n");
		fileWriter.format(textSegment);
		fileWriter.close();
	}

	/*********************************************** Supported syscalls ***********************************************/

	public void print_int(TEMP t) {
		int idx = t.getSerialNumber();
		textSegment += String.format("\tmove $a0, Temp_%d\n",idx);
		textSegment += String.format("\tli $v0, 1\n");
		textSegment += String.format("\tsyscall\n");
		textSegment += String.format("\tli $a0, 32\n");
		textSegment += String.format("\tli $v0, 11\n");
		textSegment += String.format("\tsyscall\n");
	}

	/************************************************ General ************************************************/

	// TODO delete/change
	public void load(TEMP dst, String var_name) {
		int idxdst = dst.getSerialNumber();
		textSegment += String.format("\tlw Temp_%d, global_%s\n", idxdst, var_name);
	}

	// TODO delete/change
	public void store(String var_name, TEMP src) {
		int idxsrc = src.getSerialNumber();
		textSegment += String.format("\tsw Temp_%d, global_%s\n",idxsrc,var_name);
	}

	public void loadAddress(TEMP dstReg, String label) {
		int dstRegInd = dstReg.getSerialNumber();
		textSegment += String.format("\tla Temp_%d, %s\n", dstRegInd, label);
	}

	public void label(String inlabel) {
		if (inlabel.equals("main")) {
			textSegment += String.format("user_main:\n"); // _ isn't allowed in an identifier name -> user_name is a unique label
		}
		else {
			textSegment += String.format("%s:\n",inlabel);
		}
	}

	public void jump(String inlabel) {
		fileWriter.format("\tj %s\n",inlabel);
	}

	public void liTemp(TEMP t,int value) {
		int idx=t.getSerialNumber();
		textSegment += String.format("\tli Temp_%d, %d\n",idx,value);
	}

	public void liRegString(String reg,int value) {
		fileWriter.format("\tli $%s, %d\n",reg,value);
	}

	/************************************************ Arithmetics ************************************************/

	public void checkLimits(int dstidx) {
		// check top limit and fix the result if needed
		textSegment += String.format("\tble Temp_%d, 32767, check_bottom_limit\n",dstidx); // 2^15 - 1 = 32767
		textSegment += String.format("\tli Temp_%d, 32767\n",dstidx); // fix the result

		// check bottom limit and fix the result if needed
		label("check_bottom_limit");
		textSegment += String.format("\tbge Temp_%d, -32768, after_limits_checks\n",dstidx); // -2^15 = -32768
		textSegment += String.format("\tli Temp_%d, -32768\n",dstidx); // fix the result

		label("after_limits_checks");
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		textSegment += String.format("\tadd Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		textSegment += String.format("\tsub Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}

	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		textSegment += String.format("\tmul Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		textSegment += String.format("\tdiv Temp_%d, Temp_%d, Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}

	/************************************************ Local Variables ************************************************/

	public void localVarAssignment(int varIndex, TEMP tmpRvalue) {
		int rValueTmpInd = tmpRvalue.getSerialNumber();
		int offset = (-1) * (varIndex * WORD_SIZE + TMPS_BACKUP_SPACE);
		textSegment += String.format("\tsw Temp_%d, %d($fp)\n", rValueTmpInd, offset);
	}

	public void storeLocalVar(int ind, TEMP initialValueTmp) {
		int idx = initialValueTmp.getSerialNumber();
		int offset = (-1) * (ind * WORD_SIZE + 40); // 40 is for storing tmps
		textSegment += String.format("\tsw Temp_%d, %d($fp)\n",idx,offset);
	}

	/************************************************ Global Variables ************************************************/

	// decalaring a global variable without assigning it an initial value
	public void declareGlobalVar(String label, String type) {
		if (type == "string") {
			dataSegment += String.format("%s: .asciiz \"\"\n", label);
		}
		else { // int
			dataSegment += String.format("%s: .word 0\n", label);
		}
	}

	// initialzing a global variable with a string const
	public void initializeGlobalVar(String label, String value) {
		dataSegment += String.format("%s: .asciiz %s\n", label, value);
	}

	// initialzing a global variable with a string const label
	public void initializeGlobalVarWithStringConstLabel(String globalVarLabel, String stringConstLabel) {
		dataSegment += String.format("%s: .word %s\n", globalVarLabel, stringConstLabel);
	}

	// initialzing a global variable with an int const
	public void initializeGlobalVar(String label, int value) {
		dataSegment += String.format("%s: .word %d\n", label, value);
	}

	// assign a tmp to a global variable
	public void globalVarAssignment(String globalVarLabel, TEMP tmpRvalue) {
		int tmpRvalueInd=tmpRvalue.getSerialNumber();
		textSegment += String.format("\tsw Temp_%d, %s\n", tmpRvalueInd, globalVarLabel);
	}

	/************************************************ String ************************************************/



	/************************************************ Class ************************************************/

	public int getClassSize(TYPE objectType) {
		TYPE_CLASS t = ((TYPE_CLASS)SYMBOL_TABLE.getInstance().find(objectType.name));

		int fieldsNumTotal = t.fieldsNum;
		while(t.father.isPresent()) {
			t = t.father.get();
			fieldsNumTotal += t.fieldsNum;
		}

		return fieldsNumTotal * WORD_SIZE + 4; // 4 is for the vtable
	}

	public void createNewObject(TEMP dstTempReg, TYPE objectType) {
		// malloc
		int classSize = getClassSize(objectType);
		liRegString("a0", classSize);
		liRegString("v0", 9);
		fileWriter.format("\tsyscall\n");

		// mov pointer to dstTempReg
		int dstidx = dstTempReg.getSerialNumber();
		textSegment += String.format("\tmov Temp_%d, $v0\n",dstidx);
	}

	/************************************************ Array ************************************************/

	public void createNewArray(TEMP dstTempReg, TYPE ArrayType, TEMP subscriptTemp) {
		// malloc
		TEMP wordTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		liTemp(wordTemp, 4); // wordTemp = 4

		TEMP mulTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		mul(mulTemp, subscriptTemp, wordTemp); // mulTemp = subscriptTemp * 4

		int mulTempidx = mulTemp.getSerialNumber();
		textSegment += String.format("\tmov $a0, Temp_%d\n",mulTempidx); // a0 = array size

		liRegString("v0", 9); // malloc syscall code
		textSegment += String.format("\tsyscall\n");

		// dstTempReg = array's pointer
		int dstidx = dstTempReg.getSerialNumber();
		textSegment += String.format("\tmov Temp_%d, $v0\n",dstidx);
	}

	/************************************************ Branches ************************************************/

	public void blt(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tblt Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}

	public void bgt(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tbgt Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tbge Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}

	public void ble(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tble Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}

	public void bne(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tbne Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}

	public void beq(TEMP oprnd1,TEMP oprnd2,String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		textSegment += String.format("\tbeq Temp_%d, Temp_%d, %s\n",i1,i2,label);
	}

	public void beqz(TEMP oprnd1,String label) {
		int i1 = oprnd1.getSerialNumber();

		textSegment += String.format("\tbeq Temp_%d, $zero, %s\n", i1, label);
	}

	/************************************************ Function call ************************************************/

	// Push args in reverse and return the number of args
	public int pushArgs(TEMP_LIST argsTempList) {
		// base case
		if (argsTempList == null) {
			return 0;
		}

		// recursion
		int argsNum = pushArgs(argsTempList.tail) + 1;

		// push with stack folding
		if (argsTempList.head != null) {
			pushTempReg(argsTempList.head);
		}

		return argsNum;
	}

	public void callFuncStmt(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		// push args
		int argsNum = pushArgs(argsTempList);

		// jal
		textSegment += String.format("\tjal %s\n", funcName);

		// restore sp
		textSegment += String.format("\taddu $sp, $sp, %d\n", argsNum);
	}

	public void callFuncExp(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		callFuncStmt(dst, funcName, argsTempList);

		// store return value in dst
		int dstidx=dst.getSerialNumber();
		textSegment += String.format("\tmov Temp_%d, $v0\n", dstidx);
	}

	/********************************************* Function delaration ***********************************************/

	public void pushTempReg(TEMP reg) {
		int regInd =reg.getSerialNumber();
		textSegment += String.format("\tsubu $sp, $sp, 4\n");
		textSegment += String.format("\tsw $Temp_%d, 0($sp)\n", regInd);
	}

	public void pushRegNameString(String reg) {
		textSegment += String.format("\tsubu $sp, $sp, 4\n");
		textSegment += String.format("\tsw $%s, 0($sp)\n", reg);
	}

	public void registersBackup() {
		for (int i = 0; i < 10; i++) {
			pushRegNameString(String.format("t%d",i));
		}
	}

	public void registersRestore() {
		for (int i = 0; i < 10; i++) {
			int offset = (i + 1) * -4;
			textSegment += String.format("\tlw $t%d, %d($sp)\n",i, offset);
		}
	}

	public void funcPrologue(int localVarsNum, String funcName) {
		label(funcName + "_prologue");
		pushRegNameString("ra");
		pushRegNameString("fp");
		textSegment += String.format("\tmov $fp, $sp\n");
		registersBackup();
		textSegment += String.format("\tsub $sp, $sp, %d\n", localVarsNum * WORD_SIZE);
	}

	public void funcEpilogue(String funcName) {
		label(funcName + "_epilogue");
		textSegment += String.format("\tmov $sp, $fp\n");
		registersRestore();
		textSegment += String.format("\tlw $fp, 0($sp)\n");
		textSegment += String.format("\tlw $ra, 4($sp)\n");
		textSegment += String.format("\taddu $sp, $sp, 8\n");
		textSegment += String.format("\tjr $ra\n");
	}

	public void doReturn(TEMP expRetReg) {
		int expRetRegIdx=expRetReg.getSerialNumber();
		textSegment += String.format("\tmov $v0, Temp_%d\n", expRetRegIdx);
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
