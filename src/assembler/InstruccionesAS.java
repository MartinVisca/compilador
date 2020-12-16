package assembler;

public class InstruccionesAS {
    String format = "%-25s%s%n";

    /************ Operaciones LONGINT ************/

    public String sumaLONGINT(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AX, " + b + "\n");
        codigo.append("ADD AX, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", AX \n");
        codigo.append("JO @ERROR_OVERFLOW \n");

        return codigo.toString();
    }

    public String sumaFLOAT(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AH, " + b + "\n");
        codigo.append("ADD AH, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", AH \n");

        return codigo.toString();
    }

    public String restaLONGINT(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AX, " + a + "\n");
        codigo.append("SUB AX, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", AX \n");
        codigo.append("JO @ERROR_OVERFLOW \n");

        return codigo.toString();
    }

    public String restaFLOAT(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AH, " + a + "\n");
        codigo.append("SUB AH, " + b + "\n");
        codigo.append("MOV " + auxiliar + ", AH \n");

        return codigo.toString();
    }

    public String divisionLONG() {

    }

    public String multiplicacionLONG() {

    }

    public String comparadorLONG() {

    }


    /************ Operaciones FLOAT ************/

    public String sumaFLOAT(String a, String b, String auxiliar) {
        StringBuffer codigo = new StringBuffer();

        codigo.append("MOV AH, " + b + "\n");
        codigo.append("ADD AH, " + a + "\n");
        codigo.append("MOV " + auxiliar + ", AH \n");

        return codigo.toString();
    }

    public String divisionFLOAT() {

    }

    public String multiplicacionFLOAT() {

    }

    public String comparadorFLOAT() {

    }

}