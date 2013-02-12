package plg.gr3.gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que crea un manejador para el JTable que alberga la tabla de símbolos de la aplicación.
 * */
public class SymbolTableHandler {
    
    /**
     * Tabla que representa la tabla de símbolos.
     * */
    private JTable symbolTable;
    
    /**
     * Crea una nueva tabla de símbolos de solo lectura.
     * */
    public SymbolTableHandler () {
        symbolTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel() {
            private static final long serialVersionUID = 4482796276781353831L;
            
            @Override
            public boolean isCellEditable (int row, int column) {
                return false;
            }
        };
        
        // auto resize off
        symbolTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // setting the column name
        String[] tableColumnNames = new String[5];
        tableColumnNames[0] = "Nombre";
        tableColumnNames[1] = "Tipo";
        tableColumnNames[2] = "Const";
        tableColumnNames[3] = "Valor";
        tableColumnNames[4] = "Dirección";
        String[] tableColumnNames1 = new String[5];
        tableColumnNames1[0] = "Nombre";
        tableColumnNames1[1] = "Tipo";
        tableColumnNames1[2] = "Const";
        tableColumnNames1[3] = "Valor";
        tableColumnNames1[4] = "sjdioañpinasoihdnasoihdasoihoidsahoash";
        tableModel.setColumnIdentifiers(tableColumnNames);
        
        tableModel.addRow(tableColumnNames1);
        
        symbolTable.setModel(tableModel);
        symbolTable.setVisible(true);
    }
    
    /**
     * Obtiene la tabla que representa la tabla de símbolos.
     * */
    public JTable getSymbolTable () {
        return symbolTable;
    }
}
