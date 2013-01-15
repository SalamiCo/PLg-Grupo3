package plg.gr3;

import plg.gr3.parser.Type;

/**
 * Clase que representa los operadores binarios del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum BinaryOperator {
    /** Operador de suma */
    ADDITION("+") {
        
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
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            Type type = getApplyType(Type.forValue(o1), Type.forValue(o2));
            if (type.equals(Type.NATURAL)) {
                return new Natural(n1.intValue() + n2.intValue());
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(n1.intValue() + n2.intValue());
            } else if (type.equals(Type.FLOAT)) {
                return new Float(n1.floatValue() + n2.floatValue());
            } else {
                return null;
            }
        }
        
    },
    /** Operador de resta */
    SUBTRACTION("-") {
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
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            Type type = getApplyType(Type.forValue(o1), Type.forValue(o2));
            if (type.equals(Type.NATURAL)) {
                return new Natural(n1.intValue() - n2.intValue());
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(n1.intValue() - n2.intValue());
            } else if (type.equals(Type.FLOAT)) {
                return new Float(n1.floatValue() - n2.floatValue());
            } else {
                return null;
            }
        }
    },
    
    /** Operador de multiplicación */
    PRODUCT("*") {
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
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            Type type = getApplyType(Type.forValue(o1), Type.forValue(o2));
            if (type.equals(Type.NATURAL)) {
                return new Natural(n1.intValue() * n2.intValue());
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(n1.intValue() * n2.intValue());
            } else if (type.equals(Type.FLOAT)) {
                return new Float(n1.floatValue() * n2.floatValue());
            } else {
                return null;
            }
        }
    },
    
    /** Operador de división */
    DIVISION("/") {
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
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            Type type = getApplyType(Type.forValue(o1), Type.forValue(o2));
            if (type.equals(Type.NATURAL)) {
                return new Natural(n1.intValue() / n2.intValue());
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(n1.intValue() / n2.intValue());
            } else if (type.equals(Type.FLOAT)) {
                return new Float(n1.floatValue() / n2.floatValue());
            } else {
                return null;
            }
        }
    },
    
    /** Operador de módulo */
    MODULO("%") {
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
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Number n1 = (Number) o1;
            Number n2 = (Number) o2;
            Type type = getApplyType(Type.forValue(o1), Type.forValue(o2));
            if (type.equals(Type.NATURAL)) {
                return new Natural(n1.intValue() % n2.intValue());
            } else if (type.equals(Type.INTEGER)) {
                return new Integer(n1.intValue() % n2.intValue());
            } else if (type.equals(Type.FLOAT)) {
                return new Float(n1.floatValue() % n2.floatValue());
            } else {
                return null;
            }
        }
    },
    
    /** Operador de igualdad */
    EQUALS("==") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Boolean apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            return Boolean.valueOf(o1.equals(o2));
        }
    },
    
    /** Operador de desigualdad */
    NOT_EQUALS("!=") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Boolean apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            return !(Boolean.valueOf(o1.equals(o2)));
        }
    },
    
    /** Operación menor */
    LOWER_THAN("<") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Type t = Type.forValue(o1);
            if (t.equals(t.isNumeric())) {
                return (compareNumbers((Number) o1, (Number) o2) < 0);
            } else if (t.equals(Type.CHARACTER)) {
                Character c1 = (Character) o1;
                Character c2 = (Character) o2;
                return (c1.compareTo(c2) < 0);
            } else if (t.equals(Type.BOOLEAN)) {
                Boolean b1 = (Boolean) o1;
                Boolean b2 = (Boolean) o2;
                return (b1.compareTo(b2) < 0);
            } else {
                return null;
            }
        }
    },
    
    /** Operador menor o igual */
    LOWER_EQUAL("<=") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Type t = Type.forValue(o1);
            if (t.equals(t.isNumeric())) {
                return (compareNumbers((Number) o1, (Number) o2) <= 0);
            } else if (t.equals(Type.CHARACTER)) {
                Character c1 = (Character) o1;
                Character c2 = (Character) o2;
                return (c1.compareTo(c2) <= 0);
            } else if (t.equals(Type.BOOLEAN)) {
                Boolean b1 = (Boolean) o1;
                Boolean b2 = (Boolean) o2;
                return (b1.compareTo(b2) <= 0);
            } else {
                return null;
            }
        }
    },
    
    /** Operador mayor */
    GREATER_THAN(">") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Type t = Type.forValue(o1);
            if (t.equals(t.isNumeric())) {
                return (compareNumbers((Number) o1, (Number) o2) > 0);
            } else if (t.equals(Type.CHARACTER)) {
                Character c1 = (Character) o1;
                Character c2 = (Character) o2;
                return (c1.compareTo(c2) > 0);
            } else if (t.equals(Type.BOOLEAN)) {
                Boolean b1 = (Boolean) o1;
                Boolean b2 = (Boolean) o2;
                return (b1.compareTo(b2) > 0);
            } else {
                return null;
            }
        }
    },
    
    /** Operador mayor o igual */
    GREATER_EQUALS(">=") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(t2);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Type t = Type.forValue(o1);
            if (t.equals(t.isNumeric())) {
                return (compareNumbers((Number) o1, (Number) o2) >= 0);
            } else if (t.equals(Type.CHARACTER)) {
                Character c1 = (Character) o1;
                Character c2 = (Character) o2;
                return (c1.compareTo(c2) >= 0);
            } else if (t.equals(Type.BOOLEAN)) {
                Boolean b1 = (Boolean) o1;
                Boolean b2 = (Boolean) o2;
                return (b1.compareTo(b2) >= 0);
            } else {
                return null;
            }
        }
    },
    
    /** Operación de y lógico */
    AND("and") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Boolean b1 = (Boolean) o1;
            Boolean b2 = (Boolean) o2;
            return b1 && b2;
        }
    },
    
    /** Operación o lógico */
    OR("or") {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.equals(Type.BOOLEAN) && t2.equals(Type.BOOLEAN);
        }
        
        @Override
        public Type getApplyType (Type t1, Type t2) {
            if (!canApply(t1, t2)) {
                return Type.ERROR;
            }
            return Type.getWiderType(t1, t2);
        }
        
        @Override
        public Object apply (Object o1, Object o2) {
            if (!canApply(Type.forValue(o1), Type.forValue(o2))) {
                return null;
            }
            
            Boolean b1 = (Boolean) o1;
            Boolean b2 = (Boolean) o2;
            return b1 || b2;
        }
    };
    
    /** Símbolo del operador */
    private final String symbol;
    
    /**
     * Compara dos objetos de tipo {@link Number} entre sí.
     * 
     * @param n1
     *            Primer número a comparar
     * @param n2
     *            Segundo número a comparar
     * @return Entero positivo, cero o negativo dependiendo de si <tt>n1</tt> es mayor, igual o menor, respectivamente,
     *         que <tt>n2</tt>.
     */
    private static int compareNumbers (Number n1, Number n2) {
        if (n1 instanceof Float || n2 instanceof Float) {
            return n1.floatValue() < n2.floatValue() ? -1 : n1.floatValue() > n2.floatValue() ? +1 : 0;
            
        } else if (n1 instanceof Integer || n2 instanceof Integer) {
            return n1.intValue() - n2.intValue();
            
        } else if (n1 instanceof Natural || n2 instanceof Natural) {
            return n1.intValue() - n2.intValue();
        }
        
        throw new IllegalArgumentException();
    }
    
    private BinaryOperator (String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String toString () {
        return this.symbol;
    }
    
    /**
     * Comprueba si este operador puede aplicarse para los tipos dados.
     * 
     * @param t1
     *            Tipo del primer operando
     * @param t2
     *            Tipo del segundo operando
     * @return <tt>true</tt> si este operador puede aplicarse a valores de tipo <tt>t1</tt> y <tt>t2</tt>,
     *         <tt>false</tt>
     */
    public abstract boolean canApply (Type t1, Type t2);
    
    /**
     * Devuelve el tipo resultado de aplicar este operador con los tipos dados, o un tipo error si no pueden aplicarse.
     * 
     * @param t1
     *            Tipo del primer operando
     * @param t2
     *            Tipo del segundo operando
     * @return El tipo que devolvería este operador al aplicarse sobre dos operandos de tipo <tt>t1</tt> y <tt>t2</tt>
     */
    public abstract Type getApplyType (Type t1, Type t2);
    
    /**
     * Aplica el operador sobre los argumentos dados y devuelve el resultado. Si el operador no es aplicable a los
     * argumentos dados, este método lanza una {@link IllegalArgumentException}.
     * 
     * @param o1
     *            Primer operando
     * @param o2
     *            Segundo operando
     * @return El valor resultado de aplicar el operador a los argumentos <tt>o1</tt> y <tt>o2</tt>
     * @throws IllegalArgumentException
     *             si los argumentos no se pueden operar con este operador
     */
    public abstract Object apply (Object o1, Object o2);
}
