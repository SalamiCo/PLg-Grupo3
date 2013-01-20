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
     * @throws IllegalArgumentException
     *             si el valor pasado es negativo
     */
    public Natural (int value) {
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
    
    /**
     * @param str
     *            Natural a parsear
     * @return Natural leído
     */
    public static Natural parseNat (String str) {
        int i = Integer.parseInt(str);
        if (i < 0) {
            throw new NumberFormatException(str);
        }
        
        return new Natural(i);
    }
    
    @Override
    public int hashCode () {
        return value;
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Natural)) {
            return false;
        }
        
        Natural nat = (Natural) obj;
        return (value == nat.value);
    }
    
    @Override
    public String toString () {
        return String.valueOf(this.value);
    }
    
}
