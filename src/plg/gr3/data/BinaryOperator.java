package plg.gr3.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa los operadores binarios del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum BinaryOperator implements Operator {
    /** Operador de suma */
    ADDITION("+", 0) {
        
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric();
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public NumericValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NumericValue n1 = (NumericValue) v1;
            NumericValue n2 = (NumericValue) v2;
            return n1.add(n2);
        }
        
    },
    
    /** Operador de resta */
    SUBTRACTION("-", 1) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric();
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public NumericValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NumericValue n1 = (NumericValue) v1;
            NumericValue n2 = (NumericValue) v2;
            return n1.subtract(n2);
        }
    },
    
    /** Operador de multiplicación */
    PRODUCT("*", 2) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric();
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public NumericValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NumericValue n1 = (NumericValue) v1;
            NumericValue n2 = (NumericValue) v2;
            return n1.multiply(n2);
        }
    },
    
    /** Operador de división */
    DIVISION("/", 3) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric();
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public NumericValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NumericValue n1 = (NumericValue) v1;
            NumericValue n2 = (NumericValue) v2;
            return n1.divide(n2);
        }
    },
    
    /** Operador de módulo */
    MODULO("%", 4) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return (t1.equals(Type.INTEGER) || t1.equals(Type.NATURAL)) && t2.equals(Type.NATURAL);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return t1;
        }
        
        @Override
        public NumericValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NumericValue n1 = (NumericValue) v1;
            NumericValue n2 = (NumericValue) v2;
            return n1.modulo(n2);
        }
    },
    
    /** Operador de igualdad */
    EQUALS("==", 5) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) == 0);
        }
    },
    
    /** Operador de desigualdad */
    NOT_EQUALS("!=", 6) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) != 0);
        }
    },
    
    /** Operación menor */
    LOWER_THAN("<", 7) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) < 0);
        }
    },
    
    /** Operador menor o igual */
    LOWER_EQUAL("<=", 8) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) <= 0);
        }
    },
    
    /** Operador mayor */
    GREATER_THAN(">", 9) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) > 0);
        }
    },
    
    /** Operador mayor o igual */
    GREATER_EQUALS(">=", 10) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric() || t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN)
                   || t1.equals(Type.CHARACTER) && t2.equals(Type.CHARACTER);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            return BooleanValue.valueOf(v1.compare(v2) >= 0);
        }
    },
    
    /** Operación de y lógico */
    AND("and", 11) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            BooleanValue b1 = (BooleanValue) v1;
            BooleanValue b2 = (BooleanValue) v2;
            return BooleanValue.valueOf(b1.getValue() & b2.getValue());
        }
    },
    
    /** Operación o lógico */
    OR("or", 12) {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.BOOLEAN;
        }
        
        @Override
        public BooleanValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            BooleanValue b1 = (BooleanValue) v1;
            BooleanValue b2 = (BooleanValue) v2;
            return BooleanValue.valueOf(b1.getValue() | b2.getValue());
        }
    },
    
    /** Operación desplazar a la izquierda */
    SHIFT_LEFT("<<", 13) {
        
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.NATURAL) && t2.equals(Type.NATURAL);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.NATURAL;
        }
        
        @Override
        public NaturalValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NaturalValue n1 = (NaturalValue) v1;
            NaturalValue n2 = (NaturalValue) v2;
            return NaturalValue.valueOf(n1.getValue() << n2.getValue());
        }
    },
    
    /** Operación desplazar a la derecha */
    SHIFT_RIGHT(">>", 14) {
        
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.NATURAL) && t2.equals(Type.NATURAL);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.NATURAL;
        }
        
        @Override
        public NaturalValue apply (Value v1, Value v2) {
            Type t1 = v1.getType();
            Type t2 = v2.getType();
            if (!canApply(t1, t2)) {
                throw new IllegalArgumentException();
            }
            
            NaturalValue n1 = (NaturalValue) v1;
            NaturalValue n2 = (NaturalValue) v2;
            return NaturalValue.valueOf(n1.getValue() >> n2.getValue());
        }
    };
    
    private static final Map<Integer, BinaryOperator> OPERATORS;
    static {
        Map<Integer, BinaryOperator> map = new HashMap<>();
        for (BinaryOperator op : values()) {
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
    private BinaryOperator (String symbol, int code) {
        this.symbol = symbol;
        this.code = code;
    }
    
    /** @return Código del operador */
    public int getCode () {
        return code;
    }
    
    @Override
    public String toString () {
        return symbol;
    }
    
    /**
     * Comprueba si este operador puede aplicarse para los tipos dados.
     * 
     * @param t1 Tipo del primer operando
     * @param t2 Tipo del segundo operando
     * @return <tt>true</tt> si este operador puede aplicarse a valores de tipo <tt>t1</tt> y <tt>t2</tt>,
     *         <tt>false</tt>
     */
    public abstract boolean canApply (Type t1, Type t2);
    
    /**
     * Devuelve el tipo resultado de aplicar este operador con los tipos dados, o un tipo error si no pueden aplicarse.
     * 
     * @param t1 Tipo del primer operando
     * @param t2 Tipo del segundo operando
     * @return El tipo que devolvería este operador al aplicarse sobre dos operandos de tipo <tt>t1</tt> y <tt>t2</tt>
     */
    public abstract Type getApplyType (Type t1, Type t2);
    
    /**
     * Aplica el operador sobre los argumentos dados y devuelve el resultado. Si el operador no es aplicable a los
     * argumentos dados, este método lanza una {@link IllegalArgumentException}.
     * 
     * @param v1 Primer operando
     * @param v2 Segundo operando
     * @return El valor resultado de aplicar el operador a los argumentos <tt>o1</tt> y <tt>o2</tt>
     * @throws IllegalArgumentException si los argumentos no se pueden operar con este operador
     */
    public abstract Value apply (Value v1, Value v2);
    
    /**
     * @param code Código del operador
     * @return Operador con el código pasado, o <tt>null</tt> si no existe.
     */
    public static BinaryOperator forCode (int code) {
        return OPERATORS.get(Integer.valueOf(code));
    }
}
