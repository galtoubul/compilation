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
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
	}
	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
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
	public void funcPrologue(int localVarsNum) {
		pushRegNameString("ra");
		pushRegNameString("fp");
		fileWriter.format("\tmov $fp, $sp\n");
		fileWriter.format("\tsub $sp, $sp, %d\n", localVarsNum * WORD_SIZE);
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
	// Push args in reverse
	public void pushArgs(TEMP_LIST argsTempList)
	{
		// base case
		if (argsTempList == null) {
			return;
		}

		// recursion
		pushArgs(argsTempList.tail);

		// push with stack folding
		if (argsTempList.head != null) {
			pushTempReg(argsTempList.head);
		}
	}
//	public void pushArgs(TEMP_LIST argsTempList)
//	{
//		TEMP_LIST ptr = argsTempList;
//		while (ptr != null) {
//			if (ptr.head != null) {
//				pushTempReg(ptr.head);
//			}
//			ptr = ptr.tail;
//		}
//	}
	public void callFuncStmt(TEMP dst, String funcName, TEMP_LIST argsTempList)
	{
		if (argsTempList != null) {
			pushArgs(argsTempList);
		}

		// jal
		fileWriter.format("\tjal %s\n", funcName);
	}
	public void callFuncExp(TEMP dst, String funcName, TEMP_LIST argsTempList)
	{
		if (argsTempList != null) {
			pushArgs(argsTempList);
		}

		// jal
		fileWriter.format("\tjal %s\n", funcName);

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
