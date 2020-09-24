package analizadorLexico;

import accionSemantica.AccionSemantica;
import accionSemantica.AccionSemanticaCompuesta;
import accionSemantica.accionSemanticaSimple.*;

import java.util.Hashtable;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    // TODO: 21/9/20 Definir lista de parámetros
    private Hashtable<String, Integer> tokens = new Hashtable<>();

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = null;

        /***** CARGA DE TOKENS *****/
        //Operadores aritméticos
        this.tokens.put("+", 440);
        this.tokens.put("-", 441);
        this.tokens.put("*", 442);
        this.tokens.put("/", 443);

        //Símbolos de puntuación
        this.tokens.put("(", 450);
        this.tokens.put(")", 451);
        this.tokens.put("{", 452);
        this.tokens.put("}", 453);
        this.tokens.put(".", 454);
        this.tokens.put(";", 455);

        //Comparadores
        this.tokens.put(">", 460);
        this.tokens.put("<", 461);
        this.tokens.put("==", 462);
        this.tokens.put("!=", 463);
        this.tokens.put(">=", 464);
        this.tokens.put("<=", 465);

        //Asignación
        this.tokens.put("=", 466);

        //Palabras reservadas
        this.tokens.put("IF", 480);
        this.tokens.put("ELSE", 481);
        this.tokens.put("THEN", 482);
        this.tokens.put("END_IF", 483);
        this.tokens.put("OUT", 484);
        this.tokens.put("FUNC", 485);
        this.tokens.put("RETURN", 486);
        this.tokens.put("FOR", 487);
        this.tokens.put("LONGINT", 488);
        this.tokens.put("FLOAT", 489);

        //Identificador y constante
        this.tokens.put("ID", 430);
        this.tokens.put("CTE", 431);

        // TODO: 21/9/20  Definición de acciones semánticas
        /***** ACCIONES SEMÁNTICAS *****/
        AccionSemanticaSimple accionSemantica13 = new InicializarBuffer(); //AS13 -> Inicializar buffer
        AccionSemanticaSimple accionSemantica2 = new AgregarCaracter(); //AS2 -> Agregar caracter
        AccionSemanticaSimple accionSemantica15 = new ControlarLongitudIdentificador(); //AS15 -> Controlar longitud del identificador

        //AS1 -> Inicializar buffer y agregar caracter a la cadena que contiene.
        AccionSemanticaCompuesta accionSemantica1 = new AccionSemanticaCompuesta();
        accionSemantica1.addAccion(accionSemantica13);
        accionSemantica1.addAccion(accionSemantica2);

        //AS3 -> Inicializar buffer, agregar caracter y crear token.
        AccionSemanticaCompuesta accionSemantica3 = new AccionSemanticaCompuesta();
        accionSemantica3.addAccion(accionSemantica13);
        accionSemantica3.addAccion(accionSemantica2);
        // TODO: 23/9/20 Programar CrearToken

        //AS5 -> Controlar si el token es palabra reservada y crearlo.
        AccionSemanticaCompuesta accionSemantica5 = new AccionSemanticaCompuesta();
        // TODO: 23/9/20 Programar ControlarPalabraReservada
        // TODO: 23/9/20 Agregar la creación del token

        //AS6 -> Controlar el rango de un entero largo; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica6 = new AccionSemanticaCompuesta();
        accionSemantica6.addAccion(new ControlarRangoEnteroLargo());
        // TODO: 23/9/20 Agregar la creación del token
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS7 -> Controlar rango de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica7 = new AccionSemanticaCompuesta();
        accionSemantica7.addAccion(new ControlarRangoFlotante());
        // TODO: 23/9/20 Agregar la creación del token
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS8 -> Controlar rango de exponente de flotante; crea el token en caso de ser correcto. Ultimo caracter al inicio del próximo buffer.
        AccionSemanticaCompuesta accionSemantica8 = new AccionSemanticaCompuesta();
        accionSemantica8.addAccion(new ControlarRangoExponenteFlotante());
        // TODO: 23/9/20 Agregar la creación del token
        // TODO: 23/9/20 Programar UltimoCaracterAlInicio

        //AS9 -> Agregar caracter y crear el token.
        AccionSemanticaCompuesta accionSemantica9 = new AccionSemanticaCompuesta();
        accionSemantica9.addAccion(accionSemantica2);
        // TODO: 23/9/20 Agregar la creación del token

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
}
