package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public StepActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        // TODO step
    }
}
