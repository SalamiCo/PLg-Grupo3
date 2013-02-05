package plg.gr3.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CodeDocumentListener implements DocumentListener{
	
	private final FileHandler fhandler;
	
	public CodeDocumentListener(FileHandler fh){
		fhandler = fh;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
			
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		fhandler.setModified(true);		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		fhandler.setModified(true);			
	}

}
