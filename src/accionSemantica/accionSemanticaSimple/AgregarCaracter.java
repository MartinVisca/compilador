package accionSemantica.accionSemanticaSimple;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    public AgregarCaracter() {}

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        String auxiliar = buffer + caracter;

        return true;
    }
}
