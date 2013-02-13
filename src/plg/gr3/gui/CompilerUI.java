package plg.gr3.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import plg.gr3.code.CodeWriter;
import plg.gr3.code.ListCodeWriter;
import plg.gr3.gui.LogHandler.LogType;
import plg.gr3.lexer.Lexer;
import plg.gr3.parser.Parser;

public final class CompilerUI extends JFrame {
    
    /**
     * Fuente por defecto para los paneles y consolas.
     * */
    private static final Font FONT = new Font("Monospaced", Font.PLAIN, 15);
    
    /**
     * Vistas de trabajo: modo compilador, modo debugger.
     * */
    private enum View {
        COMPILER,
        DEBUGGER
    };
    
    // Miembros privados de la clase
    private View view;
    
    private JPanel mainPanel;
    
    private JPanel compilerPanel;
    
    private JPanel debugPanel;
    
    private JScrollPane codeArea;
    
    private JTextPane sourceCodeEditor;
    
    private JTextPane byteCodeEditor;
    
    private FileHandler sourceFile;
    
    private FileHandler bytecodeFileHandler;
    
    private SymbolTableHandler symbolTableArea;
    
    // Contenido de las pestañas de log y errores de compilación
    // se crea un LogHandler static para que sea accedido desde cualquier objeto del programa
    private static LogHandler logArea = new LogHandler();
    
    private static ProblemHandler problemsArea = new ProblemHandler();
    
    // Contenido de las pestañas de consola y errores de ejecución
    private static ConsoleHandler consoleArea = new ConsoleHandler();
    
    private static ErrorHandler errorsArea = new ErrorHandler();
    
    // Objetos encargados de la compilación.
    private Lexer lexer;
    
    private Parser parser;
    
    private CodeWriter codeWriter;
    
    public static void log (LogType type, String msg) {
        logArea.log(type, msg);
    }
    
    /**
     * Crea la interfaz de usuario con la vista en modo Compilador cargada
     * */
    public CompilerUI () {
        view = View.COMPILER; // vista de compilador
        symbolTableArea = new SymbolTableHandler();
        initUI();
    }
    
    /**
     * Método para inicializar los elementos de la interfaz gráfica: ventana, menú principal y panel principal con los
     * paneles de las vistas
     */
    private void initUI () {
        
        // inicializa la ventana
        initFrame();
        
        // añadir el menú principal a la ventana
        setJMenuBar(initMenuBar());
        
        // crear panel principal con CardLayout
        mainPanel = new JPanel(new CardLayout());
        
        // añadir las vistas de compilador y debug al panel principal
        mainPanel.add(initCompilerPanel(), "panelCompiler");
        mainPanel.add(initDebugPanel(), "panelDebugger");
        
        this.add(mainPanel);
        
        CompilerUI.log(LogType.LOG, "Application initialized correctly");
    }
    
    /**
     * Inicializa la ventana principal de la aplicación
     * */
    private void initFrame () {
        this.setSize(957, 577);
        setTitle("Implacable Compiler");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // Cambia el comportamiento por defecto al cerrar el formulario
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                closeFilesAndExit();
            }
        });
    }
    
    /**
     * Inicializa el menú principal, que consta de los siguientes apartados: - Menú Archivo: opciones típicas de manejo
     * de ficheros - Menú Edición: opciones típicas de edición de ficheros - Menú Vistas: permite cambiar la vista de
     * trabajo - Menú Acciones: permite ejecuciones asociadas a la vista actual - Menú Ayuda: muestra documentos de
     * ayuda al usuario y créditos
     * */
    private JMenuBar initMenuBar () {
        // barra de menú
        JMenuBar menubar = new JMenuBar();
        menubar.add(initFileMenu());
        menubar.add(initEditMenu());
        menubar.add(initViewMenu());
        // TODO descomentar cuando estén implementados
        // menubar.add(initActionsMenu());
        // menubar.add(initHelpMenu());
        return menubar;
    }
    
    /**
     * Inicializa el menú Archivo con los siguientes Items: - Nuevo: crea un nuevo archivo - Abrir: abre un archivo del
     * sistema de ficheros - Guardar: guarda el contenido actual del área de texto - Guardar como: guarda el contenido
     * actual del área de texto en un archivo - Salir: sale de la aplicación
     * */
    private JMenu initFileMenu () {
        // iconos
        ImageIcon iconNew = new ImageIcon(getClass().getResource("new.png"));
        ImageIcon iconOpen = new ImageIcon(getClass().getResource("open.png"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("save.png"));
        ImageIcon iconSaveAs = new ImageIcon(getClass().getResource("saveas.png"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("exit.png"));
        
        // crear menú y crear items
        JMenu file = new JMenu("File");
        
        // Item Archivo->Nuevo
        JMenuItem fileNew = new JMenuItem("New", iconNew);
        fileNew.setToolTipText("Create new file");
        fileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileNew.addActionListener(new NewFileActionListener(this));
        
        // Item Archivo->Abrir
        JMenuItem fileOpen = new JMenuItem("Open", iconOpen);
        fileOpen.setToolTipText("Open an existing file");
        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileOpen.addActionListener(new OpenFileActionListener(this));
        
        // Item Archivo->Guardar
        JMenuItem fileSave = new JMenuItem("Save", iconSave);
        fileSave.setToolTipText("Save this file");
        fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileSave.addActionListener(new SaveFileActionListener(this));
        
        // Item Archivo->Guardar como...
        JMenuItem fileSaveAs = new JMenuItem("Save as...", iconSaveAs);
        fileSaveAs.setToolTipText("Save this text into a file");
        fileSaveAs.addActionListener(new SaveAsFileActionListener(this));
        
        // Item Archivo->Salir
        JMenuItem fileExit = new JMenuItem("Exit", iconExit);
        fileExit.setToolTipText("Exit application");
        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        fileExit.addActionListener(new ExitActionListener(this));
        
        // añadir items al menú
        file.add(fileNew);
        file.add(fileOpen);
        file.add(fileSave);
        file.add(fileSaveAs);
        file.addSeparator();
        file.add(fileExit);
        
        return file;
    }
    
    /**
     * Inicializa el menú Editar con los siguientes Items: - Deshacer y Rehacer - Cortar, Copiar, Pegar - Seleccionar
     * todo - Buscar
     * */
    private JMenu initEditMenu () {
        // iconos
        ImageIcon iconUndo = new ImageIcon(getClass().getResource("undo.png"));
        ImageIcon iconRedo = new ImageIcon(getClass().getResource("redo.png"));
        ImageIcon iconCut = new ImageIcon(getClass().getResource("cut.png"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("copy.png"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("paste.png"));
        ImageIcon iconFind = new ImageIcon(getClass().getResource("find.png"));
        
        // crear menú y crear items
        JMenu editMenu = new JMenu("Edit");
        
        // Item Editar->Deshacer
        JMenuItem editUndo = new JMenuItem("Undo", iconUndo);
        
        // Item Editar->Rehacer
        JMenuItem editRedo = new JMenuItem("Redo", iconRedo);
        
        // Item Editar->Cortar
        JMenuItem editCut = new JMenuItem("Cut", iconCut);
        editCut.addActionListener(new CutTextActionListener(this));
        editCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        
        // Item Editar->Copiar
        JMenuItem editCopy = new JMenuItem("Copy", iconCopy);
        editCopy.addActionListener(new CopyTextActionListener(this));
        editCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        
        // Item Editar->Pegar
        JMenuItem editPaste = new JMenuItem("Paste", iconPaste);
        editPaste.addActionListener(new PasteTextActionListener(this));
        editPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        
        // Item Editar->Seleccionar todo
        JMenuItem editSelect = new JMenuItem("Select all");
        editSelect.addActionListener(new SelectAllTextActionListener(this));
        editSelect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        
        // Item Editar->Buscar
        JMenuItem editFind = new JMenuItem("Find", iconFind);
        
        // añadir items al menú
        editMenu.add(editUndo);
        editMenu.add(editRedo);
        editMenu.addSeparator();
        editMenu.add(editCut);
        editMenu.add(editCopy);
        editMenu.add(editPaste);
        editMenu.addSeparator();
        editMenu.add(editSelect);
        editMenu.add(editFind);
        
        return editMenu;
    }
    
    /**
     * Inicializar el menú Vista con un CheckBox: si se encuentra desmarcado muestra la vista "compilador", si se
     * encuentra marcado muestra la vista "debug"
     * */
    private JMenu initViewMenu () {
        // crear menú
        JMenu viewMenu = new JMenu("View");
        
        // añadir item al menú
        JCheckBoxMenuItem sbar = new JCheckBoxMenuItem("Debugger view");
        sbar.setState(false); // empieza valiendo false para ser coherente con la vista inicial
        viewMenu.add(sbar);
        
        sbar.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent event) {
                switchViewPanels(); // cambia de panel
            }
        });
        
        return viewMenu;
    }
    
//	private JMenu initActionsMenu() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private JMenu initHelpMenu() {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
    /**
     * @return ToolBar inicializado con los botones de la vista "compilador"
     * */
    private JToolBar initCompilerToolBar () {
        // iconos
        ImageIcon iconCompile = new ImageIcon(getClass().getResource("compile.png"));
        ImageIcon iconLoad = new ImageIcon(getClass().getResource("load.png"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("exit.png"));
        ImageIcon iconNew = new ImageIcon(getClass().getResource("new.png"));
        ImageIcon iconOpen = new ImageIcon(getClass().getResource("open.png"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("save.png"));
        ImageIcon iconCut = new ImageIcon(getClass().getResource("cut.png"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("copy.png"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("paste.png"));
        
        // crear toolbar y botones
        JToolBar toolbar = new JToolBar();
        
        // botón nuevo
        JButton newFileButton = new JButton(iconNew);
        newFileButton.addActionListener(new NewFileActionListener(this));
        
        // botón open
        JButton openFileButton = new JButton(iconOpen);
        openFileButton.addActionListener(new OpenFileActionListener(this));
        
        // botón save
        JButton saveFileButton = new JButton(iconSave);
        saveFileButton.addActionListener(new SaveFileActionListener(this));
        
        // botón cut
        JButton cutButton = new JButton(iconCut);
        cutButton.addActionListener(new CutTextActionListener(this));
        
        // botón copy
        JButton copyButton = new JButton(iconCopy);
        copyButton.addActionListener(new CopyTextActionListener(this));
        
        // botón paste
        JButton pasteButton = new JButton(iconPaste);
        pasteButton.addActionListener(new PasteTextActionListener(this));
        
        // botón compilar
        JButton compileButton = new JButton(iconCompile);
        compileButton.addActionListener(new CompileActionListener(this));
        
        // botón compilar y cargar en la vm
        JButton loadButton = new JButton(iconLoad);
        loadButton.addActionListener(new LoadActionListener(this));
        
        // botón exit
        JButton exitButton = new JButton(iconExit);
        exitButton.addActionListener(new ExitActionListener(this));
        
        // añadir botones al toolbar
        toolbar.add(newFileButton);
        toolbar.add(openFileButton);
        toolbar.add(saveFileButton);
        toolbar.addSeparator();
        toolbar.add(cutButton);
        toolbar.add(copyButton);
        toolbar.add(pasteButton);
        toolbar.addSeparator();
        toolbar.add(compileButton);
        toolbar.add(loadButton);
        toolbar.add(exitButton);
        
        return toolbar;
    }
    
    /**
     * @return ToolBar inicializado con los botones de la vista "debug"
     * */
    private JToolBar initDebugToolBar () {
        // iconos
        ImageIcon iconExit = new ImageIcon(getClass().getResource("exit.png"));
        
        // crear toolbar y botones
        JToolBar toolbar = new JToolBar();
        JButton exitButton = new JButton(iconExit);
        exitButton.addActionListener(new ExitActionListener(this));
        
        // añadir botones al toolbar
        toolbar.add(exitButton);
        
        return toolbar;
    }
    
    /**
     * @return Inicializa un área de edición de código fuente (vista compilador)
     * */
    private JScrollPane initSourceCodeArea () {
        sourceCodeEditor = new JTextPane();
        sourceCodeEditor.setFont(FONT);
        
        // nuevo archivo fuente
        sourceFile = new FileHandler(sourceCodeEditor);
        
        // lo añadimos a un scrollPane con contador de líneas
        JScrollPane scrollPane = new JScrollPane(sourceCodeEditor);
        TextLineNumber tln = new TextLineNumber(sourceCodeEditor);
        scrollPane.setRowHeaderView(tln);
        return scrollPane;
    }
    
    /**
     * @return Inicializa un área de edición de código máquina (vista debug)
     * */
    private JScrollPane initByteCodeArea () {
        byteCodeEditor = new JTextPane();
        byteCodeEditor.setFont(FONT);
        
        // nuevo archivo de bytecode
        bytecodeFileHandler = new FileHandler(byteCodeEditor);
        
        // lo añadimos a un scrollPane con contador de líneas
        JScrollPane scrollPane = new JScrollPane(byteCodeEditor);
        TextLineNumber tln = new TextLineNumber(byteCodeEditor);
        scrollPane.setRowHeaderView(tln);
        return scrollPane;
    }
    
    /**
     * @return Inicializa las pestañas de Log y Errores de compilación (vista compilador)
     * */
    private JTabbedPane initLogTabs () {
        // iconos
        ImageIcon iconLog = new ImageIcon(getClass().getResource("log.png"));
        ImageIcon iconError = new ImageIcon(getClass().getResource("error.png"));
        
        // crear nuevo panel de pestañas
        JTabbedPane logTabbedPane = new JTabbedPane();
        logTabbedPane.setPreferredSize(new Dimension(636, 138));
        
        // crear paneles para Log y Errores y añadir dichas pestañas
        JComponent panel1 = new JPanel(new BorderLayout());
        panel1.add(logArea.getLogPane(), BorderLayout.CENTER);
        logTabbedPane.addTab("Events", iconLog, panel1, "Event monitor");
        
        JComponent panel2 = new JPanel(new BorderLayout());
        panel2.add(problemsArea.getProblemPane(), BorderLayout.CENTER);
        logTabbedPane.addTab("Problems", iconError, panel2, "Shows runtime errors");
        
        return logTabbedPane;
    }
    
    /**
     * @return Inicializar las pestañas de Consola y Errores en tiempo de ejecución (vista debug)
     * */
    private JTabbedPane initConsoleTabs () {
        // Iconos
        ImageIcon iconConsole = new ImageIcon(getClass().getResource("console.png"));
        ImageIcon iconError = new ImageIcon(getClass().getResource("error.png"));
        
        // crear nuevo panel de pestañas
        JTabbedPane consoleTabbedPane = new JTabbedPane();
        consoleTabbedPane.setPreferredSize(new Dimension(636, 138));
        
        // Crear paneles para Consola y Errores y añadir dichas pestañas
        JComponent panel1 = new JPanel(new BorderLayout());
        panel1.add(consoleArea.getConsoleLogPane(), BorderLayout.CENTER);
        consoleTabbedPane.addTab("Console", iconConsole, panel1, "I/O Console");
        
        JComponent panel2 = new JPanel(new BorderLayout());
        panel2.add(errorsArea.getErrorPane(), BorderLayout.CENTER);
        consoleTabbedPane.addTab("Errors", iconError, panel2, "Shows runtime errors");
        
        return consoleTabbedPane;
    }
    
    /**
     * @return Inicializa las pestañas para la Tabla de Símbolos y lista de Tokens (vista compilador)
     * */
    private JTabbedPane initSymbolTabs () {
        // Iconos
        ImageIcon iconTable = new ImageIcon(getClass().getResource("table.png"));
        ImageIcon iconToken = new ImageIcon(getClass().getResource("token.png"));
        
        // crear nuevo panel de pestañas
        JTabbedPane symTabbedPane = new JTabbedPane();
        symTabbedPane.setPreferredSize(new Dimension(380, 464));
        
        // crear paneles para Tabla de Símbolos y Lista de Tokens, añadir dichas pestañas
        JComponent panel1 = new JPanel(new BorderLayout());
        JScrollPane scrollPane =
            new JScrollPane(
                symbolTableArea.getSymbolTable(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        panel1.add(scrollPane, BorderLayout.CENTER);
        symTabbedPane.addTab("Symbol table", iconTable, panel1, "Shows symbol table content");
        
        JComponent panel2 = new JPanel();
        symTabbedPane.addTab("Tokens", iconToken, panel2, "Shows token list");
        
        return symTabbedPane;
    }
    
    /**
     * @return Inicializa las pestañas de Símbolos de Debug y de Máquina Virtual (vista debug)
     * */
    private JTabbedPane initDebugTabs () {
        // Iconos
        ImageIcon iconDebug = new ImageIcon(getClass().getResource("bug.png"));
        ImageIcon iconMachine = new ImageIcon(getClass().getResource("machine.png"));
        
        // Crear nuevo panel de pestañas
        JTabbedPane dbgTabbedPane = new JTabbedPane();
        dbgTabbedPane.setPreferredSize(new Dimension(483, 464));
        
        // Paneles para Símbolos de Debug y Máquina Virtual, añadir dichas pestañas
        JComponent panel1 = new JPanel();
        dbgTabbedPane.addTab("Debug", iconDebug, panel1, "Shows debug symbols");
        
        JComponent panel2 = new JPanel();
        dbgTabbedPane.addTab("Virtual Machine", iconMachine, panel2, "Shows virtual machine status");
        
        return dbgTabbedPane;
    }
    
    /**
     * @return Panel de la vista "compilador" inicializado
     * */
    private Component initCompilerPanel () {
        // crea un panel con BorderLayout
        compilerPanel = new JPanel(new BorderLayout());
        
        // inicializa la barra de herramientas
        JToolBar compilerToolbar = initCompilerToolBar();
        
        // inicializa el área para escribir código
        codeArea = initSourceCodeArea();
        
        // inicializa las pestañas de log y errores
        JTabbedPane logTabs = initLogTabs();
        
        // inicializa las pestañas de la tabla de símbolos y lista de tokens
        JTabbedPane symTabs = initSymbolTabs();
        
        // inicializa el panel izquierdo
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // añadir al panel izquierdo un área de texto y pestañas para log
        leftPanel.add(codeArea, BorderLayout.CENTER);
        leftPanel.add(logTabs, BorderLayout.SOUTH);
        
        // añadir componentes a la vista de compilador
        compilerPanel.add(compilerToolbar, BorderLayout.NORTH);
        compilerPanel.add(leftPanel, BorderLayout.CENTER);
        compilerPanel.add(symTabs, BorderLayout.EAST);
        
        return compilerPanel;
    }
    
    /**
     * @return Panel de la vista "debug" inicializado
     * */
    private Component initDebugPanel () {
        // crea un panel con BorderLayout
        debugPanel = new JPanel(new BorderLayout());
        
        // inicializa la barra de herramientas
        JToolBar debugToolbar = initDebugToolBar();
        
        // inicializa el área para escribir código
        codeArea = initByteCodeArea();
        
        // inicializa las pestañas de consola y errores
        JTabbedPane consoleTabs = initConsoleTabs();
        
        // inicializa las pestañas de la tabla de símbolos y lista de tokens
        JTabbedPane debugTabs = initDebugTabs();
        
        // inicializa el panel izquierdo
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // añadir al panel izquierdo un área de texto y pestañas para log
        leftPanel.add(codeArea, BorderLayout.CENTER);
        leftPanel.add(consoleTabs, BorderLayout.SOUTH);
        
        // añadir componentes a la vista de compilador
        debugPanel.add(debugToolbar, BorderLayout.NORTH);
        debugPanel.add(leftPanel, BorderLayout.CENTER);
        debugPanel.add(debugTabs, BorderLayout.EAST);
        
        return debugPanel;
    }
    
    /**
     * @return Manejador del archivo correspondiente a la vista actual
     * */
    public FileHandler getFileHandler () {
        FileHandler file;
        switch (view) {
            case COMPILER:
                file = sourceFile;
            break;
            case DEBUGGER:
                file = bytecodeFileHandler;
            break;
            default:
                file = sourceFile;
            break;
        }
        return file;
    }
    
    public Lexer getLexer () {
        return lexer;
    }
    
    public void setLexer (Lexer lexer) {
        this.lexer = lexer;
    }
    
    public Parser getParser () {
        return parser;
    }
    
    public void setParser (Parser parser) {
        this.parser = parser;
    }
    
    public CodeWriter getCodeWriter () {
        return codeWriter;
    }
    
    public void setCodeWriter (ListCodeWriter codeWriter) {
        this.codeWriter = codeWriter;
    }
    
    public SymbolTableHandler getSymbolTableArea () {
        return symbolTableArea;
    }
    
    public FileHandler getBytecodeFileHandler () {
        return bytecodeFileHandler;
    }
    
    public void setBytecodeFileHandler (FileHandler bytecodeFileHandler) {
        this.bytecodeFileHandler = bytecodeFileHandler;
    }
    
    /**
     * Cambia de vista y produce un cambio de paneles
     * */
    private void switchViewPanels () {
        String panelName;
        switch (view) {
            case COMPILER: // cambiar a DEBUGGER
                view = View.DEBUGGER;
                panelName = "panelDebugger";
            break;
            case DEBUGGER: // cambiar a COMPILER
                view = View.COMPILER;
                panelName = "panelCompiler";
            
            break;
            default: // cambiar a DEBUGGER
                view = View.DEBUGGER;
                panelName = "panelDebugger";
            break;
        }
        // Cambiar de panel en función de la vista elegida
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, panelName);
    }
    
    /**
     * Pregunta si puede cerrar los manejadores de fichero, en caso afirmativo procede al cierre de la aplicación
     * */
    public void closeFilesAndExit () {
        boolean canClose1 = sourceFile.askBeforeClose();
        boolean canClose2 = bytecodeFileHandler.askBeforeClose();
        if (canClose1 && canClose2) {
            System.exit(0);
        }
    }
    
    /**
     * Ejecuta la aplicación principal y hace visible la ventana
     * */
    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                CompilerUI compiler = new CompilerUI();
                compiler.setVisible(true);
            }
        });
    }
    
}
