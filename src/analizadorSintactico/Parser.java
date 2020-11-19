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
public final static short MENORIGUAL=275;
public final static short MAYORIGUAL=276;
public final static short IGUAL=277;
public final static short DISTINTO=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    3,    3,    5,    7,    8,
    9,   10,   10,   11,   11,   11,   11,    6,   13,   13,
   14,   14,    4,    4,    4,    4,    4,    4,   15,   15,
   16,   16,   16,   16,   17,   17,   21,   21,   18,   19,
   20,   20,   25,   25,   24,   24,   24,   26,   26,   26,
   27,   27,   27,   23,   23,   23,   23,   23,   23,   22,
   22,   12,   12,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    1,    1,    1,    3,    2,    3,
    1,    7,    6,    2,    3,    4,    5,    3,    1,    3,
    1,    2,    1,    1,    1,    1,    1,    1,    7,    9,
    9,   11,   11,   13,   12,   14,    3,    3,    4,    5,
    4,    5,    3,    5,    3,    3,    1,    3,    3,    1,
    1,    1,    2,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   62,   63,   11,    0,    0,    2,
    4,    5,    6,    7,    0,    0,    0,   23,   24,   25,
   26,   27,   28,    0,    0,    0,    0,    0,    3,    0,
    0,    0,    9,   19,    0,    0,    0,    0,   51,   52,
    0,    0,    0,   50,    0,    0,    0,    0,    0,    8,
    0,   18,    0,    0,   41,    0,    0,   53,   39,    0,
    0,    0,    0,   56,   57,   58,   59,    0,   54,   55,
    0,    0,    0,    0,   10,    0,    0,    0,    0,   20,
   43,   42,    0,    0,    0,   48,   49,    0,    0,    0,
    0,   40,    0,    0,    0,    0,    0,   14,    0,    0,
    0,    0,    0,   15,    0,    0,    0,    0,   44,    0,
    0,    0,   29,    0,   13,    0,    0,   16,    0,    0,
    0,    0,    0,   12,   17,    0,    0,   30,    0,   31,
   60,   61,    0,    0,    0,    0,    0,    0,   33,   32,
    0,    0,    0,   21,    0,   35,   34,    0,   22,   36,
};
final static short yydgoto[] = {                          8,
    9,   10,   11,   12,   13,   14,   15,   31,   16,   33,
   78,   17,   35,  145,   18,   19,   20,   21,   22,   23,
   45,  133,   71,   46,   38,   43,   44,
};
final static short yysindex[] = {                      -185,
    3,  -24,   20,   26,    0,    0,    0,    0, -185,    0,
    0,    0,    0,    0,  -91, -156, -149,    0,    0,    0,
    0,    0,    0,  -38,  -37,  -37, -179, -147,    0, -185,
   55,   73,    0,    0,  -14,   57,   58,   48,    0,    0,
 -142,   16,   44,    0,  -16,   -8,   77,   59,  -70,    0,
  -40,    0, -138, -135,    0,   65, -132,    0,    0,  -37,
  -37,  -37,  -37,    0,    0,    0,    0, -117,    0,    0,
  -37,  -37,   67, -131,    0, -191, -143,   52, -128,    0,
    0,    0,   72,   44,   44,    0,    0, -185, -175,   54,
   54,    0,   74, -126,   71, -137, -188,    0, -122,  -58,
  -96,   78,  -37,    0, -120,   80, -191, -114,    0, -162,
 -185, -118,    0,  -12,    0, -113, -110,    0,  -83,   89,
   14,   94, -165,    0,    0, -185, -108,    0, -107,    0,
    0,    0,  116,   27,   99,  100,  -33, -102,    0,    0,
 -201,  103,  106,    0,   39,    0,    0,  107,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  168,    0,
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
  -17,    2,    0,  -88,    0,    0,    0,    0,    0,    0,
    0,  -34,    0,    0,    0,    0,    0,    0,    0,    0,
   66,    0,  127,   -3,    0,   46,   49,
};
final static int YYTABLESIZE=305;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   77,   47,   37,   47,   45,   88,   45,   41,   45,   46,
   29,   46,   49,   46,   37,   26,   79,   47,   47,   38,
   47,   42,   45,   45,   68,   45,  111,   46,   46,   53,
   46,   30,   37,   37,   60,   37,   61,   38,   38,  126,
   38,   94,   24,   69,   52,   70,  123,   69,  142,   70,
   29,   69,  144,   70,   75,    1,  149,    2,   60,   27,
   61,    3,  108,   25,    4,   28,  110,   90,   91,   89,
  100,    1,  117,    2,   59,    5,    6,    3,    5,    6,
    4,    5,    6,  107,  101,   62,  102,    7,   56,  141,
   63,   57,   96,  121,   47,   97,   60,  119,   61,  120,
   32,   29,  112,  131,  132,   84,   85,   34,  134,   48,
   86,   87,   51,   50,   54,   58,   55,   73,   80,   74,
  127,   81,   29,   82,   83,   92,   93,   95,   98,   99,
  104,  105,  103,  106,  109,   29,  113,  115,  129,    1,
  116,    2,  118,  122,  124,    3,  125,  128,    4,    5,
    6,  138,  130,  135,  136,    7,  137,  139,  140,  143,
    1,  146,    2,  148,  147,  150,    3,    1,  114,    4,
    5,    6,   72,    1,    0,    2,    7,    0,    0,    3,
    0,    0,    4,    5,    6,    0,    1,    0,    2,    7,
    0,    0,    3,    0,    0,    4,    5,    6,    1,    0,
    2,    0,    7,    0,    3,    0,    0,    4,    5,    6,
    0,    0,    0,    0,    7,    0,    0,    0,   36,   39,
   40,    0,    0,    1,    0,    2,    5,    6,    0,    3,
    0,   76,    4,   47,   47,   47,   47,    0,   45,   45,
   45,   45,    0,   46,   46,   46,   46,    0,   37,   37,
   37,   37,    0,   38,   38,   38,   38,    0,   64,   65,
   66,   67,   64,   65,   66,   67,   64,   65,   66,   67,
    1,    0,    2,    0,    0,    0,    3,    0,    0,    4,
    5,    6,    0,    1,    0,    2,    7,    0,    0,    3,
    0,    0,    4,    5,    6,    1,    0,    2,    0,    7,
    0,    3,    0,    0,    4,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   41,   43,   41,   45,   41,  123,   43,   45,   45,   41,
    9,   43,   30,   45,   41,   40,   51,   59,   60,   41,
   62,   25,   59,   60,   41,   62,  123,   59,   60,   44,
   62,  123,   59,   60,   43,   62,   45,   59,   60,  123,
   62,   76,   40,   60,   59,   62,   59,   60,  137,   62,
   49,   60,  141,   62,  125,  257,  145,  259,   43,   40,
   45,  263,   97,   61,  266,   40,  125,   71,   72,   68,
   88,  257,  107,  259,   59,  267,  268,  263,  267,  268,
  266,  267,  268,  272,  260,   42,  262,  273,   41,  123,
   47,   44,   41,  111,  274,   44,   43,  260,   45,  262,
  257,  100,  101,  269,  270,   60,   61,  257,  126,  257,
   62,   63,   40,   59,   58,  258,   59,   41,  257,   61,
  119,  257,  121,   59,  257,   59,  258,  271,  257,   58,
  257,   61,   59,  271,  257,  134,   59,  258,  125,  257,
   61,  259,  257,  262,  258,  263,  257,   59,  266,  267,
  268,  125,   59,  262,  262,  273,   41,   59,   59,  262,
  257,   59,  259,  125,   59,   59,  263,    0,  103,  266,
  267,  268,   46,  257,   -1,  259,  273,   -1,   -1,  263,
   -1,   -1,  266,  267,  268,   -1,  257,   -1,  259,  273,
   -1,   -1,  263,   -1,   -1,  266,  267,  268,  257,   -1,
  259,   -1,  273,   -1,  263,   -1,   -1,  266,  267,  268,
   -1,   -1,   -1,   -1,  273,   -1,   -1,   -1,  257,  257,
  258,   -1,   -1,  257,   -1,  259,  267,  268,   -1,  263,
   -1,  272,  266,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,   -1,  275,  276,  277,  278,   -1,  275,  276,
  277,  278,  275,  276,  277,  278,  275,  276,  277,  278,
  257,   -1,  259,   -1,   -1,   -1,  263,   -1,   -1,  266,
  267,  268,   -1,  257,   -1,  259,  273,   -1,   -1,  263,
   -1,   -1,  266,  267,  268,  257,   -1,  259,   -1,  273,
   -1,  263,   -1,   -1,  266,
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
"MENORIGUAL","MAYORIGUAL","IGUAL","DISTINTO",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque",
"bloque : sentencias",
"bloque : bloque sentencias",
"sentencias : sentencias_declarativas",
"sentencias : sentencias_ejecutables",
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
"sentencia_if : IF '(' condicion ')' sentencias END_IF ';'",
"sentencia_if : IF '(' condicion ')' '{' bloque '}' END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' sentencias ELSE sentencias END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' sentencias ELSE '{' bloque '}' END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' '{' bloque '}' ELSE sentencias END_IF ';'",
"sentencia_if_else : IF '(' condicion ')' '{' bloque '}' ELSE '{' bloque '}' END_IF ';'",
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
"comparador : MENORIGUAL",
"comparador : MAYORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"incr_decr : UP",
"incr_decr : DOWN",
"tipo : LONGINT",
"tipo : FLOAT",
};


private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setAnalizadorLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setAnalizadorSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico; }

private int yylex() {
    int token = lexico.yylex();
    if (lexico.getRefTablaSimbolos() != -1)
      yyval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

private void yyerror(String string) {
	System.out.println(string);
}


//#line 385 "Parser.java"
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
case 18:

{System.out.println("Declare una variable");}
break;
//#line 538 "Parser.java"
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
