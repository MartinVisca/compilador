package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoEnteroLargo extends AccionSemanticaSimple {

    private static final long RANGO_MAXIMO = (long) Math.pow(2, 31);
    private static final long RANGO_MINIMO = (long) Math.pow(-2, 31);

    public ControlarRangoEnteroLargo(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    public static long getRangoMaximo() {
        return RANGO_MAXIMO;
    }

    public static long getRangoMinimo() {
        return RANGO_MINIMO;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        long numero = Long.valueOf(buffer.substring(0, buffer.length() - 1));
        long maximo = Long.valueOf(this.RANGO_MAXIMO);
        long minimo = Long.valueOf(this.RANGO_MINIMO);

        //Chequeo por rango -2^31 < x < 2^31 - 1.
        if (numero < maximo && numero >= minimo)
            return true;
        else {
            String error = "El entero largo indicado no está dentro del rango permitido.";
            int linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            return false;
        }
    }
}
