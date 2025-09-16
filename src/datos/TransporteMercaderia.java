package datos;

import entradaDatos.Consola;
import java.io.RandomAccessFile;
import java.io.*;

public class TransporteMercaderia extends Transporte {

    // atributos
    public double toneladasTrans;
    public boolean esPeligroso;

    private static final int TAM_REG = 31; // a confirmar

    // constructor
    public TransporteMercaderia() {
        toneladasTrans = 0;
        esPeligroso = false;
        codT = 0;
        tipo = 'M';
        horas = 0;
        dniConductor = 0;
    }

    public TransporteMercaderia(int codT, char tipo, int horas, int dniConductor, double toneladasTrans,
            boolean esPeligroso) {
        this.codT = codT;
        this.tipo = tipo;
        this.horas = horas;
        this.dniConductor = dniConductor;
        this.toneladasTrans = toneladasTrans;
        this.esPeligroso = esPeligroso;
    }

    // getters y setters
    public double getToneladasTrans() {
        return toneladasTrans;
    }

    public void setToneladasTrans(double toneladasTrans) {
        this.toneladasTrans = toneladasTrans;
    }

    public boolean getEsPeligroso() {
        return esPeligroso;
    }

    public void setEsPeligroso(boolean esPeligroso) {
        this.esPeligroso = esPeligroso;
    }

    // metodos

    public void leerToneladasTrans() {
        double cantt;
        do {
            Consola.emitirMensaje("Ingrese la cantidad de toneladas transportadas: ");
            cantt = Consola.leerDouble();
            if (cantt <= 0) {
                Consola.emitirMensajeLN("[ERROR] la cantidad de toneladas transportadas no puede ser menor ni igual a 0");
            }
        } while (cantt <= 0);
        setToneladasTrans(cantt);
    } // end leertoneladas

    public void leerEsPeligroso() {
        int op;
        do {
            Consola.emitirMensaje("La mercancia es peligrosa? (1 - SI | 2 - NO): ");
            op = Consola.leerInt();
            if (op != 1 && op != 2) {
                Consola.emitirMensajeLN("[ERROR] Opcion no valida, debe ser: (1 - SI | 2 - NO).");
            }
        } while (op != 1 && op != 2);
        if (op == 1) {
            setEsPeligroso(true);
        } else {
            setEsPeligroso(false);
        }
    } // end espeligroso

    @Override
    public void cargarDatos() {
        super.cargarDatos();
        leerToneladasTrans();
        leerEsPeligroso();
    }

    @Override
    public String toString() {
        // codear
        return "Transporte de mercaderia - Codigo: " + codT + ", Tipo: " + tipo + ", Horas: " + horas
                + ", DNI Conductor: " + dniConductor + ", Toneladas transportadas: " + toneladasTrans
                + ", Es peligroso: " + esPeligroso + ", Extra: " + extra;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(codT);
            a.writeChar(tipo);
            a.writeInt(horas);
            a.writeInt(dniConductor);
            a.writeDouble(extra);
            a.writeDouble(toneladasTrans);
            a.writeBoolean(esPeligroso);

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
            toneladasTrans = a.readDouble();
            esPeligroso = a.readBoolean();
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
        double total = 0;
        total += MONTO_X_HORA * horas;
        total += toneladasTrans * 7000;
        if (esPeligroso) {
            total += 20000;
        }
        this.extra = total;
    }

}
