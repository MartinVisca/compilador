package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class ControlarLongitudIdentificador extends AccionSemanticaSimple {

    private static final int LONGITUD_TOPE = 20;
    private static final int PRIMER_POSICION = 0;

    public ControlarLongitudIdentificador(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    public static int getLongitudTope() {
        return LONGITUD_TOPE;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (buffer.length() <= LONGITUD_TOPE)
            return true;
        else {
            String error = "Advertencia: el identificador superó la longitud permitida, por lo que fue truncado.";
            int linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            String nuevoBuffer = buffer.substring(this.PRIMER_POSICION, this.LONGITUD_TOPE - 1);
            this.getAnalizadorLexico().setBuffer(nuevoBuffer);
            return false;
        }
    }
}
