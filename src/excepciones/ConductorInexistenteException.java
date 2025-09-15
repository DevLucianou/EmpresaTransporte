
package excepciones;


public class ConductorInexistenteException extends TransporteException {
    public ConductorInexistenteException(String mensaje){
        super(mensaje);
    }
}
