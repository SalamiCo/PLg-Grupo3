package plg.gr3.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Clase que crea un manejador para el JList que muestra la pila de la máquina virtual.
 * */

public class VMHandler {
    
    /**
     * Lista que representa la pila de la máquina virtual.
     * */
    private JList<String> vmList;
    
    /**
     * Crea una nueva lista de sólo lectura para la pila de la máquina virtual.
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
     * Obtiene la lista que representa la pila de la máquina virtual
     * */
    public JList<String> getVmList () {
        return vmList;
    }
    
    public void populateList (List<String> stackList) {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        for (String stackEntry : stackList) {
            listModel.addElement(stackEntry);
        }
        
        vmList.setModel(listModel);
    }
    
}
