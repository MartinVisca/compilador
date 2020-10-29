package analizadorSintactico;

import analizadorLexico.AnalizadorLexico;
import analizadorLexico.RegistroSimbolo;
import analizadorLexico.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

public class AnalizadorSintactico {
    public static void main(String[] args) {
        //Logica de carga de archivo
        try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(entrada);
            Parser parser = new Parser(analizadorLexico);
            int resultado = parser.yyparse();
            System.out.println(resultado);

        } catch(IOException e) {}

    }
}
