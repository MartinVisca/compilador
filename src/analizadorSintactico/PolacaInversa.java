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
        this.estructuraPolaca.set(posicion, elemento);
    }

    public void vaciarPolaca() {
        this.estructuraPolaca.removeAllElements();
    }

    public int getSize() { return this.estructuraPolaca.size(); }

    public boolean esVacia() { return this.estructuraPolaca.isEmpty(); }

    public void imprimirPolaca () {
        for (int i = 0; i < this.getSize() ; i++)
            System.out.println(i + " " + this.getElemento(i));
    }

}
