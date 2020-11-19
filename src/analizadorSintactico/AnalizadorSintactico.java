package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.RegistroSimbolo;

import java.util.Vector;

public class AnalizadorSintactico {

    // Variables
    private boolean errorProc = false;  // Variable para determinar si se produjo un error cuando se declara un PROC

    // Estructuras
    private AnalizadorLexico lexico;    // Se utiliza para obtener los tokens y poder verificar la sintaxis del codigo
    private Parser parser;      // Clase Parser generada por la herramienta yacc
    Vector<String> analisisSintactico;      // Contiene las detecciones correctas de reglas de la gramática
    Vector<String> listaErrores;        // Estructura que guarda los errores sintácticos
    private PolacaInversa polaca;       // Estructura de polaca inversa que servirá para poder generar el código assembler
    Vector<RegistroSimbolo> tablaSimbolos;


    public AnalizadorSintactico(AnalizadorLexico lexico, Parser parser) {
        this.lexico = lexico;
        this.parser = parser;
        this.parser.setAnalizadorLexico(this.lexico);
        this.parser.setAnalizadorSintactico(this);
        this.listaErrores = new Vector<>();
        this.polaca = new PolacaInversa();
        this.tablaSimbolos = lexico.getTablaSimbolos();
    }

    public boolean getErrorProc() { return errorProc; }

    public void setErrorProc(boolean errorProc) { this.errorProc = errorProc; }

    public void agregarAnalisis(String analisis) { this.analisisSintactico.add(analisis); }

    public void addErrorSintactico(String error) { this.listaErrores.add(error); }

    public void agregarAPolaca(String elemento) { this.polaca.addElemento(elemento); }

    public void imprimirTablaSimbolos() {
        if (this.tablaSimbolos.isEmpty())
            System.out.println("Tabla de símbolos vacía");
        else {
            for (RegistroSimbolo simbolo : this.tablaSimbolos)
                System.out.println("Tipo del simbolo: " + simbolo.getTipoToken() + " - Lexema: " + simbolo.getLexema());
        }
    }

    public void start() {
        if (parser.yyparse() == 0) {
            System.out.println("Parser finalizo");
            imprimirTablaSimbolos();
        }

        else
            System.out.println("Parser no finalizo");
        lexico.setPosArchivo(0);
        lexico.setBuffer("");
    }

}
