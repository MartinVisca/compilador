package analizadorSintactico;

import java.util.Vector;

public class PolacaInversa {
    //Estructura para almacenar la representación de código "Polaca Inversa".

    private Vector<String> estructuraPolaca;

    public PolacaInversa() {
        this.estructuraPolaca = new Vector<>();
    }

    public String getElemento(int posicion) {
        return this.estructuraPolaca.elementAt(posicion);
    }

    public void addElemento(String elemento) {
        this.estructuraPolaca.add(elemento);
    }

    public void addElementoEnPosicion(String elemento, int posicion) {
        this.estructuraPolaca.add(posicion, elemento);
    }

    public void vaciarPolaca() {
        this.estructuraPolaca.removeAllElements();
    }

    public int getTamañoPolaca() {
        return this.estructuraPolaca.size();
    }

}
