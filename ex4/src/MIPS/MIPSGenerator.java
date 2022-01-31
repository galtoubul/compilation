package MIPS;

import java.io.PrintWriter;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import TEMP.TEMP;
import TYPES.*;
import labels.Labels;
import pair.Pair;

public class MIPSGenerator {

	private static final int WORD_SIZE = 4;
	private static final int MAX_TEMPS = 10; // $t0 - $t9
	private static final int TMPS_BACKUP_SPACE = WORD_SIZE * MAX_TEMPS; // $t0 - $t9 backup
	private static final String ABORT_LABEL = "abort_label";
	private static final String SPACE_LABLE = "WS";
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

	public void print_int(int temp) {
		textSegment += String.format("\tmove $a0, %s\n", tempString(temp));
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
		return String.format("$t%d", temp);
	}

	// TODO delete/change
	public void load(int dst, String var_name) {
		textSegment += String.format("\tlw %s, global_%s\n", tempString(dst), var_name);
	}

	// TODO delete/change
	public void store(String var_name, int src) {
		textSegment += String.format("\tsw %s, global_%s\n", tempString(src), var_name);
	}

	public void loadAddress(int dstReg, String label) {
		textSegment += String.format("\tla %s, %s\n", tempString(dstReg), label);
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

	public void liTemp(int temp, int value) {
		this.liRegString(tempString(temp), value);
	}

	public void liRegString(String reg, int value) {
		textSegment += String.format("\tli %s, %d\n", reg, value);
	}

	public void lbFromRegToReg(String dst, String src, int offset) {
		textSegment += String.format("\tlb %s, %d(%s)\n", dst, offset, src);
	}

	public void sbFromTmpToTmp(int dst, int src, int offset) {
		textSegment += String.format("\tsb %s, %d(%s)\n", tempString(dst), offset, tempString(src));
	}
	public void sbFromTmpToReg(String dst, String src, int offset) {
		textSegment += String.format("\tsb %s, %d(%s)\n", src , offset, dst);
	}
	public void addConstIntToReg(String dst, int constInt) {
		textSegment += String.format("\taddi %s, %s, %d\n", dst, dst, constInt);
	}

	public void addConstIntToTmp(int dst, int constInt) {
		textSegment += String.format("\tadd%s %s, %s, %d\n", constInt >= 0 ? "u" : "i", tempString(dst),
				tempString(dst), constInt);

		// if (constInt >= 0) {
		// textSegment += String.format("\taddu %s, %s, %d\n", tempString(dstTmpInd),
		// tempString(dstTmpInd), constInt);
		// } else {
		// textSegment += String.format("\taddi %s, %s, %d\n", dstTmpInd, dstTmpInd,
		// constInt);
		// }
	}

	public void multTmpByConstInt(int tmp, int constInt) {
		textSegment += String.format("\tmul %s, %s, %d\n", tempString(tmp), tempString(tmp), constInt);
	}

	public void initTmpWithZero(int dst) {
		moveRegisters(tempString(dst), "$zero");
		// textSegment += String.format("\tmov %s, $zero\n", tempString(dstTmpInd));
	}

	public void initRegWithZero(String dst) {
		moveRegisters(dst, "$zero");
		// textSegment += String.format("\tmov %s, $zero\n", tempString(dstTmpInd));
	}

	public void malloc(int resultPtrTmp) {
		liRegString("$v0", 9);
		textSegment += String.format("\tsyscall\n");

		moveRegisters(tempString(resultPtrTmp), "$v0");
		// textSegment += String.format("\tmov %s, $v0\n", tempString(resultPtrTmpInd));
	}

	public void malloc(int resultPtrTmp, int size) {
		liRegString("$a0", size);
		malloc(resultPtrTmp);
	}

	public void mallocTmpSize(int resultPtrTmp, int sizeTmp) {
		moveRegisters("$a0", tempString(sizeTmp));
		// textSegment += String.format("\tmov $a0, %s\n", tempString(sizeTmpInd));
		malloc(resultPtrTmp);
	}
	public void mallocRegSize(int resultPtrTmp, String sizeTmp) {
		moveRegisters("$a0", sizeTmp);
		// textSegment += String.format("\tmov $a0, %s\n", tempString(sizeTmpInd));
		malloc(resultPtrTmp);
	}

	public void exit() {
		textSegment += String.format("\tli $v0, 10\n");
		textSegment += String.format("\tsyscall\n");
	}

	public void PrintInt(int tmpInd) {
		textSegment += String.format("\tmove $a0, %s\n", tempString(tmpInd));
		textSegment += String.format("\tli $v0, 1\n");
		textSegment += String.format("\tsyscall\n");
		PrintWord(SPACE_LABLE);
	}

	public void PrintInt(String temp) {
		textSegment += String.format("\tmove $a0, %s\n", temp);
		textSegment += String.format("\tli $v0, 1\n");
		textSegment += String.format("\tsyscall\n");
		PrintWord(SPACE_LABLE);
	}

	public void PrintString(int tmpInd) {
		textSegment += String.format("\tmove $a0, %s\n", tempString(tmpInd));
		textSegment += String.format("\tli $v0, 4\n");
		textSegment += String.format("\tsyscall\n");
	}

	public void PrintWord(String word) {
		textSegment += String.format("\tla $a0, %s\n", word);
		textSegment += String.format("\tli $v0, 4\n");
		textSegment += String.format("\tsyscall\n");
	}

	/************************************************
	 * Arithmetics
	 ************************************************/
	public void checkLimits(String dst) {
		// check top limit and fix the result if needed
		String checkBottomLimitLabel = Labels.getAvialableLabel("check_bottom_limit");
		textSegment += String.format("\tble %s, 32767, %s\n", dst, checkBottomLimitLabel); // 2^15 - 1 =
		// 32767
		textSegment += String.format("\tli %s, 32767\n", dst); // fix the result

		// check bottom limit and fix the result if needed
		label(checkBottomLimitLabel);
		String afterLimitsChecksLabel = Labels.getAvialableLabel("after_limits_checks");
		textSegment += String.format("\tbge %s, -32768, %s\n", dst, afterLimitsChecksLabel); // -2^15 =
		// -32768
		textSegment += String.format("\tli %s, -32768\n", dst); // fix the result

		label(afterLimitsChecksLabel);
	}

	public void checkLimits(int dst) {
		// check top limit and fix the result if needed
		String checkBottomLimitLabel = Labels.getAvialableLabel("check_bottom_limit");
		textSegment += String.format("\tble %s, 32767, %s\n", tempString(dst), checkBottomLimitLabel); // 2^15 - 1 =
																										// 32767
		textSegment += String.format("\tli %s, 32767\n", tempString(dst)); // fix the result

		// check bottom limit and fix the result if needed
		label(checkBottomLimitLabel);
		String afterLimitsChecksLabel = Labels.getAvialableLabel("after_limits_checks");
		textSegment += String.format("\tbge %s, -32768, %s\n", tempString(dst), afterLimitsChecksLabel); // -2^15 =
																											// -32768
		textSegment += String.format("\tli %s, -32768\n", tempString(dst)); // fix the result

		label(afterLimitsChecksLabel);
	}

	private void binopRegisters(String operation, String dst, String r1, String value) {
		this.textSegment += String.format("\t%s %s, %s, %s\n", operation, dst, r1, value);
	}

	private void binopRegistersConst(String operation, String dst, String r1, int constant) {
		this.textSegment += String.format("\t%s %s, %s, %s\n", operation, dst, r1, String.valueOf(constant));
	}

	private void selfBinopRegister(String operation, String register, int constant) {
		this.binopRegisters(operation, register, register, String.valueOf(constant));
	}

	public void checkEqStrings(int dst, int str1Temp,  int str2Temp) {
		String labelNeq = Labels.getAvialableLabel("neq");
		String labelStrEqLoop = Labels.getAvialableLabel("str_eq_loop");
		String labelStrEqEnd = Labels.getAvialableLabel("str_eq_end");
		final String STR1_REG = "$s0";
		final String STR2_REG = "$s1";
		final String BIT1_REG = "$s2";
		final String BIT2_REG = "$s3";
		moveRegisters(STR1_REG,tempString(str1Temp));
		moveRegisters(STR2_REG,tempString(str2Temp));
		liTemp(dst,1);
		label(labelStrEqLoop);
		lbFromRegToReg(BIT1_REG, STR1_REG, 0);
		lbFromRegToReg(BIT2_REG, STR2_REG, 0);
		this.textSegment += String.format("\tbne %s, %s, %s\n",BIT1_REG, BIT2_REG, labelNeq);
		textSegment += String.format("\tbeq %s, $zero, %s\n", BIT1_REG, labelStrEqEnd);
		addConstIntToReg(STR1_REG, 1);
		addConstIntToReg(STR2_REG, 1);
		jump(labelStrEqLoop);
		label(labelNeq);
		liTemp(dst,0);
		label(labelStrEqEnd);


	}

	public void add(int dst, int oprnd1, int oprnd2) {
		add(dst, oprnd1, oprnd2, true);
	}

	public void add(String dst, String oprnd1, String oprnd2) {
		add(dst, oprnd1, oprnd2, true);
	}

	public void add(int dst, int oprnd1, int oprnd2, boolean checkOverflow) {
		textSegment += String.format("\tadd %s\n", binopString(dst, oprnd1, oprnd2));

		// we don't want to check overflow when summing len(s1) + len(s2) of strings for
		// mallocing mem for s1s2
		if (checkOverflow) {
			checkLimits(dst);
		}
	}

	public void add(String dst, String oprnd1, String oprnd2, boolean checkOverflow) {
		textSegment += String.format("\tadd %s\n", binopString(dst, oprnd1, oprnd2));

		// we don't want to check overflow when summing len(s1) + len(s2) of strings for
		// mallocing mem for s1s2
		if (checkOverflow) {
			checkLimits(dst);
		}
	}

	public void sub(int dst, int oprnd1, int oprnd2) {
		textSegment += String.format("\tsub %s\n", binopString(dst, oprnd1, oprnd2));
		checkLimits(dst);
	}
	public void sub(String dst, String oprnd1, String oprnd2) {
		textSegment += String.format("\tsub %s\n", binopString(dst, oprnd1, oprnd2));
	}

	public void mul(int dst, int oprnd1, int oprnd2) {
		mul(dst, oprnd1, oprnd2, true);
	}

	public void mul(int dst, int oprnd1, int oprnd2, boolean checkOverflow) {
		textSegment += String.format("\tmul %s\n", binopString(dst, oprnd1, oprnd2));

		if (checkOverflow) {
			checkLimits(dst);
		}
	}

	public void div(int dst, int oprnd1, int oprnd2) {
		String div_by_0 = Labels.getAvialableLabel("div_by_0");
		String end_div = Labels.getAvialableLabel("end_div");
		beqz(oprnd2, div_by_0);
		textSegment += String.format("\tdiv %s\n", binopString(dst, oprnd1, oprnd2));
		checkLimits(dst);
		jump(end_div);
		label(div_by_0);
		PrintWord("string_illegal_div_by_0");
		exit();
		label(end_div);
	}

	private static String binopString(int dst, int oprnd1, int oprnd2) {
		return String.format("%s, %s, %s", tempString(dst), tempString(oprnd1), tempString(oprnd2));
	}
	private static String binopString(String dst, String oprnd1, String oprnd2) {
		return String.format("%s, %s, %s", dst, oprnd1, oprnd2);
	}

	/************************************************
	 * Local Variables
	 ************************************************/

	public void localVarAssignment(int varIndex, int tmpRvalue) {
		int offset = (-1) * (varIndex * WORD_SIZE + TMPS_BACKUP_SPACE);
		textSegment += String.format("\tsw %s, %d($fp)\n", tempString(tmpRvalue), offset);
	}

	public void fieldAccess(int dst, int tmpRvalue, int fieldInd) {
		int offset = (fieldInd + 1) * WORD_SIZE;
		String invalid_ptr = Labels.getAvialableLabel("invalid_ptr");
		String end = Labels.getAvialableLabel("end");
		beqz(tmpRvalue, invalid_ptr);
		textSegment += String.format("\tlw %s, %d(%s)\n", tempString(dst), offset, tempString(tmpRvalue));
		jump(end);
		label(invalid_ptr);
		PrintWord("string_invalid_ptr_dref");
		exit();
		label(end);

	}

	public void assignToArrayElementWithOffset(int arrayTemp, int ind, int tmpRvalue) {
		int offset = (ind + 1) * WORD_SIZE;
		System.out.println("**********************" + offset);
		textSegment += String.format("\tsw %s, %d(%s)\n", tempString(tmpRvalue), offset, tempString(arrayTemp));
	}

	public void fieldAssignment(int tmpLvalue, int tmpRvalue, int fieldInd) {
		String invalid_ptr = Labels.getAvialableLabel("invalid_ptr");
		String end = Labels.getAvialableLabel("end");
		beqz(tmpLvalue, invalid_ptr);
		int offset = (fieldInd + 1) * WORD_SIZE;
		textSegment += String.format("\tsw %s, %d(%s)\n", tempString(tmpRvalue), offset, tempString(tmpLvalue));
		jump(end);
		label(invalid_ptr);
		PrintWord("string_invalid_ptr_dref");
		exit();
		label(end);
	}

	// assign <tmpRvalue> to <offsetTmp> at the local variable which its local index
	// is <varIndex>
	public void localVarAssignment(int arrayTemp, int offsetTmp, int tmpRvalue) {
		// // newOffsetTmp = local variable
		// int newOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// loadFromLocal(newOffsetTmp, varIndex);

		// newOffsetTmp += offsetTmp
		checkAccessViolation(arrayTemp, offsetTmp);
		addConstIntToTmp(offsetTmp, 1);
		System.out.format("%d, %d, %d\n", arrayTemp, offsetTmp, tmpRvalue);
		multTmpByConstInt(offsetTmp, 4);
		add(arrayTemp, arrayTemp, offsetTmp, false);
		// *newOffsetTmp = tmpRvalue
		textSegment += String.format("\tsw %s, 0(%s)\n", tempString(tmpRvalue), tempString(arrayTemp));

	}

	public void storeLocalVar(int ind, int initialValueTmp) {
		int offset = (-1) * (ind * WORD_SIZE + 40); // 40 is for storing tmps
		textSegment += String.format("\tsw %s, %d($fp)\n", tempString(initialValueTmp), offset);
	}

	public void loadFromLocal(int tmp, int localVarInd) {
		int offset = (-1) * (localVarInd * WORD_SIZE + 40); // 40 is for storing tmps
		textSegment += String.format("\tlw %s, %d($fp)\n", tempString(tmp), offset);
	}

	public void movFromTmpToTmp(int tmpDst, int tmpSrc) {
		this.moveRegisters(tempString(tmpDst), tempString(tmpSrc));
		// textSegment += String.format("\tmov %s, %s\n", tempString(tmpDst),
		// tempString(tmpSrc));
	}

	public void storeTempToPointer(int pointerTmp, int tmpRvalue, int index) {
		textSegment += String.format("\tsw %s, %d(%s)\n", tempString(tmpRvalue), index,
				tempString(pointerTmp));
	}

	public void moveRegisters(String dst, String src) {
		textSegment += String.format("\tmove %s, %s\n", dst, src);
	}

	/************************************************
	 * Parameters
	 ************************************************/

	public void loadFromParameters(int tmp, int ParamInd) {
		int offset = ParamInd * WORD_SIZE + 4; // first parameter is at 8($fp), since: prev fp = 0($fp), ra = 4($fp)
		textSegment += String.format("\tlw %s, %d($fp)\n", tempString(tmp), offset);
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
	public void globalVarAssignment(String globalVarLabel, int regRvalue) {
		textSegment += String.format("\tsw %s, %s\n", tempString(regRvalue), globalVarLabel);
	}

	public void loadFromGlobal(int tmp, String globalVarLabel) {
		textSegment += String.format("\tlw %s, %s\n", tempString(tmp), globalVarLabel);
	}

	/************************************************
	 * String
	 ************************************************/

	public void calcStringLengthIntoTmp(String stringLenTmp, String stringTmp) {
		// int stringByteTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		String loopLabel = Labels.getAvialableLabel("str_len_loop");
		String endLabel = Labels.getAvialableLabel("str_len_end");
		final String STRING_BYTE_REG = "$s5";
		final String STRING_REG = stringTmp;
		// initialize stringLenTmp
		initRegWithZero(stringLenTmp);
		// str_len_loop:
		label(loopLabel);
		// load byte
		// lbFromTmpToTmp(stringByteTemp, stringTmp, 0);
		this.textSegment += String.format("\tlb %s, %d(%s)\n", STRING_BYTE_REG, 0, STRING_REG);
		// stop counting if reached the string's end
		// beqz(stringByteTemp, endLabel);
		this.textSegment += String.format("\tbeq %s, $zero, %s\n", STRING_BYTE_REG, endLabel);
		// increase counter by one
		addConstIntToReg(stringLenTmp, 1);
		// increase byte pointer by one
		// addConstIntToTmp(stringByteTemp, 1);
		addConstIntToReg(STRING_REG, 1);

		// j str_len_loop
		jump(loopLabel);
		// str_len_end:
		label(endLabel);
	}

	/************************************************
	 * Class
	 ************************************************/

	public static int getClassSize(TYPE_CLASS objectType) {
		int fieldsNumTotal = objectType.fieldsNum;
		while (objectType.father.isPresent()) {
			objectType = objectType.father.get();
			fieldsNumTotal += objectType.fieldsNum;
		}

		return fieldsNumTotal * WORD_SIZE + 4; // 4 is for the vtable
	}

	public static String vtableLabel(String className) {
		return String.format("Vt_%s", className);
	}

	public void createNewObject(int dstTempReg, TYPE_CLASS objectType) {
		int classSize = getClassSize(objectType);
		malloc(dstTempReg, classSize);

		final String UTILITY_REG = "$s0";
		this.textSegment += String.format("\tla %s, %s\n", UTILITY_REG, vtableLabel(objectType.name));
		this.textSegment += String.format("\tsw %s, 0(%s)\n", UTILITY_REG, tempString(dstTempReg));
		System.out.format("Size: %d\n", objectType.initialValues.size());
		for (Pair<String, Optional<Object>> p : objectType.initialValues) {
			Optional<Object> o = p.getValue();
			if (o.isPresent()) {
				if (o.get() instanceof Integer) {
					this.textSegment += String.format("\tli %s, %s\n", UTILITY_REG, (Integer) o.get());
				} else {
					this.textSegment += String.format("\tli %s, %s\n", UTILITY_REG, (String) o.get());
				}
				this.textSegment += String.format("\tsw %s, 4(%s)\n", UTILITY_REG,
						tempString(dstTempReg));
			}
		}
	}

	/************************************************
	 * Array
	 ************************************************/

	public void createNewArray(int dstTemp, int subscriptTemp) {

		final String HELPER_REG = "$s0";
		final String ALLOC_SIZE_REG = "$a0";

		moveRegisters(HELPER_REG, tempString(subscriptTemp));

		// calculate the array size
		this.binopRegistersConst("add", ALLOC_SIZE_REG, HELPER_REG, 1);
		// this.selfBinopRegister("add", ALLOC_SIZE_REG, 1);
		this.selfBinopRegister("mul", ALLOC_SIZE_REG, WORD_SIZE);

		// alocate memory for the array
		malloc(dstTemp);

		// first cell of the array should contain its size
		textSegment += String.format("\tsw %s, 0(%s)\n", HELPER_REG, tempString(dstTemp));
	}

	public void loadArraySize(int dstTmp, int arrayTmp) {
		textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTmp), tempString(arrayTmp));
	}

	public void checkAccessViolation(int arrayTmp, int subscriptTemp) {

		label(Labels.getAvialableLabel("check_access_violation_start"));
		String after_check_access_violation = Labels.getAvialableLabel("after_check_access_violation");
		String invalid_access = Labels.getAvialableLabel("invalid_access");

		// add abort stub in the end of the text segment
		add_abort_flag = true;

		// check for a negative index
		bltz(subscriptTemp, invalid_access);

		// check for an index which is bigger than array size
		// int arraySizeTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// loadArraySize(arraySizeTmp, arrayTmp);

		final String ARRAY_SIZE_REG = "$s0";
		this.textSegment += String.format("\tlw %s, 0(%s)\n", ARRAY_SIZE_REG, tempString(arrayTmp));
		// bge(subscriptTemp, arraySizeTmp, ABORT_LABEL);
		this.textSegment += String.format("\tbge %s, %s, %s\n", tempString(subscriptTemp),
				ARRAY_SIZE_REG, invalid_access);
		jump(after_check_access_violation);

		label(invalid_access);
		PrintWord("string_access_violation");
		exit();

		label(after_check_access_violation);
	}

	public void checkAccessViolationWithConst(int arrayTmp, int subscript) {

		label(Labels.getAvialableLabel("check_access_violation_start"));
		String after_check_access_violation = Labels.getAvialableLabel("after_check_access_violation");
		String invalid_access = Labels.getAvialableLabel("invalid_access");

		// add abort stub in the end of the text segment
		add_abort_flag = true;

		// check for a negative index
		bltzConst(subscript, invalid_access);

		// check for an index which is bigger than array size
		// int arraySizeTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// loadArraySize(arraySizeTmp, arrayTmp);

		final String ARRAY_SIZE_REG = "$s0";
		this.textSegment += String.format("\tlw %s, 0(%s)\n", ARRAY_SIZE_REG, tempString(arrayTmp));
		// bge(subscriptTemp, arraySizeTmp, ABORT_LABEL);
		this.textSegment += String.format("\tbge %d, %s, %s\n", subscript,
				ARRAY_SIZE_REG, invalid_access);
		jump(after_check_access_violation);

		label(invalid_access);
		PrintWord("string_access_violation");
		exit();

		label(after_check_access_violation);
	}

	public void assignTmpWithArrayElement(int dstTemp, int arrayTmp, int subscriptTemp) {

		final String OFFSET_REG = "$s0";

		// check access violation
		checkAccessViolation(arrayTmp, subscriptTemp);

		// calc offset
		// int arrayOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
		// movFromTmpToTmp(arrayOffsetTmp, subscriptTemp);
		this.moveRegisters(OFFSET_REG, tempString(subscriptTemp));
		// addConstIntToTmp(arrayOffsetTmp, 1); // 1 is for the size of the array (which
		// is its first element)
		this.selfBinopRegister("add", OFFSET_REG, 1); // 1 is for the size of the array (which is its first element)
		// multTmpByConstInt(arrayOffsetTmp, WORD_SIZE);
		this.selfBinopRegister("mul", OFFSET_REG, WORD_SIZE);

		// load from offset
		// add(arrayOffsetTmp, arrayOffsetTmp, arrayTmp, false);
		this.binopRegisters("addu", OFFSET_REG, tempString(arrayTmp), OFFSET_REG);
		// int arrayOffsetTmpInd = arrayOffsetTmp.getSerialNumber();
		// textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTempInd),
		// tempString(arrayOffsetTmpInd));
		textSegment += String.format("\tlw %s, 0(%s)\n", tempString(dstTemp), OFFSET_REG);
	}

	public void assignTmpWithOffset(int dstTemp, int arrayTmp, int subscriptTemp) {
		// check access violation
		checkAccessViolation(arrayTmp, subscriptTemp);

		// offset = dstTemp = (subscriptTemp + 1) * 4
		movFromTmpToTmp(dstTemp, subscriptTemp);
		addConstIntToTmp(dstTemp, 1); // 1 is for the size of the array (which is its first element)
		multTmpByConstInt(dstTemp, WORD_SIZE);
	}

	// public void assignArrayElementWithTmp(int arrayTmp, int subscriptTemp, int
	// srcTemp) {
	//
	// // check access violation
	// String abortLabel = Labels.getAvialableLabel("abort");
	// checkAccessViolation(arrayTmp, subscriptTemp, abortLabel);
	//
	// // calc offset
	// int arrayOffsetTmp = TEMP_FACTORY.getInstance().getFreshTEMP();
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

	public void blt(int oprnd1, int oprnd2, String label) {
		this.branchBinop("blt", oprnd1, oprnd2, label);
	}

	public void bltz(int oprnd, String label) {
		textSegment += String.format("\tbltz %s, %s\n", tempString(oprnd), label);
	}

	public void bltzConst(int n, String label) {
		textSegment += String.format("\tbltz %d, %s\n", n, label);
	}

	public void bgt(int oprnd1, int oprnd2, String label) {
		this.branchBinop("bgt", oprnd1, oprnd2, label);
	}

	public void bge(int oprnd1, int oprnd2, String label) {
		this.branchBinop("bge", oprnd1, oprnd2, label);
	}

	public void ble(int oprnd1, int oprnd2, String label) {
		this.branchBinop("ble", oprnd1, oprnd2, label);
	}


	public void bne(int oprnd1, int oprnd2, String label) {
		this.branchBinop("bne", oprnd1, oprnd2, label);
	}

	public void beq(int oprnd1, int oprnd2, String label) {
		this.branchBinop("beq", oprnd1, oprnd2, label);
	}

	public void beqz(int oprnd1, String label) {
		textSegment += String.format("\tbeq %s, $zero, %s\n", tempString(oprnd1), label);
	}
	public void beqzReg(String oprnd1, String label) {
		textSegment += String.format("\tbeq %s, $zero, %s\n", oprnd1, label);
	}

	private void branchBinop(String command, int oprnd1, int oprnd2, String label) {
		this.textSegment += String.format("\t%s %s, %s, %s\n", command, tempString(oprnd1), tempString(oprnd2), label);
	}

	private void branchBinopReg(String command, String oprnd1, String oprnd2, String label) {
		this.textSegment += String.format("\t%s %s, %s, %s\n", command, oprnd1, oprnd2, label);
	}

	/************************************************
	 * Function call
	 ************************************************/

	// Push args in reverse and return the number of args
	public void pushArgs(Deque<Integer> argTemps) {
		// // base case
		// if (argsTempList == null) {
		// return 0;
		// }

		// // recursion
		// int argsNum = pushArgs(argsTempList.tail) + 1;

		// // push with stack folding
		// if (argsTempList.head != null) {
		// pushTempReg(argsTempList.head);
		// }
		// return argsNum;
		for (Integer temp : argTemps) {
			pushTempReg(temp);
		}
		// return args.size();
	}

	public void callFuncStmt(String funcName, Deque<Integer> argTemps) {
		// push args
		int argsNum = argTemps.size();
		pushArgs(argTemps);
		// jal
		textSegment += String.format("\tjal %s\n", funcName);
		// restore sp
		textSegment += String.format("\taddu $sp, $sp, %d\n", argsNum * WORD_SIZE);

	}

	public void callFuncExp(int dstTemp, String funcName, Deque<Integer> argTemps) {
		callFuncStmt(funcName, argTemps);

		// store return value in dst
		moveRegisters(tempString(dstTemp), "$v0");
	}
	public void copyString(int dstTmp) {
		String STR_REG = "$s0";
		String STR2_REG = "$s1";
		String BYTE_REG = "$s5";
		String DEST_REG = tempString(dstTmp);
		String DEST_REG_DUP = "$s6";

		String loopLabel1 = Labels.getAvialableLabel("loopLabel1");
		String loopLabel2 = Labels.getAvialableLabel("loopLabel2");
		String end1 = Labels.getAvialableLabel("end1");
		String end2 = Labels.getAvialableLabel("end2");
		moveRegisters(DEST_REG_DUP,tempString(dstTmp));
		label(loopLabel1);
		// byteOfString = 0(copiedString)
		lbFromRegToReg(BYTE_REG, STR_REG,0);

		beqzReg(BYTE_REG, end1);
		// store current byte in resultStringTmp
		sbFromTmpToReg(DEST_REG, BYTE_REG, 0);
		// addu dstTmp, dstTmp, 1
		addConstIntToReg(DEST_REG, 1);
		addConstIntToReg(STR_REG, 1);
		// addu byteOfString, byteOfString, 1
		// j loopLabel
		jump(loopLabel1);
		label(end1);
		//PrintInt(tempString(str2));
		//moveRegisters(STR2_REG,tempString(str2));
		// loopEnd:
		label(loopLabel2);
		lbFromRegToReg(BYTE_REG, STR2_REG,0);
		beqzReg(BYTE_REG, end2);
		// store current byte in resultStringTmp
		sbFromTmpToReg(DEST_REG, BYTE_REG, 0);
		// addu dstTmp, dstTmp, 1
		addConstIntToReg(DEST_REG, 1);
		addConstIntToReg(STR2_REG, 1);

		// addu byteOfString, byteOfString, 1
		// j loopLabel
		jump(loopLabel2);
		label(end2);
		sbFromTmpToReg(DEST_REG, BYTE_REG, 0);
		moveRegisters(tempString(dstTmp),DEST_REG_DUP);
	}

	public void concatStrings(int tempStr1, int tempStr2, int dstTemp) {
		final String STR1 = "$s0";
		final String STR2 = "$s1";

		final String STR1LEN_REG = "$s2";
		final String STR2LEN_REG = "$s3";
		final String CONCLEN_REG = "$s4";
		moveRegisters(STR1, tempString(tempStr1));
		moveRegisters(STR2, tempString(tempStr2));
		calcStringLengthIntoTmp(STR1LEN_REG, tempString(tempStr1));
		calcStringLengthIntoTmp(STR2LEN_REG, tempString(tempStr2));
		add(CONCLEN_REG, STR1LEN_REG, STR2LEN_REG, false);
		// add 1 for null terminator
		addConstIntToReg(CONCLEN_REG, 1);
		// ------------ allocate space on the heap for the result ------------ //
		mallocRegSize(dstTemp, CONCLEN_REG);

		copyString(dstTemp);

	}
	public void callMethodStmt(int objectTemp, int methodOffset, Deque<Integer> argTemps) {
		final String VTABLE_REG = "$s0";
		final String METHOD_REG = "$s1";

		// Push arguments, including the object itself
		argTemps.addLast(objectTemp);
		int argsNum = argTemps.size();
		this.pushArgs(argTemps);

		// Get the method from the virtual table
		this.textSegment += String.format("\tlw %s, 0(%s)\n", VTABLE_REG, tempString(objectTemp));
		this.textSegment += String.format("\tlw %s, %d(%s)\n", METHOD_REG, WORD_SIZE*(methodOffset), VTABLE_REG);

		// jalr
		this.textSegment += String.format("\tjalr %s\n", METHOD_REG);

		// restore sp
		this.textSegment += String.format("\taddu $sp, $sp, %d\n", argsNum * WORD_SIZE);
	}

	public void callMethodExp(int dstTemp, int objectTemp, int methodOffset, Deque<Integer> argTemps) {
		this.callMethodStmt(objectTemp, methodOffset, argTemps);

		// store return value in dst
		moveRegisters(tempString(dstTemp), "$v0");
	}

	/*********************************************
	 * Function delaration
	 ***********************************************/

	public void pushTempReg(int reg) {
		this.pushRegNameString(tempString(reg));
	}

	public void pushRegNameString(String reg) {
		textSegment += String.format("\tsubu $sp, $sp, 4\n");
		textSegment += String.format("\tsw %s, 0($sp)\n", reg);
	}

	public void registersBackup() {
		for (int i = 0; i < MAX_TEMPS; i++) {
			pushRegNameString(tempString(i));
		}
	}

	public void registersRestore() {
		for (int i = 0; i < MAX_TEMPS; i++) {
			int offset = (i + 1) * -4;
			textSegment += String.format("\tlw $t%d, %d($sp)\n", i, offset);
		}
	}

	public void funcPrologue(int localVarsNum, String funcName) {
		label(Labels.getAvialableLabel(funcName + "_prologue"));
		pushRegNameString("$ra");
		pushRegNameString("$fp");
		textSegment += String.format("\tmove $fp, $sp\n");
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

	public void doReturn(int expRetReg, String funcName) {
		moveRegisters("$v0", tempString(expRetReg));
		jump(funcName + "_epilogue");
		// textSegment += String.format("\tmove $v0, %s\n", tempString(expRetRegIdx));
	}

	public void addMethodToVtable(String className, String methodName) {
		dataSegment += String.format(".word %s_%s\n", methodName, className);
	}

	public void addVtable(List<Pair<String, String>> vtable, String className) {
		dataSegment += String.format("%s:\n", vtableLabel(className));
		for (Pair<String, String> entry : vtable) {
			this.addMethodToVtable(entry.getKey(), entry.getValue());
		}
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
			instance.fileWriter.format("%s: .asciiz \" \"\n", SPACE_LABLE);


		}
		return instance;
	}
}
