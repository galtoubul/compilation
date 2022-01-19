
import java.io.*;
import java.io.PrintWriter;

import SYMBOL_TABLE.SYMBOL_TABLE;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];

		try
		{
			String output = "OK";

			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);

			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/

			/*************************/
			/* [6] Print the AST ... */
			/*************************/

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			System.out.println("before AST.SemantMe();");
			try {
				AST = (AST_PROGRAM) p.parse().value;
				AST.PrintMe();
				AST.SemantMe();
				System.out.println("\n\n-------------------------------------------- IRme --------------------------------------------\n\n");
				AST.IRme();
				IR.getInstance().MIPSme();
				MIPSGenerator.getInstance().finalizeFile();
			} catch (SemanticErrorException e) {
				output = "ERROR("+e.getMessage()+")";
			} catch (syntaxErrorException e) {
				System.out.println("syntaxErrorException");
				output = "ERROR("+e.getMessage()+")";
			} catch (lexicalErrorException e) {
				output = "ERROR";
			}
			file_writer.write(output);
			System.out.println("-------------------------- FILE OUTPUT IS:");
			System.out.println(output);


			/*************************/
			/* [8] Close output file */
			/*************************/
			file_writer.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


