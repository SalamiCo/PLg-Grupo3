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
    LIT_NATURAL("0|[1-9][0-9]*"),
    LIT_INTEGER("0|-?[1-9][0-9]*"),
    LIT_FLOAT("(0|-?[1-9][0-9]*)(\\.(0|[0-9]*[1-9]))?([eE](0|-?[1-9][0-9]*))?"),
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
    RW_VARCONSTS("var-consts"),
    RW_INSTRUCTIONS("instructions"),
    RW_VAR("var"),
    RW_CONST("const"),
    RW_NATURAL("natural"),
    RW_NAT("nat"),
    RW_BOOLEAN("boolean"),
    RW_INTEGER("integer"),
    RW_INT("int"),
    RW_FLOAT("float"),
    RW_CHARACTER("character"),
    RW_CHAR("char"),
    RW_IN("in"),
    RW_OUT("out"),
    RW_SWAP1("swap1"),
    RW_SWAP2("swap2"),
    RW_AND("and"),
    RW_OR("or"),
    RW_NOT("not"),

    // Fin de fichero
    EOF("$");

    /** Patrón para esta categoría léxica */
    private final Pattern pattern;

    /**
     * @param regex
     *            Expresión regular que define la categoría léxica
     */
    private TokenType (String regex) {
        pattern = regex == null ? null : Pattern.compile(regex);
    }

    /**
     * @return Patrón asociado a esta categoría, o <tt>null</tt> en el caso especial del fin de fichero
     */
    public Pattern getPattern () {
        return pattern;
    }
}
