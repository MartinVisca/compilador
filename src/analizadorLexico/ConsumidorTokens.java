package analizadorLexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;

public class ConsumidorTokens {

    public static void main(String[] args) {
        //Logica de carga de archivo
        try {
            System.out.println("----------ANALIZADOR LÉXICO-----------");
            System.out.println("Ingrese la ruta del archivo a analizar:");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.next();
            String entrada = Files.readString(Paths.get(path));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(entrada);
            Vector<Token> tokens = analizadorLexico.getTokens();
            for (Token token : tokens) {
                System.out.println("----------------");
                System.out.println("Tipo token: " + token.getTipo());
                System.out.println("Lexema: " + token.getLexema());
                RegistroSimbolo registroSimbolo = token.getRegistro();
                if (registroSimbolo != null)
                    System.out.println("Apunta a lexema: " + registroSimbolo.getLexema());
            }

            System.out.println("----------------");
            System.out.println("ERRORES");
            analizadorLexico.imprimirErrores();

            System.out.println("----------------");
            System.out.println("TABLA DE SÍMBOLOS");
            analizadorLexico.imprimirTablaSimbolos();

        } catch(IOException e) {}
    }

}
