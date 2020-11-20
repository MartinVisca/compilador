package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        AnalizadorLexico lexico = new AnalizadorLexico("FLOAT asd; asd = -3.0f2;$");
        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
        sintactico.start();
    }
}
