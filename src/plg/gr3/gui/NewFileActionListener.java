package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewFileActionListener implements ActionListener{

	private final CompilerUI invoker; 
	public NewFileActionListener(CompilerUI compiler){
		invoker = compiler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		invoker.getFileHandler().newFileAction();	
	}

}
