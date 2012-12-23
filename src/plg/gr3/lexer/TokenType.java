package plg.gr3.lexer;

import java.util.regex.Pattern;

/**
 * Clase que representa una categoría léxica del lenguaje.
 * 
 * @author PLg Grupo 03 - 2012/2013
 */
public enum TokenType {
    // Literales e Identificadores

    // Símbolos

    // Palabras reservadas

    // Fin de fichero
    EOF(null);

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
