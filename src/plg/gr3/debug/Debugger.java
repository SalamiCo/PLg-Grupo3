package plg.gr3.debug;

import java.io.PrintWriter;
import java.util.Objects;

import plg.gr3.vm.instr.Instruction;

/**
 * Depurador de la práctica. Capaz de imprimir mensajes de información, de depuración o de error.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public enum Debugger {
    /** Singleton del depurador */
    INSTANCE;
    
    /** Flujo de salida de todos los mensajes */
    private volatile PrintWriter out;
    
    /** Flujo de salida de todos los mensajes */
    private volatile PrintWriter err;
    
    /** Línea en la que se envía un mensaje */
    private int line = 0;
    
    /** Columna en la que se envía un mensaje */
    private int column = 0;
    
    /** Posición en la que se dioel error */
    private int pos = 0;
    
    /** Fichero en el que se envía un mensaje */
    private Instruction instr = null;
    
    /** Si el *logging* está activado */
    private boolean logging;
    
    /** Si el modo depuración está activado */
    private boolean debug;
    
    /** Devuelve el depurador a su estado original */
    private void reset () {
        line = 0;
        column = 0;
        pos = 0;
        instr = null;
    }
    
    private Debugger () {
        useStandardStreams();
    }
    
    /** Establece como streams de salida y error la salida y error estándares del sistema */
    public void useStandardStreams () {
        useOutputStream(new PrintWriter(System.out));
        useErrorStream(new PrintWriter(System.err));
    }
    
    /** @param output Flujo a usar como salida */
    public void useOutputStream (PrintWriter output) {
        this.out = Objects.requireNonNull(output, "output");
    }
    
    /** @param error Flujo a usar como salida de error */
    public void useErrorStream (PrintWriter error) {
        this.err = Objects.requireNonNull(error, "error");
    }
    
    /** @param logging <tt>true</tt> para activar el <i>logging</i>, <tt>false</tt> para desactivarlo */
    public void setLoggingEnabled (boolean logging) {
        this.logging = logging;
    }
    
    /** @param debug <tt>true</tt> para activar el <i>debug</i>, <tt>false</tt> para desactivarlo */
    public void setDebugEnabled (boolean debug) {
        this.debug = debug;
    }
    
    /**
     * Define la línea y columna que afectan siguiente mensaje.
     * 
     * @param line Línea en la que se dio el error
     * @param column Columna en la que se dio el error
     * @return <tt>this</tt>
     */
    public Debugger at (int line, int column) {
        this.line = line;
        this.column = column;
        this.pos = 0;
        this.instr = null;
        return this;
    }
    
    /**
     * Define la posición de memoria y la instrucción que afectan al siguiente mensaje.
     * 
     * @param pos Posición de memoria en la que se dio el error
     * @param instr Instrucción que dio el error
     * @return <tt>this</tt>
     */
    public Debugger in (int pos, Instruction instr) {
        this.pos = pos;
        this.instr = instr;
        this.line = 0;
        this.column = 0;
        return this;
    }
    
    private void message (String type, String message, PrintWriter pw) {
        String fmtInstr = instr == null ? "" : ", %5$s";
        String fmtPos = pos <= 0 ? "" : " pos %6$s";
        
        String fmtLine = line <= 0 ? "" : " line %3$d";
        String fmtCol = column <= 0 ? "" : ", col %4$d";
        
        String fmt = "[%1$s]" + fmtPos + fmtInstr + fmtLine + fmtCol + ": %2$s%n";
        
        String msg = String.format(fmt, type, message, line, column, instr, pos + 1);
        pw.printf(msg);
        pw.flush();
    }
    
    /**
     * Escribe un mensaje informativo.
     * 
     * @param format Formato del mensaje
     * @param params Parámetros del mensaje
     */
    public void log (String format, Object... params) {
        if (logging) {
            message("LOG", String.format(format, params), out);
        }
        reset();
    }
    
    /**
     * Escribe un mensaje de depuración.
     * 
     * @param format Formato del mensaje
     * @param params Parámetros del mensaje
     */
    public void debug (String format, Object... params) {
        if (logging && debug) {
            message("DBG", String.format(format, params), out);
        }
        reset();
    }
    
    /**
     * Escribe un mensaje de error
     * 
     * @param format Formato del mensaje
     * @param params Parámetros del mensaje
     */
    public void error (String format, Object... params) {
        message("ERR", String.format(format, params), err);
        reset();
    }
}
