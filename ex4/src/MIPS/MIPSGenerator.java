package MIPS;

import java.io.PrintWriter;

import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("main:\n");
		fileWriter.print("\tjal user_main\n");
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 0\n",var_name);
	}
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
	}
	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}
	public void liTemp(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void liRegString(String reg,int value)
	{
		fileWriter.format("\tli $%s,%d\n",reg,value);
	}
	public int getClassSize(TYPE objectType)
	{
		TYPE_CLASS t = ((TYPE_CLASS)SYMBOL_TABLE.getInstance().find(objectType.name));

		int fieldsNumTotal = t.fieldsNum;
		while(t.father.isPresent()) {
			t = t.father.get();
			fieldsNumTotal += t.fieldsNum;
		}

		return fieldsNumTotal * WORD_SIZE + 4; // 4 is for the vtable
	}
	public void createNewObject(TEMP dstTempReg, TYPE objectType)
	{
		// malloc
		int classSize = getClassSize(objectType);
		liRegString("a0", classSize);
		liRegString("v0", 9);
		fileWriter.format("\tsyscall\n");

		// mov pointer to dstTempReg
		int dstidx = dstTempReg.getSerialNumber();
		fileWriter.format("\tmov Temp_%d, $v0\n",dstidx);
	}
	public void createNewArray(TEMP dstTempReg, TYPE ArrayType, TEMP subscriptTemp)
	{
		// malloc
		TEMP wordTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		liTemp(wordTemp, 4); // wordTemp = 4

		TEMP mulTemp = TEMP_FACTORY.getInstance().getFreshTEMP();
		mul(mulTemp, subscriptTemp, wordTemp); // mulTemp = subscriptTemp * 4

		int mulTempidx = mulTemp.getSerialNumber();
		fileWriter.format("\tmov $a0, Temp_%d\n",mulTempidx); // a0 = array size

		liRegString("v0", 9); // malloc syscall code
		fileWriter.format("\tsyscall\n");

		// dstTempReg = array's pointer
		int dstidx = dstTempReg.getSerialNumber();
		fileWriter.format("\tmov Temp_%d, $v0\n",dstidx);
	}
	public void checkLimits(int dstidx)
	{
		// check top limit and fix the result if needed
		fileWriter.format("\tble Temp_%d,32767,check_bottom_limit\n",dstidx); // 2^15 - 1 = 32767
		fileWriter.format("\tli Temp_%d,32767\n",dstidx); // fix the result

		// check bottom limit and fix the result if needed
		label("check_bottom_limit");
		fileWriter.format("\tbge Temp_%d,-32768,after_limits_checks\n",dstidx); // -2^15 = -32768
		fileWriter.format("\tli Temp_%d,-32768\n",dstidx); // fix the result

		label("after_limits_checks");
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		checkLimits(dstidx);
	}
	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("user_main:\n");
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}
	public void pushTempReg(TEMP reg) {
		int regInd =reg.getSerialNumber();
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $Temp_%d, 0($sp)\n", regInd);
	}
	public void pushRegNameString(String reg) {
		fileWriter.format("\tsubu $sp, $sp, 4\n");
		fileWriter.format("\tsw $%s, 0($sp)\n", reg);
	}
	public void registersBackup()
	{
		for (int i = 0; i < 10; i++) {
			pushRegNameString(String.format("t%d",i));
		}
	}
	public void registersRestore()
	{
		for (int i = 0; i < 10; i++) {
			int offset = (i + 1) * -4;
			fileWriter.format("\tlw $t%d, %d($sp)\n",i, offset);
		}
	}
	public void funcPrologue(int localVarsNum)
	{
		pushRegNameString("ra");
		pushRegNameString("fp");
		fileWriter.format("\tmov $fp, $sp\n");
		registersBackup();
		fileWriter.format("\tsub $sp, $sp, %d\n", localVarsNum * WORD_SIZE + 40); // 40 is for storing the caller tmps
	}
	public void funcEpilogue()
	{
		fileWriter.format("\tmov $sp, $fp\n");
		registersRestore();
		fileWriter.format("\tlw $fp, 0($sp)\n");
		fileWriter.format("\tlw $ra, 4($sp)\n");
		fileWriter.format("\taddu $sp, $sp, 8\n");
		fileWriter.format("\tjr $ra\n");
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tbgt Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\tble Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	// Push args in reverse and return the number of args
	public int pushArgs(TEMP_LIST argsTempList)
	{
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
	public void callFuncStmt(TEMP dst, String funcName, TEMP_LIST argsTempList)
	{
		// push args
		int argsNum = pushArgs(argsTempList);

		// jal
		fileWriter.format("\tjal %s\n", funcName);

		// restore sp
		fileWriter.format("\taddu $sp, $sp, %d\n", argsNum);
	}
	public void callFuncExp(TEMP dst, String funcName, TEMP_LIST argsTempList)
	{
		callFuncStmt(dst, funcName, argsTempList);

		// store return value in dst
		int dstidx=dst.getSerialNumber();
		fileWriter.format("\tmov Temp_%d, $v0\n", dstidx);
	}
	public void doReturn(TEMP expRetReg)
	{
		int expRetRegIdx=expRetReg.getSerialNumber();
		fileWriter.format("\tmov $v0, Temp_%d\n", expRetRegIdx);
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
