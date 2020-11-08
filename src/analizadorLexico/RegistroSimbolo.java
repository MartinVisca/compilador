package analizadorLexico;

import java.util.Objects;

@SuppressWarnings("all")
public class RegistroSimbolo {

    private String lexema;
    private String tipoToken;
    private String uso; //Uso que se le da a la variable o símbolo
    private String tipoIdCte; //Tipo de la constante o variable en cuestión
    private String ambito; //Lugar de definición de la variable, siguiendo Name Mangling
    private int invocacionesPermitidas; //Cantidad de invocaciones que se permiten para un símbolo determinado
    private String tipoPasaje; //Sólo para parámetros; indica si es pasado por referencia o por copia-valor
    private Boolean esParametro = Boolean.FALSE; //Indica si el símbolo es un parámetro; se setea en false por defecto

    private static final int INVOCACIONES_PERMITIDAS_POR_DEFECTO = Integer.MAX_VALUE;
    private static final String PASAJE_PARAMETRO_POR_DEFECTO = "COPIA VALOR";

    public RegistroSimbolo() {}

    public RegistroSimbolo(String lexema, String tipoToken) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.uso = "";
        this.tipoIdCte = "";
        this.ambito = "";
        this.invocacionesPermitidas = INVOCACIONES_PERMITIDAS_POR_DEFECTO; //Por defecto se setea el número más alto de invocaciones, indicando que no hay un límite definido
        this.tipoPasaje = "";
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

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getTipoIdCte() {
        return tipoIdCte;
    }

    public void setTipoIdCte(String tipoIdCte) {
        this.tipoIdCte = tipoIdCte;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public int getInvocacionesPermitidas() {
        return invocacionesPermitidas;
    }

    public void setInvocacionesPermitidas(int invocacionesPermitidas) {
        this.invocacionesPermitidas = invocacionesPermitidas;
    }

    public String getTipoPasaje() {
        return tipoPasaje;
    }

    public void setTipoPasaje(String tipoPasaje) {
        this.tipoPasaje = tipoPasaje;
    }

    public Boolean getEsParametro() {
        return esParametro;
    }

    public void setEsParametro(Boolean esParametro) {
        this.esParametro = esParametro;
        this.tipoPasaje = this.PASAJE_PARAMETRO_POR_DEFECTO;
    }

    @Override
    public boolean equals(Object otroRegistroSimbolo) {
        RegistroSimbolo registro = (RegistroSimbolo) otroRegistroSimbolo;
        return (this.getLexema().equals(registro.getLexema()) && this.getTipoToken().equals(registro.getTipoToken()) && this.getUso().equals(((RegistroSimbolo) otroRegistroSimbolo).getUso()) && this.getTipoIdCte().equals(((RegistroSimbolo) otroRegistroSimbolo).getTipoIdCte()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexema, tipoToken);
    }

}
