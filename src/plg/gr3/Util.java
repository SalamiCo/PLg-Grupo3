package plg.gr3;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import plg.gr3.code.ListCodeWriter;
import plg.gr3.debug.Debugger;
import plg.gr3.lexer.Lexer;
import plg.gr3.parser.Parser;
import plg.gr3.vm.VirtualMachine;

public final class Util {
    
    public static void main (String[] args) throws Exception {
        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);
        
        File file = new File("/home/darkhogg/Universidad/PLg/EjemploMem.li");
        Reader reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
        
        Lexer lexer = new Lexer(reader);
        ListCodeWriter codeWriter = new ListCodeWriter();
        
        Parser parser = new Parser(lexer, codeWriter);
        parser.parse();
        
        System.out.println(codeWriter.getList());
        
        VirtualMachine vm = new VirtualMachine(codeWriter.getList());
        vm.execute();
    }
}
