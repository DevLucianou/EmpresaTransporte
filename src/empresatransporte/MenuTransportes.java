package empresatransporte;

import datos.Conductor;
import datos.Transporte;
import datos.TransporteMercaderia;
import datos.TransportePersonas;
import entradaDatos.Consola;
import excepciones.ConductorInexistenteException;
import persistencia.Archivo;
import persistencia.Registro;

public class MenuTransportes {
    Archivo archC;
    Archivo archTP; // transporte pasajeros
    Archivo archTM; // transporte mercancias

    public MenuTransportes(Archivo archC, Archivo archTP, Archivo archTM) {
        this.archC = archC;
        this.archTP = archTP;
        this.archTM = archTM;
    }

    private void mostrarMenu() {
        Consola.emitirMensajeLN("========= Menu Transportes =========");
        Consola.emitirMensajeLN("1. Alta");
        Consola.emitirMensajeLN("2. Baja");
        Consola.emitirMensajeLN("3. Modificacion");
        Consola.emitirMensajeLN("0. Volver al menu principal");
        Consola.emitirMensajeLN("=====================================");
    }

    public void iniciar() {
        int opc;
        do {
            mostrarMenu();
            opc = Consola.leerInt();
            switch (opc) {
                case 1:
                    // alta
                    try {
                        alta();
                    } catch (Exception e) {
                        Consola.emitirMensajeLN("[ERROR - ALTA TRANSPORTES] " + e.getMessage());
                    }
                    break;
                case 2:
                    // baja
                    break;
                case 3:
                    // modificacion
                    break;
                case 0:
                    // salir
                    break;
                default:
                    Consola.emitirMensajeLN("Opcion incorrecta");
            }
        } while (opc != 0);
    }

    // CAMBIAR MANEJO DE EXCEPCIONES
    private void alta() throws Exception {
        Consola.emitirMensaje("--------------- ALTA TRANSPORTE ---------------");
        Consola.emitirMensajeLN("Seleccione el tipo de transporte a dar de alta:");
        Consola.emitirMensajeLN("1. Transporte de pasajeros");
        Consola.emitirMensajeLN("2. Transporte de mercaderias");
        Consola.emitirMensaje("Ingrese una opcion: ");
        int tipo = Consola.leerInt();

        Transporte t;
        Archivo arch;

        switch (tipo) {
            case 1:
                t = new TransportePersonas();
                arch = archTP;
                break;
            case 2:
                t = new TransporteMercaderia();
                arch = archTM;
                break;
            default:
                throw new Exception("Tipo de transporte no valido");
        }

        arch.abrirParaLeerEscribir();

        boolean b = true;
        while (b) {
            t.leerCodT();
            arch.buscarRegistro(t.hashCode());
            Registro reg = arch.leerRegistro();
            if (reg.getEstado()) {
                Consola.emitirMensajeLN("El codigo de transporte ya existe. Ingrese otro.");
            } else {
                b = false;
            }
        }

        int dni;
        try { // necesario para cerrar el archivo en caso de error
            dni = leerDniConductor();
        } catch (ConductorInexistenteException e) {
            arch.cerrarArchivo();
            throw e;
        }
        t.setDniConductor(dni);
        t.cargarDatos();

        int nroOrden = t.hashCode();
        Registro reg = new Registro(t, nroOrden);
        arch.grabarRegistro(reg);
        arch.cerrarArchivo();

        Consola.emitirMensajeLN("\n[INFO] Transporte cargado con exito.\n");
        Consola.emitirMensajeLN("\n-----------------------------------------------\n");
    }

    private int leerDniConductor() throws ConductorInexistenteException {
        archC.abrirParaLectura();
        Conductor c = new Conductor();

        c.leerDni();
        archC.buscarRegistro(c.hashCode());
        Registro reg = archC.leerRegistro();

        Conductor condEnc = (Conductor) reg.getDatos();
        if (!reg.getEstado()) {
            archC.cerrarArchivo();
            throw new ConductorInexistenteException("El conductor con DNI " + c.getDni() + " no existe.");
        }

        archC.cerrarArchivo();
        return condEnc.getDni();
    }

}
