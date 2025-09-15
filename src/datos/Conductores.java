
package datos;

import entradaDatos.Consola;
import java.io.RandomAccessFile;
import persistencia.Grabable;


public class Conductores implements Grabable{
    //atributos
    public long dniC;
    public String ape_nom;
    
     private static final int TAMARCHIVO = 100; // cantidad de registros que tendra el archivo
    private static final int TAMAREG = 64; //a confirmar 
    
    //constructor
    public Conductores(){
        
    }
    
    public Conductores(int dniC, String ape_nom){
        this.dniC = dniC;
        this.ape_nom = ape_nom;
    }
    
    //getters y setters
    public long getDniC(){
        return dniC;
    }
    
    private void setDniC(int dniC){
        this.dniC = dniC;
    }
    
    public String getApe_nom(){
        return ape_nom;
    }
    
    public void setApe_nom(String ape_nom){
        this.ape_nom = ape_nom;
    }
    
    //metodos
    
    private void leerDniC(){
        Consola.emitirMensajeLN("ingrese el DNI del conductor: ");
        int dni;
        do{
            dni = Consola.leerInt();
            if(dni <= 0 && dni >= 99999999){ //posiblemente a revisar
                Consola.emitirMensajeLN("ERROR. El DNI debe ser de 8 cifras, por favor ingrese de nuevo: ");
            }
        } while(dni <= 0 && dni >= 99999999);
        setDniC(dni);
    }
    
    private void leerApe_nom(){
       Consola.emitirMensajeLN("Ingrese el apellido y nombre del conductor: ");
       String nom;
       do{
           nom = Consola.leerString();
           if(nom.isEmpty()){
               Consola.emitirMensajeLN("ERROR. el nombre no puede ser vacio, por favor ingrese de nuevo: ");
           }
       }while(nom.isEmpty());
       setApe_nom(nom);
                
    }
    
    public void cargarDatos(){
        Consola.emitirMensajeLN("-- CARGANDO DATOS DEL CONDUCTOR --");
        leerDniC();
        leerApe_nom();
        Consola.emitirMensajeLN("----------------------------------");
    }
    
    @Override
    public int tamRegistro() {
        //codear
        return 1;
    }

    @Override
    public int tamArchivo() {
        //codear
        return 1;
    }

    @Override
    public void grabar(RandomAccessFile a) {
        //codear
    }

    @Override
    public void leer(RandomAccessFile a, int val) {
        //codear
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
    
}
