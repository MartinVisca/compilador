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
    0,    1,    1,    3,    3,    3,    3,    4,    4,    5,
    5,    2,    2,    7,    7,    7,    7,    7,    7,    7,
   11,   10,   10,   10,   13,   13,   13,   13,   13,   13,
   14,   14,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,    9,    9,    9,    9,    6,    6,    6,
    6,    6,   15,   15,   15,   15,   16,   16,   16,   16,
   16,   16,   17,   17,   17,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   18,   18,   18,   18,   18,   18,   18,   23,   24,   22,
   22,   22,   28,   19,   26,   20,   20,   20,   29,   29,
   29,   30,   30,   30,   25,   25,   25,   25,   25,   25,
   27,   27,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    3,    3,    1,    1,
    2,    1,    1,    3,    3,    2,    3,    3,    3,    2,
    0,    6,    4,    3,    4,    2,    3,    4,    2,    4,
    0,    1,    1,    4,    3,    3,    8,    7,    7,   12,
   11,   11,   13,    1,    3,    3,    2,    1,    1,    1,
    1,    1,    4,    4,    3,    3,    5,    5,    4,    3,
    3,    4,    4,    3,    4,    1,    4,    8,   12,    3,
    3,    4,    5,    4,    4,    3,    4,    6,    7,    8,
    8,    7,    8,    9,   10,   11,   12,   12,   11,   12,
    6,    6,    8,    8,    3,    5,    4,    1,    1,    3,
    2,    3,    0,   13,    3,    3,    3,    1,    3,    3,
    1,    1,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  123,  124,    0,    0,    0,    0,
    2,   13,   12,    0,    0,   48,   49,   50,   51,   52,
  112,  113,    0,    0,    0,    0,    0,    0,  111,  117,
  118,  119,  120,    0,  115,  116,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   66,    0,    0,    0,
    3,   16,   44,    0,    0,   20,    0,   17,    0,   56,
    0,  114,   55,    0,    0,    0,    0,    0,    0,    0,
   95,  101,   60,    0,    0,    0,    0,    0,    0,    0,
   24,    0,    0,    0,    0,    0,   61,    0,    0,   64,
   47,   15,    0,   14,    0,   19,   18,   65,   63,   54,
   53,    0,    0,  109,  110,   97,    0,    0,    0,    0,
   98,    0,  102,    0,   59,    0,   62,    0,    0,    0,
   29,   26,    0,   23,    0,   76,    0,    0,    0,    0,
   70,   71,   46,   45,    0,    0,    0,   96,   58,   57,
    0,   27,    0,    0,   32,   33,    0,    0,   74,   72,
   77,   67,    0,    0,   75,    0,    7,    4,   92,    0,
   91,    6,    0,   30,   25,   28,   22,    0,    0,    0,
    0,    0,   73,   99,    0,    0,    0,   35,   36,    0,
    0,    0,    0,    0,   78,   94,   93,    0,    0,   34,
    0,    0,   82,    0,    0,    0,   79,    0,  121,  122,
    0,    0,   80,   83,   68,    0,    0,   81,    0,    0,
    0,    0,    0,    0,    0,   84,  103,   38,   39,    0,
    0,    0,    0,    0,   85,    0,   37,    0,    0,   89,
    0,    0,   86,    0,    0,  104,    9,    0,   87,   90,
   69,   88,    0,    0,    0,    0,    8,   11,   41,   42,
    0,   40,    0,   43,
};
final static short yydgoto[] = {                          9,
  109,   51,  111,  236,  243,   12,   13,   14,   55,   15,
   80,  147,   81,  148,   16,   17,   18,   19,   20,   27,
   50,   38,  112,  175,   39,  177,  201,  226,   28,   29,
};
final static short yysindex[] = {                       157,
   77,   10,  -33,  -11,    0,    0,  -36,  -27,    0,  157,
    0,    0,    0, -137,  118,    0,    0,    0,    0,    0,
    0,    0, -216,  -28,  104, -210,   15,   97,    0,    0,
    0,    0,    0,   18,    0,    0,   24, -204, -199, -180,
  -30, -168,   -7, -152,  -26, -142,    0, -138, -135,   68,
    0,    0,    0, -124,   65,    0,   73,    0,  -50,    0,
   52,    0,    0,   40,   40,   40,   40, -113,   85,  143,
    0,    0,    0,   -8, -108,  102,  -51,  -95,  -85,  138,
    0,   56,  -74,  -72,  -70,   -3,    0,  146,  148,    0,
    0,    0,  -66,    0,  -65,    0,    0,    0,    0,    0,
    0,   97,   97,    0,    0,    0,  157,  130,  145,    0,
    0,  -63,    0,   67,    0,  -22,    0,  -67,  -62,  -96,
    0,    0,  -20,    0,  158,    0,  159,   33,  -55,  -54,
    0,    0,    0,    0,  100, -164,  -53,    0,    0,    0,
  151,    0,  -89,  -44,    0,    0,   -7, -186,    0,    0,
    0,    0,  -52,  -43,    0,  163,    0,    0,    0,  130,
    0,    0,  -42,    0,    0,    0,    0,    1, -102,   -2,
   62,  173,    0,    0, -154,  162,  161,    0,    0,  106,
   69,   75,   20,  204,    0,    0,    0,   40, -104,    0,
    8,  225,    0,   49,   81,   82,    0,   67,    0,    0,
   66, -134,    0,    0,    0,  -46,   87,    0,  304,  306,
   93,  -88,   21,   98,  314,    0,    0,    0,    0,  115,
  108,  122,  -39,  328,    0,  169,    0,    8,  330,    0,
   34,  124,    0,   54,  113,    0,    0, -122,    0,    0,
    0,    0,  249,  113,  126,  -82,    0,    0,    0,    0,
  116,    0,  127,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  388,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  352,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -17,    6,    0,    0,    0,    0,    0,    0,   47,
    0,    0,    0,   60,    0,    0,    0,    0,    0,    0,
    0,    0, -116,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -116,    0,    0,    0,    0,    0,    0,  331,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -116,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  269,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   88,  350,  235,    0,  165, -191,    0,   -6,    0,    0,
    0,    0,  258, -155,    0,    0,    0,    0,    0,  103,
  382,  373,  302,    0,  -12,    0,    0,    0,  112,  114,
};
final static int YYTABLESIZE=510;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        108,
  108,  232,  108,   44,  108,  171,   41,   54,   99,  120,
   75,  214,   47,   47,   84,   48,   48,   85,  108,  108,
  146,  108,  106,  106,   70,  106,  144,  106,   42,   49,
   49,   86,  116,  165,  237,  202,  140,  129,  182,   58,
  130,  106,  106,  244,  106,  107,  107,   62,  107,   34,
  107,   71,  244,   79,   26,  183,   72,   64,   68,   65,
  195,  222,   26,  196,  107,  107,   64,  107,   65,   35,
  168,   36,  238,  152,  241,   73,  153,   35,  223,   36,
    5,    6,  108,   35,   26,   36,    2,   10,   76,  205,
  154,  159,  206,   24,   64,  160,   65,  161,   26,  100,
  100,  186,   57,   82,   37,  106,  207,  187,   95,   64,
  101,   65,    8,   87,   25,   78,   24,   88,   52,   53,
   89,   26,  211,   94,    8,  108,   90,   61,  107,    5,
    6,   91,    5,    6,  245,   23,   37,   25,   66,    8,
   31,  169,  106,   67,    5,    6,  190,  117,   26,  191,
   31,   31,    8,  179,  180,  227,  252,    8,  228,  253,
  121,  143,  118,  188,  199,  200,  164,  219,  220,    8,
  122,    2,  114,  250,  251,  102,  103,  123,  124,  104,
  105,  125,  100,  126,    8,  127,  131,   26,  132,  133,
  141,  134,  138,  142,  135,  212,    8,   97,  149,  150,
  155,  156,  162,  173,  170,   98,  119,  107,    8,  163,
  213,  166,  172,  185,  176,  108,  108,  231,  108,  189,
   43,   35,  108,   36,  158,  108,  108,  108,   45,   45,
   83,  246,  108,  139,  108,  108,  108,  108,  106,  106,
   40,  106,   56,   74,  197,  106,   46,  115,  106,  106,
  106,  145,  107,  128,  181,  106,  178,  106,  106,  106,
  106,  107,  107,   77,  107,  203,   21,   22,  107,  137,
   63,  107,  107,  107,   21,   22,  194,  221,  107,  145,
  107,  107,  107,  107,   30,   31,   32,   33,  151,  240,
  198,  235,   30,   31,   32,   33,   21,   22,   30,   31,
   32,   33,    5,    2,  204,    2,    5,  100,    5,    2,
   21,   22,    2,    2,    2,  100,  100,  184,  100,    2,
   92,   93,  100,  210,  192,  100,  100,  100,   96,    1,
  193,    2,  100,   21,   22,    3,  208,  209,    4,    5,
    6,    1,  215,    2,  216,    7,  217,    3,  218,   11,
    4,    5,    6,  224,  225,  157,    1,    7,    2,   60,
   21,   22,    3,  229,   11,    4,    5,    6,  233,  234,
  239,    2,    7,  247,    1,    3,    2,  230,    4,  242,
    3,  249,  254,    4,    5,    6,    1,    1,    2,  105,
    7,   21,    3,   10,  174,    4,    5,    6,  113,   21,
   22,    1,    7,    2,  167,   59,   69,    3,  248,  136,
    4,    5,    6,    1,    0,    2,    0,    7,  110,    3,
    0,    0,    4,    5,    6,  234,    0,    2,    0,    7,
    0,    3,    0,    0,    4,    0,   30,   31,   32,   33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   11,  110,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  110,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   41,   43,   40,   45,   58,   40,   14,   59,   61,
   41,   58,   41,   41,   41,   44,   44,   44,   59,   60,
   41,   62,   40,   41,   37,   43,  123,   45,   40,   58,
   58,   58,   41,  123,  226,  191,   59,   41,   41,  256,
   44,   59,   60,  235,   62,   40,   41,  258,   43,   40,
   45,  256,  244,   61,   45,   58,  256,   43,   41,   45,
   41,   41,   45,   44,   59,   60,   43,   62,   45,   60,
  257,   62,  228,   41,   41,  256,   44,   60,   58,   62,
  267,  268,  123,   60,   45,   62,   40,    0,  257,   41,
   58,  256,   44,   40,   43,  260,   45,  262,   45,   40,
   41,  256,   15,  256,    2,  123,   58,  262,   44,   43,
   59,   45,   40,  256,   61,  123,   40,  256,  256,  257,
  256,   45,  257,   59,   40,   41,   59,   25,  123,  267,
  268,  256,  267,  268,  257,   59,   34,   61,   42,   40,
  257,  148,  256,   47,  267,  268,   41,  256,   45,   44,
  267,  268,   40,  256,  257,   41,   41,   40,   44,   44,
  256,  258,   61,  176,  269,  270,  256,  256,  257,   40,
  256,  125,   70,  256,  257,   64,   65,   40,  123,   66,
   67,  256,  123,  256,   40,  256,   41,   45,   41,  256,
  258,  257,  256,  256,  107,  202,   40,  125,   41,   41,
  256,  256,  256,   41,  257,  256,  258,  123,   40,   59,
  257,  256,  256,   41,  257,  256,  257,  257,  259,   59,
  257,   60,  263,   62,  125,  266,  267,  268,  257,  257,
  257,  238,  273,  256,  275,  276,  277,  278,  256,  257,
  274,  259,  125,  274,   41,  263,  274,  256,  266,  267,
  268,  272,  123,  257,  257,  273,  256,  275,  276,  277,
  278,  256,  257,  271,  259,   41,  257,  258,  263,  125,
  256,  266,  267,  268,  257,  258,  257,  257,  273,  272,
  275,  276,  277,  278,  275,  276,  277,  278,  256,  256,
  188,  123,  275,  276,  277,  278,  257,  258,  275,  276,
  277,  278,  256,  257,  256,  259,  260,  256,  262,  263,
  257,  258,  266,  267,  268,  256,  257,  256,  259,  273,
  256,  257,  263,  258,  256,  266,  267,  268,  256,  257,
  256,  259,  273,  257,  258,  263,  256,  256,  266,  267,
  268,  257,  256,  259,   41,  273,   41,  263,  256,    0,
  266,  267,  268,  256,   41,  256,  257,  273,  259,  256,
  257,  258,  263,  256,   15,  266,  267,  268,   41,  257,
   41,  259,  273,  125,  257,  263,  259,  256,  266,  256,
  263,  256,  256,  266,  267,  268,  257,    0,  259,   59,
  273,   40,  263,  125,  160,  266,  267,  268,  256,  257,
  258,  257,  273,  259,  147,   24,   34,  263,  244,  108,
  266,  267,  268,  257,   -1,  259,   -1,  273,   69,  263,
   -1,   -1,  266,  267,  268,  257,   -1,  259,   -1,  273,
   -1,  263,   -1,   -1,  266,   -1,  275,  276,  277,  278,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  107,  108,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  160,
};
}
final static short YYFINAL=9;
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
"bloque_sentencias : bloque '}' error",
"bloque_sentencias : '{' bloque error",
"bloque_sentencias_ejecutables : '{' sentencias_solo_ejecutables '}'",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_ejecutables",
"sentencias_solo_ejecutables : sentencias_ejecutables sentencias_solo_ejecutables",
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
"sentencias_declarativas : tipo lista_variables ';'",
"sentencias_declarativas : tipo lista_variables error",
"sentencias_declarativas : tipo error",
"sentencias_declarativas : ID ';' error",
"sentencias_declarativas : declaracion_proc bloque '}'",
"sentencias_declarativas : declaracion_proc bloque error",
"sentencias_declarativas : declaracion_proc '}'",
"$$1 :",
"declaracion_proc : PROC ID $$1 '(' lista_parametros_formales control_invocaciones",
"declaracion_proc : PROC '(' error '{'",
"declaracion_proc : PROC ID control_invocaciones",
"control_invocaciones : NI '=' CTE '{'",
"control_invocaciones : '=' error",
"control_invocaciones : NI CTE error",
"control_invocaciones : NI '=' '{' error",
"control_invocaciones : '{' error",
"control_invocaciones : NI '=' CTE error",
"modificador :",
"modificador : REF",
"lista_parametros_formales : ')'",
"lista_parametros_formales : modificador tipo ID ')'",
"lista_parametros_formales : modificador ID error",
"lista_parametros_formales : modificador tipo error",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador ID error",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo error",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ')'",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador ID error",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo error",
"lista_parametros_formales : modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' error",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ID error",
"lista_variables : tipo error",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : invocacion_proc",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_for",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' expresion error",
"asignacion : ID expresion error",
"asignacion : ID '=' error",
"salida : OUT '(' CADENA ')' ';'",
"salida : OUT '(' CADENA ')' error",
"salida : OUT '(' CADENA error",
"salida : OUT CADENA error",
"salida : '(' CADENA error",
"salida : OUT '(' ')' error",
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
"parametros : ID ':' ',' error ')'",
"parametros : ID ID error ')'",
"parametros : ID ':' ')' error",
"parametros : ID ')' error",
"parametros : ID ':' ID error",
"parametros : ID ':' ID ':' error ')'",
"parametros : ID ':' ID ',' ':' error ')'",
"parametros : ID ':' ID ',' ID ID error ')'",
"parametros : ID ':' ID ',' ID ':' ')' error",
"parametros : ID ':' ID ',' ID ')' error",
"parametros : ID ':' ID ',' ID ':' ID error",
"parametros : ID ':' ID ',' ID ':' ',' error ')'",
"parametros : ID ':' ID ',' ID ':' ID ':' error ')'",
"parametros : ID ':' ID ',' ID ':' ID ',' ':' error ')'",
"parametros : ID ':' ID ',' ID ':' ID ',' ID ID error ')'",
"parametros : ID ':' ID ',' ID ':' ID ',' ID ':' ')' error",
"parametros : ID ':' ID ',' ID ':' ID ',' ID ')' error",
"parametros : ID ':' ID ',' ID ':' ID ',' ID ':' ID error",
"sentencia_if : IF '(' condicion ')' cuerpo_if END_IF",
"sentencia_if : IF '(' condicion ')' cuerpo_if error",
"sentencia_if : IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF",
"sentencia_if : IF '(' condicion ')' cuerpo_if ELSE cuerpo_else error",
"sentencia_if : IF condicion error",
"sentencia_if : IF '(' condicion cuerpo_if error",
"sentencia_if : IF '(' ')' error",
"cuerpo_if : bloque_sentencias",
"cuerpo_else : bloque_sentencias",
"condicion : expresion comparador expresion",
"condicion : comparador error",
"condicion : expresion comparador error",
"$$2 :",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion_for ';' incr_decr CTE ')' $$2 bloque_sentencias_ejecutables",
"condicion_for : ID comparador expresion",
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

//#line 246 "gramatica.y"

private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico; }

public int yylex() {
    int token = lexico.yylex();
    if (lexico.getRefTablaSimbolos() != -1)
        yylval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

public void yyerror(String string) {
	sintactico.addErrorSintactico("par: " + string);
}
//#line 528 "Parser.java"
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
case 6:
//#line 28 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '{' al abrir el bloque."); }
break;
case 7:
//#line 29 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '}' al cerrar el bloque."); }
break;
case 14:
//#line 44 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 15:
//#line 45 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la declaracion."); }
break;
case 16:
//#line 46 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el/los identificadores."); }
break;
case 17:
//#line 47 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el tipo de la variable."); }
break;
case 18:
//#line 48 "gramatica.y"
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
case 19:
//#line 58 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el '}' que cierra el cuerpo del procedimiento."); sintactico.setErrorProc(true);}
break;
case 20:
//#line 59 "gramatica.y"
{ sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): el cuerpo del procedimiento es vacio."); }
break;
case 21:
//#line 62 "gramatica.y"
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
case 22:
//#line 71 "gramatica.y"
{ if (sintactico.getErrorProc() == false)
                                                                                sintactico.agregarAnalisis("Se reconocio una declaracion de procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                                          sintactico.setErrorProc(false);
                                                                        }
break;
case 23:
//#line 75 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el identificador del procedimiento");
                                        sintactico.setErrorProc(true);
                                      }
break;
case 26:
//#line 82 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI."); sintactico.setErrorProc(true);}
break;
case 27:
//#line 83 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones."); sintactico.setErrorProc(true); }
break;
case 28:
//#line 84 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el numero de invocaciones."); sintactico.setErrorProc(true); }
break;
case 29:
//#line 85 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones."); sintactico.setErrorProc(true); }
break;
case 30:
//#line 86 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el '{' al abrir el cuerpo del procedimiento."); sintactico.setErrorProc(true); }
break;
case 35:
//#line 96 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 36:
//#line 97 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 38:
//#line 100 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 39:
//#line 101 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 41:
//#line 104 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 42:
//#line 105 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 43:
//#line 107 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 46:
//#line 112 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la ',' para separar la lista de variables."); System.out.println(val_peek(2).ival);}
break;
case 47:
//#line 113 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue declarado."); }
break;
case 53:
//#line 123 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 54:
//#line 124 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la asignacion."); }
break;
case 55:
//#line 125 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 56:
//#line 126 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
break;
case 57:
//#line 129 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 58:
//#line 130 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la salida por pantalla."); }
break;
case 59:
//#line 131 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 60:
//#line 132 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 61:
//#line 133 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 62:
//#line 134 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 63:
//#line 137 "gramatica.y"
{
                                                if (sintactico.getErrorInvocacion() == false)
                                                    sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                sintactico.setErrorInvocacion(false);
                                           }
break;
case 64:
//#line 142 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 65:
//#line 143 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta el ';' al final de la invocacion."); }
break;
case 70:
//#line 150 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 71:
//#line 151 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 72:
//#line 152 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 73:
//#line 153 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 74:
//#line 154 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 75:
//#line 155 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 76:
//#line 156 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 77:
//#line 157 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el parentesis que cierra los parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 78:
//#line 158 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 79:
//#line 159 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 80:
//#line 160 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 81:
//#line 161 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 82:
//#line 162 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 83:
//#line 163 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el parentesis que cierra los parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 84:
//#line 164 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 85:
//#line 165 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 86:
//#line 166 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 87:
//#line 167 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 88:
//#line 168 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 89:
//#line 169 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 90:
//#line 170 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el parentesis que cierra los parametros formales."); sintactico.setErrorInvocacion(true);}
break;
case 91:
//#line 173 "gramatica.y"
{
                                                           if (sintactico.getErrorIf() == false)
                                                                sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")");
                                                           sintactico.setErrorIf(false);
                                                        }
break;
case 92:
//#line 178 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); sintactico.setErrorIf(true);}
break;
case 93:
//#line 179 "gramatica.y"
{
                                                                             if (sintactico.getErrorIf() == false)
                                                                                  sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")");
                                                                             sintactico.setErrorIf(false);
                                                                          }
break;
case 94:
//#line 184 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); sintactico.setErrorIf(true);}
break;
case 95:
//#line 185 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan '(' en la condicion del IF."); sintactico.setErrorIf(true);}
break;
case 96:
//#line 186 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan ')' en la condicion del IF."); sintactico.setErrorIf(true);}
break;
case 97:
//#line 187 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); sintactico.setErrorIf(true);}
break;
case 101:
//#line 197 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 102:
//#line 198 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 103:
//#line 202 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 104:
//#line 203 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 105:
//#line 206 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 106:
//#line 208 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 107:
//#line 209 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 109:
//#line 213 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 110:
//#line 214 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 113:
//#line 219 "gramatica.y"
{
                    String tipo = sintactico.getTipoFromTS(val_peek(0).ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo(val_peek(0).ival);
                }
break;
case 114:
//#line 224 "gramatica.y"
{
                        sintactico.setNegativoTablaSimb(val_peek(0).ival);
                    }
break;
//#line 1033 "Parser.java"
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
