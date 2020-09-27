package analizadorLexico;

import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;

import java.util.Hashtable;
import java.util.Vector;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    private Hashtable<String, Integer> idTokens;
    private Vector<RegistroSimbolo> tablaSimbolos;
    private Vector<Token> tokens;

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
        AccionSemanticaSimple accionSemantica15 = new ControlarLongitudIdentificador(this); //AS15 -> Controlar longitud del identificador.

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
        accionSemantica6.addAccion(new ControlarRangoEnteroLargo(this));
        accionSemantica6.addAccion(accionSemantica14);
        accionSemantica6.addAccion(accionSemantica12);

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
}
