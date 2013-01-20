package plg.gr3;

import plg.gr3.debug.Debugger;

/**
 * Clase que representa un error de cualquier tipo.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Error {
    
    /** @return Mensaje de error que se le montrará al usuario, sin información específica de la posición del error */
    public abstract String getErrorMessage ();
    
    /** Imprime por consola el error, utilizando {@link Debugger#error} */
    public abstract void print ();
}
