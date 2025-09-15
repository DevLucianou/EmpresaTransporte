
package datos;

import entradaDatos.Consola;
import java.io.RandomAccessFile;
import persistencia.*;
import java.io.*;


public class TransporteMercaderia extends Transporte implements Grabable, ICalculable{
    
    //atributos
    public int toneladasTrans;
    public boolean esPeligroso;
    
    private static final int TAMARCHIVO = 100; // cantidad de registros que tendra el archivo
    private static final int TAMAREG = 64; //a confirmar 
    
    //constructor
    public TransporteMercaderia(){
        
    }
    
    
    public TransporteMercaderia(int codet, char tipo, int horas, int dniAsociado, int toneladasTrans, boolean esPeligroso){
        this.codet = codet;
        this.tipo = tipo;
        this.horas = horas;
        this.dniAsociado = dniAsociado;
        this.toneladasTrans = toneladasTrans;
        this.esPeligroso = esPeligroso;
    } 
    
    //getters y setters
    public int getToneladasTrans(){
        return toneladasTrans;
    }
    
    public void setToneladasTrans(int toneladasTrans){
        this.toneladasTrans = toneladasTrans;
    }
    
    public boolean getEsPeligroso(){
        return esPeligroso;
    }
    
    public void setEsPeligroso(boolean esPeligroso) {
        this.esPeligroso = esPeligroso;
    }
    
    //metodos 
    
    public void leerToneladasTrans(){
        Consola.emitirMensajeLN("Ingrese la cantidad de toneladas transportadas: ");
        int cantt;
        do{
            cantt = Consola.leerInt();
            if(cantt <= 0) {
                Consola.emitirMensajeLN("ERROR. la cantidad de toneladas transportadas no puede ser menor ni igual a 0, por favor ingrese de nuevo: ");
            }
        }while(cantt <= 0);
        setToneladasTrans(cantt);
    } //end leertoneladas
    
    public void leerEsPeligroso(){
        Consola.emitirMensajeLN("La mercancia es peligrosa? (1 - SI | 2 - NO): ");
        int op;
        do{
            op = Consola.leerInt();
            if(op != 1 || op != 2){
                Consola.emitirMensajeLN("ERROR, por favor seleccione una de las opciones validas (1 - SI | 2 - NO), intente de nuevo: ");
            }
        }while(op != 1 || op != 2 );
        if(op == 1){
            setEsPeligroso(true);
        }
        else {
            setEsPeligroso(false);
        }
    } //end espeligroso
    
    @Override
    public void cargarDatos(){
        Consola.emitirMensajeLN("-- CARGANDO INFORMACION DEL TRANSPORTE DE PERSONAS --");
        super.cargarDatos();
        leerToneladasTrans();
        leerEsPeligroso();
        Consola.emitirMensajeLN("-----------------------------------------------------");
    }

    @Override
    public String toString() {
       //codear
       return "";
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
            a.writeInt(toneladasTrans);
            a.writeBoolean(esPeligroso);
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
               toneladasTrans = a.readInt();
               esPeligroso = a.readBoolean();
         
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
