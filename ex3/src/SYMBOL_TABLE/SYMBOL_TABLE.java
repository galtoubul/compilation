/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE {
	public static int currentScope = 0;

	public static HashMap<Integer, ArrayList<SYMBOL_TABLE_ENTRY>> scopes = new HashMap<Integer,ArrayList<SYMBOL_TABLE_ENTRY>>();
	public static HashMap<Integer, ArrayList<Integer>> scopesGraph = new HashMap<Integer,ArrayList<Integer>>();
	public static HashMap<Integer, Integer> scopeFather = new HashMap<Integer,Integer>();

	private int hashArraySize = 13;
	private int numOfScopes = 0;
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;

	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s) {
		if (s.charAt(0) == 'l') {
			return 1;
		}
		if (s.charAt(0) == 'm') {
			return 1;
		}
		if (s.charAt(0) == 'r') {
			return 3;
		}
		if (s.charAt(0) == 'i') {
			return 6;
		}
		if (s.charAt(0) == 'd') {
			return 6;
		}
		if (s.charAt(0) == 'k') {
			return 6;
		}
		if (s.charAt(0) == 'f') {
			return 6;
		}
		if (s.charAt(0) == 'S') {
			return 6;
		}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, TYPE t) {
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position */
		/* NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];

		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++);
		scopes.put(currentScope, addToScope(e, scopes.getOrDefault(currentScope, new ArrayList<SYMBOL_TABLE_ENTRY>())));
		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;

		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;

		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	public TYPE find(String name) {
		SYMBOL_TABLE_ENTRY e;

		for (e = table[hash(name)]; e != null; e = e.next) {
			if (name.equals(e.name)) {
				return e.type;
			}
		}

		return null;
	}

	// Retreive the type of the current scope in the symbol table
	public ScopeType currentScopeType() {
		for (SYMBOL_TABLE_ENTRY entry = this.top; entry != null; entry = entry.prevtop) {
			if (entry.name == "SCOPE-BOUNDARY") {
				// We've reached the scope boundry
				return ((TYPE_FOR_SCOPE_BOUNDARIES) entry.type).scopeType;
			}
		}

		// There is only the global scope, so there are no scope entris to be found
		return ScopeType.Global;
	}

	// Find the closest scope with the type `scopeType`
	public Optional<TYPE_FOR_SCOPE_BOUNDARIES> findScopeType(ScopeType scopeType) {
		for (SYMBOL_TABLE_ENTRY entry = this.top; entry != null; entry = entry.prevtop) {
			if (entry.name == "SCOPE-BOUNDARY") {
				// We've reached the scope boundry
				TYPE_FOR_SCOPE_BOUNDARIES boundry = (TYPE_FOR_SCOPE_BOUNDARIES) entry.type;
				if (boundry.scopeType == scopeType)
					return Optional.of(boundry);
			}
		}

		// There is only the global scope, so there are no scope entris to be found
		return Optional.empty();
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope(ScopeType scopeType, String scopeName) {
		numOfScopes++;
		scopesGraph.put(currentScope, addSon(scopesGraph.getOrDefault(currentScope, new ArrayList<Integer>())));
		scopeFather.put(numOfScopes,currentScope);
		currentScope = numOfScopes;

		System.out.println("now begin scope " + currentScope);
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them, */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This */
		/* class only contain their type name which is the bottom sign: _|_ */
		/************************************************************************/
		enter(
				"SCOPE-BOUNDARY",
				new TYPE_FOR_SCOPE_BOUNDARIES("NONE", scopeType, scopeName));

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure, */
	/*
	 * from most recent element entered, until a <NEW-SCOPE> element is encountered
	 */
	/********************************************************************************/
	public void endScope() {
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY") {
			table[top.index] = top.next;
			top_index = top_index - 1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index - 1;
		top = top.prevtop;

		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
		System.out.println("now ends scope " + currentScope);
		currentScope = scopeFather.getOrDefault(currentScope,0);
		System.out.println("back to scope scope " + currentScope);

	}

	public static int n = 0;

	public void PrintMe() {
		int i = 0;
		int j = 0;
		String dirname = "./output/";
		String filename = String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt", n++);

		try {
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname + filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i = 0; i < hashArraySize - 1; i++) {
				fileWriter.format("<f%d>\n%d\n|", i, i);
			}
			fileWriter.format("<f%d>\n%d\n\"];\n", hashArraySize - 1, hashArraySize - 1);

			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i = 0; i < hashArraySize; i++) {
				if (table[i] != null) {
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n", i, i);
				}
				j = 0;
				for (SYMBOL_TABLE_ENTRY it = table[i]; it != null; it = it.next) {
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ", i, j);
					if (it.type instanceof TYPE_FOR_SCOPE_BOUNDARIES) {
						fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>name=%s|<f3>prevtop=%d|<f4>next\"];\n",
								it.name,
								it.type.name,
								((TYPE_FOR_SCOPE_BOUNDARIES) it.type).scopeName,
								it.prevtop_index);
					} else {
						fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
								it.name,
								it.type.name,
								it.prevtop_index);
					}

					if (it.next != null) {
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
								"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
								i, j, i, j + 1);
						fileWriter.format(
								"node_%d_%d:f3 -> node_%d_%d:f0;\n",
								i, j, i, j + 1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {
	}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance() {
		if (instance == null) {
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int", TYPE_INT.getInstance());
			instance.enter("string", TYPE_STRING.getInstance());
			instance.enter("void", TYPE_VOID.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter(
					"PrintInt",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintInt",
							new TYPE_LIST(
									TYPE_INT.getInstance(),
									null)));
			instance.enter(
					"PrintString",
					new TYPE_FUNCTION(TYPE_VOID.getInstance(),"PrintString",new TYPE_LIST(TYPE_STRING.getInstance(),null)));
			instance.enter(
					"PrintTrace",
					new TYPE_FUNCTION(
							TYPE_VOID.getInstance(),
							"PrintTrace",
							new TYPE_LIST(
									null,
									null)));

		}
		return instance;
	}

	private ArrayList<SYMBOL_TABLE_ENTRY> addToScope(SYMBOL_TABLE_ENTRY entry, ArrayList<SYMBOL_TABLE_ENTRY> entries) {
		entries.add(entry);
		return entries;
	}

	private ArrayList<Integer> addSon(ArrayList<Integer> scopes) {
		scopes.add(numOfScopes);
		return scopes;
	}

}
