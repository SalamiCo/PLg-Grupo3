package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;
import java.util.NoSuchElementException;

import plg.gr3.debug.Debugger;
import plg.gr3.lexer.Lexer;
import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.TokenType;

/**
 * Analizador sintáctico del lenguaje.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Parser implements Closeable {
    
    /** Analizador léxico a utilizar */
    private final Lexer lexer;
    
    /** Depurador */
    private final Debugger debugger;
    
    /**
     * @param lexer
     *            Analizador léxico que será utilizado por este analizador sintáctico
     */
    public Parser (Lexer lexer, Debugger debugger) {
        this.lexer = lexer;
        this.debugger = debugger;
    }
    
    /**
     * Intenta reconocer un token de alguna de las categorías pasadas como parámero
     * 
     * @param last
     *            Si al fallar debemos mostrar un mensaje de error o no
     * @param categories
     *            Categorías que queremos reconocer
     * @return El token que ha reconocido
     * @throws IOException
     *             si ocurre un error de E/S
     */
    private LocatedToken expect (boolean last, TokenType... categories) throws IOException {
        for (TokenType category : categories) {
            if (lexer.hasNextToken(category)) {
                return lexer.nextToken(category);
            }
        }
        
        // TODO Log an error
        if (last) {
            
        }
        
        throw new NoSuchElementException();
    }
    
    public boolean parse () throws IOException {
        // TODO Analizar el lenguaje completo.
        // Este método tan sólo deberá llamar a otro, parseProg, que es el que se encarga de analizar el lenguaje
        // La idea es que cada categoría sintáctica tenga un método propio. Ese método siempre tendrá esta forma:
        // private Attributes parseXXX ( Attributes attrs ) {...}
        // El objeto que recibe son los atributos heredados, el que devuelve son los sintetizados; es decir, no hace
        // falta duplicar los atributos en caso de que haya dos versiones, sintetizada y heredada (por ejemplo, en el
        // caso de ts y tsh): se distinguirán por el objeto usado (parámetro o devuelto)
        // NOTA: La clase Attributes se construye de una forma un tanto peculiar para evitar tener que pasarle 8
        // parámetros en aquellos casos en los que sólo interesan 2. Echadle un ojo.
        Attributes attr = parseProgram(Attributes.DEFAULT);
        
        return attr != null;
    }
    
    private Attributes parseProgram (Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Program ::=
        try {
            // program ident illave
            expect(true, TokenType.RW_PROGRAM);
            expect(true, TokenType.IDENTIFIER);
            expect(true, TokenType.SYM_CURLY_LEFT);
            
            // SDecs SInsts
            Attributes decsSynAttr = parseSDecs(true, Attributes.DEFAULT);
            
            Attributes instsInhAttr = new Attributes.Builder().symbolTable(decsSynAttr.getSymbolTable()).create();
            Attributes instsSynAttr = parseSInsts(true, instsInhAttr);
            
            // fllave fin
            expect(true, TokenType.SYM_CURLY_RIGHT);
            expect(true, TokenType.EOF);
        } catch (NoSuchElementException exc) {
            return null;
        }
        return attrb.create();
    }
    
    private Attributes parseSDecs (boolean last, Attributes attr) {
        return null;
    }
    
    private Attributes parseSInsts (boolean last, Attributes attr) {
        return null;
    }
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
    
}
