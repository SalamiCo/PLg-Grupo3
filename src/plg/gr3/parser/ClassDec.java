package plg.gr3.parser;

/**
 * Tipos de declaraciones que podemos insertar en la tabla de símbolos {@link SymbolTable}
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum ClassDec {
    /** Declaraciones de variables */
    VARIABLE,

    /** Declaraciones de constantes */
    CONSTANT,

    /** Declaraciones de tipo */
    TYPE,

    /** Definiciones de subprograma */
    SUBPROGRAM,

    /** Parámetros pasados por referencia */
    PARAM_REF,

    /** Parámetros pasados por valor */
    PARAM_VALUE
}
