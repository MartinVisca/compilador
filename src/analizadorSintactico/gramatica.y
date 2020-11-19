%{

package analizadorSintactico;

import java.util.Vector;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

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

sentencias : sentencias_declarativas
           | sentencias_ejecutables
           ;

sentencias_declarativas : tipo lista_variables';' { sintactico.agregarAnalisis("Se reconocio una declaracion de variable. (Linea " + AnalizadorLexico.linea + ")"); }
                        | tipo ';'  { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro un identificador para la variable.") }
                        | ID error ';' {    // Tratamiento de errores para declaracion de variables
                                            RegistroSimbolo aux = lexico.getElemTablaSimb($1.ival);
                                            if (!aux.getUso().equals("PROC"))
                                                sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): no se declaro el tipo de la variable.");
                                       }
                        | declaracion_proc bloque '}'   { if (!sintactico.getErrorProc()) {
                                                               sintactico.agregarAnalisis("Se reconocio una declaracion de procedimiento. (Linea " + AnalizadorLexico.linea + ")");
                                                               // Agregar polaca
                                                               // Actualizar ambito
                                                          }
                                                          else {
                                                               sintactico.addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): declaracion de procedimiento invalida.");
                                                               sintactico.setErrorProc(true);
                                                          }
                                                        }
                        | declaracion_proc '}'  { sintactico.addErrorSintactico("WARNING (Linea " + AnalizadorLexico.linea "): el cuerpo del procedimiento es vacio.") }
                        ;

declaracion_proc : PROC ID  {   lexico.modificarUsoTablaSimb($2.ival, "PROC");
                                RegistroSimbolo aux = lexico.getElemTablaSimb($2.ival);
                                String nombre_procedimiento = aux.getLexema();
                                Main.tablaSimbolos.get($2.ival)[0] += "/" + Main.ambito;
                                if(variable_redeclarada($2.ival))
                                   Main.errorProc = true;
                                else {
                                   Main.referenciaParametro = $2.ival;
                                   Main.ambito += "/" + name_proc;
                                 }

                            }
                 | PROC '(' error '{' { sintactico.addErrorSintactico("ERROR SINTACTICO (Linea");
                                        Main.errorProc = true;
                                      }
                 | PROC ID control_niveles
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

declaracion_variable : tipo lista_variables';' {System.out.println("Declare una variable");}
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

private AnalizadorLexico lexico;
private AnalizadorSintactico sintactico;

public void setLexico(AnalizadorLexico lexico) { this.lexico = lexico; }

public void setSintactico(AnalizadorSintactico sintactico) { this.sintactico = sintactico }

private int yylex() {
    token = lexico.yylex();
    if (lexico.getRefTablaSimbolos() != -1)
        yyval = new ParserVal(lexico.getRefTablaSimbolos());
    return token;
}

private void yyerror(String string) {
	System.out.println(string);
}


