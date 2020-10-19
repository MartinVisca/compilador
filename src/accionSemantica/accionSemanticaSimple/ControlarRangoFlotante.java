package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoFlotante extends AccionSemanticaSimple {

    private static final float MINIMO_RANGO_NEGATIVO = -3.40282347f;
    private static final float MAXIMO_RANGO_NEGATIVO = -1.17549435f;
    private static final float MINIMO_RANGO_POSITIVO = 1.17549435f;
    private static final float MAXIMO_RANGO_POSITIVO = 3.40282347f;
    private static final float RANGO_CERO = 0.0f;

    public ControlarRangoFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    public static float getMaximoRangoNegativo() {
        return MAXIMO_RANGO_NEGATIVO;
    }

    public static float getMaximoRangoPositivo() {
        return MAXIMO_RANGO_POSITIVO;
    }

    public static float getMinimoRangoNegativo() {
        return MINIMO_RANGO_NEGATIVO;
    }

    public static float getMinimoRangoPositivo() {
        return MINIMO_RANGO_POSITIVO;
    }

    public static float getRangoCero() {
        return RANGO_CERO;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        float numero = 0.0f;
        String error = "";
        int linea = 0;

        if (buffer.charAt(buffer.length() - 1) != '.')
            numero = Float.parseFloat(buffer);
        else {
            error = "El flotante está mal definido.";
            linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            return false;
        }

        if ((numero < this.MAXIMO_RANGO_NEGATIVO && numero > this.MINIMO_RANGO_NEGATIVO) ||
            (numero < this.MAXIMO_RANGO_POSITIVO && numero > this.MINIMO_RANGO_POSITIVO) ||
            (numero == this.RANGO_CERO))
            return true;
        else {
            error = "El flotante indicado no está dentro del rango permitido.";
            linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            return false;
        }
    }

}
