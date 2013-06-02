package plg.gr3.parser;

import java.util.Objects;

import plg.gr3.data.Type;

/**
 * Definición de parámetro de un procedimiento.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Parameter {

    private final String name;

    private final Type type;

    private final boolean reference;

    /**
     * @param name Normbre del parámetros
     * @param type Tipo del parámetro
     * @param reference Si se trata de un parámetro por referencia
     */
    public Parameter (String name, Type type, boolean reference) {
        this.name = Objects.requireNonNull(name, "name");
        this.type = Objects.requireNonNull(type, "type");
        this.reference = reference;
    }

    /** @return Nombre del parámetro */
    public String getName () {
        return name;
    }

    /** @return Tipo del parámetro */
    public Type getType () {
        return type;
    }

    /** @return Si se trata de un parámetro por referencia */
    public boolean isReference () {
        return reference;
    }
}
