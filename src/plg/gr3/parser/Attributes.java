package plg.gr3.parser;

/**
 * Clase que representa los atributos, heredados y sintetizados, de cualquiera de los nodos del arbol sintáctico.
 * <p>
 * Para instanciar esta clase, debe crearse primero una instancia de {@link Builder}, llamar a sus métodos para
 * establecer los valores, y finalmente llamar a {@link Builder#create() create()} para construir un nuevo objeto.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Attributes {
    
    /** Atributos creados por defecto */
    public static final Attributes DEFAULT = new Attributes.Builder().create();
    
    /** Tipo de una variable o expresión */
    private final Type type;
    
    /** Si se trata de una constante o variable */
    private final boolean constant;
    
    /** Si hubo errores y cuales */
    private final Object error;
    
    /** El operador a usar */
    private final Object operator;
    
    /** Tabla de símbolos */
    private final SymbolTable symbolTable;
    
    /** El valor a usar */
    private final Object value;
    
    /** La dirección de memoria */
    private final int address;
    
    /**
     * @param address
     *            La dirección de memoria
     * @param constant
     *            Si se trata de una constante o variable
     * @param error
     *            Si hubo errores y cual
     * @param operator
     *            El operador a usar
     * @param type
     *            Tipo de una variable o expresión
     * @param value
     *            El valor a usar
     */
    private Attributes (
        int address, boolean constant, Object error, Object operator, SymbolTable symbolTable, Type type, Object value)
    {
        this.address = address;
        this.constant = constant;
        this.error = error;
        this.operator = operator;
        this.symbolTable = symbolTable;
        this.type = type;
        this.value = value;
    }
    
    /**
     * @return El atributo <tt>type</tt>
     */
    public Type getType () {
        return type;
    }
    
    /**
     * @return El atributo <tt>constant</tt>
     */
    public boolean getConstant () {
        return constant;
    }
    
    /**
     * @return El atributo <tt>error</tt>
     */
    public Object getError () {
        return error;
    }
    
    /**
     * @return El atributo <tt>operator</tt>
     */
    public Object getOperator () {
        return operator;
    }
    
    /**
     * @return El atributo <tt>symbolTable</tt>
     */
    public SymbolTable getSymbolTable () {
        return symbolTable;
    }
    
    /**
     * @return El atributo <tt>value</tt> si es del tipo especificado, <tt>null</tt> en caso contrario
     */
    public <T> T getValue (Class<T> cls) {
        return cls.isInstance(value) ? cls.cast(value) : null;
    }
    
    /**
     * @return El atributo <tt>address</tt>
     */
    public int getAddress () {
        return address;
    }
    
    public static final class Builder {
        private Type type;
        
        private boolean constant;
        
        private Object error; // TODO Clase Error o ErrorType
        
        private Object operator; // TODO Clase Operator
        
        private SymbolTable symbolTable;
        
        private Object value;
        
        private int address;
        
        public Builder address (int address) {
            this.address = address;
            return this;
        }
        
        public Builder constant (boolean constant) {
            this.constant = constant;
            return this;
        }
        
        public Builder error (Object error) {
            this.error = error;
            return this;
        }
        
        public Builder operator (Object operator) {
            this.operator = operator;
            return this;
        }
        
        public Builder symbolTable (SymbolTable symbolTable) {
            this.symbolTable = symbolTable;
            return this;
        }
        
        public Builder type (Type type) {
            this.type = type;
            return this;
        }
        
        public Builder value (Object value) {
            this.value = value;
            return this;
        }
        
        public Attributes create () {
            return new Attributes(address, constant, error, operator, symbolTable, type, value);
        }
    }
}
