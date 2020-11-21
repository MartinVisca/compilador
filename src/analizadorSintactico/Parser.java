//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorLexico.RegistroSimbolo;

//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CTE=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short THEN=261;
public final static short END_IF=262;
public final static short OUT=263;
public final static short FUNC=264;
public final static short RETURN=265;
public final static short FOR=266;
public final static short LONGINT=267;
public final static short FLOAT=268;
public final static short UP=269;
public final static short DOWN=270;
public final static short NI=271;
public final static short REF=272;
public final static short PROC=273;
public final static short CADENA=274;
public final static short MENORIGUAL=275;
public final static short MAYORIGUAL=276;
public final static short IGUAL=277;
public final static short DISTINTO=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    4,    4,    5,    5,    2,
    2,    2,    7,    7,    7,    7,    7,    7,   11,   10,
   10,   10,   13,   13,   13,   13,   14,   14,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,    9,    9,    9,
    9,    6,    6,    6,    6,    6,   15,   15,   15,   15,
   15,   23,   16,   21,   20,   17,   17,   17,   17,   18,
   18,   18,   18,   18,   19,   19,   19,   26,   26,   26,
   26,   26,   26,   26,   25,   25,   25,   27,   27,   27,
   28,   28,   28,   24,   24,   24,   24,   24,   24,   22,
   22,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    1,    1,    2,    1,
    1,    1,    3,    3,    2,    3,    3,    2,    0,    6,
    4,    3,    4,    4,    4,    1,    0,    1,    1,    4,
    4,    4,    8,    8,    8,    8,    8,   12,   12,   12,
   12,   12,   12,   12,   16,   16,   15,    1,    3,    3,
    2,    1,    1,    1,    1,    1,    6,    8,    8,    5,
    5,    0,   13,    3,    3,    4,    4,    3,    3,    5,
    5,    4,    4,    4,    4,    3,    4,    1,    4,    8,
   12,    3,    3,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  102,  103,    0,   12,    0,    0,
    0,    2,   11,   10,    0,    0,   52,   53,   54,   55,
   56,   91,   92,    0,    0,    0,    0,    0,    0,   90,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   78,    0,    0,    0,    3,   15,   48,    0,    0,   18,
    0,   16,    0,   69,    0,   93,   68,    0,    0,    0,
    0,    0,    0,    0,   96,   97,   98,   99,   94,   95,
    0,    0,    0,    0,    0,    0,    0,    0,   22,    0,
    0,    0,    0,    0,    0,   76,   51,   14,    0,   13,
    0,   17,   77,   75,   67,   66,    0,    0,   88,   89,
    0,    0,    0,    0,   72,   74,    0,    0,    0,    0,
    0,    0,    0,   21,    0,    0,   73,   82,   83,   50,
   49,   61,    0,    5,    0,   60,   71,   70,    0,    0,
    0,    0,   28,   29,    0,    0,   84,   79,    0,    0,
    0,   57,    0,   24,   25,   23,   20,    0,    0,    0,
    4,    0,    0,    0,    0,    0,    0,    0,    0,   59,
   58,    0,    0,   31,    0,   32,    0,   30,    0,    0,
    0,  100,  101,    0,    0,    0,    0,   80,    0,    0,
    0,    0,    0,    0,    0,    0,   62,    0,    0,    0,
    0,    0,    0,    0,    0,   34,    0,   35,    0,    0,
   36,   37,    0,   33,    0,    0,    0,    0,   63,    7,
    0,    0,    0,    0,    0,   81,    0,    8,    0,    0,
    0,    0,    0,    0,    6,    9,    0,    0,    0,    0,
    0,    0,    0,   39,   40,   41,   42,   43,   44,   38,
    0,    0,    0,    0,    0,   47,    0,   46,   45,
};
final static short yydgoto[] = {                         10,
   11,   12,  125,  209,  217,   13,   14,   15,   49,   16,
   78,  135,   79,  136,   17,   18,   19,   20,   21,   32,
  155,  174,  195,   71,   28,   44,   29,   30,
};
final static short yysindex[] = {                        74,
   86,  112,  -17,   -2,    0,    0,   -6,    0,  -12,    0,
   74,    0,    0,    0, -176,   14,    0,    0,    0,    0,
    0,    0,    0, -228,   -1,  115, -210,    7,   23,    0,
  -40, -206,  -23, -198, -230, -195, -215, -188,    5, -182,
    0, -170, -153,   48,    0,    0,    0, -141,  118,    0,
   26,    0,  -48,    0,    8,    0,    0,  119,  119,  119,
  119, -136,   87, -126,    0,    0,    0,    0,    0,    0,
  119,   75,   77,    4,   82,   92,   10,  110,    0,   40,
  -89,  -83,  120,  143,  145,    0,    0,    0,  -64,    0,
  -51,    0,    0,    0,    0,    0,   23,   23,    0,    0,
  -55,   38,  -53,   34,    0,    0,  151,  152,  -42,  -39,
  -25,  -19,  -34,    0,  183,   31,    0,    0,    0,    0,
    0,    0,   74,    0, -149,    0,    0,    0,  185,  155,
  156,  161,    0,    0, -215, -145,    0,    0,   -7,   50,
    2,    0,   15,    0,    0,    0,    0,   11, -138,  216,
    0,   24,   28,  -47,  217,   91,  127,  128,   39,    0,
    0,  119, -114,    0,   16,    0,   16,    0,   16,  132,
   34,    0,    0,   42,  -71,  -71, -174,    0,   41,  250,
   45,   46,   51,   56,  -58,  256,    0,  137,  139,  271,
  279,  144,  149,   65,   89,    0,   16,    0,   16,   16,
    0,    0,   16,    0,   16,  283,  109,  102,    0,    0,
  -71,  -71,  -71,  -71, -172,    0,   97,    0,   69,   70,
   79,   81,   88,  -56,    0,    0,  291,  293,  298,  312,
  316,  317,  150,    0,    0,    0,    0,    0,    0,    0,
   16, -167,  -32,  106,  323,    0,  337,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  379,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,  340,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -35,  -29,    0,    0,
    0,    0,    0,  -38,    0,    0,    0,    0,    0,    0,
    0,    0, -109,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -65,    0,  -65,    0, -169,    0,
  322,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -65,    0,  -65,  -65,
    0,    0,  -65,    0, -151,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -151,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    1,   25,  241,    0,    0, -148,    0,  174,    0,    0,
    0,    0,  248,  -59,    0,    0,    0,    0,    0,  353,
    0,    0,    0,  236,   33,  366,  146,  153,
};
final static int YYTABLESIZE=416;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
   62,   87,   65,   87,   27,   85,  134,   85,  246,   85,
   94,   86,   69,   86,   70,   86,   51,   87,   87,   58,
   87,   59,   35,   85,   85,   73,   85,   52,   41,   86,
   86,   42,   86,   38,   33,   45,   69,   36,   70,   41,
   76,    9,   42,   74,  108,   43,  210,   56,   81,   58,
   58,   59,   59,    9,   64,   77,   43,   72,   55,  218,
    8,   75,   82,   33,   60,    9,   96,   80,  226,   61,
  112,  138,    8,   83,  139,   45,   58,    9,   59,   46,
   47,  183,  184,  223,    8,   84,   27,   27,  243,    9,
    5,    6,    5,    6,    5,    6,    8,   27,   27,    5,
    6,   26,   85,  104,   27,  175,   86,  176,    8,  177,
  141,  148,  142,    9,   87,   27,   27,  157,  158,  101,
   26,    5,    6,  140,  123,   25,  124,  102,    9,  103,
   27,  164,    8,  105,  165,  106,    9,  211,   50,  212,
  213,    9,  109,  214,   24,  215,   26,   27,   25,  113,
   92,   31,  110,   27,  172,  173,   27,   27,   27,   27,
  123,   91,  114,   27,   45,  124,  115,  166,  168,   26,
  167,  169,  178,  116,  151,  179,   90,  196,  117,  198,
  197,  242,  199,  118,  202,  119,   26,  203,   48,  204,
  240,  120,  205,  241,  171,    5,    6,  192,  193,  232,
  233,   27,   27,   97,   98,  121,  122,   93,  126,  127,
  128,  208,   99,  100,   87,  129,   22,   23,  130,   87,
   85,  225,   65,  137,  245,   85,   86,   65,   66,   67,
   68,   86,  131,   87,   87,   87,   87,  133,  132,   85,
   85,   85,   85,  143,   39,   86,   86,   86,   86,  150,
   37,   65,   66,   67,   68,   39,   34,  152,    1,  107,
    2,   40,   57,   95,    3,  111,  156,    4,    5,    6,
    1,  154,    2,  159,    7,  163,    3,  144,  145,    4,
    5,    6,    1,  146,    2,  160,    7,  133,    3,  161,
  187,    4,    5,    6,    1,  170,    2,  186,    7,  180,
    3,  188,  189,    4,    5,    6,    1,  190,    2,  149,
    7,  191,    3,  194,  200,    4,    5,    6,   26,  201,
   26,  206,    7,  216,   26,  227,  228,   26,   26,   26,
    1,  234,    2,  235,   26,  229,    3,  230,  236,    4,
    5,    6,   22,   23,  231,  207,    7,    2,  181,  182,
  185,    3,  237,  207,    4,    2,  238,  239,  207,    3,
    2,  247,    4,  248,    3,   22,   23,    4,   22,   23,
   54,   22,   23,   88,   89,   22,   23,  249,    1,   19,
   64,  153,  147,   63,  219,  220,  221,  222,  224,  162,
   53,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  244,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   45,   41,   41,   43,   41,   45,
   59,   41,   60,   43,   62,   45,   16,   59,   60,   43,
   62,   45,   40,   59,   60,  256,   62,  256,   41,   59,
   60,   44,   62,   40,    2,   11,   60,   40,   62,   41,
  256,   40,   44,  274,   41,   58,  195,  258,   44,   43,
   43,   45,   45,   40,  261,  271,   58,  256,   26,  208,
   59,  257,   58,   31,   42,   40,   59,  256,  217,   47,
   61,   41,   59,  256,   44,   51,   43,   40,   45,  256,
  257,  256,  257,  256,   59,  256,  256,  257,  256,   40,
  267,  268,  267,  268,  267,  268,   59,  267,  268,  267,
  268,   40,  256,   71,  256,  165,   59,  167,   59,  169,
  260,  257,  262,   40,  256,  267,  268,  256,  257,  256,
   59,  267,  268,  123,  123,   40,  102,   41,   40,  256,
   45,   41,   59,   59,   44,   59,   40,  197,  125,  199,
  200,   40,   61,  203,   59,  205,   61,  257,   40,   40,
  125,   40,   61,   45,  269,  270,   45,  267,  268,   45,
  123,   44,  123,   45,  140,  141,  256,   41,   41,   61,
   44,   44,   41,  257,  125,   44,   59,   41,   59,   41,
   44,  241,   44,   41,   41,   41,  125,   44,   15,   41,
   41,  256,   44,   44,  162,  267,  268,  256,  257,  256,
  257,  267,  268,   58,   59,  257,  262,  256,  262,   59,
   59,  123,   60,   61,  256,  258,  257,  258,  258,  261,
  256,  125,  261,   41,  257,  261,  256,  275,  276,  277,
  278,  261,  258,  275,  276,  277,  278,  272,  258,  275,
  276,  277,  278,   59,  257,  275,  276,  277,  278,  257,
  257,  275,  276,  277,  278,  257,  274,  256,  257,  256,
  259,  274,  256,  256,  263,  256,  256,  266,  267,  268,
  257,  257,  259,   58,  273,   59,  263,  123,  123,  266,
  267,  268,  257,  123,  259,  262,  273,  272,  263,  262,
   41,  266,  267,  268,  257,  257,  259,  257,  273,  258,
  263,  257,  257,  266,  267,  268,  257,  257,  259,  136,
  273,  256,  263,   58,   44,  266,  267,  268,  257,   41,
  259,  257,  273,   41,  263,  257,  257,  266,  267,  268,
  257,   41,  259,   41,  273,  257,  263,  257,   41,  266,
  267,  268,  257,  258,  257,  257,  273,  259,  175,  176,
  177,  263,   41,  257,  266,  259,   41,   41,  257,  263,
  259,  256,  266,   41,  263,  257,  258,  266,  257,  258,
  256,  257,  258,  256,  257,  257,  258,   41,    0,   40,
   59,  141,  135,   31,  211,  212,  213,  214,  215,  154,
   25,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  242,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CTE","IF","ELSE","THEN","END_IF","OUT",
"FUNC","RETURN","FOR","LONGINT","FLOAT","UP","DOWN","NI","REF","PROC","CADENA",
"MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque",
"bloque : sentencias",
"bloque : bloque sentencias",
"bloque_sentencias : '{' bloque '}'",
"bloque_sentencias : sentencias",
"bloque_sentencias_ejecutables : '{' sentencias_solo_ejecutables '}'",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_solo_ejecutables sentencias_ejecutables",
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
"sentencias : ';'",
"sentencias_declarativas : tipo lista_variables ';'",
"sentencias_declarativas : tipo lista_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : ID ';' error",
"sentencias_declarativas : declaracion_proc bloque '}'",
"sentencias_declarativas : declaracion_proc '}'",
"$$1 :",
"declaracion_proc : PROC ID $$1 '(' lista_parametros_formales control_invocaciones",
"declaracion_proc : PROC '(' error '{'",
"declaracion_proc : PROC ID control_invocaciones",
"control_invocaciones : NI '=' CTE '{'",
"control_invocaciones : error '=' CTE '{'",
"control_invocaciones : NI error CTE '{'",
"control_invocaciones : error",
"modificador :",
"modificador : REF",
"lista_parametros_formales : ')'",
"lista_parametros_formales : modificador tipo ID ')'",
"lista_parametros_formales : modificador ID error ')'",
"lista_parametros_formales : modificador tipo error ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador ID error ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo error ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador ID error ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo error ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador ID error ',' modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo error ',' modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador error ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo error ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador error ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo error ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador tipo error ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador error ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador error ')'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID error",
"lista_variables : tipo error",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_for",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : invocacion_proc",
"sentencia_if : IF '(' condicion ')' bloque_sentencias END_IF",
"sentencia_if : IF '(' condicion ')' bloque_sentencias ELSE bloque_sentencias END_IF",
"sentencia_if : IF '(' condicion ')' bloque_sentencias ELSE error END_IF",
"sentencia_if : IF condicion THEN error END_IF",
"sentencia_if : IF '(' ')' error END_IF",
"$$2 :",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion_for ';' incr_decr CTE ')' $$2 bloque_sentencias_ejecutables",
"condicion_for : ID comparador expresion",
"condicion : expresion comparador expresion",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' expresion error",
"asignacion : ID expresion error",
"asignacion : ID '=' error",
"salida : OUT '(' CADENA ')' ';'",
"salida : OUT '(' CADENA error ';'",
"salida : OUT CADENA error ';'",
"salida : '(' CADENA error ';'",
"salida : OUT '(' error ';'",
"invocacion_proc : ID '(' parametros ';'",
"invocacion_proc : '(' parametros ';'",
"invocacion_proc : ID '(' parametros error",
"parametros : ')'",
"parametros : ID ':' ID ')'",
"parametros : ID ':' ID ',' ID ':' ID ')'",
"parametros : ID ':' ID ',' ID ':' ID ',' ID ':' ID ')'",
"parametros : ',' error ')'",
"parametros : ':' error ')'",
"parametros : ID ',' error ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : '-' CTE",
"comparador : '<'",
"comparador : '>'",
"comparador : MENORIGUAL",
"comparador : MAYORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"incr_decr : UP",
"incr_decr : DOWN",
"tipo : LONGINT",
"tipo : FLOAT",
};

//#line 210 "gramatica.y"

private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico; }

public int yylex() {
    int token = lexico.yylex();
    if (lexico.getRefTablaSimbolos() != -1)
        yyval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

public void yyerror(String string) {
	sintactico.addErrorSintactico(string);
}
//#line 482 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 19 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un programa. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 12:
//#line 40 "gramatica.y"
{ sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): ';' sin sentencia declarada."); }
break;
case 13:
//#line 43 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 14:
//#line 44 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ';' al final de la declaracion."); }
break;
case 15:
//#line 45 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el/los identificadores."); }
break;
case 16:
//#line 46 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el tipo de la variable."); }
break;
case 17:
//#line 47 "gramatica.y"
{ if (!sintactico.getErrorProc()) {
                                                               sintactico.agregarAnalisis("Se reconocio un procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                               /* Agregar polaca*/
                                                               /* Actualizar ambito*/
                                                          }
                                                          else {
                                                               sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): declaracion de procedimiento invalida.");
                                                               sintactico.setErrorProc(true);
                                                          }
                                                        }
break;
case 18:
//#line 57 "gramatica.y"
{ sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): el cuerpo del procedimiento es vacio."); }
break;
case 19:
//#line 60 "gramatica.y"
{   /* sintactico.setUsoTablaSimb($2.ival, "PROC");*/
                                /* RegistroSimbolo aux = lexico.getElemTablaSimb($2.ival);*/
                                /* String nombre_procedimiento = aux.getLexema();*/
                                /* sintactico.modificarAmbito($2.ival)*/
                                /* modificarAmbito hace que tablaSimbolos.get(indice).setAmbito("/" + ambito)*/
                                /*if(sintactico.variableFueDeclarada($2.ival))*/
                                   /* sintactico.setErrorProc(true);*/
                                /* sino actualizar el ambito del sintactico*/
                            }
break;
case 20:
//#line 69 "gramatica.y"
{ if (sintactico.getErrorProc() == false)
                                                                                sintactico.agregarAnalisis("Se reconocio una declaracion de procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                                        }
break;
case 21:
//#line 72 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el identificador del procedimiento");
                                        sintactico.setErrorProc(true);
                                      }
break;
case 24:
//#line 79 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI"); }
break;
case 25:
//#line 80 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones"); }
break;
case 26:
//#line 81 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones"); sintactico.setErrorProc(true); }
break;
case 31:
//#line 91 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 32:
//#line 92 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 34:
//#line 95 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 35:
//#line 96 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 36:
//#line 97 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 37:
//#line 98 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 39:
//#line 101 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 40:
//#line 102 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 41:
//#line 103 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 42:
//#line 104 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 43:
//#line 105 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 44:
//#line 106 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 45:
//#line 108 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 46:
//#line 109 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 47:
//#line 110 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 50:
//#line 115 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la ',' para separar la lista de variables."); System.out.println(val_peek(2).ival);}
break;
case 51:
//#line 116 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue declarado."); }
break;
case 57:
//#line 126 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 58:
//#line 127 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 59:
//#line 128 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en el cuerpo del ELSE."); }
break;
case 60:
//#line 129 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan los parentesis en la condicion del IF."); }
break;
case 61:
//#line 130 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); }
break;
case 62:
//#line 134 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 63:
//#line 135 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 64:
//#line 138 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 66:
//#line 143 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 67:
//#line 144 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ';' al final de la asignacion."); }
break;
case 68:
//#line 145 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 69:
//#line 146 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
break;
case 70:
//#line 149 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 71:
//#line 150 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 72:
//#line 151 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 73:
//#line 152 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 74:
//#line 153 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 75:
//#line 156 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 76:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 77:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el ';' al final de la invocacion."); }
break;
case 82:
//#line 165 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 83:
//#line 166 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 84:
//#line 167 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 85:
//#line 170 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 86:
//#line 171 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 88:
//#line 175 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 89:
//#line 176 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 92:
//#line 181 "gramatica.y"
{
                    String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                }
break;
case 93:
//#line 186 "gramatica.y"
{
                        String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat(val_peek(0).ival);
                    }
break;
//#line 900 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
