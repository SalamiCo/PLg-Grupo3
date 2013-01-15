package plg.gr3;

import plg.gr3.parser.Type;

public enum BinaryOperator {
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
    MODULE("%") {
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
    
    private final String symbol;
    
    private BinaryOperator (String symbol) {
        this.symbol = symbol;
    }
    
    private int compareNumbers (Number o1, Number o2) {
        return 0;
    }
    
    @Override
    public String toString () {
        return this.symbol;
    }
    
    public abstract boolean canApply (Type t1, Type t2);
    
    public abstract Type getApplyType (Type t1, Type t2);
    
    public abstract Object apply (Object o1, Object o2);
}
