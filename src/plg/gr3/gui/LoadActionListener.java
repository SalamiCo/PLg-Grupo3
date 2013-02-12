package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plg.gr3.gui.LogHandler.LogType;

public class LoadActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public LoadActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        invoker.log(LogType.LOG, "Compiling and running...");
    }
    
}
