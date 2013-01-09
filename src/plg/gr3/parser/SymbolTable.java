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
        this.table.putAll(table); // TODO Hace falta?
    }
    
    public boolean hasIdentifier (String ident) {
        return table.containsKey(ident);
    }
    
    private Entry getEntry (String ident) {
        if (!hasIdentifier(ident)) {
            throw new NoSuchElementException(ident);
        }
        
        return table.get(ident);
    }
    
    public int getIdentifierAddress (String ident) {
        return getEntry(ident).address;
    }
    
    public Type getIdentfierType (String ident) {
        return getEntry(ident).type;
    }
    
    public boolean isIdentifierConstant (String ident) {
        return getEntry(ident).constant;
    }
    
    public <T> T getIdentifierValue (String ident, Class<T> type) {
        Object obj = getEntry(ident).value;
        
        if (type.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }
}
