package datos;

import entradaDatos.Consola;
import java.io.*;

public class TransportePersonas extends Transporte {
    // atributos
    public int cantPasajeros;

    private static final int TAM_REG = 26;

    // constructor
    public TransportePersonas() {
        cantPasajeros = 0;
        codT = 0;
        tipo = 'P';
        horas = 0;
        dniConductor = 0;
    }

    public TransportePersonas(int codT, char tipo, int horas, int dniConductor, int cantPasajeros) {
        this.codT = codT;
        this.tipo = tipo;
        this.horas = horas;
        this.dniConductor = dniConductor;
        this.cantPasajeros = cantPasajeros;
    }

    // getters y setters
    public int getCantPasajeros() {
        return cantPasajeros;
    }

    public void setCantPasajeros(int cantPasajeros) {
        this.cantPasajeros = cantPasajeros;
    }

    // metodos

    private void leerCantPasajeros() {
        Consola.emitirMensaje("Ingrese la cantidad de personas que transporta: ");
        int cantp;
        do {
            cantp = Consola.leerInt();
            if (cantp <= 0) {
                Consola.emitirMensajeLN(
                        "[ERROR] La cantidad de personas que transporta no puede ser menor o igual que 0");
            }
        } while (cantp <= 0);
        setCantPasajeros(cantp);
    }

    @Override
    public void cargarDatos() {
        super.cargarDatos();
        leerCantPasajeros();
    }

    @Override
    public String toString() {
        // codear
        return "Transporte de personas - Codigo: " + codT + ", Tipo: " + tipo + ", Horas: " + horas
                + ", DNI Conductor: " + dniConductor + ", Cantidad de pasajeros: " + cantPasajeros + ", Extra: "
                + extra;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(codT);
            a.writeChar(tipo);
            a.writeInt(horas);
            a.writeInt(dniConductor);
            a.writeDouble(extra);
            a.writeInt(cantPasajeros);
            int resto = TAM_REGISTRO_TOTAL - TAM_REG;
            if (resto != 0) {
                while (resto > 0) {
                    a.write(0);
                    resto--;
                }
            }

        } catch (IOException e) {
            System.out.println("Error al grabar el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            if (val == 0)
                codT = a.readInt();
            tipo = a.readChar();
            horas = a.readInt();
            dniConductor = a.readInt();
            extra = a.readDouble();
            cantPasajeros = a.readInt();
            int resto = TAM_REGISTRO_TOTAL - TAM_REG;
            if (resto != 0) {
                while (resto > 0) {
                    a.read();
                    resto--;
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void mostrarRegistro(int val, boolean activo) {
        // codear
    }

    // grabable
    @Override
    public void cargarDatos(int val) {
        // codear
    }

    @Override
    public void calcularExtra() {
        double extra = 3000;
        if (cantPasajeros > 9) {
            extra = 5000;
        }
        extra = extra * horas;
        extra = extra + (MONTO_X_HORA * horas);

        this.extra = extra;
    }
}
