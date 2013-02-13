package plg.gr3.gui;

import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plg.gr3.parser.SymbolTable;
import plg.gr3.parser.SymbolTable.Row;

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
    public SymbolTableHandler () {
        symbolTable = new JTable();
        symbolTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        replaceModel(new SymbolTable());
        symbolTable.setVisible(true);
    }
    
    /**
     * Obtiene la tabla que representa la tabla de símbolos.
     * */
    public JTable getSymbolTable () {
        return symbolTable;
    }
    
    /**
     * Reemplaza el modelo por uno nuevo creado a partir del contenido de la tabla de símbolos
     * 
     * @param st Tabla de símbolos de la que se obtendrá el modelo.
     * */
    public void replaceModel (SymbolTable st) {
        
        // crear nuevo modelo para la tabla
        DefaultTableModel model = new DefaultTableModel() {
            private static final long serialVersionUID = 4482796276781353831L;
            
            @Override
            public boolean isCellEditable (int row, int column) {
                return false;
            }
        };
        
        // cabecera de la tabla
        String[] tableColumnNames = {"ID", "Type", "Constant", "Value", "Address"};
        
        // añadir cabecera
        model.setColumnIdentifiers(tableColumnNames);
        
        // añadir columnas
        fillColumns(model, st);
        
        // cambia el modelo del JTable por el nuevo modelo
        symbolTable.setModel(model);
    }
    
    private void fillColumns (DefaultTableModel model, SymbolTable st) {
        
        Object[] row = new Object[5];
        for (Entry<String, Row> stEntry : st) {
            row[0] = stEntry.getKey();
            row[1] = stEntry.getValue().getType();
            row[2] = stEntry.getValue().isConstant();
            row[3] = (boolean) row[2] ? stEntry.getValue().getValue() : "-";
            row[4] = (boolean) row[2] ? "-" : stEntry.getValue().getAddress();
            model.addRow(row);
        }
        
    }
}
