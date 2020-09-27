package analizadorLexico;

import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;

import java.util.Hashtable;
import java.util.Vector;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    // TODO: 21/9/20 Definir lista de parámetros
    private Hashtable<String, Integer> idTokens;
    private Vector<RegistroSimbolo> tablaSimbolos;
    private Vector<Token> tokens;

    public static final String IDENTIFICADOR = "ID";
    public static final String CONSTANTE = "CTE";

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = null;
        this.idTokens = new Hashtable<>();
        this.tablaSimbolos = new Vector<>();
        this.tokens = new Vector<>();

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

        //Identificador y constante
        this.idTokens.put("ID", 430);
        this.idTokens.put("CTE", 431);

        // TODO: 21/9/20  Definición de acciones semánticas
        /***** ACCIONES SEMÁNTICAS *****/
        AccionSemanticaSimple accionSemantica13 = new InicializarBuffer(); //AS13 -> Inicializar buffer
        AccionSemanticaSimple accionSemantica2 = new AgregarCaracter(); //AS2 -> Agregar caracter
        AccionSemanticaSimple accionSemantica15 = new ControlarLongitudIdentificador(); //AS15 -> Controlar longitud del identificador
        AccionSemanticaSimple accionSemantica4 = new ControlarPalabraReservada(this);
        AccionSemanticaSimple accionSemantica14 = new CrearToken(this);

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
        accionSemantica6.addAccion(new ControlarRangoEnteroLargo());
        accionSemantica6.addAccion(accionSemantica14);
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS7 -> Controlar rango de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica7 = new AccionSemanticaCompuesta();
        accionSemantica7.addAccion(new ControlarRangoFlotante());
        accionSemantica7.addAccion(accionSemantica14);
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS8 -> Controlar rango de exponente de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica8 = new AccionSemanticaCompuesta();
        accionSemantica8.addAccion(new ControlarRangoExponenteFlotante());
        accionSemantica8.addAccion(accionSemantica14);
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS9 -> Agregar caracter y crear el token.
        AccionSemanticaCompuesta accionSemantica9 = new AccionSemanticaCompuesta();
        accionSemantica9.addAccion(accionSemantica2);
        accionSemantica9.addAccion(accionSemantica14);

        //AS12 -> Descarta el buffer.
        // TODO: 23/9/20 Programar DescartarBuffer
        
        /****/
        // TODO: 21/9/20  Definición matriz de acciones semánticas
        /****/
        // TODO: 21/9/20  Definición matriz de estados
        /****/
        
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

    public Boolean esPalabraReservada(String posiblePalabra) {
        return this.idTokens.contains(posiblePalabra);
    }

    public void addToken(String stringToken) {
        int idToken = this.getIdToken(stringToken);
        String tipoToken = this.getTipoToken(stringToken);
        RegistroSimbolo simbolo = new RegistroSimbolo(stringToken, tipoToken);

        if ((tipoToken.equals(this.IDENTIFICADOR) || tipoToken.equals(this.CONSTANTE)) && !this.tablaSimbolos.contains(simbolo))
            this.tablaSimbolos.add(simbolo);

        Token token = new Token(idToken, stringToken, tipoToken, simbolo);
        this.tokens.add(token);
    }

    public int getIdToken(String stringToken) {
        // TODO: 26/9/20 Completar
        return -1;
    }

    public String getTipoToken(String stringToken) {
        // TODO: 26/9/20 Completar
        return "";
    }
}
