import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;

public class Main {

	static public void main(String argv[]) {
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try {
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			String output = "";
			while (s.sym != TokenNames.EOF && s.sym != TokenNames.ERROR) {

				/************************/
				/* [6] Print to console */
				/************************/
				output += TokenNames.class.getFields()[s.sym].getName();
				if (s.value != null) {
					output += "(" + s.value + ")";
				}
				output += "[" + l.getLine() + "," + l.getTokenStartPosition() + "]\n";
				
				/***********************/
				/* [7] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			if (s.sym == TokenNames.ERROR) {
				output = "ERROR\n";
			}

			/******************************/
			/* [8] Close lexer input file */
			/******************************/
			l.yyclose();

			/*************************/
			/* [9] Write to the file */
			/*************************/
			file_writer = new PrintWriter(outputFilename);
			file_writer.print(output);
			file_writer.close();
			System.out.print(output);
    	}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}


