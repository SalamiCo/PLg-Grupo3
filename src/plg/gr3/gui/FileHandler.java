package plg.gr3.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;


import plg.gr3.gui.LogHandler.LogType;

/**
 * Clase que crea un manejador de archivos.
 * */
public class FileHandler {

	/**
	 * Si se ha guardado un archivo en el sistema de ficheros,
	 * filePath indica la ruta hasta dicho archivo.
	 * */
	private String filePath;
	
	/**
	 * Indica si un archivo ha sido modificado después de ser
	 * creado o abierto 
	 * */
	private boolean modified;
	
	/**
	 * Referencia al área de texto donde se aloja el archivo
	 * */
	private JTextPane textEditor;

	/**
	 * Genera un nuevo manejador de archivos, sin un archivo asociado, 
	 * y con un área de texto donde cargar o guardar el contenido.
	 * */
	public FileHandler(JTextPane txtPane){
		//inicializar los atributos
		this.setFilePath("");
		this.setModified(false);
		this.setTextEditor(txtPane);
		//añadir listener al documento
		this.getTextEditor().getDocument().addDocumentListener(new CodeDocumentListener(this));
		//crear un nuevo archivo
		this.newFileAction();
	}

	//Getters y Setters
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public JTextPane getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(JTextPane textEditor) {
		this.textEditor = textEditor;
	}

	public void copyTextAction(){
		textEditor.copy();
	}
	
	public void cutTextAction(){
		textEditor.cut();
	}
	
	public void pasteTextAction(){
		textEditor.paste();
	}
	
	public void selectAllTextAction(){
		textEditor.selectAll();
	}

	/**
	 * Crea un nuevo manejador de archivos.
	 * Si ya existía y está modificado, da la opción de guardarlo.
	 * Si no tiene ninguna modificación, crea uno nuevo directamente.
	 * */
	public void newFileAction(){		
				
		if(askBeforeClose()){
			//Si no cancela el proceso, limpiar el área de texto
			this.getTextEditor().setText("");
			
			//Limpiar la ruta y marcarlo como no modificado
			this.setFilePath("");
			this.setModified(false);
		}
	}
	
	/**
	 * Carga un nuevo archivo en el manejador de archivos.
	 * Si ya existe uno cargado y está modificado, da la opción de guardarlo.
	 * Si no tiene ninguna modificación, lo abre directamente.
	 * */
	public void openFileAction(){
		if(this.askBeforeClose()){
			
			//Crear el diálogo para seleccionar un archivo
			JFileChooser fc = new JFileChooser();
	        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        
	        int r=fc.showOpenDialog(textEditor);
	        if(r==JFileChooser.CANCEL_OPTION) return;
	        
	        //Obtener el archivo seleccionado
	        File myfile = fc.getSelectedFile();
	        if(myfile == null || myfile.getName().equals(""))
	        {
	            CompilerUI.log(LogType.WARNING, "Please, select a file.");
	            return;
	        }
	        
	        try
	        {
	        	//Abrir el archivo para lectura
	            FileReader fileRead=new FileReader(myfile); // name is the file you want to read
	            textEditor.read(fileRead,null);
	            
	            //Al hacer read, crea un nuevo Document (sin Listeners)
	            this.getTextEditor().getDocument().addDocumentListener(new CodeDocumentListener(this));
	            
	            //Cambiar los atributos del manejador de archivos
	            this.setFilePath(myfile.getPath());
	            this.setModified(false);
	            
	            //Colocar el cursor sobre la última posición del editor
	            textEditor.setCaretPosition(textEditor.getDocument().getLength());
	            
	            CompilerUI.log(LogType.LOG, myfile.getName()+" successfully loaded.");
	            
	        }
	        catch(FileNotFoundException e)
	        {
	            CompilerUI.log(LogType.ERROR, "File not found: "+e.getMessage());
	        }
	        catch(IOException e)
	        {
	        	CompilerUI.log(LogType.ERROR, "Could not open file: "+e.getMessage());
	        }
		}
	}
	
	/**
	 * Guarda el archivo del manejador de archivos.
	 * Si no tiene fichero asociado y está modificado, da la opción de elegir la ruta del archivo
	 * Si tiene fichero asociado y está modificado, lo guarda directamente.
	 * */
	public boolean saveFileAction(){
		String path = getFilePath();
		
		if(path == ""){
			//si no tiene path asignado, guardar como...
			return saveAsFileAction();
		}else{
			//si tiene path asignado, guardar en dicho path
			return writeFile(path);
		}
	}
	
	/**
	 * Guardar el archivo en el manejador de archivos, dando a elegir la ruta.
	 * */
	public boolean saveAsFileAction(){
		//Elegir la ruta del archivo
		JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        //Mostrar diálogo de Guardar Como...
        int result = fc.showSaveDialog(this.getTextEditor());
        if( result == JFileChooser.CANCEL_OPTION ) return false;
        
        //Comprobar si se ha seleccionado un archivo existente
        //o si el nombre del nuevo archivo es correcto
        File myfile = fc.getSelectedFile();
        if(myfile==null || myfile.getName().equals("")){
        	//Error: nombre de archivo inválido
        	CompilerUI.log(LogType.ERROR,"Please, enter a file name");
            return false;
        }
 
        //Diálogo para sobreescribir un archivo existente
        if(myfile.exists()){
            result = JOptionPane.showConfirmDialog(this.getTextEditor(),
            			"A file with same name already exists.\nDo you want to overwrite?");
            if(result != 0) return false;
        }
        
        //Escribir archivo
        return writeFile(myfile.getPath());
	}
	
	/**
	 * Escribe el contenido del editor de texto en un archivo
	 * @param Fichero donde se desea escribir el contenido
	 * */
	private boolean writeFile(String path){
        //Salvaguardar los datos previos del manejador
        String prev_path = this.getFilePath();
        boolean prev_modified = this.isModified();
        
        //Escribir en el archivo de la ruta seleccionada
        try{
            //Escribir el contenido del área de texto en el fichero
        	FileWriter fw = new FileWriter(path);
            fw.write(this.getTextEditor().getText());
            
            //Cambiar los atributos del manejador de archivos
            this.setFilePath(path);
            this.setModified(false);
            
            //Cerrar el fichero
            fw.close();
            CompilerUI.log(LogType.LOG,"File saved: "+path);
            return true;
        }
        catch(IOException e){
        	//Error: fallo al guardar el archivo, revertir los cambios
        	this.setFilePath(prev_path);
            this.setModified(prev_modified);
            CompilerUI.log(LogType.ERROR,"Could not save file: "+e.getMessage());
            return false;
        }
	}
	
	/**
	 * Si el área de texto se ha modificado antes de cerrar, 
	 * pregunta si quiere guardarlo en un archivo.
	 * @return Devuelve la confirmación para proceder a cerrar el área de texto.
	 * */
	public boolean askBeforeClose(){
		boolean cancelled = false;
		if(this.isModified()){			
			//si ha sido modificado, dar opción de guardar los cambios
			int result = JOptionPane.showConfirmDialog(this.getTextEditor(),
							"The file has been modified.\nDo you want to save?");	
			//evaluar la respuesta del usuario
			switch(result){
            	case 0: 
            		//cancelar si no se ha podido guardar
            		cancelled = !this.saveFileAction(); 
            		break;
            	case 1: 
            		//no quiere guardar los cambios
            		cancelled = false;
            		break;
            	case 2: 
            		//quiere cancelar la operación
            		cancelled = true;
            		break;
            	default: 
            		//cancelar si no se ha podido guardar
            		cancelled = !this.saveFileAction(); 
            		break;
            }
		}
		return !cancelled;
	}
	
}
