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

    /** @return Convierte este valor a un boolean */
    public abstract BooleanValue toBooleanValue ();

    /**
     * @param other Valor a comparar
     * @return un valor <i>negativo</i>, <i>positivo</i> o <i>cero</i>, dependiendo de si <tt>this</tt> es <i>menor</i>,
     *         <i>mayor</i> o <i>igual</i> que <tt>other</tt>.
     */
    public abstract int compare (Value other);

    /**
     * @param type Tipo al que hacer el casting
     * @return Valor convertido
     */
    public final Value castTo (Type type) {
        if (type.equals(Type.CHARACTER)) {
            return toCharacterValue();

        } else if (type.equals(Type.FLOAT)) {
            return toFloatValue();

        } else if (type.equals(Type.INTEGER)) {
            return toIntegerValue();

        } else if (type.equals(Type.NATURAL)) {
            return toNaturalValue();

        } else if (type.equals(Type.BOOLEAN)) {
            return toBooleanValue();

        } else {
            throw new IllegalArgumentException("Cannot cast to type " + type);
        }
    }
}
