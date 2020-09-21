package accionSemantica.accionSemanticaSimple;

@SuppressWarnings("all")
public class ControlarLongitudIdentificador extends AccionSemanticaSimple {

    private static final int LONGITUD_TOPE = 20;

    public ControlarLongitudIdentificador(){}

    public static int getLongitudTope() {
        return LONGITUD_TOPE;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        return buffer.length() <= LONGITUD_TOPE;
    }
}
