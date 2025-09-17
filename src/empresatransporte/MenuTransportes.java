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
                    try {
                        baja();
                    } catch (Exception e) {
                        Consola.emitirMensajeLN("[ERROR - BAJA TRANSPORTES] " + e.getMessage());
                    }
                    break;
                case 3:
                    // modificacion
                    try {
                        modificarTransporte();
                    } catch (Exception e) {
                        Consola.emitirMensajeLN("[ERROR - MODIFICACION TRANSPORTES] " + e.getMessage());
                    }
                    break;
                case 0:
                    Consola.emitirMensajeLN("[Volviendo al menu Princiapal...]");
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
    
    
    private void baja(){
        archTM.abrirParaLeerEscribir();
        archTP.abrirParaLeerEscribir();
        int op;
        
        do{
            archTM.irPrincipioArchivo();
            Consola.emitirMensajeLN("Ingrese el codigo del Transporte a dar de baja: ");
            long codt = Consola.leerInt();
            archTM.buscarRegistro(codt);
            Registro reg = archTM.leerRegistro();
            Transporte t = (Transporte)reg.getDatos();
            Consola.emitirMensajeLN("");
            Consola.emitirMensajeLN("Informacion del transporte: ");
            Consola.emitirMensajeLN("-------------------------------");
            Consola.emitirMensajeLN(t.toString());
            Consola.emitirMensajeLN("-------------------------------");
            Consola.emitirMensajeLN("Confirmar baja? <1.Aceptar> <2.Cancelar>");
            int opc = Consola.leerInt();
            switch(opc){
                case 1:
                    archTM.bajaRegistro(reg);
                    Consola.emitirMensajeLN("[Baja realizada con Exito]");
                    break;
                case 2:
                    Consola.emitirMensajeLN("[Solicitud de baja cancelada]");
                    break;
                default:
                    Consola.emitirMensajeLN("[ERROR] opcion invalida.");
                    break;               
            } //end switch
             Consola.emitirMensajeLN("-------------------------------");
            Consola.emitirMensajeLN("Desea continuar? (1.Si | 0.No): ");
            op  = Consola.leerInt();
            
        }while(op != 0);
        archTM.cerrarArchivo();
        archTP.cerrarArchivo();
    } //end baja()
    
    

 private void modificarTransporte(){
        char opc = 'n';
        Archivo arch;
        Registro reg;
        Transporte t;
       do{
            Consola.emitirMensajeLN("Ingrese el codigo del Transporte para modificar:");
        int cod = Consola.leerInt();
        reg = buscarTransporte(cod);
        t = (Transporte) reg.getDatos();
        
        char res;
        do{
            switch (t.getTipo()) {
                case 'P':
                    modificarTransportePersonas((TransportePersonas) t);
                    arch = archTP;
                    break;
                case 'M':
                    modificarTransporteMercaderia((TransporteMercaderia) t);
                    arch = archTM;
                    break;
                default:
                    break;
            }
            Consola.emitirMensajeLN("Desea continuar? (s/n): ");
            res = Consola.leerChar();
        }while(res != 'n'); 
        
        Consola.emitirMensajeLN("Desea modificar otro registro? (s/n): ");
       }while(opc != 's');
        Registro regActualizado = new Registro(t, reg.getNroOrden());
        arch.grabarRegistro(regActualizado);
    } //end modificarTransporte
 
    
 
private void modificarTransportePersonas(TransportePersonas t){
       
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("El transporte de personas seleccionado es: ");
        Consola.emitirMensajeLN(t.toString());
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("");
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("Seleccione el campo a modificar: ");
        Consola.emitirMensajeLN("1. Cantidad de horas");
        Consola.emitirMensaje("2. DNI del conductor asociado");
        Consola.emitirMensajeLN("3. Cantidad de pasajeros");
        Consola.emitirMensajeLN("0. Volver");
        Consola.emitirMensajeLN("--------------------------------------------");
        int opc = Consola.leerInt();
        switch(opc){
            case 1:
                t.leerHoras();
                break;
            case 2:
                try{
                    int dni;
                    do{
                        dni = leerDniConductor();
                    }while(!existeConductor(dni));      
                    t.setDniConductor(dni);
                }catch(ConductorInexistenteException e){
                    Consola.emitirMensajeLN("[ERROR] " + e.getMessage());
                }
                break;
            case 3:
                t.leerCantPasajeros();
                break;
            case 0:
                break;
            default:
                Consola.emitirMensajeLN("[ERROR] Opcion incorrecta.");
        }
    } //end mofidicar transporte personas


    private void modificarTransporteMercaderia(TransporteMercaderia t){
       
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("El transporte de Mercaderia seleccionado es: ");
        Consola.emitirMensajeLN(t.toString());
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("");
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("Seleccione el campo a modificar: ");
        Consola.emitirMensajeLN("1. Cantidad de horas.");
        Consola.emitirMensaje("2. DNI del conductor asociado.");
        Consola.emitirMensajeLN("3. Toneladas Transportadas.");
        Consola.emitirMensajeLN("4. Es peligroso.");
        Consola.emitirMensajeLN("0. Volver");
        Consola.emitirMensajeLN("--------------------------------------------");
        int opc = Consola.leerInt();
        switch(opc){
            case 1:
                t.leerHoras();
                break;
            case 2:
                try{
                    int dni;
                    do{
                        dni = leerDniConductor();
                    }while(!existeConductor(dni));      
                    t.setDniConductor(dni);
                }catch(ConductorInexistenteException e){
                    Consola.emitirMensajeLN("[ERROR] " + e.getMessage());
                }
                break;
            case 3:
                t.leerToneladasTrans();
                break;
            case 4:
                t.leerEsPeligroso();
                break;
            case 0:
                break;
            default:
                Consola.emitirMensajeLN("[ERROR] Opcion incorrecta.");
        }
    } //end mofidicarTransporteMercaderia

    //funcion que verificar que el dni ingresado existe en los registros de los conductores.
    //es convocada por modificarTransporteMercaderia y modificarTransportePersonas
    private boolean existeConductor(int dnic){
        archC.abrirParaLectura();
        archC.irPrincipioArchivo();
        boolean existe = false;
        while(!archC.eof()) {
            Registro reg = archC.leerRegistro();
            Conductor c = (Conductor) reg.getDatos();
            if(c.getDni() == dnic){
                existe = true;
            }
        } //end while
        archC.cerrarArchivo();
        return existe;
    }
    
    //funcion buscarTransporte
    
    private Registro buscarTransporte(int cod){
        archTM.abrirParaLectura();
        archTP.abrirParaLectura();
        boolean hayTransportes = false;
        Registro reg = null;
        archTM.irPrincipioArchivo();
        while(!archTM.eof()){
            reg = archTM.leerRegistro();
            Transporte t = (Transporte) reg.getDatos();
            if(reg.getEstado() && t.getCodT() == cod){
                hayTransportes = true;
            }
            
        } //end while
        if(!hayTransportes) {
            Consola.emitirMensajeLN("[ERROR] no se encontro el transporte con el codigo: " + cod);
        }
        return reg;
    }

} //end class
