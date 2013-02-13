package plg.gr3.gui;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plg.gr3.parser.SymbolTable;

/**
 * Clase que crea un manejador para el JTable que alberga la tabla de símbolos de la aplicación.
 * */
public class SymbolTableHandler {
    
    /**
     * Tabla que representa la tabla de símbolos.
     * */
    private final JTable symbolTable;
    
    /**
     * Crea una nueva tabla de símbolos de solo lectura.
     * */
    public SymbolTableHandler (SymbolTable st) {
        this.symbolTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel() {
            private static final long serialVersionUID = 4482796276781353831L;
            
            @Override
            public boolean isCellEditable (int row, int column) {
                return false;
            }
        };
        
        // auto resize off
        this.symbolTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // setting the column name
        String[] tableColumnNames = new String[5];
        tableColumnNames[0] = "Nombre";
        tableColumnNames[1] = "Tipo";
        tableColumnNames[2] = "Const";
        tableColumnNames[3] = "Valor";
        tableColumnNames[4] = "Dirección";
        
        tableModel.setColumnIdentifiers(tableColumnNames);
        
        fillTable(st);
        
        this.symbolTable.setModel(tableModel);
        this.symbolTable.setVisible(true);
    }
    
    /**
     * Obtiene la tabla que representa la tabla de símbolos.
     * */
    public JTable getSymbolTable () {
        return symbolTable;
    }
    
    private void fillTable (SymbolTable st) {
        Set<String> keySet = st.getTable().keySet();
        
        String[] tableEntry = new String[5];
        String nextEntry;
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            nextEntry = it.next();
            tableEntry[0] = nextEntry;
            // hay que meter en tableEntry[1], 2, etc, los valores de entry
            Entry aaa = st.getTable().get(nextEntry);
        }
    }
}
