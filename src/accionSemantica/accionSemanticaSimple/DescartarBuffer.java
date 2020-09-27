package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DescartarBuffer extends AccionSemanticaSimple {

    public DescartarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        this.getAnalizadorLexico().setBuffer("" + caracter);
        return true;
    }

}
