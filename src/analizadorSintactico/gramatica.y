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
       | bloque sentencias
       ;

bloque_sentencias : '{' bloque '}'
                  | sentencias
                  | bloque '}' error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '{' al abrir el bloque."); }
                  | '{' bloque error        { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '}' al cerrar el bloque."); }
                  ;

// Para el FOR
bloque_sentencias_ejecutables : '{' sentencias_solo_ejecutables '}'
                              | sentencias_ejecutables
                              ;

sentencias_solo_ejecutables : sentencias_ejecutables
                            | sentencias_ejecutables sentencias_solo_ejecutables

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_variables';' { sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
                        | tipo lista_variables error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la declaracion."); }
                        | tipo error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el/los identificadores."); }
                        | ID ';' error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el tipo de la variable."); }
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
                        | declaracion_proc bloque error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el '}' que cierra el cuerpo del procedimiento."); sintactico.setErrorProc(true);}
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
                     | '=' error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la palabra reservada NI."); sintactico.setErrorProc(true);}
                     | NI CTE error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el simbolo '=' del control de invocaciones."); sintactico.setErrorProc(true); }
                     | NI '=' '{' error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el numero de invocaciones."); sintactico.setErrorProc(true); }
                     | '{' error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el control de invocaciones."); sintactico.setErrorProc(true); }
                     | NI '=' CTE error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta el '{' al abrir el cuerpo del procedimiento."); sintactico.setErrorProc(true); }
                     ;

modificador :
            | REF
            ;

lista_parametros_formales : ')'     // Sin parametros
                          // 1 parametro
                          | modificador tipo ID ')'
                          | modificador ID error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del parametro formal no declarado."); sintactico.setErrorProc(true); }
                          // 2 parametros
                          | modificador tipo ID ',' modificador tipo ID ')'
                          | modificador tipo ID ',' modificador ID error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del segundo parametro formal no declarado."); sintactico.setErrorProc(true); }
                          // 3 parametros
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ')'
                          | modificador tipo ID ',' modificador tipo ID ',' modificador ID error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): tipo del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): identificador del tercer parametro formal no declarado"); sintactico.setErrorProc(true); }
                          // Mas de 3 parametros
                          | modificador tipo ID ',' modificador tipo ID ',' modificador tipo ID ',' error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la cantidad de parametros formales del procedimiento fue excedida."); sintactico.setErrorProc(true); }
                          ;

lista_variables : ID
                | lista_variables ',' ID
                | lista_variables ID error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la ',' para separar la lista de variables."); System.out.println($1.ival);}
                | tipo error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el tipo de la variable ya fue declarado."); }
                ;

sentencias_ejecutables : asignacion
                       | salida
                       | invocacion_proc
                       | sentencia_if
                       | sentencia_for
                       ;

asignacion : ID '=' expresion ';'   { sintactico.agregarAnalisis("Se reconocio una asignacion. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca(sintactico.getLexemaFromTS($1.ival));
                                      sintactico.agregarAPolaca("=");
                                    }
           | ID '=' expresion error { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la asignacion."); }
           | ID expresion error { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta operador '=' en la asignacion."); }
           | ID '=' error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta expresion en la asignacion."); }
           ;

salida : OUT '(' CADENA ')' ';'    { sintactico.agregarAnalisis("Se reconocio una salida por pantalla. (Linea " + AnalizadorLexico.linea + ")");
                                     sintactico.agregarAPolaca(sintactico.getLexemaFromTS($3.ival));
                                     sintactico.agregarAPolaca("OUT");
                                   }
       | OUT '(' CADENA ')' error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta ';' al final de la salida por pantalla."); }
       | OUT '(' CADENA error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta ')' en la declaracion de la salida por pantalla."); }
       | OUT CADENA error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta '(' en la declaracion de la salida por pantalla."); }
       | '(' CADENA error    {sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): se esperaba OUT, se encontro '('."); }
       | OUT '(' ')' error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar una cadena entre los parentesis para poder imprimir."); }
       ;

invocacion_proc : ID '(' parametros ';'    { sintactico.agregarAnalisis("Se reconocio una invocacion a procedimiento. (Linea " + AnalizadorLexico.linea + ")"); }
                | '(' parametros ';'    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta declarar el identificador del procedimiento a invocar."); }
                | ID '(' parametros error    { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + (AnalizadorLexico.linea - 1) + "): falta el ';' al final de la invocacion."); }
                ;

parametros : ')'
           | ID ':' ID ')'
           | ID ':' ID ',' ID ':' ID ')'
           | ID ':' ID ',' ID ':' ID ',' ID ':' ID ')'
           | ',' error ')'   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           | ':' error ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           | ID ',' error ')'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): error en parametros formales."); }
           ;

sentencia_if : IF '(' condicion ')' cuerpo_if END_IF    { sintactico.agregarAnalisis("Se reconocio una sentencia IF. (Linea " + AnalizadorLexico.linea + ")"); }
             | IF '(' condicion ')' cuerpo_if error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); }
             | IF '(' condicion ')' cuerpo_if ELSE cuerpo_else END_IF     { sintactico.agregarAnalisis("Se reconocio una sentencia IF ELSE. (Linea " + AnalizadorLexico.linea + ")"); }
             | IF '(' condicion ')' cuerpo_if ELSE cuerpo_else error      { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta END_IF al final de la sentencia IF."); }
             | IF condicion error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan '(' en la condicion del IF."); }
             | IF '(' condicion cuerpo_if error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): faltan ')' en la condicion del IF."); }
             | IF '(' ')'  error     { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): falta la condicion del IF."); }
             ;

cuerpo_if : bloque_sentencias       { sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + (sintactico.getSizePolaca() + 2) + "]");
                                                                    sintactico.agregarAPolaca(" ");
                                                                    sintactico.pushElementoPila(sintactico.getSizePolaca() - 1);
                                                                    sintactico.agregarAPolaca("BI");
                                                                  }
          ;

cuerpo_else : bloque_sentencias     { sintactico.agregarAPolacaEnPos(sintactico.popElementoPila(), "[" + sintactico.getSizePolaca() + "]"); }
            ;

condicion : expresion comparador expresion
          | comparador error   { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
          | expresion comparador error  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): condicion invalida."); }
          ;

sentencia_for : FOR '(' ID '=' CTE ';'
                condicion_for ';' incr_decr CTE ')'     { sintactico.agregarAnalisis("Se reconocio una declaracion de FOR. (Linea " + AnalizadorLexico.linea + ")"); }
                bloque_sentencias_ejecutables       { sintactico.agregarAnalisis("Se reconocio un bloque de sentencias solo ejecutables. (Linea " + AnalizadorLexico.linea + ")"); }
              ;

condicion_for : ID comparador expresion     { sintactico.agregarAnalisis("Se reconocio la condicion de corte del FOR. (Linea " + AnalizadorLexico.linea + ")"); }
              ;

expresion : expresion '+' termino   { sintactico.agregarAnalisis("Se reconocio una suma. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca("+");}
          | expresion '-' termino   { sintactico.agregarAnalisis("Se reconocio una resta. (Linea " + AnalizadorLexico.linea + ")");
                                      sintactico.agregarAPolaca("-");}
          | termino
          ;

termino : termino '*' factor    { sintactico.agregarAnalisis("Se reconocio una multiplicacion. (Linea " + AnalizadorLexico.linea + ")");
                                  sintactico.agregarAPolaca("*");}
        | termino '/' factor    { sintactico.agregarAnalisis("Se reconocio una division. (Linea " + AnalizadorLexico.linea + ")");
                                  sintactico.agregarAPolaca("/");}
        | factor
        ;

factor : ID
       | CTE    {
                    String tipo = sintactico.getTipoFromTS($1.ival);
                    if (tipo.equals("LONGINT"))
                        sintactico.verificarRangoEnteroLargo($1.ival);
                    sintactico.agregarAPolaca(sintactico.getLexemaFromTS($1.ival));
                }
       | '-' CTE    {
                        String tipo = sintactico.getTipoFromTS($2.ival);
                        if (tipo.equals("FLOAT"))
                            sintactico.verificarRangoFloat($2.ival);
                    }
       ;

comparador : '<'    { $$.sval = new String("<"); }
           | '>'    { $$.sval = new String(">"); }
           | MENORIGUAL     { $$.sval = new String("<="); }
           | MAYORIGUAL     { $$.sval = new String(">="); }
           | IGUAL      { $$.sval = new String("=="); }
           | DISTINTO   { $$.sval = new String("!="); }
           ;

incr_decr : UP   {$$.sval = new String("UP");}
          | DOWN {$$.sval = new String("DOWN");}
          ;

tipo : LONGINT {$$.sval = new String("LONGINT");}
     | FLOAT {$$.sval = new String("FLOAT");}
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