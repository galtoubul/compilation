
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20160615 (GIT 4ac7450)
//----------------------------------------------------

import java_cup.runtime.*;
import AST.*;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20160615 (GIT 4ac7450) generated parser.
  */
@SuppressWarnings({"rawtypes"})
public class Parser extends java_cup.runtime.lr_parser {

 public final Class getSymbolContainer() {
    return TokenNames.class;
}

  /** Default constructor. */
  @Deprecated
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  @Deprecated
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\015\000\002\002\004\000\002\005\004\000\002\005" +
    "\003\000\002\003\003\000\002\003\005\000\002\003\006" +
    "\000\002\004\006\000\002\004\011\000\002\004\011\000" +
    "\002\002\003\000\002\002\003\000\002\002\005\000\002" +
    "\002\005" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\043\000\010\004\011\012\010\025\004\001\002\000" +
    "\022\006\ufffe\007\ufffe\010\ufffe\015\ufffe\016\ufffe\017\ufffe" +
    "\022\ufffe\023\ufffe\001\002\000\010\006\027\016\026\023" +
    "\043\001\002\000\014\002\uffff\004\011\012\010\021\uffff" +
    "\025\004\001\002\000\004\002\041\001\002\000\004\014" +
    "\033\001\002\000\004\014\012\001\002\000\006\024\015" +
    "\025\004\001\002\000\020\006\027\007\ufff7\010\ufff7\015" +
    "\ufff7\016\026\017\ufff7\022\ufff7\001\002\000\010\007\017" +
    "\010\020\015\016\001\002\000\014\007\ufff8\010\ufff8\015" +
    "\ufff8\017\ufff8\022\ufff8\001\002\000\004\020\023\001\002" +
    "\000\006\024\015\025\004\001\002\000\006\024\015\025" +
    "\004\001\002\000\014\007\ufff5\010\ufff5\015\ufff5\017\ufff5" +
    "\022\ufff5\001\002\000\014\007\ufff6\010\020\015\ufff6\017" +
    "\ufff6\022\ufff6\001\002\000\010\004\011\012\010\025\004" +
    "\001\002\000\004\021\025\001\002\000\014\002\ufffa\004" +
    "\ufffa\012\ufffa\021\ufffa\025\ufffa\001\002\000\006\024\015" +
    "\025\004\001\002\000\004\025\030\001\002\000\022\006" +
    "\ufffd\007\ufffd\010\ufffd\015\ufffd\016\ufffd\017\ufffd\022\ufffd" +
    "\023\ufffd\001\002\000\010\007\017\010\020\017\032\001" +
    "\002\000\022\006\ufffc\007\ufffc\010\ufffc\015\ufffc\016\ufffc" +
    "\017\ufffc\022\ufffc\023\ufffc\001\002\000\006\024\015\025" +
    "\004\001\002\000\010\007\017\010\020\015\035\001\002" +
    "\000\004\020\036\001\002\000\010\004\011\012\010\025" +
    "\004\001\002\000\004\021\040\001\002\000\014\002\ufff9" +
    "\004\ufff9\012\ufff9\021\ufff9\025\ufff9\001\002\000\004\002" +
    "\001\001\002\000\006\002\000\021\000\001\002\000\006" +
    "\024\015\025\004\001\002\000\010\007\017\010\020\022" +
    "\045\001\002\000\014\002\ufffb\004\ufffb\012\ufffb\021\ufffb" +
    "\025\ufffb\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\043\000\010\003\004\004\005\005\006\001\001\000" +
    "\002\001\001\000\002\001\001\000\010\003\004\004\005" +
    "\005\041\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\006\002\013\003\012\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\006\002\021\003\012\001\001\000\006\002\020" +
    "\003\012\001\001\000\002\001\001\000\002\001\001\000" +
    "\010\003\004\004\005\005\023\001\001\000\002\001\001" +
    "\000\002\001\001\000\006\002\030\003\012\001\001\000" +
    "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\006\002\033\003\012\001\001\000\002\001" +
    "\001\000\002\001\001\000\010\003\004\004\005\005\036" +
    "\001\001\000\002\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\006\002\043\003\012\001\001" +
    "\000\002\001\001\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$Parser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$Parser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$Parser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 0;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}


  /** Scan to get the next Symbol. */
  public java_cup.runtime.Symbol scan()
    throws java.lang.Exception
    {

	Symbol s;
	s = lexer.next_token();
	//System.out.print(s.sym);
	System.out.print("[");
	System.out.print(lexer.getLine());
	System.out.print(":");
	System.out.print(lexer.getCharPos());
	System.out.print("] ");
	System.out.print(TokenNames.terminalNames[s.sym]);
	if (s.value != null)
	{
		System.out.print("( ");
		System.out.print(s.value);
		System.out.print(" )");
	}
	System.out.print("\n");
	return s; 

    }


	public Lexer lexer;

	public Parser(Lexer lexer)
	{
		super(lexer);
		this.lexer = lexer;
	}
	public void report_error(String message, Object info)
	{
		System.out.print("ERROR >> ");		
		System.out.print("[");
		System.out.print(lexer.getLine());
		System.out.print(":");
		System.out.print(lexer.getCharPos());
		System.out.print("] ");		
		System.exit(0);
	}


/** Cup generated class to encapsulate user supplied action code.*/
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
class CUP$Parser$actions {
  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method 0 with the actual generated action code for actions 0 to 300. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action_part00000000(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$Parser$result;

      /* select the action based on the action number */
      switch (CUP$Parser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // $START ::= stmtList EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_STMT_LIST start_val = (AST_STMT_LIST)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // stmtList ::= stmt stmtList 
            {
              AST_STMT_LIST RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_STMT s = (AST_STMT)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		int lleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int lright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		AST_STMT_LIST l = (AST_STMT_LIST)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_STMT_LIST(s,l);    
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("stmtList",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // stmtList ::= stmt 
            {
              AST_STMT_LIST RESULT =null;
		int sleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int sright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		AST_STMT s = (AST_STMT)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_STMT_LIST(s,null); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("stmtList",3, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // var ::= ID 
            {
              AST_VAR RESULT =null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String name = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_VAR_SIMPLE(name);       
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("var",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // var ::= var DOT ID 
            {
              AST_VAR RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		AST_VAR v = (AST_VAR)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int fieldNameleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int fieldNameright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String fieldName = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_VAR_FIELD(v,fieldName); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("var",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // var ::= var LBRACK exp RBRACK 
            {
              AST_VAR RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		AST_VAR v = (AST_VAR)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_EXP e = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new AST_VAR_SUBSCRIPT(v,e);     
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("var",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // stmt ::= var ASSIGN exp SEMICOLON 
            {
              AST_STMT RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).left;
		int vright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)).right;
		AST_VAR v = (AST_VAR)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-3)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_EXP e = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new AST_STMT_ASSIGN(v,e); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("stmt",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-3)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // stmt ::= IF LPAREN exp RPAREN LBRACE stmtList RBRACE 
            {
              AST_STMT RESULT =null;
		int condleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).left;
		int condright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).right;
		AST_EXP cond = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-4)).value;
		int bodyleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int bodyright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_STMT_LIST body = (AST_STMT_LIST)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new AST_STMT_IF(   cond,body); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("stmt",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-6)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // stmt ::= WHILE LPAREN exp RPAREN LBRACE stmtList RBRACE 
            {
              AST_STMT RESULT =null;
		int condleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).left;
		int condright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-4)).right;
		AST_EXP cond = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-4)).value;
		int bodyleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int bodyright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		AST_STMT_LIST body = (AST_STMT_LIST)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		 RESULT = new AST_STMT_WHILE(cond,body); 
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("stmt",2, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-6)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // exp ::= INT 
            {
              AST_EXP RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		Integer i = (Integer)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_EXP_INT(i);          
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("exp",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // exp ::= var 
            {
              AST_EXP RESULT =null;
		int vleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int vright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		AST_VAR v = (AST_VAR)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_EXP_VAR(v);          
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("exp",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // exp ::= exp PLUS exp 
            {
              AST_EXP RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		AST_EXP e1 = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		AST_EXP e2 = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_EXP_BINOP(e1, e2, 0);
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("exp",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // exp ::= exp MINUS exp 
            {
              AST_EXP RESULT =null;
		int e1left = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).left;
		int e1right = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)).right;
		AST_EXP e1 = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-2)).value;
		int e2left = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int e2right = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		AST_EXP e2 = (AST_EXP)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		 RESULT = new AST_EXP_BINOP(e1, e2, 1);
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("exp",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-2)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number "+CUP$Parser$act_num+"found in internal parse table");

        }
    } /* end of method */

  /** Method splitting the generated action code into several parts. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
    int                        CUP$Parser$act_num,
    java_cup.runtime.lr_parser CUP$Parser$parser,
    java.util.Stack            CUP$Parser$stack,
    int                        CUP$Parser$top)
    throws java.lang.Exception
    {
              return CUP$Parser$do_action_part00000000(
                               CUP$Parser$act_num,
                               CUP$Parser$parser,
                               CUP$Parser$stack,
                               CUP$Parser$top);
    }
}

}
