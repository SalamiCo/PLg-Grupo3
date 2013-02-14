package plg.gr3.gui;

import javax.swing.JTextPane;

public class ConsoleHandler {
    
    public final String prompt = "> ";
    
    /**
     * Área de texto que representa la consola.
     * */
    private static JTextPane consoleLogPane;
    
    private static JTextPane consoleInputPane;
    
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
    public static JTextPane getConsoleLogPane () {
        return consoleLogPane;
    }
    
    public static JTextPane getConsoleInputPane () {
        return consoleInputPane;
    }
    
    /**
     * Inserta un nuevo mensaje precedido por el prompt en la salida de la consola.
     * */
    public void printConsole (String msg) {
        consoleLogPane.setText(consoleLogPane.getText() + prompt + msg + "\n");
    }
}
