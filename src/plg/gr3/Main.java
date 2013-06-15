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
import plg.gr3.vm.instr.CommentedInstruction;
import plg.gr3.vm.instr.Instruction;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.TAtributos;

/**
 * Aplicación principal en consola
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Main {

    private enum Mode {
        NORMAL,
        VERBOSE,
        TRACE;
    }

    /** Comando utilizado para compilar un fichero */
    private static final String CMD_COMPILE = "compile";

    /** Comando utilizado para ejecutar un programa */
    private static final String CMD_RUN = "run";

    /** Escribe por pantalla cómo se usa la aplicación */
    private static void printUsage () {
        final String CMD = "java " + Main.class.getName();
        final String SUFFIX = "[.v|.vv]";

        System.out.println("usage: " + CMD + " " + CMD_COMPILE + SUFFIX + " <input> <output>");
        System.out.println("   or: " + CMD + " " + CMD_RUN + SUFFIX + " <file>");
        System.out.println();
        System.out.println("Ambos comandos disponen de los sufijos '.v' y '.vv' que activan mensajes de depuraciób.");
        System.out
            .println("En el primer modo (.v), se mostrarán mensajes que darán detalles de lo que está ocurriendo.");
        System.out
            .println("En el segundo modo (.vv), se mostrarán muchos más detalles para la depuración del sistema.");
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
                String[] first = args[0].split("\\.");
                Mode mode = Mode.NORMAL;
                if (first.length == 0 || first.length > 2) {
                    printUsage();
                    return;

                } else if (first.length == 2) {
                    switch (first[1].trim()) {
                        case "v":
                            mode = Mode.VERBOSE;
                        break;

                        case "vv":
                            mode = Mode.TRACE;
                        break;

                        default:
                            printUsage();
                            return;
                    }
                }

                String command = first[0];
                switch (command) {
                    case CMD_COMPILE:
                        compile(mode, Arrays.copyOfRange(args, 1, args.length));
                    break;

                    case CMD_RUN:
                        run(mode, Arrays.copyOfRange(args, 1, args.length));
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
     * @param mode Modo de ejecución
     * @param args Argumentos de línea de comandos
     * @throws IOException Si hay unproblema de E/S
     * @throws Exception Si falla otra cosa
     */
    @SuppressWarnings("unchecked")
    public static void compile (Mode mode, String... args) throws Exception {
        if (args.length != 2) {
            printUsage();
        }

        boolean debug = mode == Mode.VERBOSE || mode == Mode.TRACE;
        boolean trace = mode == Mode.TRACE;

        Path pathInput = Paths.get(args[0]);
        Path pathOutput = Paths.get(args[1]);

        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(debug || trace);
        Atributo.fijaDebug(false);

        @SuppressWarnings("deprecation")
        SymbolFactory symbolFactory = new DefaultSymbolFactory();

        try (InputStream input = Files.newInputStream(pathInput, READ)) {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer, symbolFactory);

            Atributo.fijaDebug(trace);
            TAtributos result = (TAtributos) parser.parse().value;

            try (OutputStream output = Files.newOutputStream(pathOutput, WRITE, CREATE, TRUNCATE_EXISTING)) {
                StreamCodeWriter writer = new StreamCodeWriter(output);

                SymbolTable table = (SymbolTable) result.a("ts").valor();
                Debugger.INSTANCE.debug("Tabla de símbolos: %s", table.toFullString());

                List<CompileError> errors = (List<CompileError>) result.a("err").valor();
                for (CompileError error : errors) {
                    error.print();
                }

                if (errors.isEmpty()) {
                    List<Instruction> code = (List<Instruction>) result.a("cod").valor();
                    if (debug) {
                        int pos = 0;
                        for (Instruction instr : code) {
                            if (instr instanceof CommentedInstruction) {
                                System.out.printf(" --  %s%n", ((CommentedInstruction) instr).getComment());
                            }
                            System.out.printf("%3X  %s%n", pos++, instr);
                        }
                    }

                    writer.write(code);
                }
            }
        }
    }

    /**
     * Comando de ejecución.
     * 
     * @param mode Modo de ejecución
     * @param args Argumentos de línea de comandos
     */
    public static void run (Mode mode, String... args) {
        if (args.length != 1) {
            printUsage();
        }
        Path pathInput = Paths.get(args[0]);

        boolean debug = mode == Mode.VERBOSE || mode == Mode.TRACE;
        boolean trace = mode == Mode.TRACE;

        Console console = System.console();
        if (trace && console == null) {
            System.err.println("No hay ninguna terminal asociada.");
            System.err.println("Ejecuta el comando desde una terminal y sin redirecciones.");
            System.err.println();

            printUsage();
            return;
        }

        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(debug);

        Debugger.INSTANCE.debug("Cargando código...");

        try (StreamCodeReader reader = new StreamCodeReader(Files.newInputStream(pathInput))) {
            List<Instruction> code = reader.readAll();

            Debugger.INSTANCE.debug("Cargadas %d instrucciones pila.", code.size());
            VirtualMachine vm = new VirtualMachine(code);

            Debugger.INSTANCE.debug("Ejecutando...");
            while (!vm.isStopped()) {
                int pc = vm.getProgramCounter();
                Debugger.INSTANCE.debug("Instruction: %d => %s", pc, vm.getInstruction(pc));

                vm.step();

                Debugger.INSTANCE.debug("Stack: %s", vm.getStackAsString());
                Debugger.INSTANCE.debug("Memory:%n%s", vm.getMemoryAsString());
                Debugger.INSTANCE.debug("==========");

                if (trace) {
                    console.printf("Pulsa ENTER para continuar...");
                    console.readLine();
                }
            }

            RuntimeError error = vm.getError();
            if (error != null) {
                error.print();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Debugger.INSTANCE.debug("Terminado");
    }

    /** Deshabilita la posibilidad de construir objetos de esta clase */
    private Main () {
        throw new AssertionError();
    }

}
