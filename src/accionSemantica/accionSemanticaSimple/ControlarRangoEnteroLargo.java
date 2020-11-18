package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoEnteroLargo extends AccionSemanticaSimple {

    public static final long MAXIMO_LONG = 2147483648L;

    public ControlarRangoEnteroLargo(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    // Verifica si el entero largo está en rango
    public static boolean enRango(String buffer) {
        Long numero = Long.parseLong(buffer);
        return (numero >= 0 && numero <= MAXIMO_LONG);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {

        // Si no está en rango
        if (!enRango(buffer)) {
            this.getAnalizadorLexico().addErrorLexico("ERROR LEXICO (Linea " + AnalizadorLexico.linea + "): la constante LONGINT está fuera de rango.");
            this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
            return false;
        }
        else {
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "LONGINT");
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CTE"));
            this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
            return true;
        }

    }
}
