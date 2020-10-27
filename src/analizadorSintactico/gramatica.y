%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

%}


%token ID CTE IF ELSE THEN END_IF OUT FUNC RETURN FOR LONGINT FLOAT UP DOWN NI REF PROC CADENA
%start programa

%%

programa : bloque
         ;

bloque : sentencias_declarativas
       | sentencias_ejecutables
       | bloque sentencias_declarativas
       | bloque sentencias_ejecutables
       ;

sentencias_declarativas : declaracion_metodo
                        | declaracion_variable
                        ;

declaracion_metodo : encabezado_metodo cuerpo_metodo';'
                   ;

encabezado_metodo : tipo_resultado declarador_metodo
                  ;

cuerpo_metodo : '{' bloque '}'

tipo_resultado : PROC
               ;

declarador_metodo : ID '(' lista_parametros_formales ')' NI '=' CTE
                  | ID '(' ')' NI '=' CTE
                  ;

lista_parametros_formales : tipo ID
                          | REF tipo ID
                          | lista_parametros_formales ',' tipo ID
                          | lista_parametros_formales ',' REF tipo ID
                          ;

declaracion_variable : tipo lista_variables';'
                     ;

lista_variables : ID
                | lista_variables ',' ID
                ;

bloque_sentencias_ejecutables : sentencias_ejecutables
                              | bloque_sentencias_ejecutables sentencias_ejecutables
                              ;

sentencias_ejecutables : sentencia_if
                       | sentencia_if_else
                       | sentencia_for
                       | asignacion
                       | salida
                       | invocacion_proc
                       ;

sentencia_if : IF '(' condicion ')' sentencias_ejecutables END_IF';'
             | IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' END_IF';'
             ;

sentencia_if_else : IF '(' condicion ')' sentencias_ejecutables ELSE sentencias_ejecutables END_IF';'
                  | IF '(' condicion ')' sentencias_ejecutables ELSE '{' bloque_sentencias_ejecutables '}' END_IF';'
                  | IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' ELSE sentencias_ejecutables END_IF';'
                  | IF '(' condicion ')' '{' bloque_sentencias_ejecutables '}' ELSE '{' bloque_sentencias_ejecutables'}' END_IF';'
                  ;

sentencia_for : FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' sentencias_ejecutables';'
              | FOR '(' ID '=' CTE ';' condicion ';' incr_decr ')' '{' bloque_sentencias_ejecutables '}'';'

condicion : condicion comparador expresion
          | expresion comparador expresion
          ;

asignacion : ID '=' expresion ';'
           ;

salida : OUT '(' CADENA ')'';'
       ;

invocacion_proc : ID '(' ')'';'
                | ID '(' parametros ')'';'
                ;

parametros : ID ':' ID
           | parametros ',' ID ':' ID
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
       | CTE
       | '-' CTE
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

tipo : LONGINT
     | FLOAT
     ;

%%

private AnalizadorLexico analizadorLexico;


private int yylex() {
}

private void yyerror(String string) {
	System.out.println(string);
}


