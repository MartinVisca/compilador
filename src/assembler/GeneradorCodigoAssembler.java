package assembler;

import java.util.Stack;
import java.util.Vector;

public class GeneradorCodigoAssembler {

    private String assemblerGenerado; //String con el código assembler ya generado
    private StringBuffer codigoAssembler; //String que va almacenando el código a medida que se va generando. Es equivalente al .CODE
    private StringBuffer dataAssembler; //String constructor del .DATA
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

    public GeneradorCodigoAssembler() {
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
    }

    public String generarEstructuraGeneralAssembler() {
        return "";
    }
}
