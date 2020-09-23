package analizadorLexico;

import java.util.Hashtable;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    // TODO: 21/9/20 Definir lista de parámetros
    private Hashtable<String, Integer> tokens = new Hashtable<>();

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = null;

        // TODO: 21/9/20  Declaración de caracteres
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
        
        /****/
        // TODO: 21/9/20  Declaración de palabras reservadas
        /****/
        // TODO: 21/9/20  Definición de acciones semánticas
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
