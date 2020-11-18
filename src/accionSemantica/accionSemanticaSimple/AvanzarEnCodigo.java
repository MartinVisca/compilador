package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class AvanzarEnCodigo extends AccionSemanticaSimple {

    public AvanzarEnCodigo(AnalizadorLexico analizadorLexico) { super(analizadorLexico); }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        // Avanzar el índice del código sin realizar ninguna acción
        this.getAnalizadorLexico().setPosArchivo(this.getAnalizadorLexico().getPosArchivo() + 1);
        return true;
    }


}