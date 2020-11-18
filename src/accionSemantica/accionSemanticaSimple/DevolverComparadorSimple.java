package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DevolverComparadorSimple extends AccionSemanticaSimple{

    public DevolverComparadorSimple(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Buscamos el token asociado
        switch (buffer) {
            case ">":
                this.getAnalizadorLexico().setTokenActual(62);
                break;
            case "<":
                this.getAnalizadorLexico().setTokenActual(60);
                break;
            case "=":
                this.getAnalizadorLexico().setTokenActual(61);
                break;
        }
        return true;
    }


}
