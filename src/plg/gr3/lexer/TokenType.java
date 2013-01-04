package plg.gr3.lexer;

import java.util.regex.Pattern;

/**
 * Clase que representa una categoría léxica del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum TokenType {
    // Literales e Identificadores
    IDENTIFIER("[a-z][a-zA-Z0-9]*"),
    LIT_NATURAL("0|[1-9]\\d*"),
    LIT_INTEGER("0|-?[1-9]\\d*"),
    LIT_FLOAT("(0|-?[1-9]\\d*)(\\.(0|\\d*[1-9]))?([eE](0|-?[1-9]\\d*))?"),
    LIT_CHARACTER("'[a-zA-Z0-9]'"),
    
    // Símbolos
    SYM_ASIGNATION("="),
    SYM_CONST_ASIGNATION(":="),
    SYM_PAR_LEFT("\\("),
    SYM_PAR_RIGHT("\\)"),
    SYM_CURLY_LEFT("\\{"),
    SYM_CURLY_RIGHT("\\}"),
    SYM_SEMICOLON(";"),
    SYM_LOWER("<"),
    SYM_LOWER_OR_EQUAL("<="),
    SYM_GREATER(">"),
    SYM_GREATER_OR_EQUAL(">="),
    SYM_EQUAL("=="),
    SYM_NOT_EQUAL("!="),
    SYM_PLUS("\\+"),
    SYM_MINUS("-"),
    SYM_MULT("\\*"),
    SYM_DIV("/"),
    SYM_MODULO("%"),
    SYM_SHIFT_LEFT("<<"),
    SYM_SHIFT_RIGHT(">>"),
    
    // Palabras reservadas
    RW_PROGRAM("program:"),
    RW_VARCONSTS("var-consts(?=[^a-zA-Z0-9])"),
    RW_INSTRUCTIONS("instructions(?=[^a-zA-Z0-9])"),
    RW_VAR("var(?=[^a-zA-Z0-9])"),
    RW_CONST("const(?=[^a-zA-Z0-9])"),
    RW_NATURAL("natural(?=[^a-zA-Z0-9])"),
    RW_NAT("nat(?=[^a-zA-Z0-9])"),
    RW_BOOLEAN("boolean(?=[^a-zA-Z0-9])"),
    RW_INTEGER("integer(?=[^a-zA-Z0-9])"),
    RW_INT("int(?=[^a-zA-Z0-9])"),
    RW_FLOAT("float(?=[^a-zA-Z0-9])"),
    RW_CHARACTER("character(?=[^a-zA-Z0-9])"),
    RW_CHAR("char(?=[^a-zA-Z0-9])"),
    RW_IN("in(?=[^a-zA-Z0-9])"),
    RW_OUT("out(?=[^a-zA-Z0-9])"),
    RW_SWAP1("swap1(?=[^a-zA-Z0-9])"),
    RW_SWAP2("swap2(?=[^a-zA-Z0-9])"),
    RW_AND("and(?=[^a-zA-Z0-9])"),
    RW_OR("or(?=[^a-zA-Z0-9])"),
    RW_NOT("not(?=[^a-zA-Z0-9])"),
    
    // Fin de fichero. El patrón de EOF es irrelevante: Se trata de un caso especial y no se utilizará.
    // El valor null se trata en el constructor
    EOF(null);
    
    /** Patrón para esta categoría léxica */
    private final Pattern pattern;
    
    /**
     * @param regex
     *            Expresión regular que define la categoría léxica
     */
    private TokenType (String regex) {
        // Tratamos adecuadamente el caso de una expresión nula. Puesto que indica que su valor no va a usarse, no nos
        // importa su valor, pero nos aseguramos de que *no* es null puesto que otras clases se basan en ello.
        pattern = Pattern.compile(regex == null ? "" : regex);
    }
    
    /**
     * @return Patrón asociado a esta categoría
     */
    public Pattern getPattern () {
        return pattern;
    }
}
