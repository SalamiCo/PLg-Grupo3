package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitActionListener implements ActionListener{

	private final CompilerUI invoker; 
	public ExitActionListener(CompilerUI compiler){
		invoker = compiler;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		invoker.closeFilesAndExit();	
	}

}
