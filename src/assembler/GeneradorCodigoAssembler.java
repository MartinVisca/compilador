package assembler;

import analizadorSintactico.AnalizadorSintactico;

import java.util.Stack;
import java.util.Vector;

public class GeneradorCodigoAssembler {

    private String assemblerGenerado; //String con el código assembler ya generado
    private StringBuffer codigoAssembler; //String que va almacenando el código a medida que se va generando
    private static final Stack<String[]> pila = new Stack<>(); //Por cada entrada, la pila almacenará una 2-upla compuesta por el lexema y el tipo de la variable

    //Mensajes de error correspondientes a los chequeos semánticos asignados
    private final static String ERROR_OVERFLOW_SUMA = "ERROR: overflow en suma; el resultado está fuera del rango permitido para el tipo en cuestión";
    private final static String ERROR_DIVISOR_IGUAL_A_CERO = "ERROR: el divisor de la operación es igual a cero";

    //Vectores contenedores de operadores y comparadores
    private final Vector<String> operadoresBinarios;
    private final Vector<String> operadoresUnarios;
    private final Vector<String> comparadores;

    //Estructura para identificar si un registro determinado está o no ocupado
    private final Vector<Boolean> registros;

    private AnalizadorSintactico analizadorSintactico;

    public GeneradorCodigoAssembler(AnalizadorSintactico analizadorSintactico) {
        //Inicialización de estructuras
        this.operadoresBinarios = new Vector<>();
        this.operadoresUnarios = new Vector<>();
        this.comparadores = new Vector<>();
        this.registros = new Vector<>();

        //Operadores binarios aritméticos
        this.operadoresBinarios.add("+");
        this.operadoresBinarios.add("-");
        this.operadoresBinarios.add("*");
        this.operadoresBinarios.add("/");

        //Operadores binarios de comparación
        this.operadoresBinarios.add("==");
        this.operadoresBinarios.add("!=");
        this.operadoresBinarios.add("<=");
        this.operadoresBinarios.add("<");
        this.operadoresBinarios.add(">=");
        this.operadoresBinarios.add(">");

        //Operadores unarios
        this.operadoresUnarios.add("UP");
        this.operadoresUnarios.add("DOWN");
        this.operadoresUnarios.add("BI");
        this.operadoresUnarios.add("BF");
        this.operadoresUnarios.add("OUT");

        //Comparadores
        this.comparadores.add("==");
        this.comparadores.add("!=");
        this.comparadores.add("<=");
        this.comparadores.add("<");
        this.comparadores.add(">=");
        this.comparadores.add(">");

        /*Seteo de booleanos de la estructura identificadora de la ocupación de registros. Se agrega un 'false' en cuatro posiciones consecutivas, representado los registros
          A, B, C y D */
        registros.add(false);
        registros.add(false);
        registros.add(false);
        registros.add(false);

        this.analizadorSintactico = analizadorSintactico;
    }

    public String generarAssembler() {
        this.codigoAssembler = new StringBuffer();

        codigoAssembler.append("; \\masm32\\bin\\ml /c /Zd /coff ");
        codigoAssembler.append("; \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE ");
        codigoAssembler.append(".386");
        codigoAssembler.append(".model flat, stdcall");
        codigoAssembler.append("option casemap :none");
        codigoAssembler.append(";------------ INCLUDES ------------");
        codigoAssembler.append("include \\masm32\\include\\windows.inc");
        codigoAssembler.append("include \\masm32\\macros\\macros.asm");
        codigoAssembler.append("include \\masm32\\include\\masm32.inc");
        codigoAssembler.append("include \\masm32\\include\\kernel32.inc");
        codigoAssembler.append("include \\masm32\\include\\user32.inc");
        codigoAssembler.append("include \\masm32\\include\\gdi32.inc");
        codigoAssembler.append(";------------ LIBRERÍAS ------------");
        codigoAssembler.append("includelib \\masm32\\lib\\masm32.lib");
        codigoAssembler.append("includelib \\masm32\\lib\\gdi32.lib");
        codigoAssembler.append("includelib \\masm32\\lib\\kernel32.lib");
        codigoAssembler.append("includelib \\masm32\\lib\\user32.lib");
        codigoAssembler.append(".DATA ");
        codigoAssembler.append(this.generarPuntoData());
        codigoAssembler.append(".CODE");
        codigoAssembler.append("START:");
        codigoAssembler.append(this.generarPuntoCode());
        codigoAssembler.append("END START");

        return this.codigoAssembler.toString();
    }

    public String generarPuntoData() {
        StringBuffer puntoData = new StringBuffer();

        puntoData.append("overflowSuma db \\\"Error: El resultado de la suma ejecutada no está dentro del rango permitido\\\" , 0\"");
        puntoData.append("divisionPorCero db \\\"Error: La división por cero no es una operación válida\\\" , 0");

        if (!this.getVariablesDeclaradas().isEmpty())
            puntoData.append(this.getVariablesDeclaradas());

        return puntoData.toString();
    }

    public String generarPuntoCode() {
        return "";
    }

    private String getVariablesDeclaradas() {
        StringBuffer variables = new StringBuffer();

        

        return variables.toString();
    }

}
