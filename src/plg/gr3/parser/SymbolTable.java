package plg.gr3.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import plg.gr3.data.Type;
import plg.gr3.data.Value;
import plg.gr3.parser.SymbolTable.Row;

/**
 * Representación de la tabla de símbolos mediante una tabla con clave el identificador de la variable o constante que
 * se guarde y valor una tupla con los atributos de la tabla.
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
        
        private final ClassDec classDec;
        
        private final Scope scope;
        
        private final int address;
        
        private final Type type;
        
        private final Value value;
        
        /**
         * @param classDec
         * @param scope Global o local
         * @param address Dirección del identificador
         * @param type Tipo del identificador
         * @param value Valor del identificador
         */
        public Row (ClassDec classDec, Scope scope, int address, Type type, Value value) {
            this.classDec = classDec;
            this.scope = scope;
            this.address = address;
            this.type = type;
            this.value = value;
        }
        
        /** @return Clase del identificador */
        public ClassDec getClassDec () {
            return classDec;
        }
        
        /** @return Global o local */
        public Scope getScope () {
            return scope;
        }
        
        /** @return Dirección del identificador */
        public int getAddress () {
            return address;
        }
        
        /** @return Tipo del identificador */
        public Type getType () {
            return type;
        }
        
        /** @return Valor del identificador */
        public Value getValue () {
            return value;
        }
        
        @Override
        public String toString () {
            return "Row[classDec=" + classDec + ", scope=" + scope + ", address=" + address + ", type=" + type
                   + ", value=" + value + "]";
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
    public void putIdentifier (String ident, ClassDec classDec, Scope scope, int address, Type type, Value value) {
        table.put(ident, new Row(classDec, scope, address, type, value));
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
     * @return Tipo del identificador
     */
    public ClassDec getIdentfierClassDec (String ident) {
        try {
            return getRow(ident).getClassDec();
        } catch (NoSuchElementException exc) {
            throw new IllegalArgumentException("Illegal argument in get classDec of ident: " + ident);
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Tipo del identificador
     */
    public Scope getIdentfierScope (String ident) {
        try {
            return getRow(ident).getScope();
        } catch (NoSuchElementException exc) {
            throw new IllegalArgumentException("Illegal argument in get scope of ident: " + ident);
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
            throw new IllegalArgumentException("Illegal argument in get type of ident: " + ident);
        }
    }
    
    /**
     * @param ident Identificador a consultar
     * @return Dirección del identificador
     */
    public int getIdentifierAddress (String ident) {
        try {
            return getRow(ident).getAddress();
        } catch (NoSuchElementException exc) {
            throw new IllegalArgumentException("Illegal argument in get address of ident: " + ident);
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
            throw new IllegalArgumentException("Illegal argument in get value of ident: " + ident);
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
