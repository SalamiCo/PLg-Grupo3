package plg.gr3.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.List;

import plg.gr3.code.StreamCodeWriter;
import plg.gr3.gui.LogHandler.LogType;
import plg.gr3.lexer.Lexer;
import plg.gr3.lexer.LocatedToken;
import plg.gr3.parser.Parser;
import plg.gr3.parser.SymbolTable;

public class CompileActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public CompileActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    @Override
    public void actionPerformed (ActionEvent e) {
        try {
            if (!invoker.getFileHandler().isSavedBeforeCompile()) {
                CompilerUI.log(LogType.ERROR, "The file must be saved before compile.");
                return;
            }
            // Guardado y con path asignado procedemos a compilar
            CompilerUI.log(LogType.LOG, "Compiling...");
            File sourceCodeFile = new File(invoker.getFileHandler().getFilePath());
            String parentDir = sourceCodeFile.getParent();
            String byteCodeName = sourceCodeFile.getName().substring(0, sourceCodeFile.getName().lastIndexOf('.'));
            String byteCodePath = parentDir + "/" + byteCodeName + ".bytecode";
            File byteCodeFile = new File(byteCodePath);
            byteCodeFile.createNewFile();
            Reader localReader;
            OutputStream localWriter;
            
            localReader = new InputStreamReader(new FileInputStream(sourceCodeFile), Charset.forName("UTF-8"));
            localWriter = new FileOutputStream(byteCodeFile);
            invoker.setLexer(new Lexer(localReader));
            invoker.setCodeWriter(new StreamCodeWriter(localWriter));
            invoker.setParser(new Parser(invoker.getLexer(), invoker.getCodeWriter()));
            if (invoker.getParser().parse()) {
                CompilerUI.log(LogType.LOG, "Succesfully compiled.");
                
                // mostramos la tabla de símbolos
                SymbolTable st = invoker.getParser().getSymbolTable();
                invoker.getSymbolTableArea().replaceModel(st);
                
                // mostramos la lista de tokens
                List<LocatedToken> tokenList = invoker.getParser().getTokenList();
                invoker.getTokenHandler().populateList(tokenList);
                
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
