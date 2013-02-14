package plg.gr3.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Clase que crea un manejador para el JTextPane que alberga la máquina virtual.
 * */

public class VMHandler {
    
    /**
     * Área de texto que representa la maquina virtual.
     * */
    private JList<String> vmList;
    
    /**
     * Crea un nuevo área de texto de sólo lectura para la máquina virtual.
     * */
    public VMHandler () {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.addElement("prueba1");
        listModel.addElement("prueba2");
        listModel.addElement("prueba3");
        
        vmList = new JList<String>(listModel);
        vmList.setEnabled(false);
    }
    
    /**
     * Obtiene el área de texto que representa la máquina virtual
     * */
    public JList<String> getVmList () {
        return vmList;
    }
}
