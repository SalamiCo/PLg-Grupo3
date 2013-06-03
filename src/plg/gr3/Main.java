package plg.gr3;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import java_cup.runtime.DefaultSymbolFactory;
import java_cup.runtime.SymbolFactory;
import plg.gr3.code.StreamCodeReader;
import plg.gr3.code.StreamCodeWriter;
import plg.gr3.debug.Debugger;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.errors.runtime.RuntimeError;
import plg.gr3.parser.Lexer;
import plg.gr3.parser.Parser;
import plg.gr3.parser.SymbolTable;
import plg.gr3.vm.VirtualMachine;
import plg.gr3.vm.instr.Instruction;
import es.ucm.fdi.plg.evlib.TAtributos;

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

    /** Comando utilizado para ejecutar un programa */
    private static final String CMD_DEBUG = "debug";

    /** Escribe por pantalla cómo se usa la aplicación */
    private static void printUsage () {
        final String CMD = "java " + Main.class.getName();
        System.out.println("usage: " + CMD + " " + CMD_COMPILE + " <input> <output>");
        System.out.println("   or: " + CMD + " " + CMD_RUN + " <file>");
        System.out.println("   or: " + CMD + " " + CMD_DEBUG + " <file>");
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

                    case "debug":
                        debug(Arrays.copyOfRange(args, 1, args.length));
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
     * Comando de compilación.
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

//            Atributo.fijaDebug(false);
            TAtributos result = (TAtributos) parser.parse().value;

            try (OutputStream output = Files.newOutputStream(pathOutput, WRITE, CREATE, TRUNCATE_EXISTING)) {
                StreamCodeWriter writer = new StreamCodeWriter(output);

                SymbolTable table = (SymbolTable) result.a("ts").valor();
                Debugger.INSTANCE.debug("Tabla de símbolos: %s", table);

                List<CompileError> errors = (List<CompileError>) result.a("err").valor();
                Debugger.INSTANCE.debug("Errores: %s", errors);

                List<Instruction> code = (List<Instruction>) result.a("cod").valor();
                Debugger.INSTANCE.debug("Código: %s", code);

                if (errors.isEmpty()) {
                    writer.write(code);
                }
            }
        }
    }

    /**
     * Comando de ejecución.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void run (String[] args) {
        if (args.length != 1) {
            printUsage();
        }
        Path pathInput = Paths.get(args[0]);

        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(false);

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

    /**
     * Comando de depuración.
     * 
     * @param args Argumentos de línea de comandos
     */
    public static void debug (String[] args) {
        if (args.length != 1) {
            printUsage();
        }
        Path pathInput = Paths.get(args[0]);

        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);

        Console console = System.console();
        if (console == null) {
            Debugger.INSTANCE.error("¡No puedes ejecutar en modo traza sin una terminal!");
        } else {

            try (StreamCodeReader reader = new StreamCodeReader(Files.newInputStream(pathInput))) {
                VirtualMachine vm = new VirtualMachine(reader.readAll());

                while (!vm.isStopped()) {
                    int pc = vm.getProgramCounter();
                    Instruction instr = vm.getInstruction(pc);

                    Debugger.INSTANCE.in(pc, instr).log("Pulsa ENTER para ejecutar");
                    String line = console.readLine();
                    if (line == null) {
                        vm.stop();
                    } else {
                        vm.step();
                    }
                }

                RuntimeError error = vm.getError();
                if (error != null) {
                    error.print();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Deshabilita la posibilidad de construir objetos de esta clase */
    private Main () {
        throw new AssertionError();
    }

}
