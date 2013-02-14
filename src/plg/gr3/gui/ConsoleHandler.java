package plg.gr3.gui;

import javax.swing.JTextPane;

public class ConsoleHandler {
    
    public final String prompt = "> ";
    
    /**
     * Área de texto que representa la consola.
     * */
    private JTextPane consoleLogPane;
    
    private JTextPane consoleInputPane;
    
    /**
     * Crea dos paneles de texto, uno para la salida de la consola y otro para la entrada de texto.
     * */
    public ConsoleHandler () {
        consoleLogPane = new JTextPane();
        consoleLogPane.setEditable(false);
        consoleInputPane = new JTextPane();
        consoleInputPane.setEditable(true);
    }
    
    /**
     * Obtiene el área de texto que representa la salida de la consola.
     * */
    public JTextPane getConsoleLogPane () {
        return consoleLogPane;
    }
    
    public JTextPane getConsoleInputPane () {
        return consoleInputPane;
    }
    
    /**
     * Inserta un nuevo mensaje precedido por el prompt en la salida de la consola.
     * */
    public void printConsole (String msg) {
        consoleLogPane.setText(consoleLogPane.getText() + prompt + msg + "\n");
    }
}
