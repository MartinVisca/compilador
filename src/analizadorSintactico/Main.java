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
        //Logica de carga de archivo
        try {
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            Parser parser = new Parser();
            AnalizadorLexico lexico = new AnalizadorLexico(entrada);
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, parser);
            sintactico.start();

        } catch(IOException e) {}
    }
}
