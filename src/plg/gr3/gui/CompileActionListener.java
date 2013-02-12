package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import plg.gr3.gui.LogHandler.LogType;

public class CompileActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public CompileActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        // TODO acción de compilar
        invoker.log(LogType.LOG, "Compiling...");
        invoker.log(LogType.LOG, "Compiled...");
    }
    
}
