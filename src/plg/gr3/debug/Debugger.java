package plg.gr3.debug;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;

public final class Debugger {
    
    /** Flujo de salida de todos los mensajes */
    private final PrintWriter out;
    
    /** Línea en la que se envía un mensaje */
    private int line = 0;
    
    /** Columna en la que se envía un mensaje */
    private int column = 0;
    
    /** Fichero en el que se envía un mensaje */
    private Path file = null;
    
    /** Devuelve el depurador a su estado original */
    private void reset () {
        line = 0;
        column = 0;
        file = null;
    }
    
    public Debugger (Writer out) {
        this.out = new PrintWriter(out);
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
    
    private void message (String type, String message) {
        String fmtFile = file == null ? "" : " (%5$s)";
        String fmtLine = line <= 0 ? "" : " line %3$d";
        String fmtCol = column <= 0 ? "" : ", col %4$d";
        String fmt = "[%1$s]" + fmtFile + fmtLine + fmtCol + ": %2$s%n";
        out.printf(fmt, type, message, line, column, file);
        out.flush();
    }
    
    public void log (String message) {
        message("LOG", message);
        reset();
    }
    
    public void debug (String message) {
        message("DBG", message);
        reset();
    }
    
    public void error (String message) {
        message("ERR", message);
        reset();
    }
}
