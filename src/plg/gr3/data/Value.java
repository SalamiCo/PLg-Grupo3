package plg.gr3.data;

/**
 * Un valor de nuestro lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Value {
    
    /** @return El tipo del valor */
    public abstract Type getType ();
    
    /** @return Convierte este valor a un natural */
    public abstract NaturalValue toNaturalValue ();
    
    /** @return Convierte este valor a un entero */
    public abstract IntegerValue toIntegerValue ();
    
    /** @return Convierte este valor a un flotante */
    public abstract FloatValue toFloatValue ();
    
    /** @return Convierte este valor a un caracter */
    public abstract CharacterValue toCharacterValue ();
}
