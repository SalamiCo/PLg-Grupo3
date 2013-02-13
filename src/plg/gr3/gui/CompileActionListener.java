package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import plg.gr3.code.CodeWriter;
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
        File sourceCodeFile = new File(invoker.getFileHandler().getFilePath());
        File byteCodeFile = new File("");
        Reader localReader;
        Writer localWriter;
        try {
            localReader = new InputStreamReader(new FileInputStream(sourceCodeFile), Charset.forName("UTF-8"));
            invoker.setLexer(new Lexer(localReader));
            invoker.setCodeWriter(new CodeWriter());
            invoker.setParser(new Parser(invoker.getLexer(), invoker.getCodeWriter()));
            if (invoker.getParser().parse()) {
                CompilerUI.log(LogType.LOG, "Succesfully compiled.");
                
                // mostramos la tabla de símbolos
                SymbolTable st = invoker.getParser().getSymbolTable();
                invoker.getSymbolTableArea().replaceModel(st);
                
                // ponemos el codigo generado en el editor de bytecode
                localWriter = new OutputStreamWriter(new FileOutputStream(byteCodeFile), Charset.forName("UTF-8"));
                
                // invoker.getBytecodeFileHandler().getTextEditor().setText(invoker.getCodeWriter().getList().toString());
                
            } else {
                CompilerUI.log(LogType.ERROR, "Compilation error/s");
                invoker.getSymbolTableArea().replaceModel(new SymbolTable());
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
