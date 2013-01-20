package plg.gr3.lexer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que representa una categoría léxica del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum TokenType {
    // Literales e Identificadores
    IDENTIFIER("[a-z][a-zA-Z0-9]*", "(?=[^a-zA-Z0-9]|$)", false) {
        @Override
        public boolean matches (String str) {
            if (!super.matches(str)) {
                return false;
            }
            
            // Comprobar que esto no encaja con ninguna palabra reservada
            for (TokenType keyword : KEYWORDS) {
                if (keyword.matches(str)) {
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public boolean recognizes (Matcher matcher) {
            if (!super.recognizes(matcher)) {
                return false;
            }
            
            // Comprobar que esto no encaja con ninguna palabra reservada
            for (TokenType keyword : KEYWORDS) {
                if (keyword.recognizes(matcher)) {
                    return false;
                }
            }
            
            // Update the matcher with the actual match
            super.recognizes(matcher);
            return true;
        }
        
    },
    
    LIT_NATURAL("0|[1-9]\\d*", false),
    LIT_FLOAT("(0|[1-9]\\d*)(\\.(0|\\d*[1-9]))|(0|[1-9]\\d*)(\\.(0|\\d*[1-9]))?([eE](0|-?[1-9]\\d*))", false),
    LIT_CHARACTER("'[a-zA-Z0-9]'", false),
    
    // Símbolos
    SYM_ASIGNATION("=", false),
    SYM_CONST_ASIGNATION(":=", false),
    SYM_PAR_LEFT("\\(", false),
    SYM_PAR_RIGHT("\\)", false),
    SYM_CURLY_LEFT("\\{", false),
    SYM_CURLY_RIGHT("\\}", false),
    SYM_SEMICOLON(";", false),
    SYM_LOWER("<", false),
    SYM_LOWER_OR_EQUAL("<=", false),
    SYM_GREATER(">", false),
    SYM_GREATER_OR_EQUAL(">=", false),
    SYM_EQUAL("==", false),
    SYM_NOT_EQUAL("!=", false),
    SYM_PLUS("\\+", false),
    SYM_MINUS("-", false),
    SYM_MULT("\\*", false),
    SYM_DIV("/", false),
    SYM_MODULO("%", false),
    SYM_SHIFT_LEFT("<<", false),
    SYM_SHIFT_RIGHT(">>", false),
    
    // Palabras reservadas
    RW_PROGRAM("program:", true),
    RW_VARCONSTS("var-consts", true),
    RW_INSTRUCTIONS("instructions", true),
    RW_VAR("var", true),
    RW_CONST("const", true),
    RW_NATURAL("natural", true),
    RW_NAT("nat", true),
    RW_BOOLEAN("boolean", true),
    RW_INTEGER("integer", true),
    RW_INT("int", true),
    RW_FLOAT("float", true),
    RW_CHARACTER("character", true),
    RW_CHAR("char", true),
    RW_IN("in", true),
    RW_OUT("out", true),
    RW_SWAP1("swap1", true),
    RW_SWAP2("swap2", true),
    RW_AND("and", true),
    RW_OR("or", true),
    RW_NOT("not", true),
    RW_TRUE("true", true),
    RW_FALSE("false", true),
    
    // Fin de fichero. El patrón de EOF es irrelevante: Se trata de un caso especial y no se utilizará.
    // El valor null se trata en el constructor
    EOF(null, false);
    
    /** Patrón de "lookahead" para palabras reservadas */
    private static final String LOOKAHEAD = "(?=[^a-zA-Z0-9]|$)";
    
    /** Conjunto con todas las palabras reservadas */
    private static final Set<TokenType> KEYWORDS;
    static {
        Set<TokenType> set = EnumSet.noneOf(TokenType.class);
        for (TokenType tt : values()) {
            if (tt.isKeyword()) {
                set.add(tt);
            }
        }
        KEYWORDS = Collections.unmodifiableSet(set);
    }
    
    /** Patrón para esta categoría léxica */
    private final Pattern pattern;
    
    /** Patrón para esta categoría léxica, incluyendo la sección de "lookahead" si fuera necesaria */
    private final Pattern lookAheadPattern;
    
    /** Si esta categoría es una palabra reservada */
    private final boolean keyword;
    
    private TokenType (String regex, boolean keyword) {
        this(regex, keyword ? LOOKAHEAD : "", keyword);
    }
    
    /**
     * @param regex
     *            Expresión regular que define la categoría léxica
     * @param laPart
     *            Parte del look-ahead
     * @param keyword
     *            Si se trata de una palabra reservada
     */
    private TokenType (String regex, String laPart, boolean keyword) {
        // Tratamos adecuadamente el caso de una expresión nula. Puesto que indica que su valor no va a usarse, no nos
        // importa su valor, pero nos aseguramos de que *no* es null puesto que otras clases se basan en ello.
        pattern = Pattern.compile(regex == null ? "" : regex);
        lookAheadPattern = Pattern.compile(pattern.pattern() + laPart);
        this.keyword = keyword;
    }
    
    /**
     * @return Patrón asociado a esta categoría
     */
    public Pattern getPattern () {
        return pattern;
    }
    
    /**
     * @return Patrón asociado a esta categoría, incluyendo las secciones lookahead si fueran necesarias
     */
    public Pattern getLookAheadPattern () {
        return lookAheadPattern;
    }
    
    /**
     * @return <tt>true</tt> si se trata de una palabra reservada, <tt>false</tt> en caso contrario
     */
    public boolean isKeyword () {
        return keyword;
    }
    
    /**
     * @param str
     *            Cadena a comprobar
     * @return <tt>true</tt> si la cadena encaja, <tt>false</tt> si no
     */
    public boolean matches (String str) {
        return pattern.matcher(str).matches();
    }
    
    /**
     * @param matcher
     *            Matcher a comprobar
     * @return <tt>true</tt> si la cadena encaja, <tt>false</tt> si no
     */
    public boolean recognizes (Matcher matcher) {
        matcher.usePattern(lookAheadPattern);
        return matcher.lookingAt();
    }
}
