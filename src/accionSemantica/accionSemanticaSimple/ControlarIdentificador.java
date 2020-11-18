package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

public class ControlarIdentificador extends AccionSemanticaSimple {

    private static final int LONGITUD_MAXIMA = 20;

    public ControlarIdentificador(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {

        // Si es identificador
        if (this.getAnalizadorLexico().esIdentificador(buffer)) {
            // Si el identificador excede la longitud máxima permitida, lo truncamos
            if (buffer.length() > LONGITUD_MAXIMA) {
                buffer = buffer.substring(0, LONGITUD_MAXIMA - 1);
                this.getAnalizadorLexico().addErrorLexico("WARNING (linea " + AnalizadorLexico.linea + "): el identificador " + this.getAnalizadorLexico().getBuffer() + " ha sido truncado. Nuevo nombre: " + buffer);
                this.getAnalizadorLexico().setBuffer(buffer);
            }
            // Si no está en la tabla de símbolos lo agregamos, si está, se asigna la referencia a la tabla de símbolos
            this.getAnalizadorLexico().agregarTokenATablaSimbolos(buffer, "ID");
            // Devolvemos el id del token
            this.getAnalizadorLexico().setTokenActual(this.getAnalizadorLexico().getIdToken("ID"));
            return true;
        } else {
            this.getAnalizadorLexico().addErrorLexico("ERROR LEXICO (linea " + AnalizadorLexico.linea + "): la cadena " + buffer + " no es un identificador, o una palabra reservada válida.");
            return false;
        }
    }

}