package plg.gr3.parser;

/**
 * Alcance de las declaraciones que podemos insertar en la tabla de símbolos {@link SymbolTable}
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum Scope {
    /** Alcance global, aplicable a todas las declaraciones excepto parámetros */
    GLOBAL,

    /** Alcance local, aplicable a declaraciones de variables y parámetros */
    LOCAL
}
