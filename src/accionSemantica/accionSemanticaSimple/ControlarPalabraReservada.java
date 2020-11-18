package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ControlarPalabraReservada extends AccionSemanticaSimple {

    public ControlarPalabraReservada(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {

        // Si es palabra reservada, buscamos el número de token y lo asignamos a la variable tokenActual
        if (this.getAnalizadorLexico().esPalabraReservada(buffer)) {
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken(buffer));
            return true;
        }
        else
            return false;
    }

}
