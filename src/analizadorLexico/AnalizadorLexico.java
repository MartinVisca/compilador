package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;
import analizadorLexico.matrices.MatrizTransicionEstados;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class AnalizadorLexico {

    // Variables
    private String archivo;     // Código del archivo
    private String buffer = "";      // Buffer para leer el token actual
    private int posArchivo = 0;      // Índice que indica la posición del archivo que se está leyendo
    private int estadoActual = 0;       // Estado del autómata en el que me encuentro actualmente
    private int tokenActual = -1;        // ID Token que se está procesando actualmente
    private int refTablaSimbolos = -1;      // Número que indica el índice del token en la tabla de símbolos. Sirve para asignarselo al yylval en el Parser
    private boolean codigoLeido = false;        // Variable para verificar si se terminó de leer el código
    public static int linea = 1;    // Referencia a la línea de código. Comienza en 1

    // Estructuras
    private Vector<RegistroSimbolo> tablaSimbolos;      // Tabla de símbolos. Luego se pasará al Sintáctico
    private Vector<Token> listaTokens;      // Tokens resultantes del análisis léxico
    private ArrayList<String> listaErrores;     // Lista de errores léxicos. Luego se pasará al Sintáctico para agregar los errores que detecte
    private MatrizTransicionEstados matrizEstados;      // Matriz de transición de estados
    private MatrizAccionesSemanticas matrizAccionesSemanticas;      // Matriz de acciones semánticas
    private HashMap<String, Integer> idTokens;     // Contiene los id de los tokens definidos. Estructura <Token, [ID, Tipo]

    /*** CONSTRUCTOR ***/

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.archivo = this.archivo.replaceAll("\r\n", "\n");  // Cambiamos los \r\n por \n si los hubiera
        this.archivo = this.archivo + "$";  // Agregamos el caracter EOF al final
        this.tablaSimbolos = new Vector<>();
        this.listaTokens = new Vector<>();
        this.listaErrores = new ArrayList<>();
        this.matrizEstados = new MatrizTransicionEstados();
        this.matrizAccionesSemanticas = new MatrizAccionesSemanticas(15, 27);
        this.idTokens = new HashMap<>();

        /** CARGA DE TOKENS **/
        //Operadores aritméticos
        this.idTokens.put("+", (int) '+');
        this.idTokens.put("-", (int) '-');
        this.idTokens.put("*", (int) '*');
        this.idTokens.put("/", (int) '/');

        //Símbolos de puntuación
        this.idTokens.put("(", (int) '(');
        this.idTokens.put(")", (int) ')');
        this.idTokens.put("{", (int) '{');
        this.idTokens.put("}", (int) '}');
        this.idTokens.put(".", (int) '.');
        this.idTokens.put(",", (int) ',');
        this.idTokens.put(";", (int) ';');
        this.idTokens.put(":", (int) ':');

        //Comparadores
        this.idTokens.put(">", (int) '>');
        this.idTokens.put("<", (int) '<');
        this.idTokens.put("==", 277);
        this.idTokens.put("!=", 278);
        this.idTokens.put(">=", 276);
        this.idTokens.put("<=", 275);

        //Asignación
        this.idTokens.put("=", (int) '=');

        //Identificador y constante
        this.idTokens.put("ID", 257);
        this.idTokens.put("CTE", 258);

        //Palabras reservadas
        this.idTokens.put("IF", 259);
        this.idTokens.put("ELSE", 260);
        this.idTokens.put("THEN", 261);
        this.idTokens.put("END_IF", 262);
        this.idTokens.put("OUT", 263);
        this.idTokens.put("FUNC", 264);
        this.idTokens.put("RETURN", 265);
        this.idTokens.put("FOR", 266);
        this.idTokens.put("LONGINT", 267);
        this.idTokens.put("FLOAT", 268);
        this.idTokens.put("UP", 269);
        this.idTokens.put("DOWN", 270);
        this.idTokens.put("NI", 271);
        this.idTokens.put("REF", 272);
        this.idTokens.put("PROC", 273);

        // Cadena de caracteres
        this.idTokens.put("CADENA", 274);

        /** ACCIONES SEMÁNTICAS **/
        // AS1 -> Inicializar buffer y agregar caracter al buffer
        AccionSemanticaSimple AS1 = new InicializarBuffer(this);

        // AS2 -> Agregar caracter al buffer
        AccionSemanticaSimple AS2 = new AgregarCaracter(this);

        // AS3 -> Agregar caracter al buffer (caracter literal - '/')
        AccionSemanticaSimple AS3 = new DevolverLiteral(this);

        // AS4 -> Controlar si es palabra reservada
        AccionSemanticaSimple AS4 = new ControlarPalabraReservada(this);

        // AS5 -> Controlar si es identificador. Si es identificador y se excede la longitud máxima, se trunca
        AccionSemanticaSimple AS5 = new ControlarIdentificador(this);

        // AS6 -> Controla si es palabra reservada o si es identificador. Sino devuelve error
        AccionSemanticaCompuesta AS6 = new AccionSemanticaCompuesta();
        AS6.addAccion(AS4);
        AS6.addAccion(AS5);

        // AS7 -> Controla el rango de los float. Si está en rango, lo agrega a la tabla de símbolos, sino, devuelve error
        AccionSemanticaSimple AS7 = new ControlarRangoFlotante(this);

        // AS8 -> Controla el rango de los enteros largos. Si está en rango, lo agrega a la tabla de símbolos, sino, devuelve error
        AccionSemanticaSimple AS8 = new ControlarRangoEnteroLargo(this);

        // AS9 -> Controla si el token es una cadena de caracteres y la agrega a la tabla de simbolos
        AccionSemanticaSimple AS9 = new ControlarCadena(this);

        // AS10 -> Devuelve el token asociado a los comparadores simples (>, <, =)
        AccionSemanticaSimple AS10 = new DevolverComparadorSimple(this);

        // AS11 -> Devuelve el token asociado a los comparadores compuestos (<=, >=, ==, !=)
        AccionSemanticaSimple AS11 = new DevolverComparadorCompuesto(this);

        // AS12 -> Devuelve el token asociado al literal '/'
        AccionSemanticaSimple AS12 = new DevolverBarra(this);

        // AS13 -> Descarta el buffer (lo pone en vacio)
        AccionSemanticaSimple AS13 = new DescartarBuffer(this);

        // AS14 -> Aumenta el valor de posArchivo para avanzar ignorando el caracter
        AccionSemanticaSimple AS14 = new AvanzarEnCodigo(this);

        // AS15 -> Comienzo de comentario. Descarta el caracter '/' y avanza en el código
        AccionSemanticaCompuesta AS15 = new AccionSemanticaCompuesta();
        AS15.addAccion(AS13);
        AS15.addAccion(AS14);

        /** MATRIZ DE ACCIONES SEMÁNTICAS **/

        /*0*/    this.matrizAccionesSemanticas.set(0,0,AS1) ; this.matrizAccionesSemanticas.set(0,1,AS1) ; this.matrizAccionesSemanticas.set(0,2,AS1) ; this.matrizAccionesSemanticas.set(0,3,null) ; this.matrizAccionesSemanticas.set(0,4,AS1) ; this.matrizAccionesSemanticas.set(0,5,AS1) ; this.matrizAccionesSemanticas.set(0,6,AS1) ; this.matrizAccionesSemanticas.set(0,7,AS1) ; this.matrizAccionesSemanticas.set(0,8,AS1) ; this.matrizAccionesSemanticas.set(0,9,AS1) ; this.matrizAccionesSemanticas.set(0,10,AS3) ; this.matrizAccionesSemanticas.set(0,11,AS3) ; this.matrizAccionesSemanticas.set(0,12,AS3) ; this.matrizAccionesSemanticas.set(0,13,AS1)  ; this.matrizAccionesSemanticas.set(0,14,null) ; this.matrizAccionesSemanticas.set(0,15,AS14) ; this.matrizAccionesSemanticas.set(0,16,AS3)  ; this.matrizAccionesSemanticas.set(0,17,AS3); this.matrizAccionesSemanticas.set(0,18,AS3) ; this.matrizAccionesSemanticas.set(0,19,AS3) ; this.matrizAccionesSemanticas.set(0,20,AS3) ; this.matrizAccionesSemanticas.set(0,21,AS3) ; this.matrizAccionesSemanticas.set(0,22,AS3) ; this.matrizAccionesSemanticas.set(0,23,AS3) ; this.matrizAccionesSemanticas.set(0,24,null) ; this.matrizAccionesSemanticas.set(0,25,AS14) ; this.matrizAccionesSemanticas.set(0,26,null);
        /*1*/    this.matrizAccionesSemanticas.set(1,0,null) ; this.matrizAccionesSemanticas.set(1,1,AS2) ; this.matrizAccionesSemanticas.set(1,2,AS2) ; this.matrizAccionesSemanticas.set(1,3,AS14) ; this.matrizAccionesSemanticas.set(1,4,null) ; this.matrizAccionesSemanticas.set(1,5,null) ; this.matrizAccionesSemanticas.set(1,6,null) ; this.matrizAccionesSemanticas.set(1,7,null) ; this.matrizAccionesSemanticas.set(1,8,null) ; this.matrizAccionesSemanticas.set(1,9,null) ; this.matrizAccionesSemanticas.set(1,10,null) ; this.matrizAccionesSemanticas.set(1,11,null) ; this.matrizAccionesSemanticas.set(1,12,null) ; this.matrizAccionesSemanticas.set(1,13,null) ; this.matrizAccionesSemanticas.set(1,14,null) ; this.matrizAccionesSemanticas.set(1,15,null) ; this.matrizAccionesSemanticas.set(1,16,null) ; this.matrizAccionesSemanticas.set(1,17,null); this.matrizAccionesSemanticas.set(1,18,null) ; this.matrizAccionesSemanticas.set(1,19,null) ; this.matrizAccionesSemanticas.set(1,20,null) ; this.matrizAccionesSemanticas.set(1,21,null) ; this.matrizAccionesSemanticas.set(1,22,null) ; this.matrizAccionesSemanticas.set(1,23,null) ; this.matrizAccionesSemanticas.set(1,24,null) ; this.matrizAccionesSemanticas.set(1,25,null) ; this.matrizAccionesSemanticas.set(1,26,null);
        /*2*/    this.matrizAccionesSemanticas.set(2,0,null) ; this.matrizAccionesSemanticas.set(2,1,null) ; this.matrizAccionesSemanticas.set(2,2,null) ; this.matrizAccionesSemanticas.set(2,3,null) ; this.matrizAccionesSemanticas.set(2,4,AS8) ; this.matrizAccionesSemanticas.set(2,5,null) ; this.matrizAccionesSemanticas.set(2,6,null) ; this.matrizAccionesSemanticas.set(2,7,null) ; this.matrizAccionesSemanticas.set(2,8,null) ; this.matrizAccionesSemanticas.set(2,9,null) ; this.matrizAccionesSemanticas.set(2,10,null) ; this.matrizAccionesSemanticas.set(2,11,null) ; this.matrizAccionesSemanticas.set(2,12,null) ; this.matrizAccionesSemanticas.set(2,13,null) ; this.matrizAccionesSemanticas.set(2,14,null) ; this.matrizAccionesSemanticas.set(2,15,null) ; this.matrizAccionesSemanticas.set(2,16,null) ; this.matrizAccionesSemanticas.set(2,17,null); this.matrizAccionesSemanticas.set(2,18,null)  ; this.matrizAccionesSemanticas.set(2,19,null) ; this.matrizAccionesSemanticas.set(2,20,null) ; this.matrizAccionesSemanticas.set(2,21,null) ; this.matrizAccionesSemanticas.set(2,22,null) ; this.matrizAccionesSemanticas.set(2,23,null) ; this.matrizAccionesSemanticas.set(2,24,null) ; this.matrizAccionesSemanticas.set(2,25,null) ; this.matrizAccionesSemanticas.set(2,26,null);
        /*3*/    this.matrizAccionesSemanticas.set(3,0,AS7) ; this.matrizAccionesSemanticas.set(3,1,AS2) ; this.matrizAccionesSemanticas.set(3,2,AS7) ; this.matrizAccionesSemanticas.set(3,3,AS7) ; this.matrizAccionesSemanticas.set(3,4,AS7) ; this.matrizAccionesSemanticas.set(3,5,AS2) ; this.matrizAccionesSemanticas.set(3,6,AS7) ; this.matrizAccionesSemanticas.set(3,7,AS7)  ; this.matrizAccionesSemanticas.set(3,8,AS7) ; this.matrizAccionesSemanticas.set(3,9,AS7) ; this.matrizAccionesSemanticas.set(3,10,AS7) ; this.matrizAccionesSemanticas.set(3,11,AS7) ; this.matrizAccionesSemanticas.set(3,12,AS7) ; this.matrizAccionesSemanticas.set(3,13,AS7)  ; this.matrizAccionesSemanticas.set(3,14,AS7) ; this.matrizAccionesSemanticas.set(3,15,AS7) ; this.matrizAccionesSemanticas.set(3,16,AS7) ; this.matrizAccionesSemanticas.set(3,17,AS7); this.matrizAccionesSemanticas.set(3,18,AS7) ; this.matrizAccionesSemanticas.set(3,19,AS7) ; this.matrizAccionesSemanticas.set(3,20,AS7) ; this.matrizAccionesSemanticas.set(3,21,AS7) ; this.matrizAccionesSemanticas.set(3,22,AS7) ; this.matrizAccionesSemanticas.set(3,23,AS7) ; this.matrizAccionesSemanticas.set(3,24,AS7) ; this.matrizAccionesSemanticas.set(3,25,AS7) ; this.matrizAccionesSemanticas.set(3,26,AS7);
        /*4*/    this.matrizAccionesSemanticas.set(4,0,null) ; this.matrizAccionesSemanticas.set(4,1,AS2) ; this.matrizAccionesSemanticas.set(4,2,null) ; this.matrizAccionesSemanticas.set(4,3,null) ; this.matrizAccionesSemanticas.set(4,4,null) ; this.matrizAccionesSemanticas.set(4,5,null) ; this.matrizAccionesSemanticas.set(4,6,null) ; this.matrizAccionesSemanticas.set(4,7,null) ; this.matrizAccionesSemanticas.set(4,8,null) ; this.matrizAccionesSemanticas.set(4,9,null) ; this.matrizAccionesSemanticas.set(4,10,AS2) ; this.matrizAccionesSemanticas.set(4,11,AS2) ; this.matrizAccionesSemanticas.set(4,12,null) ; this.matrizAccionesSemanticas.set(4,13,null)  ; this.matrizAccionesSemanticas.set(4,14,null) ; this.matrizAccionesSemanticas.set(4,15,null) ; this.matrizAccionesSemanticas.set(4,16,null) ; this.matrizAccionesSemanticas.set(4,17,null); this.matrizAccionesSemanticas.set(4,18,null) ; this.matrizAccionesSemanticas.set(4,19,null) ; this.matrizAccionesSemanticas.set(4,20,null) ; this.matrizAccionesSemanticas.set(4,21,null) ; this.matrizAccionesSemanticas.set(4,22,null) ; this.matrizAccionesSemanticas.set(4,23,null) ; this.matrizAccionesSemanticas.set(4,24,null) ; this.matrizAccionesSemanticas.set(4,25,null) ; this.matrizAccionesSemanticas.set(4,26,null);
        /*5*/    this.matrizAccionesSemanticas.set(5,0,AS10); this.matrizAccionesSemanticas.set(5,1,AS10); this.matrizAccionesSemanticas.set(5,2,AS10); this.matrizAccionesSemanticas.set(5,3,AS10); this.matrizAccionesSemanticas.set(5,4,AS10); this.matrizAccionesSemanticas.set(5,5,AS10); this.matrizAccionesSemanticas.set(5,6,AS10); this.matrizAccionesSemanticas.set(5,7,AS10) ; this.matrizAccionesSemanticas.set(5,8,AS11) ; this.matrizAccionesSemanticas.set(5,9,AS10); this.matrizAccionesSemanticas.set(5,10,AS10); this.matrizAccionesSemanticas.set(5,11,AS10); this.matrizAccionesSemanticas.set(5,12,AS10); this.matrizAccionesSemanticas.set(5,13,AS10) ; this.matrizAccionesSemanticas.set(5,14,AS10); this.matrizAccionesSemanticas.set(5,15,AS10); this.matrizAccionesSemanticas.set(5,16,AS10); this.matrizAccionesSemanticas.set(5,17,AS10); this.matrizAccionesSemanticas.set(5,18,AS10) ; this.matrizAccionesSemanticas.set(5,19,AS10); this.matrizAccionesSemanticas.set(5,20,AS10); this.matrizAccionesSemanticas.set(5,21,AS10); this.matrizAccionesSemanticas.set(5,22,AS10); this.matrizAccionesSemanticas.set(5,23,AS10); this.matrizAccionesSemanticas.set(5,24,AS10); this.matrizAccionesSemanticas.set(5,25,AS10); this.matrizAccionesSemanticas.set(5,26,AS10);
        /*6*/    this.matrizAccionesSemanticas.set(6,0,AS2) ; this.matrizAccionesSemanticas.set(6,1,AS2) ; this.matrizAccionesSemanticas.set(6,2,AS2) ; this.matrizAccionesSemanticas.set(6,3,AS2) ; this.matrizAccionesSemanticas.set(6,4,AS2) ; this.matrizAccionesSemanticas.set(6,5,AS2) ; this.matrizAccionesSemanticas.set(6,6,AS2) ; this.matrizAccionesSemanticas.set(6,7,AS2) ; this.matrizAccionesSemanticas.set(6,8,AS2) ; this.matrizAccionesSemanticas.set(6,9,AS2) ; this.matrizAccionesSemanticas.set(6,10,AS2) ; this.matrizAccionesSemanticas.set(6,11,AS2) ; this.matrizAccionesSemanticas.set(6,12,AS2) ; this.matrizAccionesSemanticas.set(6,13,AS2)  ; this.matrizAccionesSemanticas.set(6,14,AS2) ; this.matrizAccionesSemanticas.set(6,15,null) ; this.matrizAccionesSemanticas.set(6,16,AS2) ; this.matrizAccionesSemanticas.set(6,17,AS2); this.matrizAccionesSemanticas.set(6,18,AS2) ; this.matrizAccionesSemanticas.set(6,19,AS9) ; this.matrizAccionesSemanticas.set(6,20,AS2) ; this.matrizAccionesSemanticas.set(6,21,AS2) ; this.matrizAccionesSemanticas.set(6,22,AS2) ; this.matrizAccionesSemanticas.set(6,23,AS2) ; this.matrizAccionesSemanticas.set(6,24,AS2) ; this.matrizAccionesSemanticas.set(6,25,AS2) ; this.matrizAccionesSemanticas.set(6,26,AS2);
        /*7*/    this.matrizAccionesSemanticas.set(7,0,AS12) ; this.matrizAccionesSemanticas.set(7,1,AS12) ; this.matrizAccionesSemanticas.set(7,2,AS12) ; this.matrizAccionesSemanticas.set(7,3,AS12) ; this.matrizAccionesSemanticas.set(7,4,AS12) ; this.matrizAccionesSemanticas.set(7,5,AS12) ; this.matrizAccionesSemanticas.set(7,6,AS12) ; this.matrizAccionesSemanticas.set(7,7,AS12) ; this.matrizAccionesSemanticas.set(7,8,AS12) ; this.matrizAccionesSemanticas.set(7,9,AS12) ; this.matrizAccionesSemanticas.set(7,10,AS12) ; this.matrizAccionesSemanticas.set(7,11,AS12) ; this.matrizAccionesSemanticas.set(7,12,AS12) ; this.matrizAccionesSemanticas.set(7,13,AS12)  ; this.matrizAccionesSemanticas.set(7,14,AS15) ; this.matrizAccionesSemanticas.set(7,15,AS12) ; this.matrizAccionesSemanticas.set(7,16,AS12); this.matrizAccionesSemanticas.set(7,17,AS12); this.matrizAccionesSemanticas.set(7,18,AS12); this.matrizAccionesSemanticas.set(7,19,AS12) ; this.matrizAccionesSemanticas.set(7,20,AS12) ; this.matrizAccionesSemanticas.set(7,21,AS12) ; this.matrizAccionesSemanticas.set(7,22,AS12) ; this.matrizAccionesSemanticas.set(7,23,AS12) ; this.matrizAccionesSemanticas.set(7,24,AS12) ; this.matrizAccionesSemanticas.set(7,25,AS12) ; this.matrizAccionesSemanticas.set(7,26,AS12);
        /*8*/    this.matrizAccionesSemanticas.set(8,0,AS14) ; this.matrizAccionesSemanticas.set(8,1,AS14) ; this.matrizAccionesSemanticas.set(8,2,AS14) ; this.matrizAccionesSemanticas.set(8,3,AS14) ; this.matrizAccionesSemanticas.set(8,4,AS14) ; this.matrizAccionesSemanticas.set(8,5,AS14) ; this.matrizAccionesSemanticas.set(8,6,AS14) ; this.matrizAccionesSemanticas.set(8,7,AS14) ; this.matrizAccionesSemanticas.set(8,8,AS14) ; this.matrizAccionesSemanticas.set(8,9,AS14) ; this.matrizAccionesSemanticas.set(8,10,AS14) ; this.matrizAccionesSemanticas.set(8,11,AS14) ; this.matrizAccionesSemanticas.set(8,12,AS14) ; this.matrizAccionesSemanticas.set(8,13,AS14)  ; this.matrizAccionesSemanticas.set(8,14,AS14) ; this.matrizAccionesSemanticas.set(8,15,AS14) ; this.matrizAccionesSemanticas.set(8,16,AS14) ; this.matrizAccionesSemanticas.set(8,17,AS14); this.matrizAccionesSemanticas.set(8,18,AS14) ; this.matrizAccionesSemanticas.set(8,19,AS14) ; this.matrizAccionesSemanticas.set(8,20,AS14) ; this.matrizAccionesSemanticas.set(8,21,AS14) ; this.matrizAccionesSemanticas.set(8,22,AS14) ; this.matrizAccionesSemanticas.set(8,23,AS14) ; this.matrizAccionesSemanticas.set(8,24,AS14) ; this.matrizAccionesSemanticas.set(8,25,AS14) ; this.matrizAccionesSemanticas.set(8,26,AS14);
        /*9*/    this.matrizAccionesSemanticas.set(9,0,AS14) ; this.matrizAccionesSemanticas.set(9,1,AS14) ; this.matrizAccionesSemanticas.set(9,2,AS14) ; this.matrizAccionesSemanticas.set(9,3,AS14) ; this.matrizAccionesSemanticas.set(9,4,AS14) ; this.matrizAccionesSemanticas.set(9,5,AS14) ; this.matrizAccionesSemanticas.set(9,6,AS14) ; this.matrizAccionesSemanticas.set(9,7,AS14) ; this.matrizAccionesSemanticas.set(9,8,AS14) ; this.matrizAccionesSemanticas.set(9,9,AS14) ; this.matrizAccionesSemanticas.set(9,10,AS14) ; this.matrizAccionesSemanticas.set(9,11,AS14) ; this.matrizAccionesSemanticas.set(9,12,AS14) ; this.matrizAccionesSemanticas.set(9,13,AS14)  ; this.matrizAccionesSemanticas.set(9,14,AS14) ; this.matrizAccionesSemanticas.set(9,15,AS14) ; this.matrizAccionesSemanticas.set(9,16,AS14) ; this.matrizAccionesSemanticas.set(9,17,AS14); this.matrizAccionesSemanticas.set(9,18,AS14) ; this.matrizAccionesSemanticas.set(9,19,AS14) ; this.matrizAccionesSemanticas.set(9,20,AS14) ; this.matrizAccionesSemanticas.set(9,21,AS14) ; this.matrizAccionesSemanticas.set(9,22,AS14) ; this.matrizAccionesSemanticas.set(9,23,AS14) ; this.matrizAccionesSemanticas.set(9,24,AS14) ; this.matrizAccionesSemanticas.set(9,25,AS14) ; this.matrizAccionesSemanticas.set(9,26,AS14);
        /*10*/   this.matrizAccionesSemanticas.set(10,0,AS2) ; this.matrizAccionesSemanticas.set(10,1,AS2); this.matrizAccionesSemanticas.set(10,2,AS6); this.matrizAccionesSemanticas.set(10,3,AS2); this.matrizAccionesSemanticas.set(10,4,AS2); this.matrizAccionesSemanticas.set(10,5,AS2); this.matrizAccionesSemanticas.set(10,6,AS6); this.matrizAccionesSemanticas.set(10,7,AS6) ; this.matrizAccionesSemanticas.set(10,8,AS6); this.matrizAccionesSemanticas.set(10,9,AS6); this.matrizAccionesSemanticas.set(10,10,AS6); this.matrizAccionesSemanticas.set(10,11,AS6); this.matrizAccionesSemanticas.set(10,12,AS6); this.matrizAccionesSemanticas.set(10,13,AS6); this.matrizAccionesSemanticas.set(10,14,AS6); this.matrizAccionesSemanticas.set(10,15,AS6); this.matrizAccionesSemanticas.set(10,16,AS6); this.matrizAccionesSemanticas.set(10,17,AS6); this.matrizAccionesSemanticas.set(10,18,AS6); this.matrizAccionesSemanticas.set(10,19,AS6); this.matrizAccionesSemanticas.set(10,20,AS6); this.matrizAccionesSemanticas.set(10,21,AS6); this.matrizAccionesSemanticas.set(10,22,AS6); this.matrizAccionesSemanticas.set(10,23,AS6); this.matrizAccionesSemanticas.set(10,24,AS6); this.matrizAccionesSemanticas.set(10,25,AS6); this.matrizAccionesSemanticas.set(10,26,AS6);
        /*11*/   this.matrizAccionesSemanticas.set(11,0,null) ; this.matrizAccionesSemanticas.set(11,1,AS2); this.matrizAccionesSemanticas.set(11,2,null); this.matrizAccionesSemanticas.set(11,3,null); this.matrizAccionesSemanticas.set(11,4,null); this.matrizAccionesSemanticas.set(11,5,null); this.matrizAccionesSemanticas.set(11,6,null); this.matrizAccionesSemanticas.set(11,7,null) ; this.matrizAccionesSemanticas.set(11,8,null); this.matrizAccionesSemanticas.set(11,9,null); this.matrizAccionesSemanticas.set(11,10,null); this.matrizAccionesSemanticas.set(11,11,null); this.matrizAccionesSemanticas.set(11,12,null); this.matrizAccionesSemanticas.set(11,13,null); this.matrizAccionesSemanticas.set(11,14,null); this.matrizAccionesSemanticas.set(11,15,null); this.matrizAccionesSemanticas.set(11,16,null); this.matrizAccionesSemanticas.set(11,17,null); this.matrizAccionesSemanticas.set(11,18,null); this.matrizAccionesSemanticas.set(11,19,null); this.matrizAccionesSemanticas.set(11,20,null); this.matrizAccionesSemanticas.set(11,21,null); this.matrizAccionesSemanticas.set(11,22,null); this.matrizAccionesSemanticas.set(11,23,null); this.matrizAccionesSemanticas.set(11,24,null); this.matrizAccionesSemanticas.set(11,25,null); this.matrizAccionesSemanticas.set(11,26,null);
        /*12*/    this.matrizAccionesSemanticas.set(12,0,null) ; this.matrizAccionesSemanticas.set(12,1,AS2) ; this.matrizAccionesSemanticas.set(12,2,null) ; this.matrizAccionesSemanticas.set(12,3,null) ; this.matrizAccionesSemanticas.set(12,4,null) ; this.matrizAccionesSemanticas.set(12,5,null) ; this.matrizAccionesSemanticas.set(12,6,null) ; this.matrizAccionesSemanticas.set(12,7,null) ; this.matrizAccionesSemanticas.set(12,8,null) ; this.matrizAccionesSemanticas.set(12,9,null) ; this.matrizAccionesSemanticas.set(12,10,null) ; this.matrizAccionesSemanticas.set(12,11,null) ; this.matrizAccionesSemanticas.set(12,12,null) ; this.matrizAccionesSemanticas.set(12,13,null)  ; this.matrizAccionesSemanticas.set(12,14,null) ; this.matrizAccionesSemanticas.set(12,15,null) ; this.matrizAccionesSemanticas.set(12,16,null) ; this.matrizAccionesSemanticas.set(12,17,null); this.matrizAccionesSemanticas.set(12,18,null) ; this.matrizAccionesSemanticas.set(12,19,null) ; this.matrizAccionesSemanticas.set(12,20,null) ; this.matrizAccionesSemanticas.set(12,21,null) ; this.matrizAccionesSemanticas.set(12,22,null) ; this.matrizAccionesSemanticas.set(12,23,null) ; this.matrizAccionesSemanticas.set(12,24,null) ; this.matrizAccionesSemanticas.set(12,25,null) ; this.matrizAccionesSemanticas.set(12,26,null);
        /*13*/    this.matrizAccionesSemanticas.set(13,0,AS7) ; this.matrizAccionesSemanticas.set(13,1,AS2) ; this.matrizAccionesSemanticas.set(13,2,AS7) ; this.matrizAccionesSemanticas.set(13,3,AS7) ; this.matrizAccionesSemanticas.set(13,4,AS7) ; this.matrizAccionesSemanticas.set(13,5,AS7) ; this.matrizAccionesSemanticas.set(13,6,AS7) ; this.matrizAccionesSemanticas.set(13,7,AS7) ; this.matrizAccionesSemanticas.set(13,8,AS7) ; this.matrizAccionesSemanticas.set(13,9,AS7) ; this.matrizAccionesSemanticas.set(13,10,AS7) ; this.matrizAccionesSemanticas.set(13,11,AS7) ; this.matrizAccionesSemanticas.set(13,12,AS7) ; this.matrizAccionesSemanticas.set(13,13,AS7) ; this.matrizAccionesSemanticas.set(13,14,AS7) ; this.matrizAccionesSemanticas.set(13,15,AS7) ; this.matrizAccionesSemanticas.set(13,16,AS7)  ; this.matrizAccionesSemanticas.set(13,17,AS7); this.matrizAccionesSemanticas.set(13,18,AS7) ; this.matrizAccionesSemanticas.set(13,19,AS7) ; this.matrizAccionesSemanticas.set(13,20,AS7) ; this.matrizAccionesSemanticas.set(13,21,AS7) ; this.matrizAccionesSemanticas.set(13,22,AS7) ; this.matrizAccionesSemanticas.set(13,23,AS7) ; this.matrizAccionesSemanticas.set(13,24,AS7) ; this.matrizAccionesSemanticas.set(13,25,AS7) ; this.matrizAccionesSemanticas.set(13,26,AS7);
        /*14*/    this.matrizAccionesSemanticas.set(14,0,null) ; this.matrizAccionesSemanticas.set(14,1,null) ; this.matrizAccionesSemanticas.set(14,2,null) ; this.matrizAccionesSemanticas.set(14,3,null) ; this.matrizAccionesSemanticas.set(14,4,null) ; this.matrizAccionesSemanticas.set(14,5,null) ; this.matrizAccionesSemanticas.set(14,6,null) ; this.matrizAccionesSemanticas.set(14,7,null) ; this.matrizAccionesSemanticas.set(14,8,AS11) ; this.matrizAccionesSemanticas.set(14,9,null) ; this.matrizAccionesSemanticas.set(14,10,null) ; this.matrizAccionesSemanticas.set(14,11,null) ; this.matrizAccionesSemanticas.set(14,12,null) ; this.matrizAccionesSemanticas.set(14,13,null) ; this.matrizAccionesSemanticas.set(2,14,null) ; this.matrizAccionesSemanticas.set(14,15,null) ; this.matrizAccionesSemanticas.set(14,16,null) ; this.matrizAccionesSemanticas.set(14,17,null); this.matrizAccionesSemanticas.set(14,18,null) ; this.matrizAccionesSemanticas.set(14,19,null) ; this.matrizAccionesSemanticas.set(14,20,null) ; this.matrizAccionesSemanticas.set(14,21,null) ; this.matrizAccionesSemanticas.set(14,22,null) ; this.matrizAccionesSemanticas.set(14,23,null) ; this.matrizAccionesSemanticas.set(14,24,null) ; this.matrizAccionesSemanticas.set(14,25,null) ; this.matrizAccionesSemanticas.set(14,26,null);


    }

    /*** GETTERS Y SETTERS ***/

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public int getPosArchivo() {
        return posArchivo;
    }

    public void setPosArchivo(int posArchivo) {
        this.posArchivo = posArchivo;
    }

    public int getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(int estadoActual) {
        this.estadoActual = estadoActual;
    }

    public int getTokenActual() {
        return tokenActual;
    }

    public void setTokenActual(int tokenActual) {
        this.tokenActual = tokenActual;
    }

    public int getRefTablaSimbolos() { return refTablaSimbolos; }

    public void setRefTablaSimbolos(int refTablaSimbolos) { this.refTablaSimbolos = refTablaSimbolos; }

    public boolean isCodigoLeido() { return codigoLeido; }

    public void setCodigoLeido(boolean codigoLeido) { this.codigoLeido = codigoLeido; }

    public Vector<RegistroSimbolo> getTablaSimbolos() {
        return tablaSimbolos;
    }

    public Vector<Token> getListaTokens() {
        return listaTokens;
    }

    public ArrayList<String> getListaErrores() {
        return listaErrores;
    }

    // Método para obtener el ID Token, dado un tipo de token
    public int getIdToken(String tipoToken) { return this.idTokens.get(tipoToken); }

    // Método para agregar un token a la tabla de símbolos. Si el token está en la tabla de símbolos se asigna
    // su respectiva referencia a la misma
    public void agregarTokenATablaSimbolos(String lexema, String tipo) {
        int indiceEnTS = this.getIndiceEnTablaSimbolos(lexema);
        // Si está en la tabla de símbolos, paso el índice a la referencia en tabla de símbolos
        if (indiceEnTS != -1)
            this.refTablaSimbolos = indiceEnTS;
        else {  // Si no está en la tabla de símbolos, lo agrego al final del arreglo
            RegistroSimbolo nuevo = new RegistroSimbolo(lexema, tipo);
            this.tablaSimbolos.add(nuevo);
            this.refTablaSimbolos = this.tablaSimbolos.size() - 1;
        }
    }

    // Método para buscar un token en la tabla de símbolos y devolver su índice para asignar la referencia a la TS
    public int getIndiceEnTablaSimbolos(String lexema) {
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            if (lexema.equals(tablaSimbolos.get(i).getLexema()))
                return i;
        }
        return -1;
    }

    // Método para agregar errores léxicos
    public void addErrorLexico(String error) { this.listaErrores.add(error); }

    // Método para agregar un token a la lista de análisis léxico
    public void addToken(Token nuevo) { this.listaTokens.add(nuevo); }

    // Determina si un token es palabra reservada
    public boolean esPalabraReservada(String posiblePalabra) {
        switch (posiblePalabra) {
            case "IF":
            case "THEN":
            case "ELSE":
            case "END_IF":
            case "OUT":
            case "FUNC":
            case "RETURN":
            case "FOR":
            case "LONGINT":
            case "FLOAT":
            case "UP":
            case "DOWN":
            case "NI":
            case "REF":
            case "PROC":
                return true;
            default:
                return false;
        }
    }

    // Determina si un token es un identificador
    public boolean esIdentificador(String stringToken) {
        if (!Character.isLetter(stringToken.charAt(0)))
            return false;

        for (int i = 0; i < stringToken.length(); i++) {
            char c = stringToken.charAt(i);

            if (!(c == '_' || Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))))
                return false;
        }

        return true;
    }

    // Devuelve el tipo de token para poder agregarlo al Token cuando se está creando
    public String getTipoToken(int idToken) {
        String tipo;
        switch (idToken) {
            case (int) '+':
            case (int) '-':
            case (int) '*':
            case (int) '/':
                tipo = "OPERADOR ARITMETICO";
                break;
            case (int) '(':
            case (int) ')':
            case (int) '{':
            case (int) '}':
            case (int) ',':
            case (int) ';':
            case (int) ':':
                tipo = "LITERAL";
                break;
            case (int) '<':
            case (int) '>':
            case 275:
            case 276:
            case 277:
            case 278:
                tipo = "COMPARADOR";
                break;
            case (int) '=':
                tipo = "ASIGNACION";
                break;
            case 257:
                tipo = "IDENTIFICADOR";
                break;
            case 258:
                tipo = "CONSTANTE";
                break;
            case 259:
            case 260:
            case 261:
            case 262:
            case 263:
            case 264:
            case 265:
            case 266:
            case 267:
            case 268:
            case 269:
            case 270:
            case 271:
            case 272:
            case 273:
                tipo = "PALABRA RESERVADA";
                break;
            case 274:
                tipo = "CADENA DE CARACTERES";
                break;
            default:
                tipo = "";
                break;
        }

        return tipo;
    }

    // Dado un caracter, devuelve su columna asociada en la matriz de transición de estados
    public int getColumnaCaracter(Character caracter) {
        if (caracter == null)
            return -1;
        if (caracter == 'l')
            return 4;
        if (caracter == 'f')
            return 5;
        if (Character.isLetter(caracter))
            return 0;
        if (Character.isDigit(caracter))
            return 1;
        switch (caracter) {
            case '.': return 2;
            case '_': return 3;
            case '<': return 6;
            case '>': return 7;
            case '=': return 8;
            case '!': return 9;
            case '+': return 10;
            case '-': return 11;
            case '*': return 12;
            case '/': return 13;
            case '%': return 14;
            case '\n': return 15; // Salto de linea
            case ';': return 16;
            case ':' : return 17;
            case ',': return 18;
            case 39: return 19; // Comilla simple
            case '(': return 20;
            case ')': return 21;
            case '{': return 22;
            case '}': return 23;
            case 9: return 25; // Tabulación
            case 32: return 25; // Espacio en blanco
            case '$': return 26; // EOF o '$' en cadenas y comentarios
        }
        return 24; //Otros
    }

    // Método para verificar que el '$' sea un fin de archivo
    // Si no está en la posición final del código y no está dentro de un comentario o una cadena, devuelve error
    public boolean esFinDeArchivo() {
        if (estadoActual == 0)
            if (posArchivo == (archivo.length() - 1)) {     // Si es fin de archivo
                tokenActual = 0;
                posArchivo = 0;
                linea = 1;
                codigoLeido = true;
                return true;
            }
            else {  // Si reconocí un token y el EOF no aparece al final del código
                return false;
            }
        else
            return false;
    }

    // Método para resincronizar el análisis léxico
    // Si llego a un estado -1, se descarta el buffer, avanzo posArchivo hasta un blanco, una tabulación,
    // un salto de línea, un ';' o el fin de archivo '$', y vuelvo al estado 0 para seguir detectando tokens
    public void sincronizarAnalisis(char caracterActual) {
        String aux = buffer;   // Variable para imprimir el error
        buffer = "";
        while (archivo.charAt(posArchivo) != ';' && archivo.charAt(posArchivo) != '\n' && archivo.charAt(posArchivo) != 9
                && archivo.charAt(posArchivo) != 32 && archivo.charAt(posArchivo) != '$' && posArchivo < archivo.length()) {
            aux += archivo.charAt(posArchivo);
            posArchivo++;
        }
        estadoActual = 0;
        this.addErrorLexico("ERROR LEXICO (Linea " + linea + "): \'" + aux + "\' es un token invalido");
    }

    // Método yylex que devuelve el ID del token procesado para que lo reciba el analizador sintáctico
    public int yylex() {
        tokenActual = -1;
        estadoActual = 0;
        buffer = "";
        refTablaSimbolos = -1;
        char caracterActual;
        int columnaCaracter;
        AccionSemantica accion;
        while ((posArchivo < archivo.length()) && (tokenActual != 0) && (tokenActual == -1)) {
            caracterActual = archivo.charAt(posArchivo);
            columnaCaracter = this.getColumnaCaracter(caracterActual);
            accion = this.matrizAccionesSemanticas.get(estadoActual, columnaCaracter);
            if (accion != null)
                accion.ejecutar(buffer, caracterActual);
            if (caracterActual == '\n' && estadoActual != 6)    // Si es un salto de línea y no estoy dentro de la cadena
                linea++;
            if (caracterActual == '$')
                if (esFinDeArchivo())
                    break;
                else {      // Si no cerró la cadena o el comentario, y venía el EOF
                    if (posArchivo == archivo.length())
                        if ((estadoActual == 6 || estadoActual == 8 || estadoActual == 9)) {
                            this.addErrorLexico("ERROR LEXICO (Linea " + linea + "): cadena o comentario mal cerrados");
                            tokenActual = 0;
                            posArchivo = 0;
                            linea = 1;
                            codigoLeido = true;
                            break;
                        }
                }
            estadoActual = this.matrizEstados.get(estadoActual, columnaCaracter);

            if (estadoActual == -1)
                sincronizarAnalisis(caracterActual);

        }

        if (tokenActual != 0 && tokenActual != -1) {
            String tipo = getTipoToken(tokenActual);
            Token nuevo = new Token(tokenActual, buffer, linea, tipo);
            this.addToken(nuevo);
        }

        if (posArchivo == archivo.length() && archivo.charAt(archivo.length()-1) != '$')
            codigoLeido = true;


        return tokenActual;
    }

    // Método para imprimir la tabla de símbolos
    public void imprimirTablaSimbolos() {
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del simbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema());
        }
    }

    // Método para mostrar los errores léxicos producidos durante la ejecución
    public void imprimirErrores() {
        System.out.println("\n");
        System.out.println("---------------------");
        System.out.println(" ERRORES LEXICOS");
        if (this.listaErrores.isEmpty()) {
            System.out.println("---------------------");
            System.out.println("Ejecución sin errores");
        }
        else {
            for (int i = 0; i < this.listaErrores.size(); i++)
                System.out.println(this.listaErrores.get(i));
        }
    }

}
