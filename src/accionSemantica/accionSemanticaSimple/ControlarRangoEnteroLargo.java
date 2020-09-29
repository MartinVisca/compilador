package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoEnteroLargo extends AccionSemanticaSimple {

    private static final String RANGO_MAXIMO = String.valueOf(Math.pow(2l, 31l));
    private static final String RANGO_MINIMO = String.valueOf(Math.pow(-2l, 31l));

    public ControlarRangoEnteroLargo(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    public static String getRangoMaximo() {
        return RANGO_MAXIMO;
    }

    public static String getRangoMinimo() {
        return RANGO_MINIMO;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        long numero = Long.valueOf(buffer);
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
