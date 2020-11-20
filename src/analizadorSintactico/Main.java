package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        AnalizadorLexico lexico = new AnalizadorLexico("FLOAT asd; asd = -3.0f2;$");
        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
        sintactico.start();
        //Logica de carga de archivo
        /*try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(entrada);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
            sintactico.start();
            Vector<Token> tokens = analizadorLexico.getListaTokens();
            for (Token token : tokens) {
                System.out.println("----------------");
                System.out.println("Tipo token: " + token.getTipo());
                System.out.println("Lexema: " + token.getLexema());
            }

            System.out.println("----------------");
            System.out.println("ERRORES");
            analizadorLexico.imprimirErrores();

            System.out.println("----------------");
            System.out.println("TABLA DE SÍMBOLOS");
            analizadorLexico.imprimirTablaSimbolos();

        } catch(IOException e) {}*/
    }
}
