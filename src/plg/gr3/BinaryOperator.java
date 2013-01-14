package plg.gr3;

import plg.gr3.parser.Type;

public enum BinaryOperator {
    ADDITION {
        
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric;
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
    SUBTRACTION {
        @Override
        public boolean canApply (Type t1, Type t2) {
            return t1.isNumeric() && t2.isNumeric;
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
    PRODUCT {
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
            // TODO Auto-generated method stub
            return null;
        }
    },
    DIVISION {
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
            // TODO Auto-generated method stub
            return null;
        }
    },
    MODULE {
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
            // TODO Auto-generated method stub
            return null;
        }
    },
    EQUALS {
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
    NOT_EQUALS {
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
    LOWER_THAN {
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
    LOWER_EQUAL {
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
    GREATER_THAN {
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
    GREATER_EQUALS {
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
    AND {
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
    OR {
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
    
    public abstract boolean canApply (Type t1, Type t2);
    
    public abstract Type getApplyType (Type t1, Type t2);
    
    public abstract Object apply (Object o1, Object o2);
}
