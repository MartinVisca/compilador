package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarRangoExponenteFlotante extends AccionSemanticaSimple {

    private static final int RANGO_MAXIMO = 38;
    private static final int RANGO_MINIMO = -38;

    public ControlarRangoExponenteFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    public static int getRangoMaximo() {
        return RANGO_MAXIMO;
    }

    public static int getRangoMinimo() {
        return RANGO_MINIMO;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        int inicio = buffer.length() - 2;
        int finalCadena = buffer.length();
        String prueba = buffer.substring(inicio, finalCadena);
        int numero = 0;

        try {
            numero = Integer.valueOf(prueba);
        } catch (Exception e) {
            if (Character.isDigit(buffer.charAt(finalCadena - 1))) {
                prueba = buffer.substring(inicio + 1, finalCadena);
                numero = Integer.valueOf(prueba);
            } else {
                String error = "El flotante está mal definido";
                int linea = this.getAnalizadorLexico().getLinea();
                this.getAnalizadorLexico().addErrorLexico(error, linea);
                return false;
            }
        }

        if (numero >= this.RANGO_MINIMO && numero <= this.RANGO_MAXIMO)
            return true;
        else {
            String error = "El exponente indicado no está dentro del rango permitido.";
            int linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            return false;
        }
    }
}
