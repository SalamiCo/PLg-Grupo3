package plg.gr3.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa un operador unario.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum UnaryOperator implements Operator {
    /** Operador unario de negación */
    MINUS("-", 15) {
        @Override
        public boolean canApply (Type type) {
            return type.isNumeric();
        }
        
        @Override
        public Type getApplyType (Type type) {
            return canApply(type) ? Type.getWiderType(type, Type.INTEGER) : Type.ERROR;
        }
        
        @Override
        public Value apply (Value val) {
            Type type = val.getType();
            if (!canApply(type)) {
                throw new IllegalArgumentException();
            }
            
            if (type.equals(Type.NATURAL)) {
                return IntegerValue.valueOf(-((NaturalValue) val).getValue());
                
            } else if (type.equals(Type.INTEGER)) {
                return IntegerValue.valueOf(-((IntegerValue) val).getValue());
                
            } else if (type.equals(Type.FLOAT)) {
                return FloatValue.valueOf(-((FloatValue) val).getValue());
                
            }
            
            throw new IllegalArgumentException();
        }
    },
    
    /** Operador de inversión booleana */
    NOT("not", 16) {
        @Override
        public boolean canApply (Type type) {
            return type.equals(Type.BOOLEAN);
        }
        
        @Override
        public Type getApplyType (Type type) {
            return canApply(type) ? type : Type.ERROR;
        }
        
        @Override
        public BooleanValue apply (Value val) {
            if (!canApply(val.getType())) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(!((BooleanValue) val).getValue());
        }
    };
    
    private static final Map<Integer, UnaryOperator> OPERATORS;
    static {
        Map<Integer, UnaryOperator> map = new HashMap<>();
        for (UnaryOperator op : values()) {
            map.put(Integer.valueOf(op.code), op);
        }
        
        OPERATORS = Collections.unmodifiableMap(map);
    }
    
    /** Símbolo del operador */
    private final String symbol;
    
    /** Código del operador */
    private final int code;
    
    /**
     * @param symbol Símbolo del operador
     * @param code Código del operador
     */
    private UnaryOperator (String symbol, int code) {
        this.symbol = symbol;
        this.code = code;
    }
    
    /**
     * @param type Tipo a comprobar
     * @return Si el tipo es aplicable a este operador
     */
    public abstract boolean canApply (Type type);
    
    /**
     * @param type Tipo a comprobar
     * @return Tipo resultado de aplicar el operador
     */
    public abstract Type getApplyType (Type type);
    
    /**
     * @param val Valor al que aplicar la operación
     * @return Valor resultado de aplicar la operación
     */
    public abstract Value apply (Value val);
    
    public int getCode () {
        return code;
    }
    
    @Override
    public String toString () {
        return symbol;
    }
    
    /**
     * @param code Código del operador
     * @return Operador con el código pasado, o <tt>null</tt> si no existe.
     */
    public static UnaryOperator forCode (int code) {
        return OPERATORS.get(Integer.valueOf(code));
    }
}
