package plg.gr3.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que representa una categoría léxica del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum TokenType {
    // Literales e Identificadores
    IDENTIFIER("[a-z][a-zA-Z0-9]*", false) {
        @Override
        public boolean matches (String str) {
            if (!super.matches(str)) {
                return false;
            }
            
            // Comprobar que esto no encaja con ninguna palabra reservada
            return true; // TODO
        }
        
        @Override
        public boolean recognizes (Matcher matcher) {
            if (!super.recognizes(matcher)) {
                return false;
            }
            
            // Comprobar que no empieza por una palabra reservada
            return true; // TODO
        }
        
    },
    
    LIT_NATURAL("0|[1-9]\\d*", false),
    LIT_INTEGER("0|-?[1-9]\\d*", false),
    LIT_FLOAT("(0|-?[1-9]\\d*)(\\.(0|\\d*[1-9]))?([eE](0|-?[1-9]\\d*))?", false),
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
    RW_VARCONSTS("var-consts(?=[^a-zA-Z0-9]+|$)", true),
    RW_INSTRUCTIONS("instructions(?=[^a-zA-Z0-9]+|$)", true),
    RW_VAR("var(?=[^a-zA-Z0-9]+|$)", true),
    RW_CONST("const(?=[^a-zA-Z0-9]+|$)", true),
    RW_NATURAL("natural(?=[^a-zA-Z0-9]+|$)", true),
    RW_NAT("nat(?=[^a-zA-Z0-9]+|$)", true),
    RW_BOOLEAN("boolean(?=[^a-zA-Z0-9]+|$)", true),
    RW_INTEGER("integer(?=[^a-zA-Z0-9]+|$)", true),
    RW_INT("int(?=[^a-zA-Z0-9]+|$)", true),
    RW_FLOAT("float(?=[^a-zA-Z0-9]+|$)", true),
    RW_CHARACTER("character(?=[^a-zA-Z0-9]+|$)", true),
    RW_CHAR("char(?=[^a-zA-Z0-9]+|$)", true),
    RW_IN("in(?=[^a-zA-Z0-9]+|$)", true),
    RW_OUT("out(?=[^a-zA-Z0-9]+|$)", true),
    RW_SWAP1("swap1(?=[^a-zA-Z0-9]+|$)", true),
    RW_SWAP2("swap2(?=[^a-zA-Z0-9]+|$)", true),
    RW_AND("and(?=[^a-zA-Z0-9]+|$)", true),
    RW_OR("or(?=[^a-zA-Z0-9]+|$)", true),
    RW_NOT("not(?=[^a-zA-Z0-9]+|$)", true),
    
    // Fin de fichero. El patrón de EOF es irrelevante: Se trata de un caso especial y no se utilizará.
    // El valor null se trata en el constructor
    EOF(null, false);
    
    /** Patrón para esta categoría léxica */
    private final Pattern pattern;
    
    /** Si esta categoría es una palabra reservada */
    private final boolean keyword;
    
    /**
     * @param regex
     *            Expresión regular que define la categoría léxica
     */
    private TokenType (String regex, boolean keyword) {
        // Tratamos adecuadamente el caso de una expresión nula. Puesto que indica que su valor no va a usarse, no nos
        // importa su valor, pero nos aseguramos de que *no* es null puesto que otras clases se basan en ello.
        pattern = Pattern.compile(regex == null ? "" : regex);
        this.keyword = keyword;
    }
    
    /**
     * @return Patrón asociado a esta categoría
     */
    public Pattern getPattern () {
        return pattern;
    }
    
    /**
     * @return <tt>true</tt> si se trata de una palabra reservada, <tt>false</tt> en caso contrario
     */
    public boolean isKeyword () {
        return keyword;
    }
    
    public boolean matches (String str) {
        return pattern.matcher(str).matches();
    }
    
    public boolean recognizes (Matcher matcher) {
        matcher.usePattern(pattern);
        return matcher.lookingAt();
    }
}
