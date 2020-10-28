package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;
import analizadorLexico.matrices.MatrizTransicionEstados;

import java.util.Hashtable;
import java.util.Vector;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    private Hashtable<String, Integer> idTokens;
    private Vector<RegistroSimbolo> tablaSimbolos;
    private Vector<Token> tokens;
    private MatrizAccionesSemanticas matrizAccionesSemanticas;
    private MatrizTransicionEstados matrizEstados;
    private Hashtable<String, Integer> erroresLexicos;
    private int linea;
    private Boolean descartoBuffer;
    private Boolean caracterLeido;
    private int totalTokens;
    private int contadorToken;

    public static final String IDENTIFICADOR = "ID";
    public static final String CONSTANTE = "CTE";
    public static final String OPERADOR = "OPERADOR ARITMETICO";
    public static final String SIMBOLO_PUNTUACION = "SIMBOLO DE PUNTUACION";
    public static final String COMPARADOR = "COMPARADOR";
    public static final String ASIGNACION = "ASIGNACION";
    public static final String PALABRA_RESERVADA = "PALABRA RESERVADA";
    public static final String CADENA = "CADENA DE CARACTERES";
    public static final int ID_CADENA = 274;
    public static final int PRIMER_ESTADO = 0;
    public static final int ULTIMO_ESTADO = 12;

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = "";
        this.idTokens = new Hashtable<>();
        this.tablaSimbolos = new Vector<>();
        this.tokens = new Vector<>();
        this.matrizAccionesSemanticas = new MatrizAccionesSemanticas(12,27);
        this.erroresLexicos = new Hashtable<>();
        this.linea = 1;
        this.descartoBuffer = false;
        this.caracterLeido = false;
        this.totalTokens = 0;
        this.contadorToken = 0;


        /***** CARGA DE TOKENS *****/
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


        /***** ACCIONES SEMÁNTICAS *****/
        AccionSemanticaSimple accionSemantica2 = new AgregarCaracter(this); //AS2 -> Agregar caracter (sin restricciones).
        AccionSemanticaSimple accionSemantica4 = new ControlarPalabraReservada(this); //AS4 -> Controlar si el buffer es palabra reservada.
        AccionSemanticaSimple accionSemantica12 = new DescartarBuffer(this); //AS12 -> Descartar el buffer y poner el último caracter al inicio del próximo.
        AccionSemanticaSimple accionSemantica13 = new InicializarBuffer(this); //AS13 -> Inicializar buffer.
        AccionSemanticaSimple accionSemantica14 = new CrearToken(this); //AS14 -> Crear token.

        //AS1 -> Inicializar buffer y agregar caracter a la cadena que contiene (para controlar dígitos).
        AccionSemanticaCompuesta accionSemantica1Digitos = new AccionSemanticaCompuesta();
        accionSemantica1Digitos.addAccion(accionSemantica13);
        accionSemantica1Digitos.addAccion(accionSemantica2);

        //AS1 -> Inicializar buffer y agregar carácter a la cadena que contiene (resto de caracteres).
        AccionSemanticaCompuesta accionSemantica1 = new AccionSemanticaCompuesta();
        accionSemantica1.addAccion(accionSemantica13);

        //AS3 -> Inicializar buffer, agregar caracter y crear token.
        AccionSemanticaCompuesta accionSemantica3 = new AccionSemanticaCompuesta();
        accionSemantica3.addAccion(accionSemantica13);
        accionSemantica3.addAccion(accionSemantica14);

        //AS5 -> Controlar si el token es palabra reservada y crearlo.
        AccionSemanticaCompuesta accionSemantica5 = new AccionSemanticaCompuesta();
        accionSemantica5.addAccion(accionSemantica4);
        accionSemantica5.addAccion(new ControlarLongitudIdentificador(this));
        accionSemantica5.addAccion(accionSemantica14);
        accionSemantica5.addAccion(accionSemantica12);

        //AS6 -> Controlar el rango de un entero largo; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica6 = new AccionSemanticaCompuesta();
        accionSemantica6.addAccion(accionSemantica2);
        accionSemantica6.addAccion(new ControlarRangoEnteroLargo(this));
        accionSemantica6.addAccion(accionSemantica14);

        //AS7 -> Controlar rango de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica7 = new AccionSemanticaCompuesta();
        accionSemantica7.addAccion(new ControlarRangoFlotante(this));
        accionSemantica7.addAccion(accionSemantica14);
        accionSemantica7.addAccion(accionSemantica12);

        //AS8 -> Controlar rango de exponente de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica8 = new AccionSemanticaCompuesta();
        accionSemantica8.addAccion(new ControlarRangoExponenteFlotante(this));
        accionSemantica8.addAccion(accionSemantica14);
        accionSemantica8.addAccion(accionSemantica12);

        //AS9 -> Agregar caracter y crear el token.
        AccionSemanticaCompuesta accionSemantica9 = new AccionSemanticaCompuesta();
        accionSemantica9.addAccion(accionSemantica2);
        accionSemantica9.addAccion(accionSemantica14);
        accionSemantica9.addAccion(accionSemantica12);

        //AS10 -> Crear el token y descartar el buffer.
        AccionSemanticaCompuesta accionSemantica10 = new AccionSemanticaCompuesta();
        accionSemantica10.addAccion(accionSemantica14);
        accionSemantica10.addAccion(accionSemantica12);

        //AS11 -> Muestra error por cadena multilínea.
        AccionSemanticaCompuesta accionSemantica11 = new AccionSemanticaCompuesta();
        accionSemantica11.addAccion(new ErrorCadenaMultilinea(this));
        accionSemantica11.addAccion(accionSemantica12);

        //AS15 -> Controlar longitud del identificador.
        AccionSemanticaCompuesta accionSemantica15 = new AccionSemanticaCompuesta();
        accionSemantica15.addAccion(accionSemantica2);
        accionSemantica5.addAccion(new ControlarLongitudIdentificador(this));


        /***** MATRIZ DE ESTADOS *****/
        this.matrizEstados = new MatrizTransicionEstados();


        /***** MATRIZ DE ACCIONES SEMÁNTICAS *****/
                /*0*/    this.matrizAccionesSemanticas.set(0,0,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,1,accionSemantica1Digitos) ; this.matrizAccionesSemanticas.set(0,2,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,3,null)         ; this.matrizAccionesSemanticas.set(0,4,null)         ; this.matrizAccionesSemanticas.set(0,5,null)         ; this.matrizAccionesSemanticas.set(0,6,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,7,accionSemantica1)  ; this.matrizAccionesSemanticas.set(0,8,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,9,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,10,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,11,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,12,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,13,accionSemantica1)  ; this.matrizAccionesSemanticas.set(0,14,null)         ; this.matrizAccionesSemanticas.set(0,15,null)         ; this.matrizAccionesSemanticas.set(0,16,null)         ; this.matrizAccionesSemanticas.set(0,17,accionSemantica3); this.matrizAccionesSemanticas.set(0,18,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,19,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,20,accionSemantica1) ; this.matrizAccionesSemanticas.set(0,21,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,22,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,23,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,24,accionSemantica3) ; this.matrizAccionesSemanticas.set(0,25,null)         ; this.matrizAccionesSemanticas.set(0,26,null);
                /*1*/    this.matrizAccionesSemanticas.set(1,0,null)         ; this.matrizAccionesSemanticas.set(1,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(1,2,accionSemantica2) ; this.matrizAccionesSemanticas.set(1,3,accionSemantica2) ; this.matrizAccionesSemanticas.set(1,4,null)         ; this.matrizAccionesSemanticas.set(1,5,null)         ; this.matrizAccionesSemanticas.set(1,6,null)         ; this.matrizAccionesSemanticas.set(1,7,null)          ; this.matrizAccionesSemanticas.set(1,8,null)         ; this.matrizAccionesSemanticas.set(1,9,null)         ; this.matrizAccionesSemanticas.set(1,10,null)         ; this.matrizAccionesSemanticas.set(1,11,null)         ; this.matrizAccionesSemanticas.set(1,12,null)         ; this.matrizAccionesSemanticas.set(1,13,null)          ; this.matrizAccionesSemanticas.set(1,14,null)         ; this.matrizAccionesSemanticas.set(1,15,null)         ; this.matrizAccionesSemanticas.set(1,16,null)         ; this.matrizAccionesSemanticas.set(1,17,null); this.matrizAccionesSemanticas.set(1,18,null)         ; this.matrizAccionesSemanticas.set(1,19,null)         ; this.matrizAccionesSemanticas.set(1,20,null)         ; this.matrizAccionesSemanticas.set(1,21,null)         ; this.matrizAccionesSemanticas.set(1,22,null)         ; this.matrizAccionesSemanticas.set(1,23,null)         ; this.matrizAccionesSemanticas.set(1,24,null)         ; this.matrizAccionesSemanticas.set(1,25,null)         ; this.matrizAccionesSemanticas.set(1,26,null);
                /*2*/    this.matrizAccionesSemanticas.set(2,0,null)         ; this.matrizAccionesSemanticas.set(2,1,null)         ; this.matrizAccionesSemanticas.set(2,2,null)         ; this.matrizAccionesSemanticas.set(2,3,null)         ; this.matrizAccionesSemanticas.set(2,4,accionSemantica6) ; this.matrizAccionesSemanticas.set(2,5,null)         ; this.matrizAccionesSemanticas.set(2,6,null)         ; this.matrizAccionesSemanticas.set(2,7,null)          ; this.matrizAccionesSemanticas.set(2,8,null)         ; this.matrizAccionesSemanticas.set(2,9,null)         ; this.matrizAccionesSemanticas.set(2,10,null)         ; this.matrizAccionesSemanticas.set(2,11,null)         ; this.matrizAccionesSemanticas.set(2,12,null)         ; this.matrizAccionesSemanticas.set(2,13,null)          ; this.matrizAccionesSemanticas.set(2,14,null)         ; this.matrizAccionesSemanticas.set(2,15,null)         ; this.matrizAccionesSemanticas.set(2,16,null)         ; this.matrizAccionesSemanticas.set(2,17,null); this.matrizAccionesSemanticas.set(2,18,null)         ; this.matrizAccionesSemanticas.set(2,19,null)         ; this.matrizAccionesSemanticas.set(2,20,null)         ; this.matrizAccionesSemanticas.set(2,21,null)         ; this.matrizAccionesSemanticas.set(2,22,null)         ; this.matrizAccionesSemanticas.set(2,23,null)         ; this.matrizAccionesSemanticas.set(2,24,null)         ; this.matrizAccionesSemanticas.set(2,25,null)         ; this.matrizAccionesSemanticas.set(2,26,null);
                /*3*/    this.matrizAccionesSemanticas.set(3,0,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(3,2,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,3,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,4,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,5,accionSemantica2) ; this.matrizAccionesSemanticas.set(3,6,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,7,accionSemantica7)  ; this.matrizAccionesSemanticas.set(3,8,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,9,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,10,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,11,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,12,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,13,accionSemantica7)  ; this.matrizAccionesSemanticas.set(3,14,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,15,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,16,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,17,accionSemantica7); this.matrizAccionesSemanticas.set(3,18,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,19,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,20,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,21,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,22,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,23,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,24,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,25,accionSemantica7) ; this.matrizAccionesSemanticas.set(3,26,accionSemantica7);
                /*4*/    this.matrizAccionesSemanticas.set(4,0,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(4,2,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,3,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,4,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,5,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,6,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,7,accionSemantica8)  ; this.matrizAccionesSemanticas.set(4,8,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,9,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,10,accionSemantica2) ; this.matrizAccionesSemanticas.set(4,11,accionSemantica2) ; this.matrizAccionesSemanticas.set(4,12,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,13,accionSemantica8)  ; this.matrizAccionesSemanticas.set(4,14,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,15,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,16,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,17,accionSemantica8); this.matrizAccionesSemanticas.set(4,18,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,19,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,20,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,21,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,22,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,23,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,24,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,25,accionSemantica8) ; this.matrizAccionesSemanticas.set(4,26,accionSemantica8);
                /*5*/    this.matrizAccionesSemanticas.set(5,0,accionSemantica10); this.matrizAccionesSemanticas.set(5,1,accionSemantica10); this.matrizAccionesSemanticas.set(5,2,accionSemantica10); this.matrizAccionesSemanticas.set(5,3,accionSemantica10); this.matrizAccionesSemanticas.set(5,4,accionSemantica10); this.matrizAccionesSemanticas.set(5,5,accionSemantica10); this.matrizAccionesSemanticas.set(5,6,accionSemantica10); this.matrizAccionesSemanticas.set(5,7,accionSemantica10) ; this.matrizAccionesSemanticas.set(5,8,accionSemantica9) ; this.matrizAccionesSemanticas.set(5,9,accionSemantica10); this.matrizAccionesSemanticas.set(5,10,accionSemantica10); this.matrizAccionesSemanticas.set(5,11,accionSemantica10); this.matrizAccionesSemanticas.set(5,12,accionSemantica10); this.matrizAccionesSemanticas.set(5,13,accionSemantica10) ; this.matrizAccionesSemanticas.set(5,14,accionSemantica10); this.matrizAccionesSemanticas.set(5,15,accionSemantica10); this.matrizAccionesSemanticas.set(5,16,accionSemantica10); this.matrizAccionesSemanticas.set(5,17,accionSemantica10); this.matrizAccionesSemanticas.set(5,18,accionSemantica10) ; this.matrizAccionesSemanticas.set(5,19,accionSemantica10); this.matrizAccionesSemanticas.set(5,20,accionSemantica10); this.matrizAccionesSemanticas.set(5,21,accionSemantica10); this.matrizAccionesSemanticas.set(5,22,accionSemantica10); this.matrizAccionesSemanticas.set(5,23,accionSemantica10); this.matrizAccionesSemanticas.set(5,24,accionSemantica10); this.matrizAccionesSemanticas.set(5,25,accionSemantica10); this.matrizAccionesSemanticas.set(5,26,accionSemantica10);
                /*6*/    this.matrizAccionesSemanticas.set(6,0,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,2,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,3,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,4,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,5,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,6,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,7,accionSemantica2)  ; this.matrizAccionesSemanticas.set(6,8,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,9,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,10,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,11,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,12,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,13,accionSemantica2)  ; this.matrizAccionesSemanticas.set(6,14,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,15,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,16,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,17,accionSemantica2); this.matrizAccionesSemanticas.set(6,18,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,19,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,20,accionSemantica9) ; this.matrizAccionesSemanticas.set(6,21,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,22,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,23,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,24,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,25,accionSemantica2) ; this.matrizAccionesSemanticas.set(6,26,accionSemantica2);
                /*7*/    this.matrizAccionesSemanticas.set(7,0,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,2,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,3,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,4,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,5,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,6,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,7,accionSemantica2)  ; this.matrizAccionesSemanticas.set(7,8,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,9,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,10,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,11,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,12,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,13,accionSemantica2)  ; this.matrizAccionesSemanticas.set(7,14,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,15,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,16,accionSemantica11); this.matrizAccionesSemanticas.set(7,17,accionSemantica2); this.matrizAccionesSemanticas.set(7,18,accionSemantica2); this.matrizAccionesSemanticas.set(7,19,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,20,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,21,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,22,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,23,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,24,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,25,accionSemantica2) ; this.matrizAccionesSemanticas.set(7,26,accionSemantica2);
                /*8*/    this.matrizAccionesSemanticas.set(8,0,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,1,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,2,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,3,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,4,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,5,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,6,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,7,accionSemantica10)  ; this.matrizAccionesSemanticas.set(8,8,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,9,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,10,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,11,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,12,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,13,accionSemantica10)  ; this.matrizAccionesSemanticas.set(8,14,accionSemantica2) ; this.matrizAccionesSemanticas.set(8,15,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,16,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,17,accionSemantica10); this.matrizAccionesSemanticas.set(8,18,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,19,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,20,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,21,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,22,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,23,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,24,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,25,accionSemantica10) ; this.matrizAccionesSemanticas.set(8,26,accionSemantica10);
                /*9*/    this.matrizAccionesSemanticas.set(9,0,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,1,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,2,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,3,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,4,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,5,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,6,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,7,accionSemantica2)  ; this.matrizAccionesSemanticas.set(9,8,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,9,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,10,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,11,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,12,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,13,accionSemantica2)  ; this.matrizAccionesSemanticas.set(9,14,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,15,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,16,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,17,accionSemantica2); this.matrizAccionesSemanticas.set(9,18,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,19,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,20,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,21,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,22,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,23,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,24,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,25,accionSemantica2) ; this.matrizAccionesSemanticas.set(9,26,accionSemantica2);
                /*10*/   this.matrizAccionesSemanticas.set(10,0,accionSemantica2); this.matrizAccionesSemanticas.set(10,1,accionSemantica2); this.matrizAccionesSemanticas.set(10,2,accionSemantica2); this.matrizAccionesSemanticas.set(10,3,accionSemantica2); this.matrizAccionesSemanticas.set(10,4,accionSemantica2); this.matrizAccionesSemanticas.set(10,5,accionSemantica2); this.matrizAccionesSemanticas.set(10,6,accionSemantica2); this.matrizAccionesSemanticas.set(10,7,accionSemantica2) ; this.matrizAccionesSemanticas.set(10,8,accionSemantica2); this.matrizAccionesSemanticas.set(10,9,accionSemantica2); this.matrizAccionesSemanticas.set(10,10,accionSemantica2); this.matrizAccionesSemanticas.set(10,11,accionSemantica2); this.matrizAccionesSemanticas.set(10,12,accionSemantica2); this.matrizAccionesSemanticas.set(10,13,accionSemantica12); this.matrizAccionesSemanticas.set(10,14,accionSemantica2); this.matrizAccionesSemanticas.set(10,15,accionSemantica2); this.matrizAccionesSemanticas.set(10,16,accionSemantica2); this.matrizAccionesSemanticas.set(10,17,accionSemantica2); this.matrizAccionesSemanticas.set(10,18,accionSemantica2); this.matrizAccionesSemanticas.set(10,19,accionSemantica2); this.matrizAccionesSemanticas.set(10,20,accionSemantica2); this.matrizAccionesSemanticas.set(10,21,accionSemantica2); this.matrizAccionesSemanticas.set(10,22,accionSemantica2); this.matrizAccionesSemanticas.set(10,23,accionSemantica2); this.matrizAccionesSemanticas.set(10,24,accionSemantica2); this.matrizAccionesSemanticas.set(10,25,accionSemantica2); this.matrizAccionesSemanticas.set(10,26,accionSemantica2);
                /*11*/   this.matrizAccionesSemanticas.set(11,0,accionSemantica2); this.matrizAccionesSemanticas.set(11,1,accionSemantica2); this.matrizAccionesSemanticas.set(11,2,accionSemantica5); this.matrizAccionesSemanticas.set(11,3,accionSemantica2); this.matrizAccionesSemanticas.set(11,4,accionSemantica2); this.matrizAccionesSemanticas.set(11,5,accionSemantica2); this.matrizAccionesSemanticas.set(11,6,accionSemantica5); this.matrizAccionesSemanticas.set(11,7,accionSemantica5) ; this.matrizAccionesSemanticas.set(11,8,accionSemantica5); this.matrizAccionesSemanticas.set(11,9,accionSemantica5); this.matrizAccionesSemanticas.set(11,10,accionSemantica5); this.matrizAccionesSemanticas.set(11,11,accionSemantica5); this.matrizAccionesSemanticas.set(11,12,accionSemantica5); this.matrizAccionesSemanticas.set(11,13,accionSemantica5); this.matrizAccionesSemanticas.set(11,14,accionSemantica5); this.matrizAccionesSemanticas.set(11,15,accionSemantica5); this.matrizAccionesSemanticas.set(11,16,accionSemantica5); this.matrizAccionesSemanticas.set(11,17,accionSemantica5); this.matrizAccionesSemanticas.set(11,18,accionSemantica5); this.matrizAccionesSemanticas.set(11,19,accionSemantica5); this.matrizAccionesSemanticas.set(11,20,accionSemantica5); this.matrizAccionesSemanticas.set(11,21,accionSemantica5); this.matrizAccionesSemanticas.set(11,22,accionSemantica5); this.matrizAccionesSemanticas.set(11,23,accionSemantica5); this.matrizAccionesSemanticas.set(11,24,accionSemantica5); this.matrizAccionesSemanticas.set(11,25,accionSemantica5); this.matrizAccionesSemanticas.set(11,26,accionSemantica5);


        /***** PROCESAMIENTO DEL ARCHIVO DE ENTRADA *****/
        this.procesar();
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Vector<Token> getTokens() {
        return (Vector<Token>) tokens.clone();
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public Boolean getDescartoBuffer() {
        return descartoBuffer;
    }

    public void setDescartoBuffer(Boolean descartoBuffer) {
        this.descartoBuffer = descartoBuffer;
    }

    public Boolean getCaracterLeido() {
        return caracterLeido;
    }

    public void setCaracterLeido(Boolean caracterLeido) {
        this.caracterLeido = caracterLeido;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public int getContadorToken() {
        return contadorToken;
    }

    public void setContadorToken(int contadorToken) {
        this.contadorToken = contadorToken;
    }

    public void addToken(String stringToken) {
        int idToken = this.getIdToken(stringToken);
        String tipoToken = this.getTipoToken(idToken);
        RegistroSimbolo simbolo = new RegistroSimbolo(stringToken, tipoToken);
        int linea = this.getLinea();

        if (tipoToken.equals(this.IDENTIFICADOR) || tipoToken.equals(this.CONSTANTE)) {
            if (!this.contieneSimbolo(stringToken))
                this.tablaSimbolos.add(simbolo);
            else
                simbolo = this.getRegistroSimbolo(stringToken);
        }

        Token token = new Token(idToken, stringToken, linea, tipoToken, simbolo);
        this.tokens.add(token);
        this.totalTokens++;
    }

    public int getIdToken(String stringToken) {
        int idToken = -1;

        if (this.idTokens.containsKey(stringToken))
            idToken = this.idTokens.get(stringToken);
        else if (this.esConstante(stringToken))
            idToken = this.idTokens.get(CONSTANTE);
        else if (this.esIdentificador(stringToken))
            idToken = this.idTokens.get(IDENTIFICADOR);
        else if (this.esCadena(stringToken))
            idToken = this.ID_CADENA;

        return idToken;
    }

    public String getTipoToken(int idToken) {
        if (idToken == (int) '+' || idToken == (int) '-' || idToken == (int) '*' || idToken == (int) '/')
            return this.OPERADOR;
        else if (idToken == (int) ')' || idToken == (int) '(' || idToken == (int) '}' || idToken == (int) '{' || idToken == (int) ',' || idToken == (int) ';' || idToken == (int) ':' || idToken == (int) '.')
            return this.SIMBOLO_PUNTUACION;
        else if (idToken == (int) '<' || idToken == (int) '>' || (idToken >= 275 && idToken <= 278))
            return this.COMPARADOR;
        else if (idToken == (int) '=')
            return this.ASIGNACION;
        else if (idToken == 257)
            return this.IDENTIFICADOR;
        else if (idToken == 258)
            return this.CONSTANTE;
        else if (idToken >= 259 && idToken <= 273)
            return this.PALABRA_RESERVADA;
        else if (idToken == this.ID_CADENA)
            return CADENA;

        return "";
    }

    private Boolean esConstante(String stringToken) {
        if (stringToken.contains("_l"))
            return true;
        else {
            for (int i = 0; i < stringToken.length(); i++) {
                Character c = stringToken.charAt(i);

                if (!(c.equals('f') || Character.isDigit(c) || c.equals('.')))
                    return false;
            }
        }

        return true;
    }

    public Boolean esPalabraReservada(String posiblePalabra) {
        return this.idTokens.keySet().contains(posiblePalabra);
    }

    public Boolean esIdentificador(String stringToken) {
        if (!Character.isLetter(stringToken.charAt(0)))
            return false;

        for (int i = 0; i < stringToken.length(); i++) {
            Character c = stringToken.charAt(i);

            if (!(c.equals('_') || Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))))
                return false;
        }

        return true;
    }

    private Boolean esComentario(Character caracter, int posicionCaracter) {
        if (posicionCaracter > 0)
            if (caracter == '/' && this.archivo.charAt(posicionCaracter - 1) == '%')
                return true;
        return false;
    }

    private Boolean esCadena(String posibleCadena) {
        if (posibleCadena.charAt(0) == '\'' && posibleCadena.charAt(posibleCadena.length() - 1) == '\'')
            return true;
        return false;
    }

    private Boolean esComparador(Character caracter, Character caracterAnterior) {
        int idCaracter = this.getIdToken(caracter.toString());
        int idCaracterAnterior = this.getIdToken(caracterAnterior.toString());
        String tipoCaracter = this.getTipoToken(idCaracter);
        String tipoCaracterAnterior = this.getTipoToken(idCaracterAnterior);
        if ((tipoCaracter == this.COMPARADOR || tipoCaracter == this.ASIGNACION) && (tipoCaracterAnterior == this.COMPARADOR || tipoCaracterAnterior == this.ASIGNACION || caracterAnterior == '!'))
            return true;
        return false;
    }

    public void addErrorLexico(String errorLexico, int linea) {
        this.erroresLexicos.put(errorLexico, linea);
    }

    public void procesar() {
        int estado = PRIMER_ESTADO;
        int i = 0;
        Boolean flagDescarte = false;
        Boolean flagCorte = false;

        while (i < this.archivo.length() && !flagCorte) {
            Character simbolo = this.archivo.charAt(i);
            Character simboloAnterior = null;
            if (i > 0)
                simboloAnterior = this.archivo.charAt(i - 1);
            int columnaSimbolo = this.getColumnaCaracter(simbolo, simboloAnterior);
            AccionSemantica accion = this.matrizAccionesSemanticas.get(estado, columnaSimbolo);
            int nuevoEstado = this.matrizEstados.get(estado, columnaSimbolo);

            if (flagDescarte)
                this.setDescartoBuffer(false);

            if (accion != null && !this.caracterLeido)
                accion.ejecutar(this.getBuffer(), simbolo);

            if (nuevoEstado == ULTIMO_ESTADO)
                estado = 0;
            else if (nuevoEstado != -1)
                estado = nuevoEstado;
            else if (simbolo != '$') {
                if (i > 0) {
                    if (this.archivo.charAt(i) == '\n' || (this.archivo.charAt(i - 1) == '\\' && this.archivo.charAt(i) == 'n'))
                        this.linea++;
                    else
                        this.addErrorLexico("Caracter no válido", this.getLinea());
                }
                estado = 0;
            }

            if (this.archivo.charAt(this.archivo.length() - 2) != '$')
                this.addErrorLexico("Fin de archivo incorrectamente definido", this.getLinea());

            if (this.archivo.charAt(i) == '$') {
                if (this.getBuffer().contains("/%") && !this.esComentario(simbolo, i))
                    this.addErrorLexico("Comentario mal definido", this.getLinea());
                flagCorte = true;
            }

            if (!this.getDescartoBuffer()
                    || (simbolo == 'l' && simboloAnterior == '_')
                    || (this.getDescartoBuffer() && this.esComentario(simbolo, i))
                    || (this.getDescartoBuffer() && this.esComparador(simbolo, simboloAnterior))) {
                i++;
                flagDescarte = false;
            }
            else
                flagDescarte = true;

        }
    }

    public int getColumnaCaracter(Character caracter, Character caracterAnterior) {
        if (caracter == null)
            return -1;
        if (caracter == 'l' && caracterAnterior == '_')
            return 4;
        if (caracter == 'f' && Character.isDigit(caracterAnterior))
            return 5;
        if (caracter == 'n' && caracterAnterior == '\\')
            return 16;
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
            case '\\': return 15; //Barra invertida
            case ';': return 17;
            case ':' : return 18;
            case ',': return 19;
            case 39: return 20; //Comilla simple
            case '(': return 21;
            case ')': return 22;
            case '{': return 23;
            case '}': return 24;
            case 32: return 26; //Espacio en blanco
        }
        return 25; //Otros
    }

    public Boolean contieneSimbolo(String simbolo) {
        for (RegistroSimbolo registroSimbolo : this.tablaSimbolos) {
            if (registroSimbolo.getLexema().equals(simbolo))
                return true;
        }
        return false;
    }

    public RegistroSimbolo getRegistroSimbolo(String simbolo) {
        for (RegistroSimbolo registroSimbolo : this.tablaSimbolos) {
            if (registroSimbolo.getLexema().equals(simbolo)) {
                return registroSimbolo;
            }
        }
        return null;
    }

    public void imprimirErrores() {
        if (this.erroresLexicos.isEmpty())
            System.out.println("Ejecución sin errores.");
        else {
            for (String error : this.erroresLexicos.keySet())
                if (!error.contains("Warning"))
                    System.out.println("Error: " + error + " (línea " + this.erroresLexicos.get(error) + ").");
                else
                    System.out.println(error + " (línea " + this.erroresLexicos.get(error) + ").");
        }
    }

    public void imprimirTablaSimbolos() {
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del simbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema());
        }
    }

    public Token getToken() {
        Token retorno = this.tokens.elementAt(this.contadorToken);
        this.contadorToken++;
        return retorno;
    }
}
