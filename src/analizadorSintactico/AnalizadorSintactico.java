package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import java.util.Vector;

public class AnalizadorSintactico {

    private AnalizadorLexico lexico;
    //private Parser parser;
    Vector<String> analisisSintactico;

    private AnalizadorSintactico(AnalizadorLexico lexico) { // TODO: 17/11/2020 agregar Parser al constructor
        this.lexico = lexico;
        // this.parser = parser;
    }

    public void agregarAnalisis(String analisis) { this.analisisSintactico.add(analisis); }

}
