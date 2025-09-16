package datos;

import entradaDatos.Consola;
import persistencia.Grabable;

public abstract class Transporte implements Grabable, ICalculable {
    // atributos

    protected int codT; // 4 bytes
    protected char tipo; // 2 bytes
    protected int horas; // 4 bytes
    protected int dniConductor; // 4 bytes
    protected double extra; // 8 bytes
    protected final double MONTO_X_HORA = 7500;

    protected final int TAM_REGISTRO_TOTAL = 31;
    protected final int TAM_ARCHIVO = 100; // cantidad de registros que tendra el archivo

    // getters y setters

    public int getCodT() {
        return codT;
    }

    private void setCodT(int codT) {
        this.codT = codT;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getDniConductor() {
        return dniConductor;
    }

    public void setDniConductor(int dniConductor) {
        this.dniConductor = dniConductor;
    }

    // metodos

    public void leerCodT() {
        int cod;
        do {
            Consola.emitirMensaje("Ingrese el codigo del transporte (1 -99): ");
            cod = Consola.leerInt();
            if (cod <= 0 || cod > 99) {
                Consola.emitirMensajeLN("[ERROR] el codigo del transporte debe estar entre 1 y 99, ingrese de nuevo.");
            }
        } while (cod <= 0 || cod > 99);
        setCodT(cod);
    } // end leerCodeT

    public void leerHoras() {
        Consola.emitirMensajeLN("Ingrese la cantidad de horas conducidas:  ");
        int horas;
        do {
            horas = Consola.leerInt();
            if (horas <= 0) {
                Consola.emitirMensajeLN(
                        "ERROR, la cantidad de horas conducidas no puede ser menor o igual que 0, ingrese de nuevo: ");
            }

        } while (horas <= 0);
        setHoras(horas);
    } // end leerHoras

    // no carga el codigo. El tipo se carga en el constructor de la subclase
    public void cargarDatos() {
        leerHoras();
    }

    @Override
    public int tamRegistro() {
        return TAM_REGISTRO_TOTAL;
    }

    @Override
    public int tamArchivo() {
        return TAM_ARCHIVO; // cantidad de registros que tendra el archivo
    }

    @Override
    public int hashCode() {

        return codT;
    }

    @Override
    public abstract String toString();

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
}
