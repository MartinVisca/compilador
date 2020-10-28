%{

package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;
import analizadorLexico.matrices.MatrizTransicionEstados;

import java.util.Hashtable;
import java.util.Vector;

import compilador.AnalizadorLexico
import compilador.Token

%}


%token ID CTE IF ELSE THEN END_IF OUT FUNC RETURN FOR LONGINT FLOAT UP DOWN NI REF PROC CADENA
%start programa

%%

programa : bloque
         ;


bloque : bloque_comun
	   | bloque bloque_comun
	   ;

bloque_comun : declaracion_proc
		   | declaracion
		   | bloque_control
		   ;

bloque_control : '{' bloque_sentencias '}' {System.out.println('Bloque de sentencias');}
               |  bloque_sentencias '}'  {System.out.println('Error Bloque de sentencias : falta '{');}
               | '{' bloque_sentencias {System.out.println('Error Bloque de sentencias : falta '}');}
               ;

bloque_sentencias : sentencias
                  | bloque_sentencias sentencias
                  ;

declaracion : tipo lista_variables';' {System.out.println('Declaracion de variables');}
            ;



lista_variables : ID
                | lista_variables ',' ID
                ;

declaracion_proc : encabezado_proc bloque_comun
                 ;

encabezado_proc : PROC ID '(' lista_parametros ')' NI '=' CTE
                | PROC ID '(' ')' NI '=' CTE
                ;

lista_parametros : tipo ID
 | REF tipo ID
 | lista_parametros ',' tipo ID
 | lista_parametros ',' REF tipo ID
 ;

sentencias : sentencia_unica_control
           | sentencia_unica_ejecutable
           ;

sentencia_unica_control : sentencia_if
                        | sentencia_for
                        ;

sentencia_unica_ejecutable : asignacion
                           | salida
                           | llamado_proc
                           ;

sentencia_if : IF '(' condicion ')' cuerpo_if  {System.out.println('');}
             ;

cuerpo_if : bloque_control END_IF';' {System.out.println('');}
          | bloque_control ELSE bloque_control END_IF';' {System.out.println('');}

sentencia_for : FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' bloque_control
              ;

condicion : condicion comparador expresion
          | expresion comparador expresion
          ;

asignacion : ID '=' expresion ';' {System.out.println('');}
           ;

llamado_proc : ID '(' ')'';' {System.out.println('llamada a procedimiento vacio');}
             | ID '(' parametros ')'';' {System.out.println('llamada a procedimiento con parametros');}
             ;

parametros : ID ':' ID
           | parametros ',' ID ':' ID
           ;

salida : OUT '(' CADENA ')'';' {System.out.println('Salida');}
       ;

expresion : expresion '+' termino
          | expresion '-' termino
          | termino
          ;

termino : termino '*' factor
        | termino '/' factor
        | factor
        ;

factor : ID
       | CTE {if .getTipo.equals(“LONGINT”) < 32768  }
 |'-'CTE {if .getTipo.equals(“LONGINT”)  }
       ;

tipo : LONGINT
     | FLOAT
     ;

comparador : '<'
           | '>'
           | '<='
           | '>='
           | '=='
           | '!='
           ;


incr_decr : UP
          | DOWN
          ;

%%

private AnalizadorLexico analizadorLexico;

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


