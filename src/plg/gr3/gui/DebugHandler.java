package plg.gr3.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Clase que crea un manejador para el JTextPane que alberga el debugger.
 * */

public class DebugHandler {
    
    /**
     * Área de texto que representa la maquina virtual.
     * */
    private JList<String> debugList;
    
    /**
     * Crea un nuevo área de texto de sólo lectura para el debugger.
     * */
    public DebugHandler () {
        DefaultListModel<String> listModel = populateList();
        debugList = new JList<String>();
        listModel.addElement("pruebaPruebaPrueba");
        debugList.setModel(listModel);
        debugList.setEnabled(false);
    }
    
    private DefaultListModel<String> populateList () {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        return listModel;
    }
    
    /**
     * Obtiene el área de texto que representa el debugger
     * */
    public JList<String> getDebugList () {
        return debugList;
    }
}
