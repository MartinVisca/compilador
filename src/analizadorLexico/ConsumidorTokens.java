package analizadorLexico;

import java.util.Vector;

public class ConsumidorTokens {

    public static void main(String[] args) {
        String entrada = "";
        AnalizadorLexico aLexico = new AnalizadorLexico(entrada);
        Vector<Token> tokens = aLexico.getTokens();
        for (Token t:tokens) {
            System.out.println("----------------");
            System.out.println("Tipo token: " + t.getTipo());
            System.out.println("Lexema: " + t.getLexema());
            RegistroSimbolo regSim = t.getRegistro();
            if (regSim != null) {
                System.out.println("Apunta a lexema: " + regSim.getLexema());
                System.out.println("Total de apariciones: " + regSim.getTotal());
            }
        }
    }

}
