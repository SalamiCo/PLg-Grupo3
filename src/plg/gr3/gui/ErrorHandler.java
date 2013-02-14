package plg.gr3.gui;

import javax.swing.JTextPane;

public class ErrorHandler {
    
    /**
     * Tipos de mensaje posibles para los errores de tiempo de ejecución.
     * */
    public enum ErrorType {
        
    }
    
    /**
     * Área de texto que representa los errores de tiempo de ejecución.
     * */
    private JTextPane errorPane;
    
    /**
     * Crea un nuevo área de texto (Error) de sólo lectura.
     * */
    public ErrorHandler () {
        errorPane = new JTextPane();
        errorPane.setEditable(false);
    }
    
    /**
     * Obtiene el área de texto que representa los errores de tiempo de ejecución.
     * */
    public JTextPane getErrorPane () {
        return errorPane;
    }
    
    /**
     * Inserta un nuevo mensaje del tipo especificado en el área de texto que representa los errores.
     * */
    public void printError (ErrorType type, String msg) {
        errorPane.setText(errorPane.getText() + "[" + type.toString() + "] " + msg + "\n");
    }
}
