package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class InicializarBuffer extends AccionSemanticaSimple {

    public InicializarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        String auxiliar = "" + caracter;
        this.getAnalizadorLexico().setBuffer(auxiliar);
        return true;
    }
}
