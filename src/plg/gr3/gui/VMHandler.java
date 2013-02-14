package plg.gr3.gui;

import javax.swing.JTextPane;

/**
 * Clase que crea un manejador para el JTextPane que alberga la máquina virtual.
 * */

public class VMHandler {
    
    /**
     * Área de texto que representa la maquina virtual.
     * */
    private static JTextPane vmPane;
    
    /**
     * Crea un nuevo área de texto de sólo lectura para la máquina virtual.
     * */
    public VMHandler () {
        vmPane = new JTextPane();
        vmPane.setEditable(false);
    }
    
    /**
     * Obtiene el área de texto que representa la máquina virtual
     * */
    public JTextPane getVmPane () {
        return vmPane;
    }
}
