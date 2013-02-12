package plg.gr3.gui;

import javax.swing.JTextPane;

public class ProblemHandler {
    
    /**
     * Tipos de mensaje posibles para los problemas en tiempo de compilación
     * */
    public enum ProblemType {
        ERROR
    }
    
    /**
     * Área de texto que representa los problemas en tiempo de compilación
     * */
    private final JTextPane problemPane;
    
    /**
     * Crea un nuevo área de texto (Problem) de sólo lectura.
     * */
    public ProblemHandler () {
        problemPane = new JTextPane();
        problemPane.setEditable(false);
    }
    
    /**
     * Obtiene el área de texto que representa los problemas en tiempo de compilación
     * */
    public JTextPane getLogPane () {
        return problemPane;
    }
    
    /**
     * Inserta un nuevo mensaje del tipo especificado en el area de texto que representa los problemas.
     * */
    public void printProblem (ProblemType type, String msg) {
        problemPane.setText(problemPane.getText() + "[" + type.toString() + "] " + msg + "\n");
    }
}
