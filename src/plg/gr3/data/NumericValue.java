package plg.gr3.data;

/**
 * Representación de los valores numéricos <tt>natural</tt>, <tt>integer</tt> y <tt>float</tt>.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class NumericValue extends Value {
    
    /* package */NumericValue () {
        // Solo haytres tipos numéricos
    }
    
    /**
     * @param other Valor a sumar
     * @return Suma de los valores <tt>this</tt> y <tt>other</tt>
     */
    public abstract NumericValue add (NumericValue other);
    
    /**
     * @param other Valor a restar
     * @return Resta de los valores <tt>this</tt> y <tt>other</tt>
     */
    public abstract NumericValue subtract (NumericValue other);
    
    /**
     * @param other Valor a multiplicar
     * @return Resta de los valores <tt>this</tt> y <tt>other</tt>
     */
    public abstract NumericValue multiply (NumericValue other);
    
    /**
     * @param other Valor a dividir
     * @return División de los valores <tt>this</tt> y <tt>other</tt>
     */
    public abstract NumericValue divide (NumericValue other);
    
    /**
     * @param other Valor a dividir paraobtener el resto
     * @return Resto de la división de los valores <tt>this</tt> y <tt>other</tt>
     */
    public abstract NumericValue modulo (NumericValue other);
    
    @Override
    public final int compare (Value other) {
        // nat, int y float sólo puedencompararse a otros nat, int y float
        if (!(other instanceof NumericValue)) {
            throw new IllegalArgumentException(other.toString());
        }
        
        if (this instanceof FloatValue || other instanceof FloatValue) {
            // Si alguno de los dos es float, convertimos el otro a float y comparamos
            Float thisValue = Float.valueOf(toFloatValue().getValue());
            Float otherValue = Float.valueOf(other.toFloatValue().getValue());
            return thisValue.compareTo(otherValue);
            
        } else {
            // Agrupamos nate int en un solo caso, puesto que internamente en realidad son lo mismo
            Integer thisValue = Integer.valueOf(toIntegerValue().getValue());
            Integer otherValue = Integer.valueOf(other.toIntegerValue().getValue());
            return thisValue.compareTo(otherValue);
            
        }
    }
}
