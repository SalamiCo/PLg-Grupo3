package plg.gr3;

/**
 * Clase que implementa el tipo de los Naturales del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class Natural extends Number {
    /**
     * serial version por defecto para poder serializar
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Valor que contiene el Natural (internamente es un int no negativo)
     * */
    private final int value;
    
    /**
     * Constructora del tipo de los Naturales
     * 
     * @param value
     *            valor que contiene el Natural, siempre mayor que cero.
     * */
    public Natural (int value) throws IllegalArgumentException {
        if (value >= 0) {
            this.value = value;
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    @Override
    public double doubleValue () {
        return value;
    }
    
    @Override
    public float floatValue () {
        return value;
    }
    
    @Override
    public int intValue () {
        return value;
    }
    
    @Override
    public long longValue () {
        return value;
    }
    
    public static Natural parseNat (String str) {
        return new Natural(Integer.parseInt(str));
    }
    
    @Override
    public boolean equals (Object obj) {
        boolean boEquals = false;
        if (obj instanceof Natural) {
            Natural anotherObj = (Natural) obj;
            if (this.value == anotherObj.intValue()) {
                boEquals = true;
            }
        }
        return boEquals;
    }
    
    @Override
    public String toString () {
        return String.valueOf(this.value);
    }
    
}
