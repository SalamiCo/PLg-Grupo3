package plg.gr3.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Representación interna de los tipos del lenguaje
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class Type {
    
    /** Mapa de nombres a sus tipos */
    private static final Map<String, Type> TYPES = Collections.synchronizedMap(new HashMap<String, Type>());
    
    /** Tipo predefinido para naturales */
    public static final Type NATURAL = new PrimitiveType("natural", 0, true) {
        
        @Override
        public boolean compatible (Type type) {
            return NATURAL.equals(type);
        }
    };
    
    /** Tipo predefinido para enteros */
    public static final Type INTEGER = new PrimitiveType("integer", 1, true) {
        
        @Override
        public boolean compatible (Type type) {
            return NATURAL.equals(type) || INTEGER.equals(type);
        }
    };
    
    /** Tipo predefinido para punto flotante */
    public static final Type FLOAT = new PrimitiveType("float", 2, true) {
        
        @Override
        public boolean compatible (Type type) {
            return NATURAL.equals(type) || INTEGER.equals(type) || FLOAT.equals(type);
        }
    };
    
    /** Tipo predefinido para booleanos */
    public static final Type BOOLEAN = new PrimitiveType("boolean", 3, false) {
        
        @Override
        public boolean compatible (Type type) {
            return BOOLEAN.equals(type);
        }
    };
    
    /** Tipo predefinido para caracteres */
    public static final Type CHARACTER = new PrimitiveType("character", 4, false) {
        
        @Override
        public boolean compatible (Type type) {
            return CHARACTER.equals(type);
        }
    };
    
    /** Tipo predefinido para errores en la gramática */
    public static final Type ERROR = new Type("") {
        
        @Override
        public boolean isNumeric () {
            return false;
        }
        
        @Override
        public boolean isPrimitive () {
            return false;
        }
        
        @Override
        public boolean compatible (Type type) {
            return false;
        }
    };
    
    /** Mapa de códigos a sus tipos */
    private static final Map<Integer, Type> TYPECODES;
    static {
        Map<Integer, Type> codes = new HashMap<Integer, Type>();
        for (Type type : Arrays.asList(NATURAL, INTEGER, FLOAT, BOOLEAN, CHARACTER)) {
            codes.put(Integer.valueOf(type.getCode()), type);
        }
        TYPECODES = Collections.unmodifiableMap(codes);
    }
    
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
    
    protected Type (String name) {
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
        return name;
    }
    
    public abstract boolean compatible (Type type);
    
    /**
     * Devuelve el tipo asociado al código pasado
     * 
     * @param code Código del tipo
     * @return Tipo asociado a dicho código, o {@link #ERROR Type.ERROR} si no existiera
     */
    public static Type forCode (int code) {
        Type type = TYPECODES.get(Integer.valueOf(code));
        if (type == null) {
            return Type.ERROR;
        } else {
            return type;
        }
    }
    
    /**
     * @param typeIdent Tipo del identificador
     * @param typeAssigned Tipo de la expresión a asignar
     * @return <tt>true</tt> si <tt>typeAssigned</tt> puede asignarse a <tt>typeIdent</tt>, <tt>false</tt> en caso
     *         contrario.
     */
    public static boolean assignable (Type typeIdent, Type typeAssigned) {
        if (typeIdent.equals(Type.NATURAL)) {
            return typeAssigned.equals(NATURAL);
            
        } else if (typeIdent.equals(Type.INTEGER)) {
            return typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER);
            
        } else if (typeIdent.equals(Type.FLOAT)) {
            return typeAssigned.equals(NATURAL) || typeAssigned.equals(INTEGER) || typeAssigned.equals(FLOAT);
            
        } else if (typeIdent.equals(Type.BOOLEAN)) {
            return typeAssigned.equals(BOOLEAN);
            
        } else if (typeIdent.equals(Type.CHARACTER)) {
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
    
    /** @return <tt>true</tt> si este tipo es numérico, <tt>false</tt> en otro caso */
    public abstract boolean isNumeric ();
    
    /** @return <tt>true</tt> si este tipo es primitivo */
    public abstract boolean isPrimitive ();
    
    /**
     * @param t1 Primer tipo acomprobar
     * @param t2 Segundo tipo a comprobar
     * @return El tipo más ancho de los tipos pasados, o {@link #ERROR Type.ERROR} si la compración no tiene sentido
     */
    public static Type getWiderType (Type t1, Type t2) {
        if (t1.equals(t2)) {
            // Si los tipos son iguales, devolvemos uno de ellos
            return t1;
            
        } else if (t1.equals(Type.NATURAL)) {
            // Cualquier tipo numérico es más ancho que <tt>natural</tt>
            if (t2.equals(INTEGER) || t2.equals(FLOAT)) {
                return t2;
            }
            
        } else if (t1.equals(Type.INTEGER)) {
            // <tt>natural</tt> es más estrecho que integer, <tt>float</tt> es más ancho
            if (t2.equals(NATURAL)) {
                return t1;
            } else if (t2.equals(FLOAT)) {
                return t2;
            }
            
        } else if (t1.equals(Type.FLOAT)) {
            // <tt>float</tt> es más ancho que cualquier tipo numérico
            if (t2.equals(Type.NATURAL) || t2.equals(Type.INTEGER)) {
                return t1;
            }
        }
        
        // Los tipos no se pueden comparar en anchura (p.ej.: Numérico con <tt>boolean</tt> o <tt>character</tt>)
        return Type.ERROR;
    }
    
    /**
     * Devuelve el tipo correspondiente al nombre dado.
     * <p>
     * Si el tipo aún no ha sido definido, se definirá automáticamente.
     * 
     * @param name Nombre del tipo pedido
     * @return Tipo correspondiente al nombre dado
     */
    public static Type forName (String name) {
        if (TYPES.containsKey(name)) {
            return TYPES.get(name);
        } else {
            throw new IllegalArgumentException(name + " is not a type");
        }
    }
    
    private static abstract class PrimitiveType extends Type {
        
        private final boolean numeric;
        
        protected PrimitiveType (String name, int code, boolean numeric) {
            super(name, code);
            this.numeric = numeric;
        }
        
        @Override
        public final boolean isPrimitive () {
            return true;
        }
        
        @Override
        public final boolean isNumeric () {
            return numeric;
        }
    }
}
