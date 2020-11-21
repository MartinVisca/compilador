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
                        | tipo lista_variables error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ';' al final de la declaracion."); }
                        | tipo error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el/los identificadores."); }
                        | ID ';' error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el tipo de la variable."); }
                        ;

lista_variables : ID
                | lista_variables ',' ID
                | lista_variables ID error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la ',' para separar la lista de variables."); System.out.println($1.ival);}
                | tipo error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue declarado."); }
                ;

sentencias_ejecutables : asignacion
                       ;

asignacion : ID '=' expresion ';'   { sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
           | ID '=' expresion error { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ';' al final de la asignacion."); }
           | ID expresion error { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
           | ID '=' error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
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
       | CTE    {  System.out.println("se detecto una cte");

                   }

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