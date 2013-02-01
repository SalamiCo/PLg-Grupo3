package plg.gr3.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representación interna de los tipos del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Type {
    
    /** Mapa de nombres a sus tipos */
    private static final Map<String, Type> TYPES = new HashMap<>();
    
    /** Mapa de códigos a sus tipos */
    private static final Map<Integer, Type> TYPECODES = new HashMap<>();
    
    /** Tipo predefinido para naturales */
    public static final Type NATURAL = defineType("natural", 0);
    
    /** Tipo predefinido para enteros */
    public static final Type INTEGER = defineType("integer", 1);
    
    /** Tipo predefinido para punto flotante */
    public static final Type FLOAT = defineType("float", 2);
    
    /** Tipo predefinido para booleanos */
    public static final Type BOOLEAN = defineType("boolean", 3);
    
    /** Tipo predefinido para caracteres */
    public static final Type CHARACTER = defineType("character", 4);
    
    /** Tipo predefinido para errores en la gramática */
    public static final Type ERROR = forName("");
    
    /** Nombre del tipo */
    private final String name;
    
    /** Código del tipo */
    private final int code;
    
    /**
     * @param name Nombre del tipo
     * @param code Código del tipo
     */
    private Type (String name, int code) {
        this.name = Objects.requireNonNull(name, "name");
        this.code = code;
        
        if (code != 0xFFFFFFFF) {
            TYPECODES.put(Integer.valueOf(code), this);
        }
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
    
    public static Type forValue (Value val) {
        return val.getType();
    }
    
    public static Type forCode (int code) {
        Type type = TYPES.get(Integer.valueOf(code));
        if (type == null) {
            return Type.ERROR;
        } else {
            return type;
        }
    }
    
    /*
     * Funcion que te devuelve verdadero si el tipo de typeAssigned es asignable al tipo de ident. En caso contrario
     * devuelve falso
     */
    public static boolean assignable (Type ident, Type typeAssigned) {
        if (ident.equals(Type.NATURAL)) {
            return typeAssigned.equals(NATURAL);
            
        } else if (ident.equals(Type.INTEGER)) {
            return typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER);
            
        } else if (ident.equals(Type.FLOAT)) {
            return typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER) || typeAssigned.equals(FLOAT);
            
        } else if (ident.equals(Type.BOOLEAN)) {
            return typeAssigned.equals(BOOLEAN);
            
        } else if (ident.equals(Type.CHARACTER)) {
            return typeAssigned.equals(CHARACTER);
            
        } else {
            return false;
        }
    }
    
    /**
     * @param castingType Tipo al que convertir
     * @param originalType Tipo original
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
        return equals(NATURAL) || equals(INTEGER) || equals(FLOAT);
    }
    
    /*
     * funcion que dados dos tipos te devuelve el de mayor tamaño si no son comparables te devuelve null
     */
    public static Type getWiderType (Type t1, Type t2) {
        if (t1.equals(t2)) {
            return t1;
        } else if (t1.equals(Type.NATURAL)) {
            if (t2.equals(INTEGER) || t2.equals(FLOAT)) {
                return t2;
            }
        } else if (t1.equals(Type.INTEGER)) {
            if (t2.equals(NATURAL)) {
                return t1;
            } else if (t2.equals(FLOAT)) {
                return t2;
            }
        } else if (t1.equals(Type.FLOAT)) {
            if (t2.equals(Type.NATURAL) || t2.equals(Type.INTEGER)) {
                return t1;
            }
        }
        
        return Type.ERROR;
    }
    
    public static Type forName (String name) {
        if (TYPES.containsKey(name)) {
            return TYPES.get(name);
        } else {
            return defineType(name, 0xFFFFFFFF);
        }
    }
    
    private static Type defineType (String name, int code) {
        if (TYPES.containsKey(name)) {
            throw new IllegalStateException(name + " already exists");
        }
        
        Type type = new Type(name, code);
        TYPES.put(name, type);
        return type;
    }
}
