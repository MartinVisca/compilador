package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ControlarPalabraReservada extends AccionSemanticaSimple {

    public ControlarPalabraReservada(AnalizadorLexico analizador) {
        super(analizador);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        return this.getAnalizadorLexico().esPalabraReservada(buffer);
    }

}
