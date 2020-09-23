package analizadorLexico;

public class Token {

    private int id;
    private String lexema;
    private int nroLinea;
    private String tipo;
    private RegistroSimbolo registro;

    public Token(int id, String lexema, int nroLinea, String tipo, RegistroSimbolo registro) {
        this.id = id;
        this.lexema = lexema;
        this.nroLinea = nroLinea;
        this.tipo = tipo;
        this.registro = registro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getNroLinea() {
        return nroLinea;
    }

    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public RegistroSimbolo getRegistro() {
        return registro;
    }

    public void setRegistro(RegistroSimbolo registro) {
        this.registro = registro;
    }
}
