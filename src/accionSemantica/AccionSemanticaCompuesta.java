package accionSemantica;

import java.util.Vector;

@SuppressWarnings("all")
public class AccionSemanticaCompuesta implements AccionSemantica {

    private Vector<AccionSemantica> accionSemanticas;

    public AccionSemanticaCompuesta() {
        this.accionSemanticas = new Vector<>();
    }

    public Vector<AccionSemantica> getAccionSemanticas() {
        return accionSemanticas;
    }

    public void setAccionSemanticas(Vector<AccionSemantica> accionSemanticas) {
        this.accionSemanticas = accionSemanticas;
    }

    public void addAccion(AccionSemantica accionSemantica) {
        this.accionSemanticas.add(accionSemantica);
    }

    @Override
    public boolean ejecutar(String buffer, char caracter) {
        for (AccionSemantica accion : this.accionSemanticas) {
            if (!accion.ejecutar(buffer, caracter))
                return false;
        }
        return true;
    }
}
