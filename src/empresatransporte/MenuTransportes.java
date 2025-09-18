
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
        Consola.emitirMensajeLN("\n========= Menu Transportes =========");
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
        Consola.emitirMensajeLN("\n");
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
        Consola.emitirMensajeLN("\n\n--------------- BAJA TRANSPORTE ---------------\n\n");
        archTM.abrirParaLeerEscribir();
        Transporte tAux = new TransporteMercaderia();
        int op;
        
        do{
            archTM.irPrincipioArchivo();
            tAux.leerCodT();

            int nroOrd = tAux.hashCode();
            Transporte t = buscarTransporte(nroOrd);

            if(t != null){
                Consola.emitirMensajeLN("");
                Consola.emitirMensajeLN("Informacion del transporte: ");
                Consola.emitirMensajeLN("-------------------------------");
                Consola.emitirMensajeLN(t.toString());
                Consola.emitirMensajeLN("-------------------------------");
                Consola.emitirMensajeLN("Confirmar baja? <1.Aceptar> <2.Cancelar>");
                int opc = Consola.leerInt();
                switch(opc){
                    case 1:
                        Registro reg = new Registro(t, nroOrd);
                        archTM.bajaRegistro(reg);
                        Consola.emitirMensajeLN("\n[Baja realizada con Exito]");
                        break;
                    case 2:
                        Consola.emitirMensajeLN("\n[Solicitud de baja cancelada]");
                        break;
                    default:
                        Consola.emitirMensajeLN("\n[ERROR] opcion invalida.");
                        break;               
                }//end switch
            }else{
                Consola.emitirMensajeLN("\n[ERROR] No se encontro el transporte con codigo " + tAux.getCodT());
            }

            Consola.emitirMensajeLN("\n-------------------------------");
            Consola.emitirMensajeLN("Desea dar de baja otro transporte? (1.Si | 0.No): ");
            op  = Consola.leerInt();
            
        }while(op != 0);
        archTM.cerrarArchivo();
        Consola.emitirMensajeLN("\n\n-----------------------------------------------\n\n");
    } //end baja()
    
    

 private void modificarTransporte(){
        Consola.emitirMensajeLN("\n\n--------------- MODIFICAR TRANSPORTE ---------------\n\n");
        char opc = 's';
        Archivo arch = null;
        Registro reg = null;

        Transporte t;
        Transporte tAux = new TransporteMercaderia();
       do{
            //Buscar transporte
            Consola.emitirMensajeLN(">> Buscar transporte");
            tAux.leerCodT();
            int nroOrd = tAux.hashCode();
            t = buscarTransporte(nroOrd);
        
            if(t != null){
                //Segun el tipo de transporte, llamar a la funcion correspondiente
                char continuar = 's';
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
                    }
                    //preguntar si desea modificar otro campo
                    Consola.emitirMensaje("Desea modificar otro campo? (s/n): ");
                    continuar = Consola.leerChar();
                }while (continuar == 's');

                //Grabar cambios en el archivo
                arch.abrirParaLeerEscribir();
                Registro regMod = new Registro(t, nroOrd);
                arch.grabarRegistro(regMod);
                arch.cerrarArchivo();
                Consola.emitirMensajeLN("[INFO] Transporte modificado con exito.");
            }else{
                Consola.emitirMensajeLN("[ERROR] No se encontro el transporte con codigo " + tAux.getCodT());
            }
            
        

        Consola.emitirMensajeLN("Desea modificar otro registro? (s/n): ");
        opc = Consola.leerChar();
       }while(opc != 'n');
        Registro regActualizado = new Registro(t, reg.getNroOrden());
        arch.grabarRegistro(regActualizado);
        arch.cerrarArchivo();


            //Preguntar si desea modificar otro transporte
            Consola.emitirMensajeLN("Desea modificar otro transporte? (s/n): ");
            opc = Consola.leerChar();
       }
       
 
private void modificarTransportePersonas(TransportePersonas t){
        Consola.emitirMensajeLN("\n--------------------------------------------");
        Consola.emitirMensajeLN("El transporte de personas seleccionado es: ");
        Consola.emitirMensajeLN(t.toString());
        Consola.emitirMensajeLN("--------------------------------------------");

        
        Consola.emitirMensajeLN("");
        Consola.emitirMensajeLN("--------------------------------------------");
        Consola.emitirMensajeLN("Seleccione el campo a modificar: ");
        Consola.emitirMensajeLN("1. Cantidad de horas");
        Consola.emitirMensajeLN("2. DNI del conductor asociado");
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
                    int dni = leerDniConductor();
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
       
        Consola.emitirMensajeLN("\n--------------------------------------------");
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
                        int dni = leerDniConductor();   
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

    
    /**
     * Busca un transporte por su codigo archivo de transportes.
     * @param nroOrd Codigo del transporte a buscar.
     * @return El transporte encontrado, o null si no se encuentra.
     */
    private Transporte buscarTransporte(int nroOrd){
        archTM.abrirParaLectura();
        archTP.abrirParaLectura();
        Transporte encontrado = null;
        
        archTM.buscarRegistro(nroOrd);
        Registro reg = archTM.leerRegistro();
        if(reg.getEstado()) {
            encontrado = (TransporteMercaderia) reg.getDatos();
            if(encontrado.getTipo() == 'P') {
                archTP.buscarRegistro(nroOrd);
                reg = archTP.leerRegistro();
                encontrado = (TransportePersonas) reg.getDatos();
            }
        } 

        archTM.cerrarArchivo();
        archTP.cerrarArchivo();
        return encontrado;
    }


} //end class

