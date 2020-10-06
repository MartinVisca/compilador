package analizadorLexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class ConsumidorTokens {

    public static void main(String[] args) {
        //Logica de carga de archivo
        try {
            String path = "Archivos/Erroneos/palabras_reservadas_erroneas.txt";
            String entrada = Files.readString(Paths.get(path));

            /*String entrada = "if (variable<=otravariable){" +
                    "entero__6largo= 4789_l;" +
                    "}$"; //Aca iria el string correspondiente al archivo

             */
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
            analizadorLexico.imprimirErrores();
        } catch(IOException e) {}
    }

}
