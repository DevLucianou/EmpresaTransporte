
package datos;

import entradaDatos.Consola;
import java.io.RandomAccessFile;
import persistencia.*;
import java.io.*;


public class TransportePersonas extends Transporte implements Grabable, ICalculable{
    //atributos
    public int cantPasajeros;
    
    
    private static final int TAMARCHIVO = 100; // cantidad de registros que tendra el archivo
    private static final int TAMAREG = 64; //a confirmar 
    
    //constructor
    public TransportePersonas(){
        
    }
    
    public TransportePersonas(int codet, char tipo, int horas, int dniAsociado, int cantPasajeros){     
        this.codet = codet;
        this.tipo = tipo;
        this.horas = horas;
        this.dniAsociado = dniAsociado;
        this.cantPasajeros = cantPasajeros;
    }
    
    //getters y setters
    public int getCantPasajeros(){
        return cantPasajeros;
    }
    
    public void setCantPasajeros(int cantPasajeros){
        this.cantPasajeros = cantPasajeros;
    }
    
    //metodos
    
    private void leerCantPasajeros(){
       Consola.emitirMensajeLN("Ingrese la cantidad de personas que transporta: ");
       int cantp;
       do{
           cantp = Consola.leerInt();
           if(cantp <= 0){
               Consola.emitirMensajeLN("ERROR, la cantidad de personas que transporta no puede ser menor o igual que 0, ingrese de nuevo: ");
           }
       }while(cantp <= 0);
       setCantPasajeros(cantp);
    }

    @Override
    public void cargarDatos() {
        Consola.emitirMensajeLN("-- CARGANDO INFORMACION DEL TRANSPORTE DE PERSONAS --");
        super.cargarDatos();
        leerCantPasajeros();
        Consola.emitirMensajeLN("-----------------------------------------------------");
    }

    @Override
    public String toString() {
        //codear
        return"";
        }
    
    @Override
    public int tamRegistro() {
       return TAMAREG;
    }

    @Override
    public int tamArchivo() {
        return TAMARCHIVO;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        try {
            a.writeInt(codet);
            a.writeChar(tipo);
            a.writeInt(horas);
            a.writeInt(dniAsociado);
            a.writeInt(cantPasajeros);
            // el metodo es estatico se convoca con el nombre de la clase
            
        } catch (IOException e) {
            System.out.println("Error al grabar el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
         try {
            if(val == 0)
                codet= a.readInt();
               tipo = a.readChar();
               horas = a.readInt();
               dniAsociado = a.readInt();
               cantPasajeros = a.readInt();
         
        } catch (IOException e) {
            System.out.println("Error al leer el registro: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void mostrarRegistro(int val, boolean activo) {
       //codear
    }

       //grabable
    @Override
    public void cargarDatos(int val) {
       //codear
    }

    @Override
    public float calcularExtra() {
        //codear
        return 1;
    }
}
