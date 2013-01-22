package plg.gr3.errors.compile;

import plg.gr3.debug.Debugger;
import plg.gr3.errors.Error;

/**
 * Clase que representa un error en la compilación de un programa.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public abstract class CompileError extends Error {
    
    /** Línea del fichero en la que se produjoel error */
    private final int line;
    
    /** Posición en la línea en la que seprodujo el error */
    private final int column;
    
    /**
     * @param line
     *            Línea en la que se produjo el eror
     * @param column
     *            Columna en la que se produjo el error
     */
    protected CompileError (int line, int column) {
        if (line < 1) {
            throw new IllegalArgumentException("line: " + line + " < 1");
        }
        if (column < 1) {
            throw new IllegalArgumentException("column: " + column + " < 1");
        }
        
        this.line = line;
        this.column = column;
    }
    
    /**
     * @return Línea en la que se produjo el error
     */
    public int getLine () {
        return line;
    }
    
    /**
     * @return Columna en la que se produjo el error
     */
    public int getColumn () {
        return column;
    }
    
    @Override
    public final void print () {
        Debugger.INSTANCE.at(line, column).error(getErrorMessage());
    }
}
