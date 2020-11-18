package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoFlotante extends AccionSemanticaSimple {

    public static final float MINIMO_FLOAT = 1.17549435E-38f;
    public static final float MAXIMO_FLOAT = 3.40282347E+38f;

    public ControlarRangoFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    // Para parsear el buffer a un float
    public static Float stringAFloat(String cadena) { return Float.parseFloat(cadena.replace('f', 'E')); }

    // Verifica si el float está en rango
    public static boolean enRango(String cadena) {
        float numero = stringAFloat(cadena);
        if (cadena.equals("0.0") || cadena.equals("0.") || cadena.equals(".0"))
            return true;
        return MINIMO_FLOAT < numero && numero < MAXIMO_FLOAT;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {

        // Si no está en rango
        if (!enRango(buffer)) {
            this.getAnalizadorLexico().addErrorLexico("ERROR LEXICO (Linea " + AnalizadorLexico.linea + "): la constante FLOAT está fuera de rango.");
            return false;
        }
        else {  // Si está en rango
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "FLOAT");
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("CTE"));
            return true;
        }

    }

}