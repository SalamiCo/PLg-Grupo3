package plg.gr3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

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

        private final List<Parameter> params;

        /**
         * @param classDec Tipo de declaración
         * @param scope Global o local
         * @param address Dirección del identificador
         * @param type Tipo del identificador
         * @param value Valor del identificador
         * @param params Parámetros del identificador
         */
        public Row (ClassDec classDec, Scope scope, int address, Type type, Value value, List<Parameter> params) {
            this.classDec = classDec;
            this.scope = scope;
            this.address = address;
            this.type = type;
            this.value = value;
            this.params = Collections.unmodifiableList(new ArrayList<>(params));
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

        /** @return Parámetros del identificador */
        public List<Parameter> getParams () {
            return params;
        }

        @Override
        public String toString () {
            return "Row[classDec=" + classDec + ", scope=" + scope + ", address=" + address + ", type=" + type
                   + ", value=" + value + "]";
        }

    }

    /** Estructura interna de la tabla */
    private final HashMap<String, Row> table;

    /**
     * Construye una tabla de símbolos vacía
     */
    public SymbolTable () {
        table = new HashMap<>();
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
     * @param classDec Tipo de declaración
     * @param scope Alcance de la variable
     * @param type Tipo del identificador
     * @param address Dirección de memoria del identificador
     * @param value Valor del identificador
     * @param params Lista de parámetros
     */
    private void putIdentifier (
        String ident, ClassDec classDec, Scope scope, int address, Type type, Value value, List<Parameter> params)
    {
        table.put(ident, new Row(classDec, scope, address, type, value, params));
    }

    /**
     * Añade una nueva constante a la tabla de símbolos
     * 
     * @param ident Nombre de la constante
     * @param type Tipo de la constante. Ha de ser un tipo primitivo.
     * @param value Valor de la constante.
     */
    public void putConstant (String ident, Type type, Value value) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(value, "value");

        if (!type.isPrimitive()) {
            throw new IllegalArgumentException("type " + type + " is not primitive");
        }

        putIdentifier(ident, ClassDec.CONSTANT, Scope.GLOBAL, 0, type, value, null);
    }

    /**
     * Añade una nueva variable a la tabla de símbolos
     * 
     * @param ident Nombre de la variable
     * @param scope Alcance de la variable
     * @param address Dirección de la variable
     * @param type Tipo de la variable
     */
    public void putVariable (String ident, Scope scope, int address, Type type) {
        Objects.requireNonNull(ident, "ident");
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(type, "type");

        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0");
        }

        putIdentifier(ident, ClassDec.VARIABLE, scope, address, type, null, null);
    }

    /**
     * Añade un nueo tipo a la tabla de símbolos
     * 
     * @param ident Nombre del tipo
     * @param type Tipo a añadir
     */
    public void putType (String ident, Type type) {
        Objects.requireNonNull(ident, "ident");
        Objects.requireNonNull(type, "type");

        putIdentifier(ident, ClassDec.TYPE, Scope.GLOBAL, 0, type, null, null);
    }

    /**
     * Añade un nuevo subprograma a la tabla de símbolos
     * 
     * @param ident Nombre del subprograma
     * @param params Parámetros del subprograma
     * @param address Dirección del subprograma
     */
    public void putSubprogram (String ident, List<Parameter> params, int address) {
        Objects.requireNonNull(ident, "ident");
        Objects.requireNonNull(params, "arguments");

        if (params.contains(null)) {
            throw new NullPointerException("arguments[" + params.indexOf(null) + "]");
        }

        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0");
        }

        putIdentifier(ident, ClassDec.SUBPROGRAM, Scope.GLOBAL, address, null, null, params);
    }

    /**
     * Añade un nuevo parámetro a la tabla de símbolos
     * 
     * @param ident Nombre del parámetro
     * @param address Dirección del parámetro
     * @param type Tipo del parámetro
     * @param isRef Si se trata de un parámetro por referencia
     */
    public void putParam (String ident, int address, Type type, boolean isRef) {
        Objects.requireNonNull(ident, "ident");

        if (address < 0) {
            throw new IllegalArgumentException("address: " + address + " < 0");
        }

        putIdentifier(ident, isRef ? ClassDec.PARAM_REF : ClassDec.PARAM_VALUE, Scope.LOCAL, address, type, null, null);
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
        return getRow(ident).getClassDec();
    }

    /**
     * @param ident Identificador a consultar
     * @return Tipo del identificador
     */
    public Scope getIdentfierScope (String ident) {
        return getRow(ident).getScope();
    }

    /**
     * @param ident Identificador a consultar
     * @return Tipo del identificador
     */
    public Type getIdentfierType (String ident) {
        Row row = getRow(ident);

        ClassDec cd = row.getClassDec();
        if (cd == ClassDec.SUBPROGRAM) {
            throw new IllegalArgumentException("Identifier " + ident + " declared as " + cd + " does not have a type");
        }

        return row.getType();
    }

    /**
     * @param ident Identificador a consultar
     * @return Dirección del identificador
     */
    public int getIdentifierAddress (String ident) {
        Row row = getRow(ident);

        ClassDec cd = row.getClassDec();
        if (cd == ClassDec.TYPE) {
            throw new IllegalArgumentException("Identifier " + ident + " declared as " + cd
                                               + " does not have an address");
        }

        return row.getAddress();
    }

    /**
     * @param ident Identificador a consultar
     * @return Valor del identificador
     */
    public Value getIdentifierValue (String ident) {
        Row row = getRow(ident);

        ClassDec cd = row.getClassDec();
        if (cd != ClassDec.CONSTANT) {
            throw new IllegalArgumentException("Identifier " + ident + " declared as " + cd + " does not have a value");
        }

        return row.getValue();
    }

    /**
     * @param ident Identificador a consultar
     * @param type Tipo del valor
     * @return Valor del identificador
     */
    public <V extends Value> V getIdentifierValue (String ident, Class<V> type) {
        Value obj = getIdentifierValue(ident);

        if (type.isInstance(obj)) {
            return type.cast(obj);
        } else {
            return null;
        }
    }

    /**
     * @param ident Identificador aconsultar
     * @return Lista de parámetros del identificador
     */
    public List<Parameter> getIdentifierParams (String ident) {
        Row row = getRow(ident);

        ClassDec cd = row.getClassDec();
        if (cd != ClassDec.SUBPROGRAM) {
            throw new IllegalArgumentException("Identifier " + ident + " declared as " + cd
                                               + " does not have a parameter list");
        }

        return row.getParams();
    }

    @Override
    public Iterator<Map.Entry<String, Row>> iterator () {
        return table.entrySet().iterator();
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("plg.gr3.parser.SymbolTable(\n");

        int strsize = 0;
        for (String str : table.keySet()) {
            strsize = Math.max(strsize, str.length());
        }

        for (Map.Entry<String, Row> entry : this) {
            String id = entry.getKey();
            for (int i = 0; i < strsize - id.length(); i++) {
                sb.append(' ');
            }
            sb.append("  [").append(id).append(" = (").append(entry.getValue()).append(")]\n");
        }

        return sb.toString();
    }
}
