package MIPS;

import java.io.PrintWriter;

import TEMP.*;
import TYPES.*;
import labels.Labels;
import SYMBOL_TABLE.*;

public class MIPSGenerator {

	private int WORD_SIZE = 4;
	private int TMPS_BACKUP_SPACE = 40; // t0 - t9 backup
	private String ABORT_LABEL = "abort_label";
	private boolean add_abort_flag = false;
	private PrintWriter fileWriter;
	private String dataSegment = "";
	private String textSegment = "";

	public void addMain() {
		textSegment += String.format("main:\n");
		textSegment += String.format("\tjal user_main\n");
		exit();
	}

	public void finalizeFile() {
		addMain();

		if (add_abort_flag) {
			textSegment += String.format("%s:\n", ABORT_LABEL);
			exit();
		}

		fileWriter.format(dataSegment);
		fileWriter.format(".text\n");
		fileWriter.format(textSegment);

		fileWriter.close();
	}

	/***********************************************
	 * Supported syscalls
	 ***********************************************/

	public void print_int(TEMP t) {
		int idx = t.getSerialNumber();
		textSegment += String.format("\tmove $a0, %s\n", tempString(idx));
		textSegment += String.format("\tli $v0, 1\n");
		textSegment += String.format("\tsyscall\n");
		textSegment += String.format("\tli $a0, 32\n");
		textSegment += String.format("\tli $v0, 11\n");
		textSegment += String.format("\tsyscall\n");
	}

	/************************************************
	 * General
	 ************************************************/

	private static String tempString(int temp) {
		return String.format("Temp_%d", temp);
	}

	// TODO delete/change
	public void load(TEMP dst, String var_name) {
		int idxdst = dst.getSerialNumber();
		textSegment += String.format("\tlw %s, global_%s\n", tempString(idxdst), var_name);
	}

	// TODO delete/change
	public void store(String var_name, TEMP src) {
		int idxsrc = src.getSerialNumber();
		textSegment += String.format("\tsw %s, global_%s\n", tempString(idxsrc), var_name);
	}

	public void loadAddress(TEMP dstReg, String label) {
		int dstRegInd = dstReg.getSerialNumber();
		textSegment += String.format("\tla %s, %s\n", tempString(dstRegInd), label);
	}

	public void label(String inlabel) {
		if (inlabel.equals("main")) {
			textSegment += String.format("user_main:\n"); // _ isn't allowed in an identifier name -> user_name is a
															// unique label
		} else {
			textSegment += String.format("%s:\n", inlabel);
		}
	}

	public void jump(String inlabel) {
		textSegment += String.format("\tj %s\n", inlabel);
	}

	public void liTemp(TEMP t, int value) {
		int idx = t.getSerialNumber();
		textSegment += String.format("\tli %s, %d\n", tempString(idx), value);
	}

	public void liRegString(String reg, int value) {
		textSegment += String.format("\tli $%s, %d\n", reg, value);
	}

	public void lbFromTmpToTmp(TEMP dstTmp, TEMP srcTmp, int offset) {
		int dstTmpInd = dstTmp.getSerialNumber();
		int srcTmpInd = srcTmp.getSerialNumber();
		textSegment += String.format("\tlb %s, %d(%s)\n", tempString(dstTmpInd), offset, tempString(srcTmpInd));
	}

	public void sbFromTmpToTmp(TEMP dstTmp, TEMP srcTmp, int offset) {
		int dstTmpInd = dstTmp.getSerialNumber();
		int srcTmpInd = srcTmp.getSerialNumber();
		textSegment += String.format("\tsb %s, %d(%s)\n", tempString(dstTmpInd), offset, tempString(srcTmpInd));
	}

	public void addConstIntToTmp(TEMP dstTmp, int constInt) {
		int dstTmpInd = dstTmp.getSerialNumber();

		textSegment += String.format("\tadd%s %s, %s, %d\n", constInt >= 0 ? "u" : "i", tempString(dstTmpInd),
				tempString(dstTmpInd), constInt);

		// if (constInt >= 0) {
		// textSegment += String.format("\taddu %s, %s, %d\n", tempString(dstTmpInd),
		// tempString(dstTmpInd), constInt);
		// } else {
		// textSegment += String.format("\taddi %s, %s, %d\n", dstTmpInd, dstTmpInd,
		// constInt);
		// }
	}

	public void multTmpByConstInt(TEMP tmp, int constInt) {
		int tmpInd = tmp.getSerialNumber();
		textSegment += String.format("\tmul %s, %s, %d\n", tempString(tmpInd), tempString(tmpInd), constInt);
	}

	public void initTmpWithZero(TEMP dstTmp) {
		int dstTmpInd = dstTmp.getSerialNumber();
		moveRegisters(tempString(dstTmpInd), "$zero");
		// textSegment += String.format("\tmov %s, $zero\n", tempString(dstTmpInd));
	}

	public void malloc(TEMP resultPtrTmp) {
		liRegString("v0", 9);
		textSegment += String.format("\tsyscall\n");

		int resultPtrTmpInd = resultPtrTmp.getSerialNumber();
		moveRegisters(tempString(resultPtrTmpInd), "$v0");
		// textSegment += String.format("\tmov %s, $v0\n", tempString(resultPtrTmpInd));
	}

	public void malloc(TEMP resultPtrTmp, int size) {
		liRegString("a0", size);
		malloc(resultPtrTmp);
	}

	public void malloc(TEMP resultPtrTmp, TEMP sizeTmp) {
		int sizeTmpInd = sizeTmp.getSerialNumber();
		moveRegisters("$a0", tempString(sizeTmpInd));
		// textSegment += String.format("\tmov $a0, %s\n", tempString(sizeTmpInd));
		malloc(resultPtrTmp);
	}

	public void exit() {
		textSegment += String.format("\tli $v0, 10\n");
		textSegment += String.format("\tsyscall\n");
	}

	public void PrintInt(int tmpInd) {
		textSegment += String.format("\tli $a0, %d\n", tmpInd);
		textSegment += String.format("\tli $v0, 1\n");
		textSegment += String.format("\tsyscall\n");
	}

	public void PrintString(int tmpInd) {
		textSegment += String.format("\tli $a0, %s\n", tempString(tmpInd));
		textSegment += String.format("\tli $v0, 4\n");
		textSegment += String.format("\tsyscall\n");
	}

	/************************************************
	 * Arithmetics
	 ************************************************/

	public void checkLimits(TEMP dst) {
		int dstidx = dst.getSerialNumber();

		// check top limit and fix the result if needed
		String checkBottomLimitLabel = Labels.getAvialableLabel("check_bottom_limit");
		textSegment += String.format("\tble %s, 32767, %s\n", tempString(dstidx), checkBottomLimitLabel); // 2^15 - 1 =
																											// 32767
		textSegment += String.format("\tli %s, 32767\n", tempString(dstidx)); // fix the result

		// check bottom limit and fix the result if needed
		label(checkBottomLimitLabel);
		String afterLimitsChecksLabel = Labels.getAvialableLabel("after_limits_checks");
		textSegment += String.format("\tbge %s, -32768, %s\n", tempString(dstidx), afterLimitsChecksLabel); // -2^15 =
																											// -32768
		textSegment += String.format("\tli %s, -32768\n", tempString(dstidx)); // fix the result

		label(afterLimitsChecksLabel);
	}

	private void binopRegisters(String operation, String dst, String r1, String value) {
		this.textSegment += String.format("\t%s %s, %s, %s\n", operation, dst, r1, value);
	}

	private void selfBinopRegister(String operation, String register, int constant) {
		this.binopRegisters(operation, register, register, String.valueOf(constant));
	}

	public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		add(dst, oprnd1, oprnd2, true);
	}

	public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2, boolean checkOverflow) {
		textSegment += String.format("\tadd %s\n", binopString(dst, oprnd1, oprnd2));

		// we don't want to check overflow when summing len(s1) + len(s2) of strings for
		// mallocing mem for s1s2
		if (checkOverflow) {
			checkLimits(dst);
		}
	}

	public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		textSegment += String.format("\tsub %s\n", binopString(dst, oprnd1, oprnd2));
		checkLimits(dst);
	}

	public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		mul(dst, oprnd1, oprnd2, true);
	}

	public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2, boolean checkOverflow) {
		textSegment += String.format("\tmul %s\n", binopString(dst, oprnd1, oprnd2));

		if (checkOverflow) {
			checkLimits(dst);
		}
	}

	public void div(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		String div_by_0 = Labels.getAvialableLabel("div_by_0");
		String end_div = Labels.getAvialableLabel("end_div");
		beqz(oprnd2, div_by_0);
		textSegment += String.format("\tdiv %s\n", binopString(dst, oprnd1, oprnd2));
		checkLimits(dst);
		jump(end_div);
		label(div_by_0);
		textSegment += String.format("\tli $a0, $string_illegal_div_by_0\n");
		textSegment += String.format("\tli $v0, 4\n");
		textSegment += String.format("\tsyscall\n");
		label(end_div);
	}

	private static String binopString(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();

		return String.format("\t%s, %s, %s", tempString(dstidx), tempString(i1), tempString(i2));
	}

	/************************************************
	 * Local Variables
	 ************************************************/

	public void localVarAssignment(int varIndex, TEMP tmpRvalue) {
		int rValueTmpInd = tmpRvalue.getSerialNumber();
		int offset = (-1) * (varIndex * WORD_SIZE + TMPS_BACKUP_SPACE);
		textSegment += String.format("\tsw %s, %d($fp)\n", tempString(rValueTmpInd), offset);
	}

	// assign <tmpRvalue> to <offsetTmp> at the local variable which its local index
	// is <varIndex>
	public void localVarAssignment(TEMP arrayTemp, TEMP offsetTmp, TEMP tmpRvalue) {
		// // newOffsetTmp = local variable
		// TEMP newOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// loadFromLocal(newOffsetTmp, varIndex);

		// newOffsetTmp += offsetTmp
		add(arrayTemp, arrayTemp, offsetTmp, false);

		// *newOffsetTmp = tmpRvalue
		int arrayTempInd = arrayTemp.getSerialNumber();
		int tmpRvalueInd = tmpRvalue.getSerialNumber();
		textSegment += String.format("\tsw %s, 0(%s)\n", tempString(tmpRvalueInd), tempString(arrayTempInd));
	}

	public void storeLocalVar(int ind, TEMP initialValueTmp) {
		int idx = initialValueTmp.getSerialNumber();
		int offset = (-1) * (ind * WORD_SIZE + 40); // 40 is for storing tmps
		textSegment += String.format("\tsw %s, %d($fp)\n", tempString(idx), offset);
	}

	public void loadFromLocal(TEMP tmp, int localVarInd) {
		int tmpInd = tmp.getSerialNumber();
		int offset = (-1) * (localVarInd * WORD_SIZE + 40); // 40 is for storing tmps
		textSegment += String.format("\tlw %s, %d($fp)\n", tempString(tmpInd), offset);
	}

	public void movFromTmpToTmp(TEMP tmpDst, TEMP tmpSrc) {
		int tmpDstInd = tmpDst.getSerialNumber();
		int tmpSrcInd = tmpSrc.getSerialNumber();
		this.moveRegisters(tempString(tmpDstInd), tempString(tmpSrcInd));
		// textSegment += String.format("\tmov %s, %s\n", tempString(tmpDstInd),
		// tempString(tmpSrcInd));
	}

	public void storeTempToPointer(TEMP pointerTmp, TEMP tmpRvalue, int index) {
		textSegment += String.format("\tsw %s, %d(%s)\n", tempString(tmpRvalue.getSerialNumber()), index,
				tempString(pointerTmp.getSerialNumber()));
	}

	public void moveRegisters(String dst, String src) {
		textSegment += String.format("\tmove %s, %s\n", dst, src);
	}

	/************************************************
	 * Parameters
	 ************************************************/

	public void loadFromParameters(TEMP tmp, int ParamInd) {
		int tmpInd = tmp.getSerialNumber();
		int offset = ParamInd * WORD_SIZE + 4; // first parameter is at 8($fp), since: prev fp = 0($fp), ra = 4($fp)
		textSegment += String.format("\tlw %s, %d($fp)\n", tempString(tmpInd), offset);
	}

	/************************************************
	 * Global Variables
	 ************************************************/

	// decalaring a global variable without assigning it an initial value
	public void declareGlobalVar(String label, String type) {
		if (type == "string") {
			dataSegment += String.format("%s: .asciiz \"\"\n", label);
		} else { // int
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
		int tmpRvalueInd = tmpRvalue.getSerialNumber();
		textSegment += String.format("\tsw %s, %s\n", tempString(tmpRvalueInd), globalVarLabel);
	}

	public void loadFromGlobal(TEMP tmp, String globalVarLabel) {
		int tmpInd = tmp.getSerialNumber();
		textSegment += String.format("\tlw %s, %s\n", tempString(tmpInd), globalVarLabel);
	}

	/************************************************
	 * String
	 ************************************************/

	public void calcStringLengthIntoTmp(TEMP stringLenTmp, TEMP stringTmp) {
		// TEMP stringByteTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		String loopLabel = Labels.getAvialableLabel("str_len_loop");
		String endLabel = Labels.getAvialableLabel("str_len_end");

		final String STRING_LENGTH_REG = "$s0";

		// initialize stringLenTmp
		initTmpWithZero(stringLenTmp);

		// str_len_loop:
		label(loopLabel);

		// load byte
		// lbFromTmpToTmp(stringByteTemp, stringTmp, 0);
		this.textSegment += String.format("\tlb %s, %d(%s)\n", STRING_LENGTH_REG, 0,
				tempString(stringTmp.getSerialNumber()));

		// stop counting if reached the string's end
		// beqz(stringByteTemp, endLabel);
		this.textSegment += String.format("\tbeq %s, $zero, %s\n", STRING_LENGTH_REG, endLabel);

		// increase counter by one
		addConstIntToTmp(stringLenTmp, 1);

		// increase byte pointer by one
		// addConstIntToTmp(stringByteTemp, 1);
		this.selfBinopRegister("add", STRING_LENGTH_REG, 1);

		// j str_len_loop
		jump(loopLabel);

		// str_len_end:
		label(endLabel);
	}

	/************************************************
	 * Class
	 ************************************************/

	public int getClassSize(TYPE objectType) {
		TYPE_CLASS t = ((TYPE_CLASS) SYMBOL_TABLE.getInstance().find(objectType.name));

		int fieldsNumTotal = t.fieldsNum;
		while (t.father.isPresent()) {
			t = t.father.get();
			fieldsNumTotal += t.fieldsNum;
		}

		return fieldsNumTotal * WORD_SIZE + 4; // 4 is for the vtable
	}

	public void createNewObject(TEMP dstTempReg, TYPE objectType) {
		int classSize = getClassSize(objectType);
		malloc(dstTempReg, classSize);
	}

	/************************************************
	 * Array
	 ************************************************/

	public void createNewArray(TEMP dstTemp, TEMP subscriptTemp) {

		// calculate the array size
		// TEMP sizeTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// movFromTmpToTmp(sizeTemp, subscriptTemp);

		final String ALLOC_SIZE_REG = "$a0";
		moveRegisters(ALLOC_SIZE_REG, tempString(subscriptTemp.getSerialNumber()));
		this.selfBinopRegister("add", ALLOC_SIZE_REG, 1);
		this.selfBinopRegister("mul", ALLOC_SIZE_REG, WORD_SIZE);

		// alocate memory for the array
		malloc(dstTemp);

		// first cell of the array should contain its size
		int subscriptTempInd = subscriptTemp.getSerialNumber();
		int dstTempInd = dstTemp.getSerialNumber();
		textSegment += String.format("\tsw %s, 0(%s)\n", tempString(subscriptTempInd), tempString(dstTempInd));
	}

	public void loadArraySize(TEMP dstTmp, TEMP arrayTmp) {
		int dstTmpInd = dstTmp.getSerialNumber();
		int arrayTmpInd = arrayTmp.getSerialNumber();

		textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTmpInd), tempString(arrayTmpInd));
	}

	public void checkAccessViolation(TEMP arrayTmp, TEMP subscriptTemp) {

		label(Labels.getAvialableLabel("check_access_violation_start"));
		String after_check_access_violation = Labels.getAvialableLabel("after_check_access_violation");
		String invalid_access = Labels.getAvialableLabel("invalid_access");

		// add abort stub in the end of the text segment
		add_abort_flag = true;

		// check for a negative index
		bltz(subscriptTemp, invalid_access);

		// check for an index which is bigger than array size
		// TEMP arraySizeTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// loadArraySize(arraySizeTmp, arrayTmp);

		final String ARRAY_SIZE_REG = "$s0";
		this.textSegment += String.format("lw %s, 0(%s)", ARRAY_SIZE_REG, tempString(arrayTmp.getSerialNumber()));
		// bge(subscriptTemp, arraySizeTmp, ABORT_LABEL);
		this.textSegment += String.format("bge %s, %s, %s", tempString(subscriptTemp.getSerialNumber()),
				ARRAY_SIZE_REG, invalid_access);

		jump(after_check_access_violation);
		label(invalid_access);
		textSegment += String.format("\tli $a0, $string_access_violation\n");
		textSegment += String.format("\tli $v0, 4\n");
		textSegment += String.format("\tsyscall\n");

		label(after_check_access_violation);
	}

	public void assignTmpWithArrayElement(TEMP dstTemp, TEMP arrayTmp, TEMP subscriptTemp) {

		final String OFFSET_REG = "$s0";

		// check access violation
		checkAccessViolation(arrayTmp, subscriptTemp);

		// calc offset
		// TEMP arrayOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// movFromTmpToTmp(arrayOffsetTmp, subscriptTemp);
		this.moveRegisters(OFFSET_REG, tempString(subscriptTemp.getSerialNumber()));
		// addConstIntToTmp(arrayOffsetTmp, 1); // 1 is for the size of the array (which
		// is its first element)
		this.selfBinopRegister("add", OFFSET_REG, 1); // 1 is for the size of the array (which is its first element)
		// multTmpByConstInt(arrayOffsetTmp, WORD_SIZE);
		this.selfBinopRegister("mul", OFFSET_REG, WORD_SIZE);

		// load from offset
		// add(arrayOffsetTmp, arrayOffsetTmp, arrayTmp, false);
		this.binopRegisters("addu", OFFSET_REG, tempString(arrayTmp.getSerialNumber()), OFFSET_REG);
		int dstTempInd = dstTemp.getSerialNumber();
		// int arrayOffsetTmpInd = arrayOffsetTmp.getSerialNumber();
		// textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTempInd),
		// tempString(arrayOffsetTmpInd));
		textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTempInd), OFFSET_REG);
	}

	public void assignTmpWithOffset(TEMP dstTemp, TEMP arrayTmp, TEMP subscriptTemp) {
		// check access violation
		checkAccessViolation(arrayTmp, subscriptTemp);

		// offset = dstTemp = (subscriptTemp + 1) * 4
		movFromTmpToTmp(dstTemp, subscriptTemp);
		addConstIntToTmp(dstTemp, 1); // 1 is for the size of the array (which is its first element)
		multTmpByConstInt(dstTemp, WORD_SIZE);
	}

	// public void assignArrayElementWithTmp(TEMP arrayTmp, TEMP subscriptTemp, TEMP
	// srcTemp) {
	//
	// // check access violation
	// String abortLabel = Labels.getAvialableLabel("abort");
	// checkAccessViolation(arrayTmp, subscriptTemp, abortLabel);
	//
	// // calc offset
	// TEMP arrayOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
	// movFromTmpToTmp(arrayOffsetTmp, subscriptTemp);
	// addConstIntToTmp(arrayOffsetTmp, 1); // 1 is for the size of the array (which
	// is its first element)
	// multTmpByConstInt(arrayOffsetTmp, WORD_SIZE);
	//
	// // load from offset
	// add(arrayOffsetTmp, arrayOffsetTmp, arrayTmp, false);
	// int dstTempInd = dstTemp.getSerialNumber();
	// int arrayOffsetTmpInd = arrayOffsetTmp.getSerialNumber();
	// textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTempInd),
	// tempString(arrayOffsetTmpInd));
	//
	// // abort:
	// label(abortLabel);
	// exit();
	// }

	/************************************************
	 * Branches
	 ************************************************/

	public void blt(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("blt", oprnd1, oprnd2, label);
	}

	public void bltz(TEMP oprnd, String label) {
		int i = oprnd.getSerialNumber();
		textSegment += String.format("\tbltz %s, %s\n", tempString(i), label);
	}

	public void bgt(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("bgt", oprnd1, oprnd2, label);
	}

	public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("bge", oprnd1, oprnd2, label);
	}

	public void ble(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("ble", oprnd1, oprnd2, label);
	}

	public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("bne", oprnd1, oprnd2, label);
	}

	public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
		this.branchBinop("beq", oprnd1, oprnd2, label);
	}

	public void beqz(TEMP oprnd1, String label) {
		int i1 = oprnd1.getSerialNumber();

		textSegment += String.format("\tbeq %s, $zero, %s\n", tempString(i1), label);
	}

	private void branchBinop(String command, TEMP oprnd1, TEMP oprnd2, String label) {
		int i1 = oprnd1.getSerialNumber();
		int i2 = oprnd2.getSerialNumber();

		this.textSegment += String.format("\t%s %s, %s, %s\n", command, tempString(i1), tempString(i2), label);
	}

	/************************************************
	 * Function call
	 ************************************************/

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
		textSegment += String.format("\taddu $sp, $sp, %d\n", argsNum * WORD_SIZE);
		if (funcName.equals("PrintInt")) {
			PrintInt(argsTempList.head.getSerialNumber());
		}
		if (funcName.equals("PrintString")) {
			PrintString(argsTempList.head.getSerialNumber());
		}
	}

	public void callFuncExp(TEMP dst, String funcName, TEMP_LIST argsTempList) {
		callFuncStmt(dst, funcName, argsTempList);

		// store return value in dst
		int dstidx = dst.getSerialNumber();
		moveRegisters(tempString(dstidx), "$v0");
		// textSegment += String.format("\tmov %s, $v0\n", tempString(dstidx));
	}

	/*********************************************
	 * Function delaration
	 ***********************************************/

	public void pushTempReg(TEMP reg) {
		int regInd = reg.getSerialNumber();
		textSegment += String.format("\tsubu $sp, $sp, 4\n");
		textSegment += String.format("\tsw $%s, 0($sp)\n", tempString(regInd));
	}

	public void pushRegNameString(String reg) {
		textSegment += String.format("\tsubu $sp, $sp, 4\n");
		textSegment += String.format("\tsw $%s, 0($sp)\n", reg);
	}

	public void registersBackup() {
		for (int i = 0; i < 10; i++) {
			pushRegNameString(String.format("t%d", i));
		}
	}

	public void registersRestore() {
		for (int i = 0; i < 10; i++) {
			int offset = (i + 1) * -4;
			textSegment += String.format("\tlw $t%d, %d($sp)\n", i, offset);
		}
	}

	public void funcPrologue(int localVarsNum, String funcName) {
		label(Labels.getAvialableLabel(funcName + "_prologue"));
		pushRegNameString("ra");
		pushRegNameString("fp");
		textSegment += String.format("\tmov $fp, $sp\n");
		registersBackup();
		textSegment += String.format("\tsub $sp, $sp, %d\n", localVarsNum * WORD_SIZE);
	}

	public void funcEpilogue(String funcName) {
		label(Labels.getAvialableLabel(funcName + "_epilogue"));
		moveRegisters("$sp", "$fp");
		// textSegment += String.format("\tmov $sp, $fp\n");
		registersRestore();
		textSegment += String.format("\tlw $fp, 0($sp)\n");
		textSegment += String.format("\tlw $ra, 4($sp)\n");
		textSegment += String.format("\taddu $sp, $sp, 8\n");
		textSegment += String.format("\tjr $ra\n");
	}

	public void doReturn(TEMP expRetReg) {
		int expRetRegIdx = expRetReg.getSerialNumber();
		moveRegisters("$v0", tempString(expRetRegIdx));
		// textSegment += String.format("\tmov $v0, %s\n", tempString(expRetRegIdx));
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance() {
		if (instance == null) {
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try {
				/*********************************************************************************/
				/*
				 * [1] Open the MIPS text file and write data section with error message strings
				 */
				/*********************************************************************************/
				String dirname = "./output/";
				String filename = String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname + filename);
			} catch (Exception e) {
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
