package plg.gr3.gui;

import java.util.Iterator;
import java.util.List;

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
        debugList = new JList<String>();
        debugList.setEnabled(false);
    }
    
    public DefaultListModel<String> populateList () {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        return listModel;
    }
    
    /**
     * Obtiene la lista que se muestra en la pestaña debug.
     * */
    public JList<String> getDebugList () {
        return debugList;
    }
    
    public void populateList (List<String> memoryList) {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        int i = 0;
        Iterator<String> it = memoryList.iterator();
        
        while (it.hasNext()) {
            listModel.addElement("[" + i + "] " + it.next());
            i++;
        }
        
        debugList.setModel(listModel);
    }
    
}
