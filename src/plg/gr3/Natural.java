package plg.gr3;

/**
 * Clase que implementa el tipo de los Naturales del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class Natural {
    /**
     * Valor que contiene el Natural
     * */
    private final int value;
    
    /**
     * Constructora del tipo de los Naturales
     * 
     * @param value
     *            valor que contiene el Natural, siempre mayor que cero.
     * */
    public Natural (int value) throws IllegalArgumentException {
        if (value >= 0)
            this.value = value;
        else
            throw new IllegalArgumentException();
    }
    
    /**
     * @return valor del Natural
     * */
    public int getValue () {
        return value;
    }
    
}
