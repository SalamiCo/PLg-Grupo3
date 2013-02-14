package plg.gr3.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Clase que crea un manejador para el JList que se muestra en la pestaña de debug.
 * */

public class DebugHandler {
    
    /**
     * Lista para la pestaña debug.
     * */
    private JList<String> debugList;
    
    /**
     * Crea una nueva lista de sólo lectura para la pestaña debug.
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
     * Obtiene la lista que se muestra en la pestaña debug.
     * */
    public JList<String> getDebugList () {
        return debugList;
    }
}
