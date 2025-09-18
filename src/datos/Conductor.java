
package datos;

import entradaDatos.Consola;
import java.io.RandomAccessFile;
import persistencia.Grabable;
import persistencia.Registro;

public class Conductor implements Grabable {
    // atributos
    public int dni;
    public String ape_nom;

    private static final int TAM_ARCHIVO = 100; // cantidad de registros que tendra el archivo
    private static final int TAM_REG = 44; // a confirmar
    private final int TAM_APE_NOM = 20;

    // constructor
    public Conductor() {
        dni = 0;
        ape_nom = "";
    }

    public Conductor(int dni, String ape_nom) {
        this.dni = dni;
        this.ape_nom = ape_nom;
    }

    // getters y setters
    public int getDni() {
        return dni;
    }

    private void setDni(int dni) {
        this.dni = dni;
    }

    public String getApe_nom() {
        return ape_nom;
    }

    public void setApe_nom(String ape_nom) {
        this.ape_nom = ape_nom;
    }

    // metodos

    public void leerDni() {
        int dni;
        do {
            Consola.emitirMensaje("Ingrese el DNI del conductor: ");
            dni = Consola.leerInt();
            if (dni <= 0 || dni >= 99999999) { // posiblemente a revisar
                Consola.emitirMensajeLN("ERROR. El DNI debe ser de 8 cifras");
            }
        } while (dni <= 0 && dni >= 99999999);
        setDni(dni);
    }

    private void leerApe_nom() {
        String nom;
        do {
            Consola.emitirMensaje("Ingrese el apellido y nombre del conductor: ");
            nom = Consola.leerString();
            if (nom.isEmpty()) {
                Consola.emitirMensajeLN("[ERROR] el Nombre no puede ser vacio");
            }
            if (nom.length() > TAM_APE_NOM) {
                Consola.emitirMensajeLN("[ERROR] El nombre no puede superar los " + TAM_APE_NOM + " caracteres");
            }
        } while (nom.isEmpty() || nom.length() > TAM_APE_NOM);
        setApe_nom(nom);

    }

    public void cargarDatos() {
        leerApe_nom();
    }

    @Override
    public int tamRegistro() {
        // codear
        return TAM_REG;
    }

    @Override
    public int tamArchivo() {
        // codear
        return TAM_ARCHIVO;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(dni);
            Registro.writeString(a, ape_nom, TAM_APE_NOM);
        } catch (Exception e) {
            System.out.println("[ERROR] al grabar el registro de conductor");
        }
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        try {
            if (val == 0) {
                dni = a.readInt();
            }
            ape_nom = Registro.leerString(a, TAM_APE_NOM).trim();
        } catch (Exception e) {
            System.out.println("[ERROR] al leer el registro de conductor");
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
    public int hashCode() {
        double raiz = Math.sqrt(5) - 1 / 2;
        double dec = (raiz * dni) - Math.floor(raiz * dni);
        int hash = (int) Math.floor(TAM_ARCHIVO * dec);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Conductor other = (Conductor) obj;
        if (dni != other.dni)
            return false;
        if (ape_nom == null) {
            if (other.ape_nom != null)
                return false;
        } else if (!ape_nom.equals(other.ape_nom))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Conductor [dni=" + dni + ", ape_nom=" + ape_nom + "]";
    }
}
