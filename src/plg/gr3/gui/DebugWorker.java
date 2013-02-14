package plg.gr3.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.SwingWorker;

public final class DebugWorker extends SwingWorker<Void, String> {
    
    private final Writer writer;
    
    private final Reader reader;
    
    private final JTextPane pane;
    
    public DebugWorker (Writer writer, Reader reader, JTextPane pane) {
        this.writer = writer;
        this.reader = new BufferedReader(reader);
        this.pane = pane;
    }
    
    @Override
    protected Void doInBackground () throws Exception {
        char[] buf = new char[128];
        while (!isCancelled()) {
            int n = reader.read(buf, 0, buf.length);
            pane.setText(pane.getText() + new String(buf, 0, n));
        }
        
        return null;
    }
    
    @Override
    protected void process (List<String> chunks) {
        for (String line : chunks) {
            pane.setText(pane.getText() + "\n" + line);
        }
    }
    
    public void cancel () {
        cancel(true);
        
        try {
            writer.close();
            reader.close();
        } catch (IOException exc) {
            // Nothing
        }
    }
    
}
