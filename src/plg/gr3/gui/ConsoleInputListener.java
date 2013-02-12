package plg.gr3.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsoleInputListener implements KeyListener {
    
    @Override
    public void keyTyped (KeyEvent e) {
    }
    
    @Override
    public void keyPressed (KeyEvent e) {
    }
    
    @Override
    public void keyReleased (KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // TODO raul 2012-02-12
        }
    }
}
