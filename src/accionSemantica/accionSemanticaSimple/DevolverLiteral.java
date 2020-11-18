package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class DevolverLiteral extends AccionSemanticaSimple {

    public DevolverLiteral(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Inicializo el buffer y agrego el caracter
        buffer = "" + caracter;
        this.getAnalizadorLexico().setBuffer(buffer);

        switch (buffer) {
            case "+":
                this.getAnalizadorLexico().setTokenActual(43);
                break;
            case "-":
                this.getAnalizadorLexico().setTokenActual(45);
                break;
            case "*":
                this.getAnalizadorLexico().setTokenActual(42);
                break;
            case ",":
                this.getAnalizadorLexico().setTokenActual(44);
                break;
            case ":":
                this.getAnalizadorLexico().setTokenActual(58);
                break;
            case ";":
                this.getAnalizadorLexico().setTokenActual(59);
                break;
            case "(":
                this.getAnalizadorLexico().setTokenActual(40);
                break;
            case ")":
                this.getAnalizadorLexico().setTokenActual(41);
                break;
            case "{":
                this.getAnalizadorLexico().setTokenActual(123);
                break;
            case "}":
                this.getAnalizadorLexico().setTokenActual(125);
                break;
        }

        // Avanzo en el código
        this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
        return true;
    }
}
