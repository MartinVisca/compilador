package analizadorSintactico;

import accionSemantica.accionSemanticaSimple.ControlarRangoEnteroLargo;
import accionSemantica.accionSemanticaSimple.ControlarRangoFlotante;
import analizadorLexico.AnalizadorLexico;
import analizadorLexico.RegistroSimbolo;
import analizadorLexico.Token;

import javax.naming.ldap.Control;
import java.util.Stack;
import java.util.Vector;

public class AnalizadorSintactico {

    // Variables
    private boolean errorProc = false;  // Variable para determinar si se produjo un error cuando se declara un PROC
    private boolean errorIf = false;    // Variable para determinar si se produjo un error cuando se declara un IF
    private boolean errorInvocacion = false;    // Variable para determinar si se produjo un error en la invocacion de un PROC
    private String ambito = "main";     // Ambito principal del programa
    private String tipo = "";       // Guarda el tipo para que cuando se haga una declaración se actualice el tipo del identificador en la TS

    // Estructuras
    private AnalizadorLexico lexico;    // Se utiliza para obtener los tokens y poder verificar la sintaxis del codigo
    private Parser parser;      // Clase Parser generada por la herramienta yacc
    Vector<String> analisisSintactico;      // Contiene las detecciones correctas de reglas de la gramática
    Vector<String> listaErrores;        // Estructura que guarda los errores sintácticos
    private PolacaInversa polaca;       // Estructura de polaca inversa que servirá para poder generar el código assembler
    private Stack<Integer> pila;        // Pila para guardar los estados de la polaca inversa
    Vector<RegistroSimbolo> tablaSimbolos;      // Tabla de símbolos


    public AnalizadorSintactico(AnalizadorLexico lexico, Parser parser) {
        this.lexico = lexico;
        this.parser = parser;
        this.parser.setLexico(this.lexico);
        this.parser.setSintactico(this);
        this.analisisSintactico = new Vector<>();
        this.listaErrores = new Vector<>();
        this.polaca = new PolacaInversa();
        this.pila = new Stack<>();
        this.tablaSimbolos = lexico.getTablaSimbolos();
    }

    public boolean getErrorProc() { return errorProc; }

    public void setErrorProc(boolean errorProc) { this.errorProc = errorProc; }

    public boolean getErrorIf() { return errorIf; }

    public void setErrorIf(boolean errorIf) { this.errorIf = errorIf; }

    public boolean getErrorInvocacion() { return errorInvocacion; }

    public void setErrorInvocacion(boolean errorInvocacion) { this.errorInvocacion = errorInvocacion; }

    public void agregarAnalisis(String analisis) { this.analisisSintactico.add(analisis); }

    public void addErrorSintactico(String error) { this.listaErrores.add(error); }

    public void agregarAPolaca(String elemento) { this.polaca.addElemento(elemento); }

    // Método para agregar a la polaca inversa, en una posicion dada
    public void agregarAPolacaEnPos(int posicion, String elemento) { this.polaca.addElementoEnPosicion(elemento, posicion); }

    // Obtener el tamaño del arreglo de polaca inversa
    public int getSizePolaca() { return this.polaca.getSize(); }

    // Apila un elemento
    public void pushElementoPila(int valor) { this.pila.push(valor); }

    // Devuelve el elemento más alto de la pila
    public int popElementoPila() { return this.pila.pop(); }

    // Método para obtener el lexema de un token almacenado en la tabla de símbolos, dado su índice
    public String getLexemaFromTS(int indice) { return this.tablaSimbolos.get(indice).getLexema(); }

    // Método para obtener el tipo de un token almacenado en la tabla de símbolos, dado su índice
    public String getTipoFromTS(int indice) { return this.tablaSimbolos.get(indice).getTipoToken(); }

    // Método para obtener el uso de un token almacenado en la tabla de símbolos, dado su índice
    public String getUsoFromTS(int indice) { return this.tablaSimbolos.get(indice).getUso(); }

    // Método para obtener el tipo de variable de un token almacenado en la tabla de símbolos, dado su índice
    public String getTipoVariableFromTS(int indice) { return this.tablaSimbolos.get(indice).getTipoVariable(); }

    // Método para obtener un token de la tabla de símbolos dado su indice
    public RegistroSimbolo getRegistroFromTS(int indice) { return this.tablaSimbolos.get(indice); }

    // Método para modificar el uso de un elemento en la tabla de símbolos
    public void setUsoTablaSimb(int indice, String uso) { this.tablaSimbolos.get(indice).setUso(uso); }

    // Método para modificar el ámbito de un elemento en la tabla de símbolos
    public void setAmbitoTablaSimb(int indice) { this.tablaSimbolos.get(indice).setAmbito(this.tablaSimbolos.get(indice).getLexema() + "@" + this.ambito); }

    // Método para modificar el tipo (variable declarada en esta clase)
    public void setTipo(String nuevoTipo) { this.tipo = nuevoTipo; }

    // Método para modificar el tipo de variable de un elemento en la tabla de símbolos
    public void setTipoVariableTablaSimb(int indice) { this.tablaSimbolos.get(indice).setTipoVariable(this.tipo); }

    // Agrega el signo '-' a los números negativos en la tabla de símbolos
    public void setNegativoTablaSimb(int indice) { this.tablaSimbolos.get(indice).setLexema('-' + this.tablaSimbolos.get(indice).getLexema()); }

    // Método para verificar si un identificador ya fue declarado o no. Si fue declarado, agrega un error
    public boolean variableFueDeclarada(int referenciaATS) {
        String idAVerificar = this.tablaSimbolos.get(referenciaATS).getLexema();
        for (int i = 0; i < this.tablaSimbolos.size(); i++)
            // Si el id a verificar está en la tabla de símbolos y la entrada en la tabla de símbolos es distinta a la agregada
            if (this.tablaSimbolos.get(i).getLexema().equals(idAVerificar) && i != referenciaATS) {
                // Si tienen el mismo uso
                if (this.tablaSimbolos.get(i).getUso().equals(this.tablaSimbolos.get(referenciaATS).getUso()))
                    // Si el identificador se usa para definir una variable
                    if (this.tablaSimbolos.get(i).getUso().equals("VARIABLE"))
                        addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la variable ya fue declarada con ese nombre.");
                    else
                        addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): el procedimiento ya fue declarado con ese nombre.");
                else    // Si tienen distinto uso
                    if (this.tablaSimbolos.get(i).getUso().equals("VARIABLE"))
                        addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): ya existe un procedimiento con ese identificador.");
                    else
                        addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): ya existe una variable con ese identificador.");
                this.tablaSimbolos.remove(referenciaATS);   // Se elimina la entrada recién agregada
                return true;    // La variable ya fue declarada
            }

            return false;   // La variable aun no fue declarada
    }

    // Método para verificar el rango de un LONGINT positivo
    public void verificarRangoEnteroLargo(int indice) {
        String lexema = this.tablaSimbolos.get(indice).getLexema();
        Long numero = Long.parseLong(lexema);
        // Si el numero es positivo y es mayor que 2^31 - 1
        if (numero == ControlarRangoEnteroLargo.MAXIMO_LONG) {
            this.tablaSimbolos.remove(indice);    // Se elimina la entrada de la tabla de simbolos
            addErrorSintactico("ERROR SINTACTICO (Linea " + AnalizadorLexico.linea + "): la constante LONGINT esta fuera de rango");
        }
    }

    // Método para imprimir la tabla de simbolos luego del analisis sintactico
    public void imprimirTablaSimbolos() {
        System.out.println("\n");
        System.out.println("----------TABLA DE SIMBOLOS-----------");
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del simbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema() + " - Tipo Variable: " + simbolo.getTipoVariable() + " - Uso: " + simbolo.getUso());
        }
    }

    // Método para imprimir los errores léxicos
    public void imprimirErroresLexicos() {
        lexico.imprimirErrores();
    }

    // Método para imprimir los errores sintácticos
    public void imprimirErroresSintacticos() {
        System.out.println("\n");
        System.out.println("---------------------");
        System.out.println(" ERRORES SINTACTICOS");
        System.out.println("---------------------");
        if (this.listaErrores.isEmpty()) {
            System.out.println("---------------------");
            System.out.println("Ejecución sin errores");
        }

        else {
            for (String listaErrore : this.listaErrores)
                System.out.println(listaErrore);
        }
    }

    // Método para imprimir el análisis léxico
    public void imprimirAnalisisLexico() {
        System.out.println("----------ANALISIS LEXICO-----------");
        Vector<Token> tokens = this.lexico.getListaTokens();
        for (Token token : tokens) {
            System.out.println("----------------");
            System.out.println("Tipo token: " + token.getTipo());
            System.out.println("Lexema: " + token.getLexema());
        }
    }

    // Método para imprimir el análisis sintáctico
    public void imprimirAnalisisSintactico() {
        System.out.println("\n");
        System.out.println("----------ANALISIS SINTACTICO-----------");
        if (!analisisSintactico.isEmpty())
            for (String string : analisisSintactico)
                System.out.println(string);
        else
            System.out.println("Analisis sintactico vacio.");
    }

    public void imprimirPolaca() {
        System.out.println("----------POLACA INVERSA-----------");
        if (!polaca.esVacia())
            polaca.imprimirPolaca();
        else
            System.out.println("Polaca vacia");
    }

    public void start() {
        if (parser.yyparse() == 0) {
            System.out.println("Parser finalizo");
            imprimirAnalisisSintactico();
            imprimirTablaSimbolos();
        }
        else
            System.out.println("Parser no finalizo");
        lexico.imprimirErrores();
        imprimirPolaca();
        imprimirErroresSintacticos();
        lexico.setPosArchivo(0);
        lexico.setBuffer("");
    }

}
