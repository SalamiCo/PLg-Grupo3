package plg.gr3.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import plg.gr3.lexer.LocatedToken;

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
        tokenList = new JList<String>();
        tokenList.setEnabled(false);
    }
    
    public void populateList (List<LocatedToken> tokens) {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        for (LocatedToken token : tokens) {
            listModel.addElement(token.toString());
        }
        
        tokenList.setModel(listModel);
    }
    
    /**
     * Obtiene la lista que muestra los tokens
     * */
    public JList<String> getTokenList () {
        return tokenList;
    }
}
