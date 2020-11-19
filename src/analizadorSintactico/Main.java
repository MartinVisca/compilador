package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        AnalizadorLexico lexico = new AnalizadorLexico("f1(z:i, y:j, x:k);$");
        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
        sintactico.start();
    }
}
