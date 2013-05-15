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
import java.util.List;

import plg.gr3.code.ListCodeWriter;
import plg.gr3.gui.LogHandler.LogType;
import plg.gr3.lexer.OLD.Lexer;
import plg.gr3.lexer.OLD.LocatedToken;
import plg.gr3.parser.OLD.Parser_OLD;
import plg.gr3.parser.OLD.SymbolTable;
import plg.gr3.vm.instr.Instruction;

public class LoadActionListener implements ActionListener {
    
    private final CompilerUI invoker;
    
    public LoadActionListener (CompilerUI compiler) {
        invoker = compiler;
    }
    
    private String printInstructionList (List<Instruction> list) {
        String str = "";
        for (Instruction inst : list) {
            str += inst.toString() + "\n";
        }
        return str;
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
            Reader localReader;
            
            localReader = new InputStreamReader(new FileInputStream(sourceCodeFile), Charset.forName("UTF-8"));
            ListCodeWriter codeWriter = new ListCodeWriter();
            
            invoker.setLexer(new Lexer(localReader));
            invoker.setParser(new Parser_OLD(invoker.getLexer(), codeWriter));
            if (invoker.getParser().parse()) {
                CompilerUI.log(LogType.LOG, "Succesfully compiled.");
                
                // Pasar la lista de instrucciones al programa
                List<Instruction> instructions = codeWriter.getList();
                invoker.setProgram(instructions);
                invoker.getBytecodeFileHandler().getTextEditor().setText(printInstructionList(instructions));
                // limpiamos los errores
                invoker.getProblemsHandler().getProblemPane().setText("");
                
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
