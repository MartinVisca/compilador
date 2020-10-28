%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

%}


%token ID CTE IF ELSE THEN END_IF OUT FUNC RETURN FOR LONGINT FLOAT UP DOWN NI REF PROC CADENA MENORIGUAL MAYORIGUAL IGUAL DISTINTO
%start programa

%%

programa : bloque
         ;

bloque : sentencias
       | bloque sentencias
       ;

sentencias : sentencias_declarativas
           | sentencias_ejecutables
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

sentencia_if : IF '(' condicion ')' sentencias END_IF';'
             | IF '(' condicion ')' '{' bloque '}' END_IF';'
             ;

sentencia_if_else : IF '(' condicion ')' sentencias ELSE sentencias END_IF';'
                  | IF '(' condicion ')' sentencias ELSE '{' bloque '}' END_IF';'
                  | IF '(' condicion ')' '{' bloque '}' ELSE sentencias END_IF';'
                  | IF '(' condicion ')' '{' bloque '}' ELSE '{' bloque'}' END_IF';'
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
           | MENORIGUAL
           | MAYORIGUAL
           | IGUAL
           | DISTINTO
           ;

incr_decr : UP
          | DOWN
          ;

tipo : LONGINT
     | FLOAT
     ;

%%

private AnalizadorLexico analizadorLexico;
private Token token;

public

private int yylex() {

    this.token = analizadorLexico.getToken();

    yylval = new ParserVal(token.getLexema());

    if (token.getTipo().equals("ID") || token.getTipo().equals("CTE") || token.getTipo().equals("CADENA DE CARACTERES")
        return ;
    else
        if (token.getTipo().equals("OPERADOR ARITMETICO") || token.getTipo().equals("SIMBOLO DE PUNTUACION") || token.getTipo().equals("COMPARADOR") || token.getTipo().equals("ASIGNACION") || token.getTipo().equals("PALABRA RESERVADA"))
            return ;
    return ;

}

private void yyerror(String string) {
	System.out.println(string);
}


