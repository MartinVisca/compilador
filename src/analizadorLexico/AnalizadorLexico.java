package analizadorLexico;

public class AnalizadorLexico {

    private String buffer;
    private String archivo;
    // TODO: 21/9/20 Definir lista de parámetros

    public AnalizadorLexico(String archivo) {
        this.archivo = archivo;
        this.buffer = null;

        // TODO: 21/9/20  Declaración de caracteres
        /****/
        // TODO: 21/9/20  Declaración de palabras reservadas
        /****/
        // TODO: 21/9/20  Definición de acciones semánticas
        /****/
        // TODO: 21/9/20  Definición matriz de acciones semánticas
        /****/
        // TODO: 21/9/20  Definición matriz de estados
        /****/
        
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
