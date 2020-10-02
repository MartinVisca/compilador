package analizadorLexico.matrices;

import accionSemantica.AccionSemantica;

public class MatrizAccionesSemanticas {

    private AccionSemantica[][] matriz;

    public MatrizAccionesSemanticas() {}

    public MatrizAccionesSemanticas(int estadosOrigen, int simbolos) {
        this.matriz = new AccionSemantica[estadosOrigen][simbolos];
    }

    public AccionSemantica get(int estadoOrigen, int simbolo) {
        return matriz[estadoOrigen][simbolo];
    }

    public void set(int estadoOrigen, int simbolo, AccionSemantica accionSemantica) {
        matriz[estadoOrigen][simbolo] = accionSemantica;
    }
}


