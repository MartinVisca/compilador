package accionSemantica.accionSemanticaSimple;

import analizadorLexico.AnalizadorLexico;

import java.util.Vector;

@SuppressWarnings("all")
public class AgregarCaracter extends AccionSemanticaSimple {

    private Vector<Character> caracteresPermitidos;

    public AgregarCaracter(AnalizadorLexico analizadorLexico, Vector<Character> caracteresPermitidos) {
        super(analizadorLexico);
        this.caracteresPermitidos = caracteresPermitidos;
    }

    public Vector<Character> getCaracteresPermitidos() {
        return caracteresPermitidos;
    }

    public void setCaracteresPermitidos(Vector<Character> caracteresPermitidos) {
        this.caracteresPermitidos = caracteresPermitidos;
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        if (this.caracteresPermitidos.isEmpty()) {
            buffer = buffer + caracter;
            this.getAnalizadorLexico().setBuffer(buffer);
            return true;
        } else {
            if (this.caracteresPermitidos.contains('d')) {
                if (Character.isDigit(caracter)) {
                    buffer = buffer + caracter;
                    this.getAnalizadorLexico().setBuffer(buffer);
                    return true;
                }
            }
            else if ((this.caracteresPermitidos.contains('_') && caracter == '_') || (this.caracteresPermitidos.contains('.') && caracter == '.')) {
                buffer = buffer + caracter;
                this.getAnalizadorLexico().setBuffer(buffer);
                return true;
            }
            else if (this.caracteresPermitidos.contains('l') && caracter == 'l') {
                buffer = buffer + caracter;
                this.getAnalizadorLexico().setBuffer(buffer);
                return true;
            }
        }
        return false;
    }

}
