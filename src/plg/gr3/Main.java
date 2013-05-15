package plg.gr3;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import plg.gr3.code.StreamCodeReader;
import plg.gr3.debug.Debugger;
import plg.gr3.errors.runtime.RuntimeError;
import plg.gr3.parser.Lexer;
import plg.gr3.parser.Parser;
import plg.gr3.vm.VirtualMachine;

/**
 * Aplicación principal en consola
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Main {
    
    /** Comando utilizado para compilar un fichero */
    private static final String CMD_COMPILE = "compile";
    
    /** Comando utilizado para ejecutar un programa */
    private static final String CMD_RUN = "run";
    
    /** Escribe por pantalla cómo se usa la aplicación */
    private static void printUsage () {
        final String CMD = "java " + Main.class.getName();
        System.out.println("usage: " + CMD + " " + CMD_COMPILE + " <input> <output>");
        System.out.println("   or: " + CMD + " " + CMD_RUN + " <file>");
        System.out.println();
    }
    
    /**
     * Punto de entrada de la aplicación
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void main (String[] args) {
        if (args.length < 1) {
            printUsage();
        } else {
            
            try {
                String command = args[0];
                switch (command) {
                    case "compile":
                        compile(Arrays.copyOfRange(args, 1, args.length));
                    break;
                    
                    case "run":
                        run(Arrays.copyOfRange(args, 1, args.length));
                    break;
                    
                    default:
                        printUsage();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
    
    /**
     * Comando de compilación
     * 
     * @param args Argumentos de línea de comandos
     * @throws IOException Si hay unproblema de E/S
     * @throws Exception Si falla otra cosa
     */
    public static void compile (String[] args) throws Exception {
        if (args.length != 2) {
            printUsage();
        }
        Path pathInput = Paths.get(args[0]);
        Path pathOutput = Paths.get(args[1]);
        
        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);
        
        @SuppressWarnings("deprecation")
        SymbolFactory symbolFactory = new DefaultSymbolFactory();
        
        try (InputStream input = Files.newInputStream(pathInput, READ)) {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer, symbolFactory);
            Symbol symbol = parser.parse();
            
            try (OutputStream output = Files.newOutputStream(pathOutput, WRITE, CREATE, TRUNCATE_EXISTING)) {
                // TODO Output results
            }
        }
    }
    
    /**
     * Comando de ejecución
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void run (String[] args) {
        if (args.length != 1) {
            printUsage();
        }
        Path pathInput = Paths.get(args[0]);
        
        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);
        
        try (StreamCodeReader reader = new StreamCodeReader(Files.newInputStream(pathInput))) {
            VirtualMachine vm = new VirtualMachine(reader.readAll());
            vm.execute();
            
            RuntimeError error = vm.getError();
            if (error != null) {
                error.print();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /** Deshabilita la posibilidad de construir objetos de esta clase */
    private Main () {
        throw new AssertionError();
    }
    
}
