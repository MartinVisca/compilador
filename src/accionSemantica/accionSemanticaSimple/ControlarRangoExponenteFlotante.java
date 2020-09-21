package accionSemantica.accionSemanticaSimple;

import accionSemantica.AccionSemantica;

public class ControlarRangoExponenteFlotante implements AccionSemantica {

    private static final int RANGO_MAXIMO = 38;
    private static final int RANGO_MINIMO = -38;

    public ControlarRangoExponenteFlotante(){}

    public static int getRangoMaximo() {
        return RANGO_MAXIMO;
    }

    public static int getRangoMinimo() {
        return RANGO_MINIMO;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        int numero = Integer.valueOf(buffer);

        if (numero >= this.RANGO_MINIMO && numero <= this.RANGO_MAXIMO)
            return true;

        return false;
    }
}
