package plg.gr3.parser;

import java.util.Objects;

import plg.gr3.Natural;

/**
 * Representación interna de los tipos del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Type {
    
    /** Tipo predefinido para naturales */
    public static final Type NATURAL = new Type("natural", 0);
    
    /** Tipo predefinido para enteros */
    public static final Type INTEGER = new Type("integer", 1);
    
    /** Tipo predefinido para punto flotante */
    public static final Type FLOAT = new Type("float", 2);
    
    /** Tipo predefinido para booleanos */
    public static final Type BOOLEAN = new Type("boolean", 3);
    
    /** Tipo predefinido para caracteres */
    public static final Type CHARACTER = new Type("character", 4);
    
    /** Tipo predefinido para errores en la gramática */
    public static final Type ERROR = new Type("");
    
    /** Nombre del tipo */
    private final String name;
    
    /** Código del tipo */
    private final int code;
    
    /**
     * @param name
     *            Nombre del tipo
     * @param code
     *            Código del tipo
     */
    private Type (String name, int code) {
        this.name = Objects.requireNonNull(name, "name");
        this.code = code;
    }
    
    /** param name Nombre del tipo */
    public Type (String name) {
        this(name, 0xFFFFFFFF);
    }
    
    /** @return Nombre del tipo */
    public String getName () {
        return name;
    }
    
    /** @return Código del tipo */
    public int getCode () {
        return code;
    }
    
    @Override
    public int hashCode () {
        return Objects.hashCode(name);
    }
    
    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof Type)) {
            return false;
        }
        
        Type type = (Type) obj;
        return name.equals(type.name);
    }
    
    @Override
    public String toString () {
        return "Type(" + name + ")";
    }
    
    /*
     * funcion que dado un objeto te devuelve de que tipo es en caso de erro te devuelve null
     */
    public static Type forValue (Object obj) {
        if (obj instanceof java.lang.Float) {
            return Type.FLOAT;
        }
        if (obj instanceof java.lang.Integer) {
            return Type.INTEGER;
        }
        if (obj instanceof Natural) {
            return Type.NATURAL;
        }
        if (obj instanceof java.lang.Boolean) {
            return Type.BOOLEAN;
        }
        if (obj instanceof java.lang.Character) {
            return Type.CHARACTER;
        }
        return null;
    }
    
    public static Type forCode (int code) {
        return null;
    }
    
    /*
     * Funcion que te devuelve verdadero si el tipo de typeAssigned es asignable al tipo de ident. En caso contrario
     * devuelve falso
     */
    public boolean typeMatch (Type ident, Type typeAssigned) {
        if (ident.equals(Type.NATURAL)) {
            if (typeAssigned.equals(NATURAL)) {
                return true;
            } else {
                return false;
            }
        }
        if (ident.equals(Type.INTEGER)) {
            if (typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (ident.equals(Type.FLOAT)) {
            if (typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER) || typeAssigned.equals(FLOAT)) {
                return true;
            } else {
                return false;
            }
        }
        if (ident.equals(Type.BOOLEAN)) {
            if (typeAssigned.equals(BOOLEAN)) {
                return true;
            } else {
                return false;
            }
        }
        
        if (ident.equals(Type.CHARACTER)) {
            if (typeAssigned.equals(CHARACTER)) {
                return true;
            } else {
                return false;
            }
        }
        
        return false;
        
    }
    
    /**
     * @param castingType
     *            Tipo al que convertir
     * @param originalType
     *            Tipo original
     * @return <tt>true</tt> si se puede convertir de <tt>typeCasted</tt> a <tt>typeCasting</tt>, <tt>false></tt> en
     *         caso contrario
     */
    public static boolean canCast (Type castingType, Type originalType) {
        if (castingType.equals(Type.NATURAL)) {
            return originalType.equals(NATURAL) || originalType.equals(CHARACTER);
            
        } else if (castingType.equals(Type.INTEGER)) {
            return originalType.isNumeric() || originalType.equals(CHARACTER);
            
        } else if (castingType.equals(Type.FLOAT)) {
            return originalType.isNumeric() || originalType.equals(CHARACTER);
            
        } else if (castingType.equals(Type.CHARACTER)) {
            return originalType.equals(NATURAL) || originalType.equals(CHARACTER);
            
        } else {
            return false;
        }
    }
    
    /*
     * Funcion que nos devuelve verdadero si es de tipo numerico en cualquier otro caso devuelve false
     */
    public boolean isNumeric () {
        if (this.equals(NATURAL) || this.equals(INTEGER) || this.equals(FLOAT)) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * funcion que dados dos tipos te devuelve el de mayor tamaño si no son comparables te devuelve null
     */
    public static Type getWiderType (Type ident, Type typeAssigned) {
        if (ident.equals(typeAssigned)) {
            return forValue(ident);
        }
        if (ident.equals(Type.NATURAL)) {
            if (typeAssigned.equals(INTEGER)) {
                return Type.INTEGER;
            }
            if (typeAssigned.equals(FLOAT)) {
                return Type.FLOAT;
            }
        }
        if (ident.equals(Type.INTEGER)) {
            if (typeAssigned.equals(NATURAL)) {
                return Type.INTEGER;
            }
            if (typeAssigned.equals(FLOAT)) {
                return Type.FLOAT;
            }
        }
        if (ident.equals(Type.FLOAT)) {
            if (typeAssigned.equals(NATURAL)) {
                return Type.NATURAL;
            }
            if (typeAssigned.equals(INTEGER)) {
                return Type.FLOAT;
            }
        }
        return null;
    }
}
