package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    public AgregarCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        String auxiliar = buffer + caracter;

        return true;
    }

}
