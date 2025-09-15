
package datos;

import entradaDatos.Consola;


public abstract class Transporte {
    //atributos
    
    protected int codet;
    protected char tipo;
    protected int horas;
    protected int dniAsociado;
    
    //getters y setters
      
    public int getCodet(){
        return codet;
    }
    
    private void setCodeT(int codeT){
        this.codet = codet;
    }
    
   public char getTipo(){
       return tipo;
   }
   
   public void setTipo(char tipo){
       this.tipo = tipo;
   }
   
   public int getHoras(){
       return horas;
   }
   public void setHoras(int horas){
       this.horas = horas;
   }
   
   public int getDniAsociado(){
       return dniAsociado;
   }
   
   public void setDniAsociado(int dniAsociado){
       this.dniAsociado = dniAsociado;
   }
    
    //metodos 
   
   public void leerCodeT(){
       Consola.emitirMensajeLN("Ingrese el codigo del transporte (1 -99): ");
       int cod;
       do{
           cod = Consola.leerInt();
           if(cod <= 0 && cod > 99){
               Consola.emitirMensajeLN("ERROR. debe ingresar un codigo dentro del rango valido (1-99), ingrese de nuevo: ");
           }
       }while(cod <= 0 && cod > 99);
       setCodeT(cod);
   } //end leerCodeT
   
   public void leerTipo(){
       Consola.emitirMensajeLN("Ingrese el tipo de transporte (M - mercaderia | P - personas): ");
       char tipo;
       do{
           tipo = Consola.leerChar();
           if(tipo != 'M' && tipo != 'P'){
               Consola.emitirMensajeLN("ERROR, debe ingresar un tipo valido (M - Mercaderia | P - peronas), ingrese de nuevo: ");
           }
       }while(tipo != 'M' && tipo != 'P');
       setTipo(tipo);
   } //leerTipo
   
   public void leerHoras(){
       Consola.emitirMensajeLN("Ingrese la cantidad de horas conducidas:  ");
       int horas;
       do{
           horas = Consola.leerInt();
           if(horas <= 0) {
               Consola.emitirMensajeLN("ERROR, la cantidad de horas conducidas no puede ser menor o igual que 0, ingrese de nuevo: ");
           }
          
       }while(horas <= 0);
        setHoras(horas);
   } //end leerHoras
   
   public void leerDniAsociado(){
       //codear
       
       
   } //end leerDniAsociado
   
   public void cargarDatos(){
      
       leerCodeT();
       leerTipo();
       leerHoras();
       leerDniAsociado();
   }
   
   
   
    @Override
   public abstract String toString();
   
}
