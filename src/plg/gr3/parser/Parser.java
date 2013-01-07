package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;

import plg.gr3.lexer.Lexer;

/**
 * Analizador sintáctico del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Parser implements Closeable {
    
    /** Nobre del tipo <tt>natural</tt> */
    public static final String TYPE_NATURAL = "natural";
    
    /** Nobre del tipo <tt>integer</tt> */
    public static final String TYPE_INTEGER = "integer";
    
    /** Nobre del tipo <tt>float</tt> */
    public static final String TYPE_FLOAT = "float";
    
    /** Nobre del tipo <tt>boolean</tt> */
    public static final String TYPE_BOOLEAN = "boolean";
    
    /** Nobre del tipo <tt>character</tt> */
    public static final String TYPE_CHARACTER = "character";
    
    /** Analizador léxico a utilizar */
    private final Lexer lexer;
    
    /**
     * @param lexer
     *            Analizador léxico que será utilizado por este analizador sintáctico
     */
    public Parser (Lexer lexer) {
        this.lexer = lexer;
    }
    
    public void parse () {
        // TODO Analizar el lenguaje completo.
        // Este método tan sólo deberá llamar a otro, parseProg, que es el que se encarga de analizar el lenguaje
        // La idea es que cada categoría sintáctica tenga un método propio. Ese método siempre tendrá esta forma:
        // private Attributes parseXXX ( Attributes attrs ) {...}
        // El objeto que recibe son los atributos heredados, el que devuelve son los sintetizados; es decir, no hace
        // falta duplicar los atributos en caso de que haya dos versiones, sintetizada y heredada (por ejemplo, en el
        // caso de ts y tsh): se distinguirán por el objeto usado (parámetro o devuelto)
        // NOTA: La clase Attributes se construye de una forma un tanto peculiar para evitar tener que pasarle 8
        // parámetros en aquellos casos en los que sólo interesan 2. Echadle un ojo.
    }
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
    
}
