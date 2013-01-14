package plg.gr3;

import java.nio.file.Path;
import java.util.Set;

import plg.gr3.debug.Debugger;
import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.TokenType;

/**
 * Clase que representa un error en la compilación de un programa.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class CompileError {
    
    /** Fichero en el que se ha producido el error */
    private final Path file;
    
    /** Línea del fichero en la que se produjoel error */
    private final int line;
    
    /** Posición en la línea en la que seprodujo el error */
    private final int column;
    
    /**
     * @param file
     *            Fichero en el que se produjo el error
     * @param line
     *            Línea en la que se produjo el eror
     * @param column
     *            Columna en la que se produjo el error
     */
    protected CompileError (Path file, int line, int column) {
        if (line < 1) {
            throw new IllegalArgumentException("line: " + line + " < 1");
        }
        if (column < 1) {
            throw new IllegalArgumentException("column: " + column + " < 1");
        }
        
        this.file = file;
        this.line = line;
        this.column = column;
    }
    
    public Path getFile () {
        return file;
    }
    
    public int getLine () {
        return line;
    }
    
    public int getColumn () {
        return column;
    }
    
    /** @return Mensaje de error que se le montrará al usuario, sin información específica de la posición del error */
    public abstract String getErrorMessage ();
    
    /** Imprime por consola el error, utilizando {@link Debugger#error} */
    public final void print () {
        Debugger.INSTANCE.in(file).at(line, column).error(getErrorMessage());
    }
    
    public static UnexpectedTokenError newUnexpectedTokenError (LocatedToken found, Set<TokenType> expected) {
        return new UnexpectedTokenError(found, expected);
    }
}
