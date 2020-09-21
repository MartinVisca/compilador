package accionSemantica.accionSemanticaSimple;

@SuppressWarnings("all")
public class InicializarBuffer extends AccionSemanticaSimple {

    public InicializarBuffer() {}

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        buffer = "";

        return true;
    }
}
