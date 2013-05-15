package plg.gr3.parser.OLD;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.parser.OLD.SymbolTable.Row;

/**
 * Representación de la tabla de símbolos mediante una tabla con clave el identificador de la variable o costante que se
 * guarde y valor una tupla con los atributos de la tabla.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class SymbolTable implements Iterable<Map.Entry<String, Row>> {
    
    /**
     * Fila de la tabla de símbolos, sin incluir el identificador en sí.
     * 
     * @author PLg Grupo 03 2012/2013
     */
    public static final class Row {
        
        private final Type type;
        
        private final boolean constant;
        
        private final int address;
        
        private final Value value;
        
        /**
         * @param type Tipo del identificador
         * @param constant Si el identificador es una constant
         * @param address Dirección del identificador
         * @param value Valor del identificador
         */
        public Row (Type type, boolean constant, int address, Value value) {
            this.type = type;
            this.constant = constant;
            this.address = address;
            this.value = value;
        }
        
        /** @return Tipo del identificador */
        public Type getType () {
            return type;
        }
        
        /** @return Si el identificador es una constante */
        public boolean isConstant () {
            return constant;
        }
        
        /** @return Dirección del identificador */
        public int getAddress () {
            return address;
        }
        
        /** @return Valor del identificador */
        public Value getValue () {
            return value;
        }
        
        @Override
        public String toString () {
            return "Row[type=" + type + ", constant=" + constant + ", address=" + address + ", value=" + value + "]";
        }
        
    }
    
    /** Estructura interna de la tabla */
    private final HashMap<String, Row> table = new HashMap<>();
    
    /**
     * Construye una tabla de símbolos vacía
     */
    public SymbolTable () {
        // Do nothing
    }
    
    /**
     * @param ident Identificador que se quiere comprobar
     * @return <tt>true</tt> si el identificador existe, <tt>false</tt> si no
     */
    public boolean hasIdentifier (String ident) {
        return table.containsKey(ident);
    }
    
    /**
     * @param ident Identificador a añadir
     * @param type Tipo del identificador
     * @param constant Si el identificador es una constante
     * @param address Dirección de memoria del identificador
     * @param value Valor del identificador
     */
    public void putIdentifier (String ident, Type type, boolean constant, int address, Value value) {
        table.put(ident, new Row(type, constant, address, value));
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Fila de la tabla completa
     */
    private Row getRow (String ident) {
        if (!hasIdentifier(ident)) {
            throw new NoSuchElementException(ident);
        }
        
        return table.get(ident);
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Dirección del identificador
     */
    public int getIdentifierAddress (String ident) {
        try {
            return getRow(ident).getAddress();
        } catch (NoSuchElementException exc) {
            return 0;
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Tipo del identificador
     */
    public Type getIdentfierType (String ident) {
        try {
            return getRow(ident).getType();
        } catch (NoSuchElementException exc) {
            return Type.ERROR;
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Si el identificador es una constante
     */
    public boolean isIdentifierConstant (String ident) {
        try {
            return getRow(ident).constant;
        } catch (NoSuchElementException exc) {
            return false;
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Valor del identificador
     */
    public Value getIdentifierValue (String ident) {
        try {
            return getIdentifierValue(ident, Value.class);
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @param type Tipo del valor
     * @return Valor del identificador
     */
    public <V extends Value> V getIdentifierValue (String ident, Class<V> type) {
        Value obj = getRow(ident).value;
        
        if (type.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }
    
    @Override
    public Iterator<Map.Entry<String, Row>> iterator () {
        return table.entrySet().iterator();
    }
}
