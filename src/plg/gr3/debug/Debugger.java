package plg.gr3.debug;

import java.io.PrintWriter;
import java.nio.file.Path;

/**
 * Depurador de la práctica. Capaz de imprimir mensajes de información, de depuración o de error.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum Debugger {
    INSTANCE;
    
    /** Flujo de salida de todos los mensajes */
    private final PrintWriter out;
    
    /** Flujo de salida de todos los mensajes */
    private final PrintWriter err;
    
    /** Línea en la que se envía un mensaje */
    private int line = 0;
    
    /** Columna en la que se envía un mensaje */
    private int column = 0;
    
    /** Fichero en el que se envía un mensaje */
    private Path file = null;
    
    /** Si el *logging* está activado */
    private boolean logging;
    
    /** Si el modo depuración está activado */
    private boolean debug;
    
    /** Devuelve el depurador a su estado original */
    private void reset () {
        line = 0;
        column = 0;
        file = null;
    }
    
    private Debugger () {
        this.out = new PrintWriter(System.out);
        this.err = new PrintWriter(System.err);
    }
    
    public void setLoggingEnabled (boolean logging) {
        this.logging = logging;
    }
    
    public void setDebugEnabled (boolean debug) {
        this.debug = debug;
    }
    
    public Debugger at (int line) {
        this.line = line;
        this.column = 0;
        return this;
    }
    
    public Debugger at (int line, int column) {
        this.line = line;
        this.column = column;
        return this;
    }
    
    public Debugger in (Path file) {
        this.file = file;
        return this;
    }
    
    private void message (String type, String message, PrintWriter pw) {
        String fmtFile = file == null ? "" : " (%5$s)";
        String fmtLine = line <= 0 ? "" : " line %3$d";
        String fmtCol = column <= 0 ? "" : ", col %4$d";
        String fmt = "[%1$s]" + fmtFile + fmtLine + fmtCol + ": %2$s%n";
        
        pw.printf(fmt, type, message, line, column, file);
        pw.flush();
    }
    
    public void log (String format, Object... params) {
        if (logging) {
            message("LOG", String.format(format, params), out);
        }
        reset();
    }
    
    public void debug (String format, Object... params) {
        if (logging && debug) {
            message("DBG", String.format(format, params), out);
        }
        reset();
    }
    
    public void error (String format, Object... params) {
        message("ERR", String.format(format, params), err);
        reset();
    }
}
