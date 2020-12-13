package assembler;

public class InstruccionesAS {
    String format = "%-25s%s%n";

    // Operaciones LONGINT  (Hacer las operaciones utilizandos los registros del procesador)
    public String sumaLONG() {
    }

    public String restaLONG() {

    }

    public String divisionLONG() {

    }

    public String multiplicacionLONG() {

    }

    public String comparadorLONG() {}
// Operaciones FLOAT

    // Operaciones FLOAT (Hacer las operaciones utilizandos variables auxiliares y el co-procesador 80X87)
    public String sumaFLOAT(String izq, String der, String aux) {
        String codigo="";
        codigo += String.format(format,"\t FLD " + izq, ";-------------  ADD DOUBLE ---- ("+izq+"+"+der+") "); // FLD ST(sum) introduce una copia de ST(num) en ST
        codigo += "\t FADD " + der + "\n"; // FADD mem: Hace ST <- ST + [mem]. En mem deberá haber un número real en punto flotante.
        codigo += "\t FSTP " + aux + "\n"; // FSTP mem : Extrae una copia de ST en mem. El destino puede ser un operando de memoria de 4, 8 ó 10 bytes, donde se carga el número en punto flotante.
        codigo += "\t FLD " + aux + "\n";
        codigo += "\t FXAM" + "\n"; // NO LO ENCONTRE
        codigo += "\t FSTSW aux_mem_2bytes" + "\n"; //FSTSW mem2byte Almacena la palabra de estado en la memoria.
        codigo += "\t MOV ax , aux_mem_2bytes" + "\n"; // MOV dest,src : Copia el contenido del operando fuente (src) en el destino (dest). Operación: dest  src
        codigo += "\t FWAIT" + "\n"; // NO LO ENCONTRE
        codigo += "\t SAHF" + "\n"; // SAHF : Almacena en los ocho bits menos significativos del registro de indicadores el valor del registro AH.
        codigo += "\t JZ @LABEL_SIGUIENTE_INSTRUCCION" + aux+ "\n"; // JZ label Saltar si el resultado es cero (ZF = 1).
        codigo += "\t JPE @LABEL_C2is1" + aux + "\n"; // NO LO ENCONTRE
        codigo += "\t JMP @LABEL_SIGUIENTE_INSTRUCCION" + aux+ "\n"; //JMP label Saltar hacia la dirección label.
        codigo += "\t @LABEL_C2is1"+ aux+ ":" + "\n"; // ????
        codigo += "\t JC    @LABEL_OVERFLOW" + "\n"; // JC label Saltar si hubo arrastre/préstamo (CF = 1).
        codigo += "\t @LABEL_SIGUIENTE_INSTRUCCION"+ aux +":"+ "\n"; // ????
        return codigo;
    }



    public String restaFLOAT() {

    }

    public String divisionFLOAT() {

    }

    public String multiplicacionFLOAT() {

    }

    public String comparadorFLOAT() {}


}