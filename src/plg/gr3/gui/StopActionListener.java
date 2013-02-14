package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public StopActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        // TODO stop
    }
}
