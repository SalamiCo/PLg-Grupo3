package plg.gr3.gui;

import javax.swing.JTextPane;

/**
 * Clase que crea un manejador para el JTextPane que alberga el log de la aplicación.
 * */
public class LogHandler {
    
    /**
     * Tipos de mensaje posibles para el Log.
     * */
    public enum LogType {
        ERROR,
        WARNING,
        LOG
    }
    
    /**
     * Área de texto que representa el Log.
     * */
    private JTextPane logPane;
    
    /**
     * Crea un nuevo área de texto (Log) de sólo lectura.
     * */
    public LogHandler () {
        logPane = new JTextPane();
        logPane.setEditable(false);
    }
    
    /**
     * Obtiene el área de texto que representa el Log.
     * */
    public JTextPane getLogPane () {
        return logPane;
    }
    
    /**
     * Inserta un nuevo mensaje del tipo especificado en el area de texto del log.
     * */
    public void log (LogType type, String msg) {
        logPane.setText(logPane.getText() + "[" + type.toString() + "] " + msg + "\n");
    }
}
