package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class CrearToken extends AccionSemanticaSimple {

    public CrearToken(AnalizadorLexico analizador) {
        super(analizador);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        this.getAnalizadorLexico().addToken(this.getAnalizadorLexico().getBuffer());
        return true;
    }

}
