/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/

	private Symbol symbol(int type) { return new Symbol(type, yyline, yycolumn); }

	private Symbol symbol(int type, Object value) { return new Symbol(type, yyline, yycolumn, value); }

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }

	private Symbol getIntSymbol(Integer integer) {
	    if (integer < Math.pow(2, 15)) {
        	        return symbol(TokenNames.INT, integer);
        } else {
        	        return symbol(TokenNames.ERROR);
        }
	}
%}

/***********************/
/* MACRO DECLARATIONS */
/***********************/

LINE_TERMINATOR	         = \r | \n | \r\n
WHITESPACE		         = [ \t\f] | {LINE_TERMINATOR}
INTEGER			         = 0 | [1-9][0-9]*
BAD_INTEGER              = 0[0-9]+
ID				         = [a-zA-Z][a-zA-Z0-9]*
STRING                   = \"[a-zA-Z]*\"

CharsInCommentsNoDivStar = [()[]{}?!\-\+.;a-zA-Z0-9] | {WHITESPACE} | {LINE_TERMINATOR}
CharsInCommentsNoStar    = {CharsInCommentsNoDivStar} | \/
BlockCommentContent      = {CharsInCommentsNoStar}* (\* | (\*{CharsInCommentsNoDivStar}{CharsInCommentsNoStar}*))*
CharsInLineComments      = {CharsInCommentsNoStar} | \*
/* l;akshdfaasdh/ asdflkjhgahl *f/asdhuewrahuajkdfas **sglj */
LINE_COMMENT             = \/\/{CharsInLineComments}*{LINE_TERMINATOR}
UNCLOSED_COMMENT         = \/\* {BlockCommentContent}
BLOCK_COMMENT            = {UNCLOSED_COMMENT} \*\/
COMMENT                  = {LINE_COMMENT} | {BLOCK_COMMENT}

ANY                      = .

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"+"					{ return symbol(TokenNames.PLUS);                           }
"-"					{ return symbol(TokenNames.MINUS);                          }
"*"				    { return symbol(TokenNames.TIMES);                          }
"/"					{ return symbol(TokenNames.DIVIDE);                         }
"("					{ return symbol(TokenNames.LPAREN);                         }
")"					{ return symbol(TokenNames.RPAREN);                         }
"["					{ return symbol(TokenNames.LBRACK);                         }
"]"					{ return symbol(TokenNames.RBRACK);                         }
"{"					{ return symbol(TokenNames.LBRACE);                         }
"}"					{ return symbol(TokenNames.RBRACE);                         }
"="					{ return symbol(TokenNames.EQ);                             }
":="				{ return symbol(TokenNames.ASSIGN);                         }
";"				    { return symbol(TokenNames.SEMICOLON);                      }
"."				    { return symbol(TokenNames.DOT);                            }
","				    { return symbol(TokenNames.COMMA);                          }
">"				    { return symbol(TokenNames.GT);                             }
"<"				    { return symbol(TokenNames.LT);                             }
"class"				{ return symbol(TokenNames.CLASS);                          }
"nil"				{ return symbol(TokenNames.NIL);                            }
"array"				{ return symbol(TokenNames.ARRAY);                          }
"while"				{ return symbol(TokenNames.WHILE);                          }
"extends"			{ return symbol(TokenNames.EXTENDS);                        }
"return"			{ return symbol(TokenNames.RETURN);                         }
"new"				{ return symbol(TokenNames.NEW);                            }
"if"				{ return symbol(TokenNames.IF);                             }
{STRING}            { return symbol(TokenNames.STRING, new String(yytext()));   }
{INTEGER}			{ try {
                        return getIntSymbol(new Integer(yytext()));
                      } catch (Exception e) {
                        // Integer token overflows Integer constructor
                        return symbol(TokenNames.ERROR);
                      }                                                         }
{ID}				{ return symbol(TokenNames.ID, new String(yytext()));       }
{WHITESPACE}		{ /* just skip what was found, do nothing */                }
{COMMENT}		    { /* just skip what was found, do nothing */                }
{UNCLOSED_COMMENT}	{ return symbol(TokenNames.NEW);                            }
{BAD_INTEGER}       { return symbol(TokenNames.ERROR);                          }
<<EOF>>				{ return symbol(TokenNames.EOF);                            }
{ANY}           	{ return symbol(TokenNames.ERROR);                          }

}
