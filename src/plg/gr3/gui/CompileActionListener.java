package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import plg.gr3.code.ListCodeWriter;
import plg.gr3.data.Type;
import plg.gr3.gui.LogHandler.LogType;
import plg.gr3.lexer.Lexer;
import plg.gr3.parser.Parser;
import plg.gr3.parser.SymbolTable;

public class CompileActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public CompileActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        CompilerUI.log(LogType.LOG, "Compiling...");
        File file = new File(invoker.getFileHandler().getFilePath());
        Reader localReader;
        try {
            localReader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
            invoker.setLexer(new Lexer(localReader));
            invoker.setCodeWriter(new ListCodeWriter());
            invoker.setParser(new Parser(invoker.getLexer(), invoker.getCodeWriter()));
            if (invoker.getParser().parse()) {
                CompilerUI.log(LogType.LOG, "Succesfully compiled.");
//                invoker.getSymbolTableArea().replaceModel(invoker.getParser().getSymbolTable());
                SymbolTable st = new SymbolTable();
                st.putIdentifier("ident", Type.BOOLEAN, false, 1234, null);
                invoker.getSymbolTableArea().replaceModel(st);
            } else {
                CompilerUI.log(LogType.ERROR, "Compilation error/s");
            }
            
        } catch (FileNotFoundException e1) {
            CompilerUI.log(LogType.ERROR, "File not found. " + e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e2) {
            CompilerUI.log(LogType.ERROR, "IO Exception. " + e2.getMessage());
            e2.printStackTrace();
        }
    }
}
