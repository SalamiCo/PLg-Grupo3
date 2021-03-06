
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Mon May 13 16:56:14 CEST 2013
//----------------------------------------------------

package plg.pruebas;

import es.ucm.fdi.plg.evlib.TAtributos;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Mon May 13 16:56:14 CEST 2013
  */
public class Parser extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public Parser() {super();}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public Parser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\010\000\002\002\003\000\002\002\004\000\002\003" +
    "\003\000\002\003\003\000\002\003\003\000\002\003\003" +
    "\000\002\003\003\000\002\003\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\012\000\016\004\012\005\013\006\007\007\011\010" +
    "\006\011\010\001\002\000\004\002\001\001\002\000\004" +
    "\002\014\001\002\000\004\002\uffff\001\002\000\004\002" +
    "\ufffc\001\002\000\004\002\ufffe\001\002\000\004\002\ufffb" +
    "\001\002\000\004\002\ufffa\001\002\000\004\002\ufffd\001" +
    "\002\000\004\002\000\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\012\000\006\002\004\003\003\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001\000\002\001\001" });

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
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$Parser$actions {


   private EAtribucion atrb = new EAtribucion();

  private final Parser parser;

  /** Constructor */
  CUP$Parser$actions(Parser parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$Parser$do_action(
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
          case 7: // I ::= IDENT 
            {
              TAtributos RESULT =null;
		int identleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int identright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String ident = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
  RESULT = atrb.iR6(ident);

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // I ::= LITCHAR 
            {
              TAtributos RESULT =null;
		int litcharleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int litcharright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String litchar = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
  RESULT = atrb.iR5(litchar);

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // I ::= LITFLOAT 
            {
              TAtributos RESULT =null;
		int litfloatleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int litfloatright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String litfloat = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
  RESULT = atrb.iR4(litfloat);

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // I ::= LITNAT 
            {
              TAtributos RESULT =null;
		int litnatleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int litnatright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		String litnat = (String)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
  RESULT = atrb.iR3(litnat);

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // I ::= FALSE 
            {
              TAtributos RESULT =null;
		
  RESULT = atrb.iR2();

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // I ::= TRUE 
            {
              TAtributos RESULT =null;
		
  RESULT = atrb.iR1();

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("I",1, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= S EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$Parser$stack.elementAt(CUP$Parser$top-1)).value;
		RESULT = start_val;
              CUP$Parser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.elementAt(CUP$Parser$top-1)), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$Parser$parser.done_parsing();
          return CUP$Parser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // S ::= I 
            {
              Object RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()).right;
		TAtributos i = (TAtributos)((java_cup.runtime.Symbol) CUP$Parser$stack.peek()).value;
		
  RESULT = atrb.sR1(i);

              CUP$Parser$result = parser.getSymbolFactory().newSymbol("S",0, ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), ((java_cup.runtime.Symbol)CUP$Parser$stack.peek()), RESULT);
            }
          return CUP$Parser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

