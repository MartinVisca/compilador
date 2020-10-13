package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    public AgregarCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        buffer = buffer + caracter;
        this.getAnalizadorLexico().setBuffer(buffer);
        return true;
    }

}
