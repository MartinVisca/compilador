package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ControlarPalabraReservada extends AccionSemanticaSimple {

    public ControlarPalabraReservada(AnalizadorLexico analizador) {
        super(analizador);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (this.getAnalizadorLexico().esPalabraReservada(buffer))
            return true;
        else if (this.getAnalizadorLexico().esIdentificador(buffer))
            return true;
        else {
            String error = "La cadena ingresada no es una palabra reservada";
            int linea = this.getAnalizadorLexico().getLinea();
            this.getAnalizadorLexico().addErrorLexico(error, linea);
            return false;
        }
    }

}
