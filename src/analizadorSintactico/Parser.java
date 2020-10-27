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






//#line 2 "E:\usuario\Documents\Universidad\Diseño de Compiladores I\Compilador\src\analizadorSintactico\gramatica.y"

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

//#line 26 "Parser.java"




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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    2,    2,    4,    6,    7,
    8,    9,    9,   10,   10,   10,   10,    5,   12,   12,
   13,   13,    3,    3,    3,    3,    3,    3,   14,   14,
   15,   15,   15,   15,   16,   16,   20,   20,   17,   18,
   19,   19,   24,   24,   23,   23,   23,   25,   25,   25,
   26,   26,   26,   22,   22,   22,   22,   22,   22,   21,
   21,   11,   11,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    1,    1,    3,    2,    3,
    1,    7,    6,    2,    3,    4,    5,    3,    1,    3,
    1,    2,    1,    1,    1,    1,    1,    1,    7,    9,
    9,   11,   11,   13,   12,   14,    3,    3,    4,    5,
    4,    5,    3,    5,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   62,   63,   11,    0,    0,    2,
    3,    6,    7,    0,    0,    0,   23,   24,   25,   26,
   27,   28,    0,    0,    0,    0,    0,    4,    5,    0,
    0,    0,    9,   19,    0,    0,    0,    0,   51,   52,
    0,    0,    0,   50,    0,    0,    0,    0,    0,    8,
    0,   18,    0,    0,   41,    0,    0,   53,   39,    0,
    0,    0,    0,    0,   54,   55,   56,   57,   58,   59,
    0,    0,    0,    0,   10,    0,    0,    0,    0,   20,
   43,   42,    0,    0,    0,   48,   49,    0,    0,    0,
    0,   40,    0,    0,    0,    0,    0,   14,    0,   21,
    0,    0,    0,    0,   15,    0,    0,    0,    0,   44,
    0,   22,    0,    0,   29,    0,   13,    0,    0,   16,
    0,    0,    0,    0,    0,   12,   17,    0,    0,   30,
    0,   31,   60,   61,    0,    0,    0,    0,    0,    0,
   33,   32,    0,    0,    0,    0,   35,   34,    0,   36,
};
final static short yydgoto[] = {                          8,
    9,   10,  100,   12,   13,   14,   31,   15,   33,   78,
   16,   35,  101,   17,   18,   19,   20,   21,   22,   45,
  135,   71,   46,   38,   43,   44,
};
final static short yysindex[] = {                      -188,
  -29,  -13,    5,   15,    0,    0,    0,    0, -188,    0,
    0,    0,    0,  -58, -184, -180,    0,    0,    0,    0,
    0,    0,  -38,  -37,  -37, -179, -156,    0,    0, -188,
   53,   75,    0,    0,   22,   58,   59,   12,    0,    0,
 -141,   31,   17,    0,  -16,   -8,   78,   60, -119,    0,
  -40,    0, -137, -135,    0,   64, -133,    0,    0,  -37,
  -37,  -37,  -37, -107,    0,    0,    0,    0,    0,    0,
  -37,  -37,   66, -132,    0, -162, -144,   42, -129,    0,
    0,    0,   71,   17,   17,    0,    0, -166, -178,   49,
   49,    0,   72, -127,   73, -139, -165,    0, -124,    0,
  -82, -106,   76,  -37,    0, -122,   82, -162, -120,    0,
 -164,    0, -166, -123,    0,  -12,    0, -113, -111,    0,
  -93,   96,  -67,   99, -161,    0,    0, -166, -100,    0,
  -97,    0,    0,    0,  126,  -62,  109,  110,  -72,  -91,
    0,    0, -166,  113,  115,  -57,    0,    0,  119,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  180,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -36,  -31,    0,    0,    0,    0,  -26,
  -21,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  152,   13,   40,    0,    0,    0,    0,    0,    0,    0,
   -9,    0,  -56,    0,    0,    0,    0,    0,    0,   79,
    0,  142,  -11,    0,   50,   51,
};
final static int YYTABLESIZE=270;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   77,   47,   37,   47,   45,   75,   45,   41,   45,   46,
   23,   46,   42,   46,   37,   88,  113,   47,   47,   38,
   47,   28,   45,   45,   64,   45,   25,   46,   46,  128,
   46,   24,   37,   37,   60,   37,   61,   38,   38,   11,
   38,   79,  111,   65,   26,   66,  125,   65,   29,   66,
  143,   65,   56,   66,   27,   57,  123,  131,   62,   90,
   91,   28,  140,   63,   30,   53,   94,  149,    1,   11,
    2,  136,   32,   60,    3,   61,   34,    4,    5,    6,
   52,  102,   96,  103,    7,   97,  146,  109,   29,   59,
    1,   60,    2,   61,   47,  121,    3,  122,  119,    4,
   48,    5,    6,   89,    5,    6,  108,  133,  134,   84,
   85,   50,   86,   87,   51,   54,   58,   55,   73,   80,
   74,   81,   82,   83,   92,   93,   95,   98,   99,  105,
  104,  107,  110,  106,  115,  117,  120,    1,  124,    2,
  112,  114,  118,    3,  126,  127,    4,    5,    6,    1,
    1,    2,    2,    7,  130,    3,    3,  132,    4,    4,
  129,  137,  112,    1,  138,    2,  139,  141,  142,    3,
  145,  147,    4,  148,    1,  112,    2,  150,  144,    1,
    3,   49,  116,    4,    1,  112,    2,   72,    0,    1,
    3,    2,    0,    4,    1,    3,    2,    0,    4,    1,
    3,    2,    0,    4,    0,    3,    0,    0,    4,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   36,   39,
   40,    0,    0,    0,    0,    0,    5,    6,    0,    0,
    0,   76,    0,   47,   47,   47,   47,    0,   45,   45,
   45,   45,    0,   46,   46,   46,   46,    0,   37,   37,
   37,   37,    0,   38,   38,   38,   38,    0,   67,   68,
   69,   70,   67,   68,   69,   70,   67,   68,   69,   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   41,  125,   43,   45,   45,   41,
   40,   43,   24,   45,   41,  123,  123,   59,   60,   41,
   62,    9,   59,   60,   41,   62,   40,   59,   60,  123,
   62,   61,   59,   60,   43,   62,   45,   59,   60,    0,
   62,   51,  125,   60,   40,   62,   59,   60,    9,   62,
  123,   60,   41,   62,   40,   44,  113,  125,   42,   71,
   72,   49,  125,   47,  123,   44,   76,  125,  257,   30,
  259,  128,  257,   43,  263,   45,  257,  266,  267,  268,
   59,  260,   41,  262,  273,   44,  143,   97,   49,   59,
  257,   43,  259,   45,  274,  260,  263,  262,  108,  266,
  257,  267,  268,   64,  267,  268,  272,  269,  270,   60,
   61,   59,   62,   63,   40,   58,  258,   59,   41,  257,
   61,  257,   59,  257,   59,  258,  271,  257,   58,  257,
   59,  271,  257,   61,   59,  258,  257,  257,  262,  259,
  101,  102,   61,  263,  258,  257,  266,  267,  268,  257,
  257,  259,  259,  273,   59,  263,  263,   59,  266,  266,
  121,  262,  123,  257,  262,  259,   41,   59,   59,  263,
  262,   59,  266,   59,  257,  136,  259,   59,  139,    0,
  263,   30,  104,  266,  257,  146,  259,   46,   -1,  257,
  263,  259,   -1,  266,  257,  263,  259,   -1,  266,  257,
  263,  259,   -1,  266,   -1,  263,   -1,   -1,  266,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,
  258,   -1,   -1,   -1,   -1,   -1,  267,  268,   -1,   -1,
   -1,  272,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,  275,  276,  277,  278,  275,  276,  277,  278,
};
}
final static short YYFINAL=8;
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
"bloque : sentencias_declarativas",
"bloque : sentencias_ejecutables",
"bloque : bloque sentencias_declarativas",
"bloque : bloque sentencias_ejecutables",
"sentencias_declarativas : declaracion_metodo",
"sentencias_declarativas : declaracion_variable",
"declaracion_metodo : encabezado_metodo cuerpo_metodo ';'",
"encabezado_metodo : tipo_resultado declarador_metodo",
"cuerpo_metodo : '{' bloque '}'",
"tipo_resultado : PROC",
"declarador_metodo : ID '(' lista_parametros_formales ')' NI '=' CTE",
"declarador_metodo : ID '(' ')' NI '=' CTE",
"lista_parametros_formales : tipo ID",
"lista_parametros_formales : REF tipo ID",
"lista_parametros_formales : lista_parametros_formales ',' tipo ID",
"lista_parametros_formales : lista_parametros_formales ',' REF tipo ID",
"declaracion_variable : tipo lista_variables ';'",
"lista_variables : ID",
"lista_variables : lista_variables ',' ID",
"bloque_sentencias_ejecutables : sentencias_ejecutables",
"bloque_sentencias_ejecutables : bloque_sentencias_ejecutables sentencias_ejecutables",
"sentencias_ejecutables : sentencia_if",
"sentencias_ejecutables : sentencia_if_else",
"sentencias_ejecutables : sentencia_for",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : salida",
"sentencias_ejecutables : invocacion_proc",
"sentencia_if : IF '(' condicion ')' sentencias_ejecutables END_IF ';'",
"sentencia_if : IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' sentencias_ejecutables ELSE sentencias_ejecutables END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' sentencias_ejecutables ELSE '{' bloque_sentencias_ejecutables '}' END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' ELSE sentencias_ejecutables END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables '}' END_IF ';'",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' sentencias_ejecutables ';'",
"sentencia_for : FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' '{' bloque_sentencias_ejecutables '}' ';'",
"condicion : condicion comparador expresion",
"condicion : expresion comparador expresion",
"asignacion : ID '=' expresion ';'",
"salida : OUT '(' CADENA ')' ';'",
"invocacion_proc : ID '(' ')' ';'",
"invocacion_proc : ID '(' parametros ')' ';'",
"parametros : ID ':' ID",
"parametros : parametros ',' ID ':' ID",
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
"comparador : \"<=\"",
"comparador : \">=\"",
"comparador : \"==\"",
"comparador : \"!=\"",
"incr_decr : UP",
"incr_decr : DOWN",
"tipo : LONGINT",
"tipo : FLOAT",
};

//#line 134 "E:\usuario\Documents\Universidad\Diseño de Compiladores I\Compilador\src\analizadorSintactico\gramatica.y"

private AnalizadorLexico analizadorLexico;


private int yylex() {
}

private void yyerror(String string) {
	System.out.println(string);
}


//#line 363 "Parser.java"
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



}
//################### END OF CLASS ##############################
