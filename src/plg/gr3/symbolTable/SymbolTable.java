package plg.gr3.symbolTable;

import java.util.HashMap;

/**
 * Tabla de simbolos.
 * 
 * Esta clase implementa la tabla de simbolos. Es un hash map que contiene como clave el identificador de la variable o
 * costante que se guarde. Como valor, contiene una tupla con los atributos que la tabla de símbolos guarda tipo, si es
 * constante o no, la dirección y el valor para las constantes. Como las constantes pueden ser de cualquier tipo
 * guardamos el lexema del valor. Luego cuando necesitamos el valor tendremos que convertirlo a su tipo correspondiente.
 * 
 * TODO Revisar lo del valor de las constantes
 * 
 * @author PLg Grupo 03 2012/2013
 */

public class SymbolTable {
    
    /*
     * [ID | <type, const, dir, value>]
     */
    
    private class Tuple {
        public Type type;
        
        public boolean consta;
        
        public int dir;
        
        public String value; // valor de las constantes 
        
    }
    
    private HashMap<String, Tuple> table;
    
    public SymbolTable () {
        table = new HashMap<String, Tuple>();
    }
    
    public SymbolTable (HashMap<String, Tuple> table) {
        this.table = table;
    }
    
    public Integer addressIdent (String ident) {
        Tuple tuple;
        tuple = table.get(ident);
        if (tuple != null) {
            return tuple.dir;
        }
        return -1;
    }
    
    public Type typeOfIden (String ident) {
        Tuple tuple;
        tuple = table.get(ident);
        if (tuple != null) {
            return tuple.type;
        }
        return new Type("Error");
    }
    
    public boolean identExist (String ident) {
        return table.containsKey(ident);
    }
    
}
