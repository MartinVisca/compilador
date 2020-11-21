%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorSintactico.AnalizadorSintactico;
import analizadorLexico.RegistroSimbolo;

%}


%token ID CTE IF ELSE THEN END_IF OUT FUNC RETURN FOR LONGINT FLOAT UP DOWN NI REF PROC CADENA MENORIGUAL MAYORIGUAL IGUAL DISTINTO
%start programa

%%

programa : bloque   { sintactico.agregarAnalisis("Se reconocio un programa. (Linea " + AnalizadorLexico.linea + ")"); }
         ;

bloque : sentencias     { System.out.println("Se reconocio un bloque"); }
       | sentencias bloque      { System.out.println("Se reconocio un bloque"); }
       ;

sentencias : sentencias_ejecutables     { System.out.println("Se reconocio una sentencia ejecutable"); }
           | sentencias_declarativas      {  System.out.println("Se reconocio una sentencia declarativa");   }
           ;

sentencias_declarativas : tipo lista_variables';' { sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
                        ;

lista_variables : ID
                | ID ',' lista_variables
                ;

sentencias_ejecutables : asignacion
                       ;

asignacion : ID '=' expresion ';'   { sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
           ;

expresion : expresion '+' termino   { sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
          | expresion '-' termino   { sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
          | termino
          ;

termino : termino '*' factor    { sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
        | termino '/' factor    { sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
        | factor
        ;

factor : ID     { System.out.println("Se detecto un ID"); }
       | CTE    {  System.out.println("se detecto una cte"); }
       | '-' CTE    {  System.out.println("Se detecto una cte negativa");}
       ;

tipo : LONGINT
     | FLOAT
     ;

%%

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