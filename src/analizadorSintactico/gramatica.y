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

bloque : sentencias
       | sentencias bloque
       ;

bloque_sentencias : '{' bloque '}'
                  | sentencias
                  ;

// Para el FOR
bloque_sentencias_ejecutables : '{' sentencias_solo_ejecutables '}'
                              | sentencias_ejecutables
                              ;

sentencias_solo_ejecutables : sentencias_ejecutables
                            | sentencias_ejecutables sentencias_solo_ejecutables

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           | ';'    { sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): ';' sin sentencia declarada."); }
           ;

sentencias_declarativas : tipo lista_variables';' { sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
                        | tipo ';'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro un identificador para la variable."); }
                        | ID error ';' {    // Tratamiento de errores para declaracion de variables
                                            //RegistroSimbolo aux = sintactico.getElemTablaSimb($1.ival);
                                            //if (!aux.getUso().equals("PROC"))
                                                //sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro el tipo de la variable.");
                                       }
                        | declaracion_proc bloque '}'   { if (!sintactico.getErrorProc()) {
                                                               sintactico.agregarAnalisis("Se reconocio un procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                               // Agregar polaca
                                                               // Actualizar ambito
                                                          }
                                                          else {
                                                               sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): declaracion de procedimiento invalida.");
                                                               sintactico.setErrorProc(true);
                                                          }
                                                        }
                        | declaracion_proc '}'  { sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea + "): el cuerpo del procedimiento es vacio."); }
                        ;

declaracion_proc : PROC ID  {   // sintactico.setUsoTablaSimb($2.ival, "PROC");
                                // RegistroSimbolo aux = lexico.getElemTablaSimb($2.ival);
                                // String nombre_procedimiento = aux.getLexema();
                                // sintactico.modificarAmbito($2.ival)
                                // modificarAmbito hace que tablaSimbolos.get(indice).setAmbito("/" + ambito)
                                //if(sintactico.variableFueDeclarada($2.ival))
                                   // sintactico.setErrorProc(true);
                                // sino actualizar el ambito del sintactico
                            }
                   '(' lista_parametros_formales control_invocaciones   { if (sintactico.getErrorProc() == false)
                                                                                sintactico.agregarAnalisis("Se reconocio una declaracion de procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                                        }
                 | PROC '(' error '{' { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el identificador del procedimiento");
                                        sintactico.setErrorProc(true);
                                      }
                 | PROC ID control_invocaciones
                 ;

control_invocaciones : NI '=' CTE '{'
                     | error '=' CTE '{'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI"); }
                     | NI error CTE '{'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones"); }
                     | error '{'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones"); sintactico.setErrorProc(true); }
                     ;

modificador :
            | REF
            ;

lista_parametros_formales : ')'     // Sin parametros
                          // 1 parametro
                          | modificador tipo ID ')'
                          | modificador ID error ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo error ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
                          // 2 parametros
                          | modificador tipo ID ',' modificador tipo ID ')'
                          | modificador ID error ',' modificador tipo ID ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo error  ',' modificador tipo ID ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador ID error ')'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo error ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
                          // 3 parametros
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ')'
                          | modificador ID error ',' modificador tipo ID ',' modificador tipo ID ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo error ',' modificador tipo ID ',' modificador tipo ID ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del primer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador error ID ',' modificador tipo ID ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo error ',' modificador tipo ID ')'      { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo ID ',' modificador error ID ')'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo error ')'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          // Mas de 3 parametros
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador tipo error ')'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador error ID ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' modificador error ')'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
                          ;

lista_variables : ID
                | ID ',' lista_variables
                | ID lista_variables error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en la declaracion de variable. Falta el caracter separador ','."); }
                | tipo error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue definido."); }
                ;

sentencias_ejecutables : sentencia_if
                       | sentencia_for
                       | asignacion
                       | salida
                       | invocacion_proc
                       ;

sentencia_if : IF '(' condicion ')' bloque_sentencias END_IF    { sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
             | IF '(' condicion ')' bloque_sentencias ELSE bloque_sentencias END_IF     { sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
             | IF '(' condicion ')' bloque_sentencias ELSE error END_IF     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en el cuerpo del ELSE."); }
             | IF condicion THEN error END_IF   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan los parentesis en la condicion del IF."); }
             | IF '(' ')'  error  END_IF     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); }
             ;

cuerpo_if | bloque_sentencias
          ;

cuerpo_else | bloque_sentencias
            ;

sentencia_for : FOR '(' ID '=' CTE ';'
                condicion_for ';' incr_decr CTE ')'     { sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
                bloque_sentencias_ejecutables       { sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
              ;

condicion_for : ID comparador expresion     { sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }

condicion : expresion comparador expresion
          | error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
          ;

asignacion : ID '=' expresion ';'   { sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")"); }
           | ID '=' error ';'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la asignacion no tiene expresion asignada."); }
           | ID expresion error ';'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
           | ID ';'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el operador '=' y la asignacion no tiene expresion asignada."); }
           ;

salida : OUT '(' CADENA ')' ';'    { sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")"); }
       | OUT '(' CADENA error ';'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
       | OUT CADENA error ';'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
       | '(' CADENA error ';'    {sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
       | OUT '(' error ';'     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
       ;

invocacion_proc : ID '(' parametros ';'    { sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
                | '(' parametros ';'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
                | ID '(' parametros error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el ';' al final de la invocacion."); }
                ;

parametros : ')'
           | ID ':' ID ')'
           | ID ':' ID ',' ID ':' ID ')'
           | ID ':' ID ',' ID ':' ID ',' ID ':' ID ')'
           | ',' error ')'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           | ':' error ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           | ID ',' error ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           ;

expresion : expresion '+' termino   { sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")"); }
          | expresion '-' termino   { sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")"); }
          | termino
          ;

termino : termino '*' factor    { sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")"); }
        | termino '/' factor    { sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")"); }
        | factor
        ;

factor : ID
       | CTE    {
                    String tipo = sintactico.getTipoElemTablaSimb($1.ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo($1.ival);
                }
       | '-' CTE    {
                        String tipo = sintactico.getTipoElemTablaSimb($2.ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat($2.ival);
                    }
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