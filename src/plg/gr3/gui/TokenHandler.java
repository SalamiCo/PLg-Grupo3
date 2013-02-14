package plg.gr3.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Clase que crea un manejador para el JList que muestra los tokens.
 * */

public class TokenHandler {
    
    /**
     * Área de texto que representa la lista de tokens.
     * */
    private JList<String> tokenList;
    
    /**
     * Crea una nueva lista de solo lectura para mostrar los tokens.
     * */
    public TokenHandler () {
        DefaultListModel<String> listModel = populateList();
        tokenList = new JList<String>();
        listModel.addElement("pruebaPruebaPrueba");
        tokenList.setModel(listModel);
        tokenList.setEnabled(false);
    }
    
    private DefaultListModel<String> populateList () {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        return listModel;
    }
    
    /**
     * Obtiene la lista que muestra los tokens
     * */
    public JList<String> getTokenList () {
        return tokenList;
    }
}
