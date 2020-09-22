package analizadorLexico;

import java.util.Objects;

@SuppressWarnings("all")
public class RegistroSimbolo {

    private String lexema;
    private String tipoToken;
    private int total;

    public static final int INCREMENTO = 1;

    public RegistroSimbolo(String lexema, String tipoToken) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.total = 1;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }

    public void incrementarTotal() {
        this.total += this.INCREMENTO;
    }

    @Override
    public boolean equals(Object otroRegistroSimbolo) {
        RegistroSimbolo registro = (RegistroSimbolo) otroRegistroSimbolo;
        return (this.getLexema().equals(registro.getLexema()) && this.getTipoToken().equals(registro.getTipoToken()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema, tipoToken, total);
    }

}
