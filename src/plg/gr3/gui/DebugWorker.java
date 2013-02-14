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
    
    private final BufferedReader reader;
    
    private final JTextPane pane;
    
    public DebugWorker (Writer writer, Reader reader, JTextPane pane) {
        this.writer = writer;
        this.reader = new BufferedReader(reader);
        this.pane = pane;
    }
    
    @Override
    protected Void doInBackground () throws Exception {
        String line;
        while (!isCancelled() && (line = reader.readLine()) != null) {
            publish(line);
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
