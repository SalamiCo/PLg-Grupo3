package plg.gr3;

import plg.gr3.parser.Type;

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
            return canApply(type) ? type : Type.ERROR;
        }
        
        @Override
        public Number apply (Object obj) {
            Type type = Type.forValue(obj);
            if (!canApply(type)) {
                return null;
            }
            
            Number num = (Number) obj;
            if (type.equals(Type.NATURAL)) {
                return new Natural(-num.intValue());
                
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(-num.intValue());
                
            } else if (type.equals(Type.FLOAT)) {
                return new Float(-num.floatValue());
                
            } else {
                return null;
            }
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
        public Boolean apply (Object obj) {
            return canApply(Type.forValue(obj)) ? Boolean.valueOf(!((Boolean) obj).booleanValue()) : null;
        }
    };
    
    /** Símbolo del operador */
    private final String symbol;
    
    /** Código del operador */
    private final int code;
    
    /**
     * @param symbol
     *            Símbolo del operador
     * @param code
     *            Código del operador
     */
    private UnaryOperator (String symbol, int code) {
        this.symbol = symbol;
        this.code = code;
    }
    
    /**
     * @param type
     *            Tipo a comprobar
     * @return Si el tipo es aplicable a este operador
     */
    public abstract boolean canApply (Type type);
    
    /**
     * @param type
     *            Tipo a comprobar
     * @return Tipo resultado de aplicar el operador
     */
    public abstract Type getApplyType (Type type);
    
    /**
     * @param obj
     *            Valor al que aplicar la operación
     * @return Valor resultado de aplicar la operación
     */
    public abstract Object apply (Object obj);
    
    public int getCode () {
        return code;
    }
    
    @Override
    public String toString () {
        return symbol;
    }
}
