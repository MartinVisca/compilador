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

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;
import analizadorLexico.matrices.MatrizTransicionEstados;

import java.util.Hashtable;
import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

//#line 33 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character
AnalizadorLexico analizadorLexico;
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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    4,    4,    7,
    7,    5,   10,   10,   11,   11,    9,    9,    3,   12,
   12,   13,   13,    6,    6,   14,   14,   15,   15,   15,
   16,   22,   22,   17,   24,   21,   21,   18,   20,   20,
   27,   27,   19,   26,   26,   26,   28,   28,   28,   29,
   29,    8,    8,   25,   25,   25,   25,   25,   25,   23,
   23,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    1,    1,    1,    3,    1,
    2,    3,    2,    3,    1,    3,    1,    3,    2,    8,
    7,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    5,    3,    5,   11,    1,    3,    3,    4,    4,    5,
    3,    5,    5,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   52,   53,    0,    0,    0,    0,
    2,    3,    6,    7,    8,    0,    0,   24,   25,   26,
   27,   28,   29,   30,    0,    0,    0,    0,    0,    0,
   10,    0,    4,    5,   17,    0,   22,    0,    0,    0,
    0,   50,   51,    0,    0,   49,    0,    0,    0,    0,
    0,    9,   11,   12,    0,   23,    0,   39,    0,    0,
   38,    0,    0,    0,    0,    0,   54,   55,   56,   57,
   58,   59,    0,    0,    0,    0,    0,    0,    0,   15,
    0,   18,   41,    0,   40,    0,    0,   47,   48,    0,
   31,    0,    0,   43,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   16,    0,   42,    0,
   32,    0,   21,    0,    0,    0,   20,   33,   60,   61,
    0,    0,   35,   34,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   13,   14,   15,   32,   16,   36,   80,
   81,   17,   38,   18,   19,   20,   21,   22,   23,   24,
   47,   91,  121,  124,   73,   48,   41,   45,   46,
};
final static short yysindex[] = {                      -117,
   10,    3,   21,   35,    0,    0, -188, -185,    0, -117,
    0,    0,    0,    0,    0, -169,  -96,    0,    0,    0,
    0,    0,    0,    0,  -38, -241, -241, -195, -164,   57,
    0,  -95,    0,    0,    0,   -2,    0,  -96,   41,   39,
   32,    0,    0,    8,    7,    0,  -16,    2,   59,   42,
   -1,    0,    0,    0, -156,    0, -155,    0, -153,   46,
    0, -241, -241, -241, -241,  -96,    0,    0,    0,    0,
    0,    0, -241, -241,   47, -151, -181, -163, -169,    0,
   36,    0,    0,   51,    0,    7,    7,    0,    0, -178,
    0,   40,   40,    0,   52, -169,   49,   68, -209, -158,
 -143,  -96,   56, -241,   68, -141,    0,   58,    0, -144,
    0,    6,    0, -138,   62, -180,    0,    0,    0,    0,
   81,  -96,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  123,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -36,  -31,    0,    0,    0,
    0,  -26,  -21,    0,    0,    0,    0,   83,    0,    0,
    0,    0,    0,    0,   84,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  116,  117,   -6,    0,    5,    0,  -29,  -44,   31,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   24,    0,    0,    0,   85,  -18,    0,   29,   30,
};
final static int YYTABLESIZE=284;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   19,   46,   40,   46,   44,    8,   44,   44,   44,   45,
   37,   45,   31,   45,   36,   42,   43,   46,   46,   37,
   46,   79,   44,   44,   66,   44,    8,   45,   45,   52,
   45,   56,   36,   36,   98,   36,   53,   37,   37,   78,
   37,   55,   27,   67,   62,   68,   63,   96,   64,   25,
   62,  105,   63,   65,   92,   93,   54,    5,    6,   90,
   28,   67,   77,   68,  116,   67,   61,   68,   30,   79,
   26,    1,   60,    2,   29,   59,  100,    3,   49,   99,
    4,  102,   62,  103,   63,    5,    6,   35,  119,  120,
   86,   87,   50,   88,   89,  110,   51,   58,   57,   75,
   82,   83,   76,   84,   85,   94,   95,   97,  101,  106,
  104,   55,  108,  109,  111,  123,  113,  115,  114,  117,
  118,  122,    1,   13,   14,   33,   34,  112,    0,  107,
    0,    0,   74,    0,    0,    0,    0,    0,    0,    1,
    0,    2,    0,    0,    0,    3,    0,    0,    4,    5,
    6,    0,    0,    0,    0,    7,    0,    0,    0,    0,
    1,    1,    2,    2,    0,    0,    3,    3,    0,    4,
    4,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   46,   46,   46,   46,    0,   44,   44,
   44,   44,    0,   45,   45,   45,   45,    0,   36,   36,
   36,   36,    0,   37,   37,   37,   37,    0,   69,   70,
   71,   72,    0,    0,    0,    5,    6,   19,   19,    0,
   77,    0,    0,   19,    0,    0,   69,   70,   71,   72,
   69,   70,   71,   72,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   43,   41,   45,   41,  123,   43,   26,   45,   41,
   17,   43,    8,   45,   41,  257,  258,   59,   60,   41,
   62,   51,   59,   60,   41,   62,  123,   59,   60,  125,
   62,   38,   59,   60,   79,   62,   32,   59,   60,   41,
   62,   44,   40,   60,   43,   62,   45,   77,   42,   40,
   43,   96,   45,   47,   73,   74,   59,  267,  268,   66,
   40,   60,  272,   62,   59,   60,   59,   62,  257,   99,
   61,  257,   41,  259,   40,   44,   41,  263,  274,   44,
  266,  260,   43,  262,   45,  267,  268,  257,  269,  270,
   62,   63,  257,   64,   65,  102,   40,   59,   58,   41,
  257,  257,   61,  257,   59,   59,  258,  271,   58,   61,
   59,   44,  271,  257,   59,  122,  258,  262,   61,  258,
   59,   41,    0,   41,   41,   10,   10,  104,   -1,   99,
   -1,   -1,   48,   -1,   -1,   -1,   -1,   -1,   -1,  257,
   -1,  259,   -1,   -1,   -1,  263,   -1,   -1,  266,  267,
  268,   -1,   -1,   -1,   -1,  273,   -1,   -1,   -1,   -1,
  257,  257,  259,  259,   -1,   -1,  263,  263,   -1,  266,
  266,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,   -1,   -1,  267,  268,  267,  268,   -1,
  272,   -1,   -1,  273,   -1,   -1,  275,  276,  277,  278,
  275,  276,  277,  278,
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
"\"<=\"","\">=\"","\"==\"","\"!=\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque",
"bloque : bloque_comun",
"bloque : declaracion_proc",
"bloque : bloque bloque_comun",
"bloque : bloque declaracion_proc",
"bloque_comun : bloque_control",
"bloque_comun : declaracion",
"bloque_control : sentencias",
"bloque_control : '{' bloque_sentencias '}'",
"bloque_sentencias : sentencias",
"bloque_sentencias : bloque_sentencias sentencias",
"declaracion : tipo lista_variables ';'",
"declaracion_variable_proc : tipo lista_variables",
"declaracion_variable_proc : REF tipo lista_variables",
"lista_variables_proc : declaracion_variable_proc",
"lista_variables_proc : lista_variables_proc ',' declaracion_variable_proc",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"declaracion_proc : encabezado_proc bloque_proc",
"encabezado_proc : PROC ID '(' lista_variables_proc ')' NI '=' CTE",
"encabezado_proc : PROC ID '(' ')' NI '=' CTE",
"bloque_proc : bloque_control",
"bloque_proc : bloque_proc bloque_control",
"sentencias : sentencia_unica_control",
"sentencias : sentencia_unica_ejecutable",
"sentencia_unica_control : sentencia_if",
"sentencia_unica_control : sentencia_for",
"sentencia_unica_ejecutable : asignacion",
"sentencia_unica_ejecutable : salida",
"sentencia_unica_ejecutable : llamado_proc",
"sentencia_if : IF '(' condicion ')' cuerpo_if",
"cuerpo_if : bloque_control END_IF ';'",
"cuerpo_if : bloque_control ELSE bloque_control END_IF ';'",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' cuerpo_for",
"cuerpo_for : bloque_control",
"condicion : condicion comparador expresion",
"condicion : expresion comparador expresion",
"asignacion : ID '=' expresion ';'",
"llamado_proc : ID '(' ')' ';'",
"llamado_proc : ID '(' parametros ')' ';'",
"parametros : ID ':' ID",
"parametros : parametros ',' ID ':' ID",
"salida : OUT '(' CADENA ')' ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"tipo : LONGINT",
"tipo : FLOAT",
"comparador : '<'",
"comparador : '>'",
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"==\"",
"comparador : \"!=\"",
"incr_decr : UP",
"incr_decr : DOWN",
};

//#line 147 "gramatica.y"

//#line 355 "Parser.java"
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

  private int yylex() {
    Token token=analizadorLexico.getToken();

    if (token!=null){
      yylval = new ParserVal(token);
      return token.getId();
    }

    return 0;
  }

  private void yyerror(String string) {
    System.out.println(string);
  }

}



//################### END OF CLASS ##############################
