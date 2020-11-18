package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DevolverComparadorCompuesto extends AccionSemanticaSimple{

    public DevolverComparadorCompuesto(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Agregamos el caracter al buffer
        buffer += caracter;
        this.getAnalizadorLexico().setBuffer(buffer);
        // Buscamos el token asociado
        switch (buffer) {
            case "<=":
                this.getAnalizadorLexico().setTokenActual(275);
                break;
            case ">=":
                this.getAnalizadorLexico().setTokenActual(276);
                break;
            case "==":
                this.getAnalizadorLexico().setTokenActual(277);
                break;
            case "!=":
                this.getAnalizadorLexico().setTokenActual(278);
                break;
        }
        // Avanzamos en el código
        this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
        return true;
    }

}
