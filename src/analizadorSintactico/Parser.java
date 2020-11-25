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
   21,   21,   18,   18,   18,   18,   18,   18,   18,   23,
   24,   22,   22,   22,   28,   19,   26,   20,   20,   20,
   29,   29,   29,   30,   30,   30,   25,   25,   25,   25,
   25,   25,   27,   27,    8,    8,
};
final static short yylen[] = {                            2,
    1,    1,    2,    3,    1,    3,    3,    3,    1,    1,
    2,    1,    1,    3,    3,    2,    3,    3,    3,    2,
    0,    6,    4,    3,    4,    2,    3,    4,    2,    4,
    0,    1,    1,    4,    3,    3,    8,    7,    7,   12,
   11,   11,   13,    1,    3,    3,    2,    1,    1,    1,
    1,    1,    4,    4,    3,    3,    5,    5,    4,    3,
    3,    4,    4,    3,    4,    1,    4,    8,   12,    3,
    3,    4,    6,    6,    8,    8,    3,    5,    4,    1,
    1,    3,    2,    3,    0,   13,    3,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,  105,  106,    0,    0,    0,    0,
    2,   13,   12,    0,    0,   48,   49,   50,   51,   52,
   94,   95,    0,    0,    0,    0,    0,    0,   93,   99,
  100,  101,  102,    0,   97,   98,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   66,    0,    0,    0,
    3,   16,   44,    0,    0,   20,    0,   17,    0,   56,
    0,   96,   55,    0,    0,    0,    0,    0,    0,    0,
   77,   83,   60,    0,    0,    0,    0,    0,    0,    0,
   24,    0,    0,    0,   61,    0,    0,   64,   47,   15,
    0,   14,    0,   19,   18,   65,   63,   54,   53,    0,
    0,   91,   92,   79,    0,    0,    0,    0,   80,    0,
   84,    0,   59,    0,   62,    0,    0,    0,   29,   26,
    0,   23,    0,    0,   70,   71,   46,   45,    0,    0,
    0,   78,   58,   57,    0,   27,    0,    0,   32,   33,
    0,    0,   72,   67,    0,    7,    4,   74,    0,   73,
    6,    0,   30,   25,   28,   22,    0,    0,    0,   81,
    0,    0,    0,   35,   36,    0,    0,   76,   75,    0,
    0,   34,    0,    0,    0,  103,  104,    0,    0,   68,
    0,    0,    0,    0,    0,   85,   38,   39,    0,    0,
    0,   37,    0,    0,    0,    0,   86,    9,    0,   69,
    0,    0,    0,    0,    8,   11,   41,   42,    0,   40,
    0,   43,
};
final static short yydgoto[] = {                          9,
  107,   51,  109,  197,  201,   12,   13,   14,   55,   15,
   80,  141,   81,  142,   16,   17,   18,   19,   20,   27,
   50,   38,  110,  161,   39,  163,  178,  191,   28,   29,
};
final static short yysindex[] = {                       157,
   77,   10,  -33,  -13,    0,    0,  -36,  -27,    0,  157,
    0,    0,    0, -148,  118,    0,    0,    0,    0,    0,
    0,    0, -219,  -28,  -43, -205,   15,   34,    0,    0,
    0,    0,    0,   18,    0,    0,   24, -199, -195, -183,
  -30, -175,   -7, -168,   -6, -151,    0, -146, -144,   59,
    0,    0,    0, -133,   33,    0,   73,    0,  -53,    0,
  -11,    0,    0,   40,   40,   40,   40, -128,   85,  104,
    0,    0,    0,   -8, -123,   78,  -52, -115, -106,  114,
    0,   48,  -99,  -82,    0,  135,  136,    0,    0,    0,
  -77,    0,  -76,    0,    0,    0,    0,    0,    0,   34,
   34,    0,    0,    0,  157,  130,  145,    0,    0,  -74,
    0,   52,    0,  -51,    0,  -72,  -69, -113,    0,    0,
  -20,    0,  143,   70,    0,    0,    0,    0,  100, -114,
  -68,    0,    0,    0,  131,    0, -105,  -67,    0,    0,
   -7, -193,    0,    0,  -66,    0,    0,    0,  130,    0,
    0,  -65,    0,    0,    0,    0,  -63,  -96,  137,    0,
 -221,  162,  140,    0,    0,   83,  -61,    0,    0,   40,
 -107,    0,  -78,  103,   52,    0,    0,  -57, -178,    0,
  -55,  163,  -46,  -92,  153,    0,    0,    0,  111,  -45,
  169,    0,  -78,  177,   54,  113,    0,    0, -164,    0,
   95,  113,  -24,  -90,    0,    0,    0,    0,  115,    0,
  -22,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  231,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -40,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  214,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -17,
    6,    0,    0,    0,    0,    0,    0,   47,    0,    0,
    0,   60,    0,    0,    0,    0,    0,    0,    0,    0,
 -136,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -136,    0,  196,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -136,    0,    0,    0,    0,    0,    0,    0,
    0,  132,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
   25,   29,  117,    0,   75, -100,    0,    1,    0,    0,
    0,    0,  139, -137,    0,    0,    0,    0,    0,   37,
  254,  257,  199,    0,  -25,    0,    0,    0,  109,  102,
};
final static int YYTABLESIZE=440;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         90,
   90,   26,   90,   44,   90,   97,   41,  134,  118,  138,
   75,   70,   47,   47,   54,   48,   48,  154,   90,   90,
  140,   90,   88,   88,   10,   88,   42,   88,   11,   49,
   49,   64,  114,   65,  168,  179,   58,   83,   37,   57,
  169,   88,   88,   11,   88,   89,   89,   99,   89,   34,
   89,   84,   62,   79,   26,  199,   71,   64,   68,   65,
   72,   61,   26,  157,   89,   89,   64,   89,   65,   35,
   37,   36,   73,    5,    6,   66,   93,   35,  183,   36,
   67,   76,   90,   35,   26,   36,    2,   82,    5,    6,
  198,   92,  203,   24,   64,  202,   65,  108,   26,   82,
   82,  202,    5,    6,   85,   88,  112,   52,   53,   86,
  144,   87,    8,  145,   25,   78,   24,   88,    5,    6,
   31,   26,   89,  172,    8,  106,  173,  104,   89,  129,
   31,   31,  115,   11,  108,   23,  170,   25,  116,    8,
  119,  148,  158,  180,  137,  149,  181,  150,   26,  120,
  153,  192,    8,  121,  193,  210,  123,    8,  211,  165,
  166,  176,  177,  188,  189,  208,  209,  102,  103,    8,
  122,    2,  100,  101,  124,  125,  126,  108,  127,  184,
  128,  132,   82,  143,    8,  135,  136,  151,  155,  152,
  159,  162,  164,  139,  167,  174,    8,   95,  171,  204,
  182,  185,   96,  186,  133,  117,  175,  105,    8,  187,
  190,  194,   60,   21,   22,   90,   90,  200,   90,  205,
   43,   35,   90,   36,  147,   90,   90,   90,   45,   45,
    1,  207,   90,  212,   90,   90,   90,   90,   88,   88,
   40,   88,   56,   74,   98,   88,   46,  113,   88,   88,
   88,  139,  105,   21,   87,   88,   10,   88,   88,   88,
   88,   89,   89,   77,   89,  160,   21,   22,   89,  131,
   63,   89,   89,   89,   21,   22,  206,   59,   89,  156,
   89,   89,   89,   89,   30,   31,   32,   33,   90,   91,
   69,  196,   30,   31,   32,   33,   21,   22,   30,   31,
   32,   33,    5,    2,  130,    2,    5,    0,    5,    2,
   21,   22,    2,    2,    2,   82,   82,    0,   82,    2,
    0,    0,   82,    0,    0,   82,   82,   82,   94,    1,
    0,    2,   82,   21,   22,    3,    0,    0,    4,    5,
    6,    1,    0,    2,    0,    7,    0,    3,    0,    0,
    4,    5,    6,    0,    0,  146,    1,    7,    2,  111,
   21,   22,    3,    0,    0,    4,    5,    6,    0,  195,
    0,    2,    7,    0,    1,    3,    2,    0,    4,    0,
    3,    0,    0,    4,    5,    6,    1,    0,    2,    0,
    7,    0,    3,    0,    0,    4,    5,    6,    0,    0,
    0,    1,    7,    2,    0,    0,    0,    3,    0,    0,
    4,    5,    6,    1,    0,    2,    0,    7,    0,    3,
    0,    0,    4,    5,    6,  195,    0,    2,    0,    7,
    0,    3,    0,    0,    4,    0,   30,   31,   32,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   41,   45,   43,   40,   45,   59,   40,   59,   61,  123,
   41,   37,   41,   41,   14,   44,   44,  123,   59,   60,
   41,   62,   40,   41,    0,   43,   40,   45,    0,   58,
   58,   43,   41,   45,  256,  173,  256,   44,    2,   15,
  262,   59,   60,   15,   62,   40,   41,   59,   43,   40,
   45,   58,  258,   61,   45,  193,  256,   43,   41,   45,
  256,   25,   45,  257,   59,   60,   43,   62,   45,   60,
   34,   62,  256,  267,  268,   42,   44,   60,  257,   62,
   47,  257,  123,   60,   45,   62,   40,  256,  267,  268,
  191,   59,  257,   40,   43,  196,   45,   69,   45,   40,
   41,  202,  267,  268,  256,  123,   70,  256,  257,  256,
   41,  256,   40,   44,   61,  123,   40,   59,  267,  268,
  257,   45,  256,   41,   40,   41,   44,  256,  123,  105,
  267,  268,  256,  105,  106,   59,  162,   61,   61,   40,
  256,  256,  142,   41,  258,  260,   44,  262,   45,  256,
  256,   41,   40,   40,   44,   41,  256,   40,   44,  256,
  257,  269,  270,  256,  257,  256,  257,   66,   67,   40,
  123,  125,   64,   65,  257,   41,   41,  149,  256,  179,
  257,  256,  123,   41,   40,  258,  256,  256,  256,   59,
  257,  257,  256,  272,   58,  257,   40,  125,   59,  199,
  258,  257,  256,   41,  256,  258,  170,  123,   40,  256,
   58,  257,  256,  257,  258,  256,  257,   41,  259,  125,
  257,   60,  263,   62,  125,  266,  267,  268,  257,  257,
    0,  256,  273,  256,  275,  276,  277,  278,  256,  257,
  274,  259,  125,  274,  256,  263,  274,  256,  266,  267,
  268,  272,  123,   40,   59,  273,  125,  275,  276,  277,
  278,  256,  257,  271,  259,  149,  257,  258,  263,  125,
  256,  266,  267,  268,  257,  258,  202,   24,  273,  141,
  275,  276,  277,  278,  275,  276,  277,  278,  256,  257,
   34,  123,  275,  276,  277,  278,  257,  258,  275,  276,
  277,  278,  256,  257,  106,  259,  260,   -1,  262,  263,
  257,  258,  266,  267,  268,  256,  257,   -1,  259,  273,
   -1,   -1,  263,   -1,   -1,  266,  267,  268,  256,  257,
   -1,  259,  273,  257,  258,  263,   -1,   -1,  266,  267,
  268,  257,   -1,  259,   -1,  273,   -1,  263,   -1,   -1,
  266,  267,  268,   -1,   -1,  256,  257,  273,  259,  256,
  257,  258,  263,   -1,   -1,  266,  267,  268,   -1,  257,
   -1,  259,  273,   -1,  257,  263,  259,   -1,  266,   -1,
  263,   -1,   -1,  266,  267,  268,  257,   -1,  259,   -1,
  273,   -1,  263,   -1,   -1,  266,  267,  268,   -1,   -1,
   -1,  257,  273,  259,   -1,   -1,   -1,  263,   -1,   -1,
  266,  267,  268,  257,   -1,  259,   -1,  273,   -1,  263,
   -1,   -1,  266,  267,  268,  257,   -1,  259,   -1,  273,
   -1,  263,   -1,   -1,  266,   -1,  275,  276,  277,  278,
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

//#line 217 "gramatica.y"

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
//#line 480 "Parser.java"
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
                                                                        }
break;
case 23:
//#line 74 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el identificador del procedimiento");
                                        sintactico.setErrorProc(true);
                                      }
break;
case 26:
//#line 81 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI."); sintactico.setErrorProc(true);}
break;
case 27:
//#line 82 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones."); sintactico.setErrorProc(true); }
break;
case 28:
//#line 83 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el numero de invocaciones."); sintactico.setErrorProc(true); }
break;
case 29:
//#line 84 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones."); sintactico.setErrorProc(true); }
break;
case 30:
//#line 85 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el '{' al abrir el cuerpo del procedimiento."); sintactico.setErrorProc(true); }
break;
case 35:
//#line 95 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 36:
//#line 96 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 38:
//#line 99 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 39:
//#line 100 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
break;
case 41:
//#line 103 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 42:
//#line 104 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
break;
case 43:
//#line 106 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
break;
case 46:
//#line 111 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la ',' para separar la lista de variables."); System.out.println(val_peek(2).ival);}
break;
case 47:
//#line 112 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue declarado."); }
break;
case 53:
//#line 122 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 54:
//#line 123 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la asignacion."); }
break;
case 55:
//#line 124 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
break;
case 56:
//#line 125 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
break;
case 57:
//#line 128 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 58:
//#line 129 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la salida por pantalla."); }
break;
case 59:
//#line 130 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
break;
case 60:
//#line 131 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
break;
case 61:
//#line 132 "gramatica.y"
{sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
break;
case 62:
//#line 133 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
break;
case 63:
//#line 136 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 64:
//#line 137 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
break;
case 65:
//#line 138 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta el ';' al final de la invocacion."); }
break;
case 70:
//#line 145 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 71:
//#line 146 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 72:
//#line 147 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
break;
case 73:
//#line 150 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 74:
//#line 151 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); }
break;
case 75:
//#line 152 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 76:
//#line 153 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); }
break;
case 77:
//#line 154 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan '(' en la condicion del IF."); }
break;
case 78:
//#line 155 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan ')' en la condicion del IF."); }
break;
case 79:
//#line 156 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); }
break;
case 83:
//#line 166 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 84:
//#line 167 "gramatica.y"
{ sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
break;
case 85:
//#line 171 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 86:
//#line 172 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 87:
//#line 175 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 88:
//#line 177 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 89:
//#line 178 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 91:
//#line 182 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 92:
//#line 183 "gramatica.y"
{ sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
break;
case 95:
//#line 188 "gramatica.y"
{
                    /*String tipo = sintactico.getTipoElemTablaSimb($1.ival);*/
                    /*if (tipo.equals("LONGINT"))*/
                        /*sintactico.verificarRangoEnteroLargo($1.ival);*/
                }
break;
case 96:
//#line 193 "gramatica.y"
{
                        /*String tipo = sintactico.getTipoElemTablaSimb($2.ival);*/
                        /*if (tipo.equals("FLOAT"))*/
                        /*    sintactico.verificarRangoFloat($2.ival);*/
                    }
break;
//#line 902 "Parser.java"
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
