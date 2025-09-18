package empresatransporte;

import java.io.File;

import datos.*;
import persistencia.*;
import entradaDatos.Consola;

public class MenuPrincipal {
    private Archivo archC;
    private Archivo archTP; // transporte pasajeros
    private Archivo archTM; // transporte mercancias
    private MenuTransportes menuT;

    // CONSTANTES
    private final String ARCHIVO_CONDUCTORES = "conductores.dat";
    private final String ARCHIVO_TRANSPORTES = "transportes.dat";

    public MenuPrincipal() {
        TransportePersonas tp = new TransportePersonas();
        TransporteMercaderia tm = new TransporteMercaderia();
        Conductor c = new Conductor();
        try {
            archTP = new Archivo(ARCHIVO_TRANSPORTES, tp);
            archTM = new Archivo(ARCHIVO_TRANSPORTES, tm);
            archC = new Archivo(ARCHIVO_CONDUCTORES, c);
            menuT = new MenuTransportes(archC, archTP, archTM);
            inicializarArchivos();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void inicializarArchivos() {
        File fdCond = archC.getFd();
        File fdTrans = archTM.getFd();

        if (!fdCond.exists()) { // si no existe el archivo de conductores, lo crea
            Registro regT = new Registro(new Conductor(), 0);
            archC.crearArchivoVacio(regT);
        }

        if (!fdTrans.exists()) { // si no existe el archivo de transportes, lo crea
            // para cualquiera de los dos tipos de transporte
            Registro regT = new Registro(new TransporteMercaderia(), 0);
            archTM.crearArchivoVacio(regT);
        }
        if (!fdCond.exists()) { // si no existe el archivo de transportes, lo crea
            // para cualquiera de los dos tipos de transporte
            Registro regC = new Registro(new Conductor(), 0);
            archC.crearArchivoVacio(regC);
        }
    }

    private void mostrarMenu() {
        Consola.emitirMensajeLN("========= Menu Principal =========");
        Consola.emitirMensajeLN("1. Cargar conductor");
        Consola.emitirMensajeLN("2. Actualizacion transportes");
        Consola.emitirMensajeLN("3. Listar sueldos");
        Consola.emitirMensajeLN("4. Listar transportes por conductor");
        Consola.emitirMensajeLN("-1 . Mostrar conductores (TEST)");
        Consola.emitirMensajeLN("-2 . Mostrar transportes (TEST)");
        Consola.emitirMensajeLN("0. Salir");
        Consola.emitirMensajeLN("==================================");
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenu();
            Consola.emitirMensaje("Seleccione una opcion: ");
            opcion = Consola.leerInt();
            switch (opcion) {
                case 1:
                    cargarConductor();
                    break;
                case 2:
                    actualizarTransportes();
                    break;
                case 3:
                    // listar sueldos
                    listarSueldos();
                    break;
                case 4:
                    // listar transportes por conductor
                    listarTransportesPorConductor();
                    break;
                case 0:
                    Consola.emitirMensajeLN("Saliendo del programa...");
                    break;
                default:
                    Consola.emitirMensajeLN("[!] Opcion invalida. Por favor, intente de nuevo.");
            }

            switch (opcion) {
                case -1:
                    listarConductoresActivos();
                    break;
                case -2:
                    listarTransportesActivos();
                    break;
            }
        } while (opcion != 0);
    }

    private void actualizarTransportes() {
        Consola.emitirMensajeLN("\n\n");
        menuT.iniciar();
    }

    private void listarTransportesPorConductor() {
        Consola.emitirMensajeLN("\n\n----- Listar Transportes por Conductor -----\n\n");
        archC.abrirParaLectura();

        Conductor condTmp = new Conductor();
        condTmp.leerDni();
        archC.buscarRegistro(condTmp.hashCode());
        Registro reg = archC.leerRegistro();
        if (reg.getEstado()) {
            Conductor cond = (Conductor) reg.getDatos();
            System.out.println("");
            mostrarTransportesDeConductor(cond);
        } else {
            Consola.emitirMensajeLN("[ERROR] No existe un conductor con ese DNI.");
        }
        archC.cerrarArchivo();
        Consola.emitirMensajeLN("\n\n---------------------------------------------\n\n");
    }

    private void mostrarTransportesDeConductor(Conductor cond) {
        // Recorremos a partir de un archivo pero necesitamos los dos para obtener el
        // objeto correcto
        archTM.abrirParaLectura();
        archTP.abrirParaLectura();
        boolean tieneTransportes = false;

        archTM.irPrincipioArchivo();
        while (!archTM.eof()) {
            Registro reg = archTM.leerRegistro();
            Transporte t = (Transporte) reg.getDatos();
            if (reg.getEstado() && t.getDniConductor() == cond.getDni()) {
                tieneTransportes = true;

                if (t.getTipo() == 'P') {
                    archTP.buscarRegistro(reg.getNroOrden());
                    reg = archTP.leerRegistro();
                    t = (Transporte) reg.getDatos();
                }

                t.calcularExtra();
                Consola.emitirMensajeLN(t.toString());
                Consola.emitirMensajeLN("Conductor asociado: " + cond.getApe_nom());
            }
        }

        if (!tieneTransportes) {
            Consola.emitirMensajeLN("El conductor " + cond.getApe_nom() + " no tiene transportes asociados.");
        }
        archTM.cerrarArchivo();
        archTP.cerrarArchivo();
    }

    private void listarSueldos() {

         Consola.emitirMensajeLN("\n\n----- Listar Sueldos de Conductores -----\n\n");
            archC.abrirParaLectura();
            archC.irPrincipioArchivo();
            boolean hayConductores = false;

            while(!archC.eof()){
                Registro reg = archC.leerRegistro();
                if(reg.getEstado()){
                    hayConductores = true;
                    Conductor c = (Conductor) reg.getDatos();
                   double totalExtras = calcularTotalExtras(c);
                   double sueldoFijo = 400000;
                   double sueldoTotal = (double) sueldoFijo + totalExtras;
                   Consola.emitirMensajeLN("-------------------------------");
                   Consola.emitirMensajeLN(c.toString());
                   Consola.emitirMensajeLN("Sueldo total: " + sueldoTotal);
                   Consola.emitirMensajeLN("-------------------------------");
                }

            }//end while
            if(!hayConductores){
                Consola.emitirMensajeLN("No hay conductores registrados");
            }
            archC.cerrarArchivo();
            Consola.emitirMensajeLN("\n\n-----------------------------------------\n\n");
    } //end listarSueldos
    
    /*Funcion que devuelve el calculo de el total de extras por cada transporte asociado al conductor*/

    private double calcularTotalExtras(Conductor c){
        archTM.abrirParaLectura();
        archTP.abrirParaLectura();
        archTM.irPrincipioArchivo();
        double totalExtra = 0;

        while(!archTM.eof()) {
            Registro reg = archTM.leerRegistro();
            Transporte t = (Transporte) reg.getDatos();
            if(reg.getEstado() && t.getDniConductor() == c.getDni()){
                if(t.getTipo() == 'P'){
                    archTP.buscarRegistro(reg.getNroOrden());
                    reg = archTP.leerRegistro();
                    t = (Transporte) reg.getDatos();
                }
                t.calcularExtra();
                totalExtra += t.getExtra();
            }

        } //end while
        archTM.cerrarArchivo();
        archTP.cerrarArchivo();
        return totalExtra;
    } //end calcularTotalExtras
    

    private void cargarConductor() {
        Consola.emitirMensajeLN("\n----- Cargar Conductor -----\n");

        archC.abrirParaLeerEscribir();
        Conductor c = new Conductor();

        boolean b = true;
        while (b) {
            c.leerDni();
            archC.buscarRegistro(c.hashCode());
            Registro reg = archC.leerRegistro();

            if (reg.getEstado()) {
                Consola.emitirMensajeLN("[ERROR] Ya existe un conductor con ese DNI.");
            } else {
                b = false;
            }
        }

        c.cargarDatos(); // cargar datos restantes
        int nroOrd = c.hashCode();
        Registro reg = new Registro(c, nroOrd);
        archC.grabarRegistro(reg);
        archC.cerrarArchivo();

        Consola.emitirMensajeLN("\n[INFO] Conductor cargado con exito.\n");
        Consola.emitirMensajeLN("\n----------------------------\n");
    }

    /*
     * =============================================================================
     * -----------------------------|| METODOS DE TEST||----------------------------
     * =============================================================================
     */

    private void listarConductoresActivos() {
        Consola.emitirMensajeLN("\n\n----- Lista de Conductores (TEST) -----\n\n");

        archC.abrirParaLectura();
        archC.irPrincipioArchivo();
        boolean hayConductores = false;

        while (!archC.eof()) {
            Registro reg = archC.leerRegistro();
            if (reg.getEstado()) {
                hayConductores = true;
                Conductor c = (Conductor) reg.getDatos();
                Consola.emitirMensajeLN(c.toString());
                Consola.emitirMensajeLN("");
            }
        }

        if (!hayConductores) {
            Consola.emitirMensajeLN("No hay conductores cargados.");
        }

        archC.cerrarArchivo();
        Consola.emitirMensajeLN("\n\n--------------------------------\n\n");
    }

    private void listarTransportesActivos() {
        // Recorremos a partir de un archivo pero necesitamos los dos para obtener el
        // objeto correcto
        archTM.abrirParaLectura();
        archTP.abrirParaLectura();
        boolean hayTransportes = false;

        Consola.emitirMensajeLN("\n\n----- Lista de Transportes (TEST) -----\n\n");
        archTM.irPrincipioArchivo();
        while (!archTM.eof()) {
            Registro reg = archTM.leerRegistro();
            Transporte t = (Transporte) reg.getDatos();
            if (reg.getEstado()) {
                hayTransportes = true;
                System.out.println("\nNroOrde: " + reg.getNroOrden());
                System.out.println("Estado: " + reg.getEstado());
                if (t.getTipo() == 'P') {
                    archTP.buscarRegistro(reg.getNroOrden());
                    reg = archTP.leerRegistro();
                    t = (Transporte) reg.getDatos();
                }

                t.calcularExtra();
                Consola.emitirMensajeLN(t.toString());
            }
        }

        if (!hayTransportes) {
            Consola.emitirMensajeLN("No hay transportes cargados.");
        }
        archTM.cerrarArchivo();
        archTP.cerrarArchivo();
        Consola.emitirMensajeLN("\n\n-------------------------------------\n\n");
    }
}
