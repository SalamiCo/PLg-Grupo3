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
                
            } else if (t.equals(Type.CHARACTER)) {
                
            } else if (t.equals(Type.BOOLEAN)) {
                
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
            // TODO Auto-generated method stub
            return null;
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
            // TODO Auto-generated method stub
            return null;
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
            // TODO Auto-generated method stub
            return null;
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
            // TODO Auto-generated method stub
            return null;
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
            // TODO Auto-generated method stub
            return null;
        }
    };
    
    private final String symbol;
    
    private BinaryOperator (String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String toString () {
        return this.symbol;
    }
    
    public abstract boolean canApply (Type t1, Type t2);
    
    public abstract Type getApplyType (Type t1, Type t2);
    
    public abstract Object apply (Object o1, Object o2);
}
