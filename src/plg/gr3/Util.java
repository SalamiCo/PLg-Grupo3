package plg.gr3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map.Entry;

import plg.gr3.code.CodeReader;
import plg.gr3.code.CodeWriter;
import plg.gr3.code.StreamCodeReader;
import plg.gr3.code.StreamCodeWriter;
import plg.gr3.debug.Debugger;
import plg.gr3.lexer.Lexer;
import plg.gr3.parser.Parser;
import plg.gr3.parser.SymbolTable.Row;
import plg.gr3.vm.VirtualMachine;

public final class Util {
    
    public static void main (String[] args) throws Exception {
        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);
        
        File file = new File("/home/darkhogg/Universidad/PLg/EjemploMem.li");
        Reader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
        
        Path path = Paths.get("/tmp/bytecode");
        
        Lexer lexer = new Lexer(reader);
        CodeWriter codeWriter = new StreamCodeWriter(Files.newOutputStream(path, StandardOpenOption.CREATE));
        
        try (Parser parser = new Parser(lexer, codeWriter)) {
            parser.parse();
            if (!codeWriter.isInhibited()) {
                for (Entry<String, Row> entry : parser.getSymbolTable()) {
                    System.out.println(entry.getKey() + " ==> " + entry.getValue());
                }
                
                CodeReader codeReader = new StreamCodeReader(Files.newInputStream(path, StandardOpenOption.READ));
                
                VirtualMachine vm = new VirtualMachine(codeReader.readAll());
                vm.execute();
            }
        }
    }
}
