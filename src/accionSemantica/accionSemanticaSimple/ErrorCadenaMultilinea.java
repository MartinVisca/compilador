package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ErrorCadenaMultilinea extends AccionSemanticaSimple {

    public ErrorCadenaMultilinea(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        String error = "Cadena multilínea no permitida";
        int linea = this.getAnalizadorLexico().getLinea();
        this.getAnalizadorLexico().addErrorLexico(error, linea);
        return true;
    }
}
