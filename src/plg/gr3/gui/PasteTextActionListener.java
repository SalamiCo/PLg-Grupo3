package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasteTextActionListener implements ActionListener{

	private final CompilerUI invoker; 
	public PasteTextActionListener(CompilerUI compiler){
		invoker = compiler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		invoker.getFileHandler().pasteTextAction();	
	}

}
