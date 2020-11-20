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
    2,    2,    7,    7,    7,    7,    7,   11,   10,   10,
   10,   13,   13,   13,   13,   14,   14,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,    9,    9,    9,    9,
    6,    6,    6,    6,    6,   15,   15,   15,   15,   15,
   23,   16,   21,   20,   20,   17,   17,   17,   17,   18,
   18,   18,   18,   18,   19,   19,   19,   26,   26,   26,
   26,   26,   26,   26,   25,   25,   25,   27,   27,   27,
   28,   28,   28,   24,   24,   24,   24,   24,   24,   22,
   22,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    1,    1,    2,    1,
    1,    1,    3,    2,    3,    3,    2,    0,    6,    4,
    3,    4,    4,    4,    2,    0,    1,    1,    4,    4,
    4,    8,    8,    8,    8,    8,   12,   12,   12,   12,
   12,   12,   12,   16,   16,   15,    1,    3,    3,    2,
    1,    1,    1,    1,    1,    6,    8,    8,    5,    5,
    0,   13,    3,    3,    1,    4,    4,    4,    2,    5,
    5,    4,    4,    4,    4,    3,    4,    1,    4,    8,
   12,    3,    3,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  102,  103,    0,   12,    0,    0,
    1,    0,   11,   10,    0,    0,   51,   52,   53,   54,
   55,    0,   91,   92,   69,    0,    0,    0,    0,    0,
   90,   65,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   78,    0,    0,    0,    3,    0,   14,    0,
    0,   17,    0,   15,    0,    0,    0,   93,    0,    0,
    0,    0,    0,    0,    0,    0,   96,   97,   98,   99,
   94,   95,    0,    0,    0,    0,    0,    0,    0,    0,
   21,    0,    0,    0,    0,    0,    0,   76,    0,    0,
   50,   13,   16,   77,   75,   67,   66,   68,    0,    0,
   88,   89,    0,    0,    0,    0,   72,   74,    0,    0,
    0,   25,    0,    0,    0,    0,   20,    0,    0,   73,
   82,   83,   48,   49,   60,    0,    5,    0,   59,   71,
   70,    0,    0,    0,    0,   27,   28,    0,    0,   84,
   79,    0,    0,    0,   56,    0,   23,   24,   22,   19,
    0,    0,    0,    4,    0,    0,    0,    0,    0,    0,
    0,    0,   58,   57,    0,    0,   30,    0,   31,    0,
   29,    0,    0,    0,  100,  101,    0,    0,    0,    0,
   80,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,    0,    0,    0,    0,    0,    0,    0,   33,    0,
   34,    0,    0,   35,   36,    0,   32,    0,    0,    0,
    0,   62,    7,    0,    0,    0,    0,    0,   81,    0,
    0,    0,    0,    0,    0,    0,    0,    6,    9,    0,
    0,    0,    0,    0,    0,    0,   38,   39,   40,   41,
   42,   43,   37,    0,    0,    0,    0,    0,   46,    0,
   45,   44,
};
final static short yydgoto[] = {                         10,
   11,   12,  128,  212,  220,   13,   14,   15,   51,   16,
   80,  138,   81,  139,   17,   18,   19,   20,   21,   34,
  158,  177,  198,   73,   29,   46,   30,   31,
};
final static short yysindex[] = {                        38,
   51,   77,  -17,    3,    0,    0,  -27,    0,  -12,    0,
    0,   38,    0,    0,   -1,   14,    0,    0,    0,    0,
    0,   -4,    0,    0,    0,   -6,   86, -184,    8,   42,
    0,    0,   83, -157,  -23, -150, -199, -146, -208, -141,
   -8, -118,    0, -105,  -72,   75,    0,  -39,    0,  -69,
  129,    0,   66,    0,  -50,  133,   27,    0,  134,   45,
   45,   45,   45,  -62,  154,  -60,    0,    0,    0,    0,
    0,    0,   45,  139,  140,    4,  109,  -20,  -46,  160,
    0,   78,  -54,  -53,  144,  164,  166,    0, -127,  -47,
    0,    0,    0,    0,    0,    0,    0,    0,   42,   42,
    0,    0,  -51,   26,  -49,  126,    0,    0,  153,  155,
  -42,    0,  -36,  -33,  -25,  -34,    0,  178,   58,    0,
    0,    0,    0,    0,    0,   38,    0, -213,    0,    0,
    0,  172,  121,  149,  156,    0,    0, -208, -122,    0,
    0,  -18,  138,    2,    0,   -7,    0,    0,    0,    0,
   20, -176,  220,    0,   22,   24,   54,  229,   82,  111,
  112,   33,    0,    0,   45,  -97,    0,   19,    0,   19,
    0,   19,  116,  126,    0,    0,   40,  -93,  -93, -180,
    0,   39,  259,   60,   64,   65,   67,  -79,  270,    0,
  117,  118,  292,  296,  122,  123,   81,   53,    0,   19,
    0,   19,   19,    0,    0,   19,    0,   19,  304,   68,
   61,    0,    0,  -93,  -93,  -93,  -93, -185,    0,  221,
   61,   90,   91,   97,   98,   99,  -77,    0,    0,  316,
  317,  318,  319,  320,  321,  124,    0,    0,    0,    0,
    0,    0,    0,   19, -149,  -40,  107,  323,    0,  324,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,  326,    0,
    0,    0,    0,    0,    0,    0,    0,  -48,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -35,  -29,
    0,    0,    0,    0,    0,  -38,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -120,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -86,    0,  -86,
    0, -124,    0,  308,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -86,
    0,  -86,  -86,    0,    0,  -86,    0, -147,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  243,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -147,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
   28,  -76,  225,    0,  150, -142,    0,  135,  -22,    0,
    0,    0,  232, -108,    0,    0,    0,    0,    0,  339,
    0,    0,    0,  216,   32,  348,  125,  127,
};
final static int YYTABLESIZE=380;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
  249,   87,   64,   87,   89,   85,  137,   85,   95,   85,
   47,   86,   40,   86,  115,   86,    2,   87,   87,   60,
   87,   61,   37,   85,   85,   90,   85,  127,   43,   86,
   86,   44,   86,   35,   43,   83,   71,   44,   72,   47,
  113,    9,   38,   53,  110,   45,  144,   78,  145,   84,
   60,   45,   61,    9,   54,  213,   75,   49,   57,  178,
    8,  179,   79,  180,   35,    9,  123,  127,  221,   60,
  226,   61,    8,   58,   76,  186,  187,    9,  221,  160,
  161,    5,    6,   62,    8,   97,    5,    6,   63,   28,
   26,  214,    9,  215,  216,   28,    8,  217,  141,  218,
    9,  142,  112,   66,  106,   74,  246,   26,   26,   25,
   77,   27,   28,   71,   82,   72,   33,    5,    6,   26,
   26,   28,  167,   64,  126,  168,   25,   28,   27,   48,
   28,   26,   26,   88,  151,  245,   26,   85,   52,    5,
    6,    2,   26,   26,    5,    6,   26,   26,  126,   50,
   86,  169,  171,  143,  170,  172,  181,  199,  201,  182,
  200,  202,  205,  207,  243,  206,  208,  244,   60,  111,
   61,  175,  176,    5,    6,  211,  195,  196,  235,  236,
   26,   26,   50,   87,   99,  100,   91,   92,  101,  102,
   93,   96,   98,  103,  104,  105,  174,  107,  108,  116,
  117,  118,  120,  119,  121,   94,  122,   47,  124,  114,
  125,  130,  129,  131,   87,  132,  248,   48,  140,   87,
   85,  133,   64,   50,  134,   85,   86,    5,    6,   39,
  146,   86,  135,   87,   87,   87,   87,  136,  153,   85,
   85,   85,   85,  147,   41,   86,   86,   86,   86,  157,
   41,   67,   68,   69,   70,   48,   36,  155,    1,  109,
    2,   42,  154,   59,    3,    5,    6,    4,    5,    6,
    1,  148,    2,  152,    7,  159,    3,  162,  149,    4,
    5,    6,    1,  163,    2,  164,    7,  166,    3,  173,
  136,    4,    5,    6,    1,  189,    2,  183,    7,  190,
    3,   23,   24,    4,    5,    6,   22,   23,   24,  210,
    7,    2,  184,  185,  188,    3,  191,  210,    4,    2,
  192,  193,  194,    3,   23,   24,    4,  197,   67,   68,
   69,   70,   32,   23,   24,  203,  204,  209,   32,   23,
   24,   56,   23,   24,  219,  228,  230,  231,  222,  223,
  224,  225,  227,  232,  233,  234,  237,  238,  239,  240,
  241,  242,  250,  251,  252,   18,   63,    8,  156,  150,
  229,   65,  165,   55,    0,    0,    0,    0,    0,  247,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   44,   41,   41,   43,   59,   45,
   59,   41,   40,   43,   61,   45,    0,   59,   60,   43,
   62,   45,   40,   59,   60,   48,   62,  104,   41,   59,
   60,   44,   62,    2,   41,   44,   60,   44,   62,   12,
   61,   40,   40,   16,   41,   58,  260,  256,  262,   58,
   43,   58,   45,   40,   59,  198,  256,   59,   27,  168,
   59,  170,  271,  172,   33,   40,   89,  144,  211,   43,
  256,   45,   59,  258,  274,  256,  257,   40,  221,  256,
  257,  267,  268,   42,   59,   59,  267,  268,   47,   45,
   40,  200,   40,  202,  203,   45,   59,  206,   41,  208,
   40,   44,  123,  261,   73,  256,  256,   40,  256,   59,
  257,   61,   45,   60,  256,   62,   40,  267,  268,  267,
  268,   45,   41,   41,  123,   44,   59,   45,   61,  257,
   45,  256,  257,   59,  257,  244,  257,  256,  125,  267,
  268,  125,  267,  268,  267,  268,  267,  268,  123,   15,
  256,   41,   41,  126,   44,   44,   41,   41,   41,   44,
   44,   44,   41,   41,   41,   44,   44,   44,   43,   61,
   45,  269,  270,  267,  268,  123,  256,  257,  256,  257,
  267,  268,   48,  256,   60,   61,  256,   59,   62,   63,
  125,   59,   59,  256,   41,  256,  165,   59,   59,   40,
  123,  256,   59,  257,   41,  256,   41,  256,  256,  256,
  262,   59,  262,   59,  256,  258,  257,  257,   41,  261,
  256,  258,  261,   89,  258,  261,  256,  267,  268,  257,
   59,  261,  258,  275,  276,  277,  278,  272,  257,  275,
  276,  277,  278,  123,  257,  275,  276,  277,  278,  257,
  257,  275,  276,  277,  278,  257,  274,  256,  257,  256,
  259,  274,  125,  256,  263,  267,  268,  266,  267,  268,
  257,  123,  259,  139,  273,  256,  263,   58,  123,  266,
  267,  268,  257,  262,  259,  262,  273,   59,  263,  257,
  272,  266,  267,  268,  257,  257,  259,  258,  273,   41,
  263,  257,  258,  266,  267,  268,  256,  257,  258,  257,
  273,  259,  178,  179,  180,  263,  257,  257,  266,  259,
  257,  257,  256,  263,  257,  258,  266,   58,  275,  276,
  277,  278,  256,  257,  258,   44,   41,  257,  256,  257,
  258,  256,  257,  258,   41,  125,  257,  257,  214,  215,
  216,  217,  218,  257,  257,  257,   41,   41,   41,   41,
   41,   41,  256,   41,   41,   40,   59,  125,  144,  138,
  221,   33,  157,   26,   -1,   -1,   -1,   -1,   -1,  245,
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
"bloque : sentencias bloque",
"bloque_sentencias : '{' bloque '}'",
"bloque_sentencias : sentencias",
"bloque_sentencias_ejecutables : '{' sentencias_solo_ejecutables '}'",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_ejecutables sentencias_solo_ejecutables",
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
"sentencias : ';'",
"sentencias_declarativas : tipo lista_variables ';'",
"sentencias_declarativas : tipo ';'",
"sentencias_declarativas : ID error ';'",
"sentencias_declarativas : declaracion_proc bloque '}'",
"sentencias_declarativas : declaracion_proc '}'",
"$$1 :",
"declaracion_proc : PROC ID $$1 '(' lista_parametros_formales control_invocaciones",
"declaracion_proc : PROC '(' error '{'",
"declaracion_proc : PROC ID control_invocaciones",
"control_invocaciones : NI '=' CTE '{'",
"control_invocaciones : error '=' CTE '{'",
"control_invocaciones : NI error CTE '{'",
"control_invocaciones : error '{'",
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
"lista_variables : ID ',' lista_variables",
"lista_variables : ID lista_variables error",
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
"condicion : error",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' error ';'",
"asignacion : ID expresion error ';'",
"asignacion : ID ';'",
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

//#line 214 "gramatica.y"

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
	sintactico.addErrorSintactico("par: " + string);
}
//#line 477 "Parser.java"
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
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro un identificador para la variable."); }
break;
case 15:
//#line 45 "gramatica.y"
{    /* Tratamiento de errores para declaracion de variables*/
                                            /*RegistroSimbolo aux = sintactico.getElemTablaSimb($1.ival);*/
                                            /*if (!aux.getUso().equals("PROC"))*/
                                                /*sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro el tipo de la variable.");*/
                                       }
break;
case 16:
//#line 50 "gramatica.y"
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
case 17:
//#line 60 "gramatica.y"
{ sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): el cuerpo del procedimiento es vacio."); }
break;
case 18:
//#line 63 "gramatica.y"
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
case 19:
//#line 72 "gramatica.y"
{ if (sintactico.getErrorProc() == false)
                                                                                sintactico.agregarAnalisis("Se reconocio una declaracion de procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                                        }
break;
case 20:
//#line 75 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el identificador del procedimiento");
                                        sintactico.setErrorProc(true);
                                      }
break;
case 23:
//#line 82 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI"); }
break;
case 24:
//#line 83 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones"); }
break;
case 25:
//#line 84 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones"); sintactico.setErrorProc(true); }
break;
case 30:
//#line 94 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 31:
//#line 95 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 33:
//#line 98 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 34:
//#line 99 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 35:
//#line 100 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 36:
//#line 101 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 38:
//#line 104 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 39:
//#line 105 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 40:
//#line 106 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 41:
//#line 107 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 42:
//#line 108 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 43:
//#line 109 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 44:
//#line 111 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 45:
//#line 112 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 46:
//#line 113 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 49:
//#line 118 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en la declaracion de variable. Falta el caracter separador ','."); }
break;
case 50:
//#line 119 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue definido."); }
break;
case 56:
//#line 129 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 57:
//#line 130 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 58:
//#line 131 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en el cuerpo del ELSE."); }
break;
case 59:
//#line 132 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan los parentesis en la condicion del IF."); }
break;
case 60:
//#line 133 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); }
break;
case 61:
//#line 137 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 62:
//#line 138 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 63:
//#line 141 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 65:
//#line 144 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 66:
//#line 147 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 67:
//#line 148 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la asignacion no tiene expresion asignada."); }
break;
case 68:
//#line 149 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 69:
//#line 150 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el operador '=' y la asignacion no tiene expresion asignada."); }
break;
case 70:
//#line 153 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 71:
//#line 154 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 72:
//#line 155 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 73:
//#line 156 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 74:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 75:
//#line 160 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 76:
//#line 161 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 77:
//#line 162 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el ';' al final de la invocacion."); }
break;
case 82:
//#line 169 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 83:
//#line 170 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 84:
//#line 171 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 85:
//#line 174 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 86:
//#line 175 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 88:
//#line 179 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 89:
//#line 180 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 92:
//#line 185 "gramatica.y"
{
                    String tipo = sintactico.getTipoElemTablaSimb(val_peek(0).ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                }
break;
case 93:
//#line 190 "gramatica.y"
{
                        String tipo = sintactico.getTipoElemTablaSimb(val_peek(0).ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat(val_peek(0).ival);
                    }
break;
//#line 899 "Parser.java"
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
