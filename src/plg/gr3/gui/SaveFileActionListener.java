package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveFileActionListener implements ActionListener{

	private final CompilerUI invoker; 
	public SaveFileActionListener(CompilerUI compiler){
		invoker = compiler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		invoker.getFileHandler().saveFileAction();
	}

}
