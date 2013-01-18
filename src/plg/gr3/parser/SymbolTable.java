package plg.gr3.parser;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Representación de la tabla de símbolos mediante una tabla con clave el identificador de la variable o costante que se
 * guarde y valor una tupla con los atributos de la tabla.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class SymbolTable {
    
    /**
     * Estructura simple estilo C para representar internamente los atributos de los identificadores
     * 
     * @author PLg Grupo 03 2012/2013
     */
    private final class Entry {
        
        public final Type type;
        
        public final boolean constant;
        
        public final int address;
        
        public final Object value;
        
        public Entry (Type type, boolean constant, int address, Object value) {
            this.type = type;
            this.constant = constant;
            this.address = address;
            this.value = value;
        }
    }
    
    /** Estructura interna de la tabla */
    private final HashMap<String, Entry> table = new HashMap<>();
    
    /**
     * Construye una tabla de símbolos vacía
     */
    public SymbolTable () {
        // Do nothing
    }
    
    /**
     * Construye una nueva tabla de simbolos y añade los elementos especificados
     * 
     * @param table
     *            Tabla con los identificadores a añadir
     */
    public SymbolTable (HashMap<String, Entry> table) {
        this.table.putAll(table); // TODO Hace falta este constructor?
    }
    
    /**
     * @param ident
     *            Identificador que se quiere comprobar
     * @return <tt>true</tt> si el identificador existe, <tt>false</tt> si no
     */
    public boolean hasIdentifier (String ident) {
        return table.containsKey(ident);
    }
    
    /**
     * @param ident
     *            Identificador a añadir
     * @param type
     *            Tipo del identificador
     * @param constant
     *            Si el identificador es una constante
     * @param address
     *            Dirección de memoria del identificador
     * @param value
     *            Valor del identificador
     */
    public void putIdentifier (String ident, Type type, boolean constant, int address, Object value) {
        table.put(ident, new Entry(type, constant, address, value));
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @return Fila de la tabla completa
     */
    private Entry getEntry (String ident) {
        if (!hasIdentifier(ident)) {
            throw new NoSuchElementException(ident);
        }
        
        return table.get(ident);
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @return Dirección del identificador
     */
    public int getIdentifierAddress (String ident) {
        return getEntry(ident).address;
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @return Tipo del identificador
     */
    public Type getIdentfierType (String ident) {
        return getEntry(ident).type;
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @return Si el identificador es una constante
     */
    public boolean isIdentifierConstant (String ident) {
        return getEntry(ident).constant;
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @return Valor del identificador
     */
    public Object getIdentifierValue (String ident) {
        return getIdentifierValue(ident, Object.class);
    }
    
    /**
     * @param ident
     *            Identificador a consultar
     * @param type
     *            Tipo del valor
     * @return Valor del identificador
     */
    public <T> T getIdentifierValue (String ident, Class<T> type) {
        Object obj = getEntry(ident).value;
        
        if (type.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }
}
