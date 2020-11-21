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
public final static short cuerpo_else=279;
public final static short cuerpo_if=280;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    4,    4,    5,    5,    2,
    2,    2,    7,    7,    7,    7,    7,    7,   11,   10,
   10,   10,   13,   13,   13,   13,   14,   14,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,    9,    9,    9,
    9,    6,    6,    6,    6,    6,   18,   18,   18,   18,
   21,   18,   22,   18,   18,   25,   19,   23,   20,   20,
   15,   15,   15,   15,   16,   16,   16,   16,   16,   17,
   17,   17,   28,   28,   28,   28,   28,   28,   28,   27,
   27,   27,   29,   29,   29,   30,   30,   30,   26,   26,
   26,   26,   26,   26,   24,   24,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    1,    1,    2,    1,
    1,    1,    3,    3,    2,    3,    3,    2,    0,    6,
    4,    3,    4,    4,    4,    2,    0,    1,    1,    4,
    4,    4,    8,    8,    8,    8,    8,   12,   12,   12,
   12,   12,   12,   12,   16,   16,   15,    1,    3,    3,
    2,    1,    1,    1,    1,    1,    6,    8,    8,    5,
    0,    7,    0,    3,    1,    0,   13,    3,    3,    1,
    4,    4,    3,    3,    5,    5,    4,    4,    4,    4,
    3,    4,    1,    4,    8,   12,    3,    3,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    2,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  107,  108,    0,    0,   12,    0,
    0,    0,    0,    0,   11,   10,    0,    0,   52,   53,
   54,   55,   56,   96,   97,    0,    0,    0,    0,    0,
    0,   95,   70,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   83,    0,    0,    0,    0,    0,
   15,   48,    0,    0,   18,    0,   16,    0,   74,    0,
   98,   73,    0,    0,    0,    0,    0,    0,    0,  101,
  102,  103,  104,   99,  100,    0,    0,    0,    0,    0,
    0,    0,    0,   22,    0,    4,    0,    0,    0,    0,
    0,   81,   64,   51,   14,    0,   13,    0,   17,   82,
   80,   72,   71,    0,    0,   93,   94,    0,    0,    0,
    0,   77,   79,    0,    0,    0,   26,    0,    0,    0,
    0,   21,    0,    0,   78,   87,   88,   50,   49,   61,
    0,    0,   60,   76,   75,    0,    0,    0,    0,   28,
   29,    0,    0,   89,   84,    0,    0,    0,   57,    0,
    0,   24,   25,   23,   20,    0,    0,    0,   62,    0,
    0,    0,    0,    0,    0,    0,    0,   59,   58,    0,
    0,   31,    0,   32,    0,   30,    0,    0,    0,  105,
  106,    0,    0,    0,    0,   85,    0,    0,    0,    0,
    0,    0,    0,    0,   66,    0,    0,    0,    0,    0,
    0,    0,    0,   34,    0,   35,    0,    0,   36,   37,
    0,   33,    0,    0,    0,    5,   67,    7,    0,    0,
    0,    0,    0,   86,    0,    0,    0,    0,    0,    0,
    0,    0,    6,    9,    0,    0,    0,    0,    0,    0,
    0,   39,   40,   41,   42,   43,   44,   38,    0,    0,
    0,    0,    0,   47,    0,   46,   45,
};
final static short yydgoto[] = {                         11,
   42,   13,   14,  217,  225,   15,   16,   17,   54,   18,
   83,  142,   84,  143,   19,   20,   21,   22,   23,   35,
  147,   50,  163,  182,  203,   76,   36,   48,   31,   32,
};
final static short yysindex[] = {                        85,
   -2,   98,  -23,  -17,    0,    0,  -18,   85,    0,   -8,
    0,   85,    0,    0,    0,    0, -175,   48,    0,    0,
    0,    0,    0,    0,    0, -227,   -7,  104, -206,   29,
    6,    0,    0,   20, -184,  -30, -148, -212, -137, -217,
 -145,   60,  -16, -128,    0, -121, -114,   96,    0, -134,
    0,    0,  -87,   -4,    0,   72,    0,  -47,    0,   35,
    0,    0,    4,    4,    4,    4,  -84,  110,  -77,    0,
    0,    0,    0,    0,    0,    4,  123,  137,  -32,  144,
  -26,  -50,  158,    0,   84,    0,  -42,  -39,  168,  192,
  203,    0,    0,    0,    0,    7,    0,   -3,    0,    0,
    0,    0,    0,    6,    6,    0,    0,   -5, -105,    3,
   46,    0,    0,  212,  213,   34,    0,   38,   50,   51,
  -34,    0,  246,   23,    0,    0,    0,    0,    0,    0,
 -100,   52,    0,    0,    0,  247,  190,  195,  197,    0,
    0, -217, -110,    0,    0,   65,   44,   69,    0,   53,
   73,    0,    0,    0,    0,   78,  -69,  278,    0,   75,
   79,  101,  284,   32,   42,   55,   89,    0,    0,    4,
  -79,    0,   77,    0,   77,    0,   77,   57,   46,    0,
    0,  106,  -68,  -68, -154,    0,   90,  309,  108,  112,
  113,  115,  -55,  314,    0,   86,   88,  330,  334,   92,
   93,  124,  100,    0,   77,    0,   77,   77,    0,    0,
   77,    0,   77,  339,   85,    0,    0,    0,  -68,  -68,
  -68,  -68, -150,    0,  257,   85,  127,  128,  129,  130,
  131,  -53,    0,    0,  342,  348,  349,  350,  351,  352,
  126,    0,    0,    0,    0,    0,    0,    0,   77, -146,
  -38,  138,  354,    0,  355,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  397,    1,   16,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,    0,  358,
    0,    0,    0,    0,    0,    0,    0,    0,   31,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -35,   25,    0,    0,    0,    0,    0,
  -36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -91,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -57,    0,  -57,    0, -152,    0,  340,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -57,    0,  -57,  -57,    0,    0,
  -57,    0, -103,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -111,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -103,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   45,   67,    0,    0,  174, -157,    0,    9,    0,    0,
    0,    0,  259,  -27,    0,    0,    0,    0,    0,  368,
    0,    0,    0,    0,    0,  241,   19,  377,  149,  151,
};
final static int YYTABLESIZE=404;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         92,
    2,   92,  254,   92,   69,   90,  141,   90,  115,   90,
  120,  101,   63,    8,   64,   65,   38,   92,   92,   30,
   92,   41,   39,   90,   90,   53,   90,   87,   57,   74,
    3,   75,   45,   45,  118,   46,   46,   27,   81,   98,
    2,   88,   29,   78,   12,  218,   60,   65,   29,   47,
   47,   61,   66,   82,   97,   65,   26,  226,   28,    2,
   67,   79,   56,  145,   29,   91,  146,   91,  226,   91,
    3,   63,  172,   64,   65,  173,   69,   63,   49,   64,
   51,   52,  174,   91,   91,  175,   91,   10,   63,    3,
   64,    5,    6,  103,  111,  176,  117,  186,  177,   10,
  187,  191,  192,   27,   27,  231,    9,   77,   49,  251,
   85,   10,    5,    6,   27,   27,    5,    6,    9,   80,
    5,    6,   49,    2,   10,    2,  204,   89,  206,  205,
    9,  207,  210,  212,   90,  211,  213,   34,   65,   10,
   65,   91,   29,    9,   93,  183,  156,  184,   29,  185,
  109,  157,   27,    3,   92,    3,    5,    6,    9,  148,
   74,  149,   75,   27,   27,   27,  248,   11,   94,  249,
    8,  108,   55,  131,  132,   27,   27,  219,  110,  220,
  221,  112,    8,  222,   86,  223,  165,  166,  179,  180,
  181,  189,  190,  193,    8,  113,   99,  121,    5,    6,
  200,  201,  240,  241,  116,  119,  122,    8,  100,   27,
   27,  104,  105,  123,   92,  106,  107,  124,  253,   92,
   90,  250,  215,  114,   69,   90,  125,  227,  228,  229,
  230,  232,  126,   92,   92,   92,   92,  140,   40,   90,
   90,   90,   90,  127,   70,   71,   72,   73,   43,   43,
   37,   95,   96,  129,   24,   25,  130,    2,  252,    2,
   24,   25,  128,    2,  133,   44,    2,    2,    2,  216,
  134,  135,   65,    2,   65,   33,   24,   25,   65,    5,
   91,   65,   65,   65,   62,   91,  144,    3,   65,    3,
  102,  136,  216,    3,   63,  137,    3,    3,    3,   91,
   91,   91,   91,    3,    1,  151,    2,  138,  139,    5,
    3,  150,  152,    4,    5,    6,    1,  153,    2,  154,
    7,  158,    3,  159,  160,    4,    5,    6,    1,  162,
    2,  161,    7,  164,    3,  167,  168,    4,    5,    6,
  169,    1,  171,    2,    7,  178,  194,    3,  140,  195,
    4,    5,    6,   33,   24,   25,    1,    7,    2,   59,
   24,   25,    3,  188,  196,    4,    5,    6,  197,  198,
  199,  202,    7,  208,  209,   70,   71,   72,   73,  224,
  214,  233,  242,  235,  236,  237,  238,  239,  243,  244,
  245,  246,  247,  255,  256,  257,    1,   19,   68,  234,
  155,   68,  170,   58,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   41,   45,   41,   41,   41,   43,   41,   45,
   61,   59,   43,  125,   45,    0,   40,   59,   60,    1,
   62,   40,   40,   59,   60,   17,   62,   44,  256,   60,
    0,   62,   41,   41,   61,   44,   44,   40,  256,   44,
   40,   58,   45,  256,    0,  203,   28,   42,   45,   58,
   58,  258,   47,  271,   59,   40,   59,  215,   61,   59,
   41,  274,   18,   41,   45,   41,   44,   43,  226,   45,
   40,   43,   41,   45,   59,   44,  261,   43,   12,   45,
  256,  257,   41,   59,   60,   44,   62,   40,   43,   59,
   45,  267,  268,   59,   76,   41,  123,   41,   44,   40,
   44,  256,  257,  256,  257,  256,   59,  256,   42,  256,
  256,   40,  267,  268,  267,  268,  267,  268,   59,  257,
  267,  268,   56,  123,   40,  125,   41,  256,   41,   44,
   59,   44,   41,   41,  256,   44,   44,   40,  123,   40,
  125,  256,   45,   59,  279,  173,  257,  175,   45,  177,
   41,  143,  256,  123,   59,  125,  267,  268,   59,  260,
   60,  262,   62,  267,  268,  257,   41,  279,  256,   44,
  123,  256,  125,  279,  280,  267,  268,  205,  256,  207,
  208,   59,  123,  211,  125,  213,  256,  257,  170,  269,
  270,  183,  184,  185,  123,   59,  125,   40,  267,  268,
  256,  257,  256,  257,   61,  256,  123,  123,  256,  267,
  268,   63,   64,  256,  256,   65,   66,  257,  257,  261,
  256,  249,  123,  256,  261,  261,   59,  219,  220,  221,
  222,  223,   41,  275,  276,  277,  278,  272,  257,  275,
  276,  277,  278,   41,  275,  276,  277,  278,  257,  257,
  274,  256,  257,  257,  257,  258,  262,  257,  250,  259,
  257,  258,  256,  263,  262,  274,  266,  267,  268,  203,
   59,   59,  257,  273,  259,  256,  257,  258,  263,  279,
  256,  266,  267,  268,  256,  261,   41,  257,  273,  259,
  256,  258,  226,  263,  279,  258,  266,  267,  268,  275,
  276,  277,  278,  273,  257,   59,  259,  258,  258,  279,
  263,  260,  123,  266,  267,  268,  257,  123,  259,  123,
  273,  257,  263,  280,  256,  266,  267,  268,  257,  257,
  259,  279,  273,  256,  263,   58,  262,  266,  267,  268,
  262,  257,   59,  259,  273,  257,  257,  263,  272,   41,
  266,  267,  268,  256,  257,  258,  257,  273,  259,  256,
  257,  258,  263,  258,  257,  266,  267,  268,  257,  257,
  256,   58,  273,   44,   41,  275,  276,  277,  278,   41,
  257,  125,   41,  257,  257,  257,  257,  257,   41,   41,
   41,   41,   41,  256,   41,   41,    0,   40,   59,  226,
  142,   34,  162,   27,
};
}
final static short YYFINAL=11;
final static short YYMAXTOKEN=280;
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
"MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO","cuerpo_else","cuerpo_if",
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
"sentencias_solo_ejecutables : sentencias_ejecutables sentencias_solo_ejecutables",
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
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID error",
"lista_variables : tipo error",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : invocacion_proc",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_for",
"sentencia_if : IF '(' condicion ')' cuerpo_else END_IF",
"sentencia_if : IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF",
"sentencia_if : IF '(' condicion ')' cuerpo_else ELSE error END_IF",
"sentencia_if : IF condicion THEN error END_IF",
"$$2 :",
"sentencia_if : IF '(' ')' error END_IF $$2 cuerpo_if",
"$$3 :",
"sentencia_if : bloque_sentencias $$3 cuerpo_else",
"sentencia_if : bloque_sentencias",
"$$4 :",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion_for ';' incr_decr CTE ')' $$4 bloque_sentencias_ejecutables",
"condicion_for : ID comparador expresion",
"condicion : expresion comparador expresion",
"condicion : error",
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

//#line 228 "gramatica.y"

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
//#line 490 "Parser.java"
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
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 58:
//#line 127 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
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
case 63:
//#line 133 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]");
                                sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BI");
                              }
break;
case 65:
//#line 140 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + sintactico.getSizePolaca() + "]"); }
break;
case 66:
//#line 144 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 67:
//#line 145 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 68:
//#line 148 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 70:
//#line 151 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 71:
//#line 154 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(3).ival));
                                      sintactico.agregarAPolaca("=");}
break;
case 72:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ';' al final de la asignacion."); }
break;
case 73:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 74:
//#line 159 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
break;
case 75:
//#line 162 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")");sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(2).ival)); sintactico.agregarAPolaca("OUT"); }
break;
case 76:
//#line 163 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 77:
//#line 164 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 78:
//#line 165 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 79:
//#line 166 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 80:
//#line 169 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 81:
//#line 170 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 82:
//#line 171 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el ';' al final de la invocacion."); }
break;
case 87:
//#line 178 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 88:
//#line 179 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 89:
//#line 180 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 90:
//#line 183 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca("+");}
break;
case 91:
//#line 185 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca("-");}
break;
case 93:
//#line 190 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")");
                                  sintactico.agregarAPolaca("*");}
break;
case 94:
//#line 192 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")");
                                  sintactico.agregarAPolaca("/");}
break;
case 97:
//#line 198 "gramatica.y"
{
                    String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                    sintactico.agregarAPolaca(sintactico.getLexemaFromTS(val_peek(0).ival));
                }
break;
case 98:
//#line 204 "gramatica.y"
{
                        String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat(val_peek(0).ival);
                    }
break;
case 99:
//#line 211 "gramatica.y"
{ yyval.sval = new String("<"); }
break;
case 100:
//#line 212 "gramatica.y"
{ yyval.sval = new String(">"); }
break;
case 101:
//#line 213 "gramatica.y"
{ yyval.sval = new String("<="); }
break;
case 102:
//#line 214 "gramatica.y"
{ yyval.sval = new String(">="); }
break;
case 103:
//#line 215 "gramatica.y"
{ yyval.sval = new String("=="); }
break;
case 104:
//#line 216 "gramatica.y"
{ yyval.sval = new String("!="); }
break;
case 105:
//#line 219 "gramatica.y"
{yyval.sval = new String("UP");}
break;
case 106:
//#line 220 "gramatica.y"
{yyval.sval = new String("DOWN");}
break;
case 107:
//#line 223 "gramatica.y"
{yyval.sval = new String("LONGINT");}
break;
case 108:
//#line 224 "gramatica.y"
{yyval.sval = new String("FLOAT");}
break;
//#line 971 "Parser.java"
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
