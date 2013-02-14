package plg.gr3.gui;

import javax.swing.JTextPane;

/**
 * Clase que crea un manejador para el JTextPane que alberga el debugger.
 * */

public class DebugHandler {
    
    /**
     * Área de texto que representa la maquina virtual.
     * */
    private static JTextPane debugPane;
    
    /**
     * Crea un nuevo área de texto de sólo lectura para el debugger.
     * */
    public DebugHandler () {
        debugPane = new JTextPane();
        debugPane.setEditable(false);
    }
    
    /**
     * Obtiene el área de texto que representa el debugger
     * */
    public static JTextPane getDebugPane () {
        return debugPane;
    }
}
