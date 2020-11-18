package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    public AgregarCaracter(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Agrego el caracter al buffer
        buffer += caracter;
        this.getAnalizadorLexico().setBuffer(buffer);
        // Avanzo en el código
        this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
        return true;
    }

}
