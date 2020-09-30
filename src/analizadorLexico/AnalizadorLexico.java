package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;
import analizadorLexico.matrices.MatrizAccionesSemanticas;

import java.util.Hashtable;
import java.util.Vector;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    private Hashtable<String, Integer> idTokens;
    private Vector<RegistroSimbolo> tablaSimbolos;
    private Vector<Token> tokens;
    private MatrizAccionesSemanticas matrizAccionesSemanticas;
    private Hashtable<String, Integer> erroresLexicos;
    private int linea;

    public static final String IDENTIFICADOR = "ID";
    public static final String CONSTANTE = "CTE";
    public static final String OPERADOR = "OPERADOR ARITMETICO";
    public static final String SIMBOLO_PUNTUACION = "SIMBOLO DE PUNTUACION";
    public static final String COMPARADOR = "COMPARADOR";
    public static final String ASIGNACION = "ASIGNACION";
    public static final String PALABRA_RESERVADA = "PALABRA RESERVADA";

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = null;
        this.idTokens = new Hashtable<>();
        this.tablaSimbolos = new Vector<>();
        this.tokens = new Vector<>();
        this.matrizAccionesSemanticas = new MatrizAccionesSemanticas();
        this.erroresLexicos = new Hashtable<>();
        this.linea = 0;


        /***** CARGA DE TOKENS *****/
        //Operadores aritméticos
        this.idTokens.put("+", 440);
        this.idTokens.put("-", 441);
        this.idTokens.put("*", 442);
        this.idTokens.put("/", 443);

        //Símbolos de puntuación
        this.idTokens.put("(", 450);
        this.idTokens.put(")", 451);
        this.idTokens.put("{", 452);
        this.idTokens.put("}", 453);
        this.idTokens.put(".", 454);
        this.idTokens.put(";", 455);

        //Comparadores
        this.idTokens.put(">", 460);
        this.idTokens.put("<", 461);
        this.idTokens.put("==", 462);
        this.idTokens.put("!=", 463);
        this.idTokens.put(">=", 464);
        this.idTokens.put("<=", 465);

        //Asignación
        this.idTokens.put("=", 466);

        //Identificador y constante
        this.idTokens.put("ID", 470);
        this.idTokens.put("CTE", 471);

        //Palabras reservadas
        this.idTokens.put("IF", 480);
        this.idTokens.put("ELSE", 481);
        this.idTokens.put("THEN", 482);
        this.idTokens.put("END_IF", 483);
        this.idTokens.put("OUT", 484);
        this.idTokens.put("FUNC", 485);
        this.idTokens.put("RETURN", 486);
        this.idTokens.put("FOR", 487);
        this.idTokens.put("LONGINT", 488);
        this.idTokens.put("FLOAT", 489);


        /***** ACCIONES SEMÁNTICAS *****/
        AccionSemanticaSimple accionSemantica2 = new AgregarCaracter(this); //AS2 -> Agregar caracter.
        AccionSemanticaSimple accionSemantica4 = new ControlarPalabraReservada(this); //AS4 -> Controlar si el buffer es palabra reservada.
        AccionSemanticaSimple accionSemantica12 = new DescartarBuffer(this); //AS12 -> Descartar el buffer y poner el último caracter al inicio del próximo.
        AccionSemanticaSimple accionSemantica13 = new InicializarBuffer(this); //AS13 -> Inicializar buffer.
        AccionSemanticaSimple accionSemantica14 = new CrearToken(this); //AS14 -> Crear token.

        //AS1 -> Inicializar buffer y agregar caracter a la cadena que contiene.
        AccionSemanticaCompuesta accionSemantica1 = new AccionSemanticaCompuesta();
        accionSemantica1.addAccion(accionSemantica13);
        accionSemantica1.addAccion(accionSemantica2);

        //AS3 -> Inicializar buffer, agregar caracter y crear token.
        AccionSemanticaCompuesta accionSemantica3 = new AccionSemanticaCompuesta();
        accionSemantica3.addAccion(accionSemantica13);
        accionSemantica3.addAccion(accionSemantica2);
        accionSemantica3.addAccion(accionSemantica14);

        //AS5 -> Controlar si el token es palabra reservada y crearlo.
        AccionSemanticaCompuesta accionSemantica5 = new AccionSemanticaCompuesta();
        accionSemantica5.addAccion(accionSemantica4);
        accionSemantica5.addAccion(accionSemantica14);

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

        //AS10 -> Crear el token y descartar el buffer.
        AccionSemanticaCompuesta accionSemantica10 = new AccionSemanticaCompuesta();
        accionSemantica10.addAccion(accionSemantica14);
        accionSemantica10.addAccion(accionSemantica12);

        //AS15 -> Controlar longitud del identificador.
        AccionSemanticaCompuesta accionSemantica15 = new AccionSemanticaCompuesta();
        accionSemantica15.addAccion(accionSemantica2);
        accionSemantica15.addAccion(new ControlarLongitudIdentificador(this));

        /****/

        //NICO
        AccionSemantica[][] matrizAS = {
                {accionSemantica1, accionSemantica1, accionSemantica1, null, null, null, accionSemantica1, accionSemantica1, accionSemantica1, accionSemantica1, accionSemantica3, accionSemantica3, accionSemantica3, accionSemantica1, null, null, null, accionSemantica3, accionSemantica3, accionSemantica1, accionSemantica3, accionSemantica3, accionSemantica3, accionSemantica3, null, null,},
                {null, accionSemantica2, accionSemantica2, accionSemantica2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,},
                {null, null, null, null, accionSemantica2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,},
                {accionSemantica7, accionSemantica2, null, accionSemantica7, accionSemantica7, accionSemantica2, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7, accionSemantica7,},
                {accionSemantica8, accionSemantica2, null, accionSemantica8, accionSemantica8, null, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8, accionSemantica8,},
                {accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica9, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10, accionSemantica10},
                {accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica9, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2,},
                {accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica10, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2,},
                {accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica2, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9, accionSemantica9,},
                {accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2,},
                {accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica10, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2, accionSemantica2,},
                {accionSemantica10, accionSemantica10, accionSemantica5, accionSemantica10, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5, accionSemantica5,},
                {accionSemantica6, null, accionSemantica6, null, null, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6, accionSemantica6,},
        };
        this.matrizAccionesSemanticas.set(matrizAS);

        /*
        //|-----------------0------------------------------------|----------------------------------------1----------------|----------------------------------------2----------------|-------------------------------------3-------------------|---------------------------------------4----------------|---------------------------------------5----------------|---------------------------------------6----------------|--------------------------------------7----------------|---------------------------------------8----------------|----------------------------------------9----------------|----------------------------------------10----------------|----------------11----------------|----------------12----------------|----------------13----------------|----------------14----------------|----------------15----------------|----------------16----------------|----------------17----------------|----------------18----------------|----------------19----------------|----------------20----------------|----------------21----------------|----------------22----------------|----------------23----------------|----------------24----------------|----------------25----------------|----------------26----------------|
        this.mat.set(0,0,accionSemantica1);  this.mat.set(0,1,accionSemantica1);  this.mat.set(0,2,accionSemantica1);  this.mat.set(0,3,-);                 this.mat.set(0,4,-);                this.mat.set(0,5,-);                this.mat.set(0,6,accionSemantica1); this.mat.set(0,7,accionSemantica1); this.mat.set(0,8,accionSemantica1); this.mat.set(0,9,accionSemantica1);   this.mat.set(0,10,accionSemantica3); this.mat.set(0,11,accionSemantica);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,)this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(1,0,-);                 this.mat.set(1,1,accionSemantica2);  this.mat.set(1,2,accionSemantica2);  this.mat.set(1,3,accionSemantica2);  this.mat.set(1,4,-);                this.mat.set(1,5,-);                this.mat.set(1,6,-);                this.mat.set(1,7,-);                this.mat.set(1,8,-);                this.mat.set(1,9,-);                  this.mat.set(1,10,-);                this.mat.set(1,11,);this.mat.set(,12,;this.mat.set(,13,);this.mat.set(,14,;this.mat.set(,15,);this.mat.set(,16,;this.mat.set(,17,);this.mat.set(,18,;this.mat.set(,19,);this.mat.set(,20,;this.mat.set(,21,);this.mat.set(,22,;this.mat.set(,23,);this.mat.set(,24,;this.mat.set(,25,);
        this.mat.set(2,0,-);                 this.mat.set(2,1,-);                 this.mat.set(2,2,-);                 this.mat.set(2,3,-);                 this.mat.set(2,4,accionSemantica2); this.mat.set(2,5,-);                this.mat.set(2,6,-);                this.mat.set(2,7,-);                this.mat.set(2,8,-);                this.mat.set(2,9,-);                  this.mat.set(2,10,-);                this.mat.set(2,11,);this.mat.set(,12,;this.mat.set(,13,);this.mat.set(,14,;this.mat.set(,15,);this.mat.set(,16,;this.mat.set(,17,);this.mat.set(,18,;this.mat.set(,19,);this.mat.set(,20,;this.mat.set(,21,);this.mat.set(,22,;this.mat.set(,23,);this.mat.set(,24,;this.mat.set(,25,);
        this.mat.set(3,0,accionSemantica7);  this.mat.set(3,1,accionSemantica2);  this.mat.set(3,2,-);                 this.mat.set(3,3,accionSemantica7);  this.mat.set(3,4,accionSemantica7); this.mat.set(3,5,accionSemantica2); this.mat.set(3,6,accionSemantica7); this.mat.set(3,7,accionSemantica7); this.mat.set(3,8,accionSemantica7); this.mat.set(3,9,accionSemantica7);   this.mat.set(3,10,accionSemantica7); this.mat.set(3,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(4,0,accionSemantica8);  this.mat.set(4,1,accionSemantica2);  this.mat.set(4,2,-);                 this.mat.set(4,3,accionSemantica8);  this.mat.set(4,4,accionSemantica8); this.mat.set(4,5,-);                this.mat.set(4,6,accionSemantica8); this.mat.set(4,7,accionSemantica8); this.mat.set(4,8,accionSemantica8); this.mat.set(4,9,accionSemantica8);   this.mat.set(4,10,accionSemantica8); this.mat.set(4,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(5,0,accionSemantica10); this.mat.set(5,1,accionSemantica10); this.mat.set(5,2,accionSemantica10); this.mat.set(5,3,accionSemantica10); this.mat.set(5,4,accionSemantica10);this.mat.set(5,5,accionSemantica10);this.mat.set(5,6,accionSemantica10);this.mat.set(5,7,accionSemantica10);this.mat.set(5,8,accionSemantica9); this.mat.set(5,9,accionSemantica10);  this.mat.set(5,10,accionSemantica10);this.mat.set(5,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(6,0,accionSemantica2);  this.mat.set(6,1,accionSemantica2);  this.mat.set(6,2,accionSemantica2);  this.mat.set(6,3,accionSemantica2);  this.mat.set(6,4,accionSemantica2); this.mat.set(6,5,accionSemantica2); this.mat.set(6,6,accionSemantica2); this.mat.set(6,7,accionSemantica2); this.mat.set(6,8,accionSemantica2); this.mat.set(6,9,accionSemantica2);   this.mat.set(6,10,accionSemantica2); this.mat.set(6,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(7,0,accionSemantica2);  this.mat.set(7,1,accionSemantica2);  this.mat.set(7,2,accionSemantica2);  this.mat.set(7,3,accionSemantica2);  this.mat.set(7,4,accionSemantica2); this.mat.set(7,5,accionSemantica2); this.mat.set(7,6,accionSemantica2); this.mat.set(7,7,accionSemantica2); this.mat.set(7,8,accionSemantica2); this.mat.set(7,9,accionSemantica2);   this.mat.set(7,10,accionSemantica2); this.mat.set(7,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(8,0,accionSemantica9);  this.mat.set(8,1,accionSemantica9);  this.mat.set(8,2,accionSemantica9);  this.mat.set(8,3,accionSemantica9);  this.mat.set(8,4,accionSemantica9); this.mat.set(8,5,accionSemantica9); this.mat.set(8,6,accionSemantica9); this.mat.set(8,7,accionSemantica9); this.mat.set(8,8,accionSemantica9); this.mat.set(8,9,accionSemantica9);   this.mat.set(8,10,accionSemantica9); this.mat.set(8,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(9,0,accionSemantica2);  this.mat.set(9,1,accionSemantica2);  this.mat.set(9,2,accionSemantica2);  this.mat.set(9,3,accionSemantica2);  this.mat.set(9,4,accionSemantica2); this.mat.set(9,5,accionSemantica2); this.mat.set(9,6,accionSemantica2); this.mat.set(9,7,accionSemantica2); this.mat.set(9,8,accionSemantica2); this.mat.set(9,9,accionSemantica2);   this.mat.set(9,10,accionSemantica2); this.mat.set(9,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(10,0,accionSemantica2); this.mat.set(10,1,accionSemantica2); this.mat.set(10,2,accionSemantica2); this.mat.set(10,3,accionSemantica2); this.mat.set(10,4,accionSemantica2);this.mat.set(10,5,accionSemantica2);this.mat.set(10,6,accionSemantica2);this.mat.set(10,7,accionSemantica2);this.mat.set(10,8,accionSemantica2);this.mat.set(10,9,accionSemantica2);  this.mat.set(10,10,accionSemantica2);this.mat.set(10,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(11,0,accionSemantica15);this.mat.set(11,1,accionSemantica15);this.mat.set(11,2,accionSemantica5); this.mat.set(11,3,accionSemantica15);this.mat.set(11,4,accionSemantica5);this.mat.set(11,5,accionSemantica5);this.mat.set(11,6,accionSemantica5);this.mat.set(11,7,accionSemantica5);this.mat.set(11,8,accionSemantica5);this.mat.set(11,9,accionSemantica5);  this.mat.set(11,10,accionSemantica5);this.mat.set(11,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        this.mat.set(12,0,accionSemantica6); this.mat.set(12,1,-);                this.mat.set(12,2,accionSemantica6); this.mat.set(12,3,-);                this.mat.set(12,4,-);               this.mat.set(12,5,accionSemantica9);this.mat.set(12,6,accionSemantica6);this.mat.set(12,7,accionSemantica6);this.mat.set(12,8,accionSemantica6);this.mat.set(12,9,accionSemantica6);  this.mat.set(12,10,accionSemantica6);this.mat.set(12,11,);this.mat.set(,12,);this.mat.set(,13,);this.mat.set(,14,);this.mat.set(,15,);this.mat.set(,16,);this.mat.set(,17,);this.mat.set(,18,);this.mat.set(,19,);this.mat.set(,20,);this.mat.set(,21,);this.mat.set(,22,);this.mat.set(,23,);this.mat.set(,24,);this.mat.set(,25,);
        */
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

    public Boolean esPalabraReservada(String posiblePalabra) {
        return this.idTokens.contains(posiblePalabra);
    }

    public void addToken(String stringToken) {
        int idToken = this.getIdToken(stringToken);
        String tipoToken = this.getTipoToken(idToken);
        RegistroSimbolo simbolo = new RegistroSimbolo(stringToken, tipoToken);

        if ((tipoToken.equals(this.IDENTIFICADOR) || tipoToken.equals(this.CONSTANTE)) && !this.tablaSimbolos.contains(simbolo))
            this.tablaSimbolos.add(simbolo);

        Token token = new Token(idToken, stringToken, tipoToken, simbolo);
        this.tokens.add(token);
    }

    public int getIdToken(String stringToken) {
        int idToken = -1;

        if (this.idTokens.contains(stringToken))
            idToken = this.idTokens.get(stringToken);
        else if (this.esConstante(stringToken))
            idToken = this.idTokens.get(CONSTANTE);
        else if (this.esIdentificador(stringToken))
            idToken = this.idTokens.get(IDENTIFICADOR);

        return idToken;
    }

    public String getTipoToken(int idToken) {
        if (idToken >= 440 && idToken <= 443)
            return this.OPERADOR;
        else if (idToken >= 450 && idToken <= 455)
            return this.SIMBOLO_PUNTUACION;
        else if (idToken >= 460 && idToken <= 465)
            return this.COMPARADOR;
        else if (idToken == 466)
            return this.ASIGNACION;
        else if (idToken == 470)
            return this.IDENTIFICADOR;
        else if (idToken == 471)
            return this.CONSTANTE;
        else if (idToken >= 480 && idToken <= 489)
            return this.PALABRA_RESERVADA;

        return "";
    }

    private Boolean esConstante(String stringToken) {
        if (stringToken.contains("_l"))
            return true;
        else {
            for (int i = 0; i < stringToken.length(); i++) {
                Character c = stringToken.charAt(i);

                if (!(c.equals('f') || Character.isDigit(c)) || c.equals('.'))
                    return false;
            }
        }

        return true;
    }

    private Boolean esIdentificador(String stringToken) {
        for (int i = 0; i < stringToken.length(); i++) {
            Character c = stringToken.charAt(i);

            if (!(c.equals('_') || Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))))
                return false;
        }

        return true;
    }

    public void addErrorLexico(String errorLexico, int linea) {
        this.erroresLexicos.put(errorLexico, linea);
    }
}
