package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.TYPE;
import TYPES.TYPE_LIST;

import java.util.Optional;

public class AST_PARM_LIST extends AST_Node
{
	public AST_PARAM head;
	public AST_PARM_LIST tail;

	public AST_PARM_LIST(AST_PARAM head, AST_PARM_LIST tail)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null) System.out.print("====================== PARMs -> PARM PARMs\n");
		if (tail == null) System.out.print("====================== PARMs -> PARM      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	public void PrintMe()
	{
		System.out.print("AST NODE PARM LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"PARM\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	// TODO: check that we don't have 2 parameters with same name
	public TYPE_LIST SemantMe() {
		System.out.println("-- AST_PARM_LIST SemantMe");

		TYPE paramType;
		TYPE_LIST paramsTypesList = null;
		for (AST_PARM_LIST paramsListPtr = this; paramsListPtr != null; paramsListPtr = paramsListPtr.tail) {
			paramType = SYMBOL_TABLE.getInstance().find(paramsListPtr.head.type.name());
			if (paramType == null) {
				System.out.format(">> ERROR [line] non existing type for variable %s\n", paramsListPtr.head.id);
				throw new semanticErrorException("line");
			}
			else {
				paramsTypesList = new TYPE_LIST(paramType, paramsTypesList);
				SYMBOL_TABLE.getInstance().enter(paramsListPtr.head.id, paramType);
				System.out.format("-- AST_PARM_LIST\n\t\tinserted parameter %s of type %s to the symbol table\n", paramsListPtr.head.id, paramType.name);
			}
		}
		return paramsTypesList;
	}
}
