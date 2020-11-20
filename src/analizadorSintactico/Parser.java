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
    2,    2,    7,    7,    7,    7,    7,   11,   10,   10,
   10,   13,   13,   13,   13,   14,   14,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,    9,    9,    9,    9,
    6,    6,    6,    6,    6,   18,   18,   18,   18,   21,
   18,   22,   18,   18,   25,   19,   23,   20,   20,   15,
   15,   15,   15,   16,   16,   16,   16,   16,   17,   17,
   17,   28,   28,   28,   28,   28,   28,   28,   27,   27,
   27,   29,   29,   29,   30,   30,   30,   26,   26,   26,
   26,   26,   26,   24,   24,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    1,    1,    2,    1,
    1,    1,    3,    2,    3,    3,    2,    0,    6,    4,
    3,    4,    4,    4,    2,    0,    1,    1,    4,    4,
    4,    8,    8,    8,    8,    8,   12,   12,   12,   12,
   12,   12,   12,   16,   16,   15,    1,    3,    3,    2,
    1,    1,    1,    1,    1,    6,    8,    8,    5,    0,
    7,    0,    3,    1,    0,   13,    3,    3,    1,    4,
    4,    4,    2,    5,    5,    4,    4,    4,    4,    3,
    4,    1,    4,    8,   12,    3,    3,    4,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  106,  107,    0,    0,   12,    0,
    0,    1,    0,    0,   11,   10,    0,    0,   51,   52,
   53,   54,   55,    0,   95,   96,   73,    0,    0,    0,
    0,    0,   94,   69,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   82,    0,    0,    0,    3,
    0,    0,   14,    0,    0,   17,    0,   15,    0,    0,
    0,   97,    0,    0,    0,    0,    0,    0,    0,    0,
  100,  101,  102,  103,   98,   99,    0,    0,    0,    0,
    0,    0,    0,    0,   21,    0,    4,    0,    0,    0,
    0,    0,   80,   63,    0,    0,   50,   13,   16,   81,
   79,   71,   70,   72,    0,    0,   92,   93,    0,    0,
    0,    0,   76,   78,    0,    0,    0,   25,    0,    0,
    0,    0,   20,    0,    0,   77,   86,   87,   48,   49,
   60,    0,    0,   59,   75,   74,    0,    0,    0,    0,
   27,   28,    0,    0,   88,   83,    0,    0,    0,   56,
    0,    0,   23,   24,   22,   19,    0,    0,    0,   61,
    0,    0,    0,    0,    0,    0,    0,    0,   58,   57,
    0,    0,   30,    0,   31,    0,   29,    0,    0,    0,
  104,  105,    0,    0,    0,    0,   84,    0,    0,    0,
    0,    0,    0,    0,    0,   65,    0,    0,    0,    0,
    0,    0,    0,    0,   33,    0,   34,    0,    0,   35,
   36,    0,   32,    0,    0,    0,    5,   66,    7,    0,
    0,    0,    0,    0,   85,    0,    0,    0,    0,    0,
    0,    0,    0,    6,    9,    0,    0,    0,    0,    0,
    0,    0,   38,   39,   40,   41,   42,   43,   37,    0,
    0,    0,    0,    0,   46,    0,   45,   44,
};
final static short yydgoto[] = {                         11,
   43,   13,   14,  218,  226,   15,   16,   17,   55,   18,
   84,  143,   85,  144,   19,   20,   21,   22,   23,   36,
  148,   51,  164,  183,  204,   77,   37,   49,   32,   33,
};
final static short yysindex[] = {                        31,
    5,   61,  -40,   -8,    0,    0,  -36,   31,    0,  -21,
    0,    0,   31,    0,    0,    0,   69,   19,    0,    0,
    0,    0,    0,  -15,    0,    0,    0,   26,   67, -205,
    3,   40,    0,    0,   64, -196,   -5, -167, -198, -163,
 -183, -158,   23,   33, -156,    0, -146, -140,   71,    0,
  -93,   -2,    0,  -87,  135,    0,   72,    0,  -34,  139,
    9,    0,  140,  -10,  -10,  -10,  -10,  -56,  160,  -54,
    0,    0,    0,    0,    0,    0,  -10,  144,  145,  -25,
  146,  -30,  -47,  165,    0,   83,    0,  -48,  -41,  151,
  178,  179,    0,    0, -172,  -24,    0,    0,    0,    0,
    0,    0,    0,    0,   40,   40,    0,    0,  -37, -106,
   -6,   18,    0,    0,  168,  174,   -4,    0,   -1,   17,
   35,  -31,    0,  238,   73,    0,    0,    0,    0,    0,
    0, -181,   24,    0,    0,    0,  230,  172,  173,  180,
    0,    0, -183, -128,    0,    0,   34,   25,   45,    0,
   28,   51,    0,    0,    0,    0,   74,  -78,  269,    0,
   66,   70,   37,  270,   97,  109,  111,   76,    0,    0,
  -10,  -89,    0,   59,    0,   59,    0,   59,  115,   18,
    0,    0,   77,  -85,  -85, -149,    0,   81,  293,   82,
   84,   85,   87,  -72,  282,    0,  119,  120,  300,  304,
  121,  126,   89,   43,    0,   59,    0,   59,   59,    0,
    0,   59,    0,   59,  306,   31,    0,    0,    0,  -85,
  -85,  -85,  -85, -164,    0,  223,   31,   92,   93,   94,
   95,   96,  -68,    0,    0,  313,  314,  315,  316,  317,
  318,  127,    0,    0,    0,    0,    0,    0,    0,   59,
 -134,  -29,  104,  320,    0,  321,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    2,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -38,    0,    0,    0,    0,    0,    0,    0,    0,
  323,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   21,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -32,  -26,    0,    0,    0,    0,
    0,  -35,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -110,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -77,    0,  -77,    0, -136,    0,  305,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -77,    0,  -77,  -77,    0,
    0,  -77,    0, -131,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -107,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -131,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   56, -178,    0,    0,  138, -165,    0,   -9,  -23,    0,
    0,    0,  224,  -63,    0,    0,    0,    0,    0,  331,
    0,    0,    0,    0,    0,  205,   46,  341,  128,  129,
};
final static int YYTABLESIZE=369;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         39,
   64,    2,   91,   42,   91,   68,   91,   54,   89,  142,
   89,  255,   89,  121,   90,  116,   90,    8,   90,   46,
   91,   91,   47,   91,  101,  217,   89,   89,   96,   89,
  119,   40,   90,   90,   30,   90,   48,   64,  219,   65,
   64,   95,   54,   58,   28,   64,   31,   65,  217,   30,
  227,   64,   62,   65,   75,   12,   76,   79,   10,   64,
   64,  227,   65,   27,   70,   29,   46,  103,   50,   47,
   10,  129,   82,   57,   61,   80,   88,    9,  149,   47,
  150,   66,   10,   48,   52,   54,   67,   83,   78,    9,
   89,  232,  118,   81,    5,    6,   75,   86,   76,   90,
   35,    9,    5,    6,   68,   30,  192,  193,   30,   91,
  184,   30,  185,  146,  186,   92,  147,    5,    6,   26,
   26,  252,  112,   64,   26,   64,    2,   53,  157,   93,
   26,   26,    5,    6,  158,   26,   26,  173,    5,    6,
  174,    8,  220,   56,  221,  222,   26,   87,  223,  175,
  224,  177,  176,    8,  178,  187,   26,   26,  188,  205,
  207,  211,  206,  208,  212,  216,  213,  249,   97,  214,
  250,   11,  132,  133,  190,  191,  194,  166,  167,  181,
  182,    5,    6,  201,  202,   94,  251,  241,  242,   26,
   26,  105,  106,   98,  107,  108,   99,  102,  104,  109,
  110,  111,  113,  114,  122,  123,  117,  124,  120,  126,
  228,  229,  230,  231,  233,  125,  180,   91,  127,  128,
   41,  100,   91,   89,  131,   68,  135,  254,   89,   90,
  115,  130,  136,   38,   90,   44,   91,   91,   91,   91,
  141,  253,   89,   89,   89,   89,   25,   26,   90,   90,
   90,   90,   45,  137,   52,  134,  138,   64,   63,   64,
   24,   25,   26,   64,    5,    6,   64,   64,   64,   71,
   72,   73,   74,   64,  139,    1,   47,    2,  145,   62,
    5,    3,   44,  151,    4,    5,    6,    1,  152,    2,
  159,    7,  140,    3,  153,  154,    4,    5,    6,    1,
  161,    2,  155,    7,  160,    3,  162,  163,    4,    5,
    6,   71,   72,   73,   74,    7,   34,   25,   26,   34,
   25,   26,   60,   25,   26,   52,  168,  169,  172,  165,
  141,  170,  179,  196,  189,    5,    6,  195,  197,  203,
  198,  199,  200,  209,  210,  215,  225,  234,  236,  237,
  238,  239,  240,  243,  244,  245,  246,  247,  248,  256,
  257,  258,   18,   67,  235,   69,  156,  171,   59,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    0,   41,   40,   43,   41,   45,   17,   41,   41,
   43,   41,   45,   61,   41,   41,   43,  125,   45,   41,
   59,   60,   44,   62,   59,  204,   59,   60,   52,   62,
   61,   40,   59,   60,   45,   62,   58,   43,  204,   45,
   40,   44,   52,   59,   40,   43,    1,   45,  227,   45,
  216,   43,  258,   45,   60,    0,   62,  256,   40,   59,
   43,  227,   45,   59,  261,   61,   41,   59,   13,   44,
   40,   95,  256,   18,   29,  274,   44,   59,  260,   59,
  262,   42,   40,   58,  257,   95,   47,  271,  256,   59,
   58,  256,  123,  257,  267,  268,   60,  256,   62,  256,
   40,   59,  267,  268,   41,   45,  256,  257,   45,  256,
  174,   45,  176,   41,  178,  256,   44,  267,  268,  256,
  257,  256,   77,  123,  256,  125,  125,   59,  257,   59,
  267,  268,  267,  268,  144,  267,  268,   41,  267,  268,
   44,  123,  206,  125,  208,  209,  257,  125,  212,   41,
  214,   41,   44,  123,   44,   41,  267,  268,   44,   41,
   41,   41,   44,   44,   44,  123,   41,   41,  256,   44,
   44,  279,  279,  280,  184,  185,  186,  256,  257,  269,
  270,  267,  268,  256,  257,  279,  250,  256,  257,  267,
  268,   64,   65,   59,   66,   67,  125,   59,   59,  256,
   41,  256,   59,   59,   40,  123,   61,  256,  256,   59,
  220,  221,  222,  223,  224,  257,  171,  256,   41,   41,
  257,  256,  261,  256,  262,  261,   59,  257,  261,  256,
  256,  256,   59,  274,  261,  257,  275,  276,  277,  278,
  272,  251,  275,  276,  277,  278,  257,  258,  275,  276,
  277,  278,  274,  258,  257,  262,  258,  257,  256,  259,
  256,  257,  258,  263,  267,  268,  266,  267,  268,  275,
  276,  277,  278,  273,  258,  257,  256,  259,   41,  279,
  279,  263,  257,  260,  266,  267,  268,  257,   59,  259,
  257,  273,  258,  263,  123,  123,  266,  267,  268,  257,
  256,  259,  123,  273,  280,  263,  279,  257,  266,  267,
  268,  275,  276,  277,  278,  273,  256,  257,  258,  256,
  257,  258,  256,  257,  258,  257,   58,  262,   59,  256,
  272,  262,  257,   41,  258,  267,  268,  257,  257,   58,
  257,  257,  256,   44,   41,  257,   41,  125,  257,  257,
  257,  257,  257,   41,   41,   41,   41,   41,   41,  256,
   41,   41,   40,   59,  227,   35,  143,  163,   28,
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
//#line 481 "Parser.java"
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
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 57:
//#line 130 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
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
case 62:
//#line 136 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]");
                                sintactico.agregarAPolaca(" ");
                                sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                sintactico.agregarAPolaca("BI");
                              }
break;
case 64:
//#line 143 "gramatica.y"
{ sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + sintactico.getSizePolaca() + "]"); }
break;
case 65:
//#line 147 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 66:
//#line 148 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 67:
//#line 151 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 68:
//#line 153 "gramatica.y"
{ sintactico.agregarAPolaca(val_peek(1).sval);
                                             sintactico.agregarAPolaca(" ");
                                             sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                             sintactico.agregarAPolaca("BF");
                                           }
break;
case 69:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 70:
//#line 161 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 71:
//#line 162 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la asignacion no tiene expresion asignada."); }
break;
case 72:
//#line 163 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 73:
//#line 164 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el operador '=' y la asignacion no tiene expresion asignada."); }
break;
case 74:
//#line 167 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 75:
//#line 168 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 76:
//#line 169 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 77:
//#line 170 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 78:
//#line 171 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 79:
//#line 174 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 80:
//#line 175 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 81:
//#line 176 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el ';' al final de la invocacion."); }
break;
case 86:
//#line 183 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 87:
//#line 184 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 88:
//#line 185 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 89:
//#line 188 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 90:
//#line 189 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 92:
//#line 193 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 93:
//#line 194 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 96:
//#line 199 "gramatica.y"
{
                    String tipo = sintactico.getTipoElemTablaSimb(val_peek(0).ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                }
break;
case 97:
//#line 204 "gramatica.y"
{
                        String tipo = sintactico.getTipoElemTablaSimb(val_peek(0).ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat(val_peek(0).ival);
                    }
break;
case 98:
//#line 211 "gramatica.y"
{ yyval.sval = new String("<"); }
break;
case 99:
//#line 212 "gramatica.y"
{ yyval.sval = new String(">"); }
break;
case 100:
//#line 213 "gramatica.y"
{ yyval.sval = new String("<="); }
break;
case 101:
//#line 214 "gramatica.y"
{ yyval.sval = new String(">="); }
break;
case 102:
//#line 215 "gramatica.y"
{ yyval.sval = new String("=="); }
break;
case 103:
//#line 216 "gramatica.y"
{ yyval.sval = new String("!="); }
break;
//#line 947 "Parser.java"
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
