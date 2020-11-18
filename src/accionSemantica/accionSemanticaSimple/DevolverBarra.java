package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DevolverBarra extends AccionSemanticaSimple {

    public DevolverBarra(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Devolvemos el id token de '/'
        this.getAnalizadorLexico().setTokenActual(47);
        return true;
    }

}