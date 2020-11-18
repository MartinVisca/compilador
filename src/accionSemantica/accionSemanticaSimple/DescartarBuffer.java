package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DescartarBuffer extends AccionSemanticaSimple {

    public DescartarBuffer(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Seteamos el buffer en vacío
        this.getAnalizadorLexico().setBuffer("");
        // Devolvemos false para que pueda realizar la siguiente acción
        return false;
    }

}