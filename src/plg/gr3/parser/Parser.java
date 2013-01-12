package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
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
    
    /** Tabla de símbolos */
    private final SymbolTable symbolTable;
    
    /**
     * @param lexer
     *            Analizador léxico que será utilizado por este analizador sintáctico
     */
    public Parser (Lexer lexer) {
        this.lexer = lexer;
        this.symbolTable = new SymbolTable();
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
        LocatedToken token = lexer.nextToken();
        
        if (token != null) {
            for (TokenType category : categories) {
                if (category.equals(token.getToken().getType())) {
                    Debugger.INSTANCE.at(token.getLine(), token.getColumn()).debug("Reconocido " + token.getToken());
                    return token;
                }
            }
        }
        
        // TODO Log an error
        if (last) {
            Debugger.INSTANCE.at(lexer.getLine(), lexer.getColumn()).error(
                "Esperado uno de " + Arrays.toString(categories) + ", se encontró "
                    + (token == null ? "nada" : token.getToken().getType() + " en su lugar"));
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
            parseSDecs(true, Attributes.DEFAULT);
            parseSInsts(true, Attributes.DEFAULT);
            
            // fllave fin
            expect(true, TokenType.SYM_CURLY_RIGHT);
            expect(true, TokenType.EOF);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseSDecs (boolean last, Attributes attr) {
        return Attributes.DEFAULT;
    }
    
    private Attributes parseSInsts (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //SInsts ::=
        try {
            //instructions illave
            expect(true, TokenType.RW_INSTRUCTIONS);
            expect(true, TokenType.SYM_CURLY_LEFT);
            
            //Insts
            parseInsts(true, Attributes.DEFAULT);
            
            //fllave
            expect(true, TokenType.SYM_CURLY_RIGHT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
        
    }
    
    private Attributes parseInsts (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //Insts ::=
        try {
            //Inst 
            parseInst(true, Attributes.DEFAULT);
            
            //RInst 
            parseRInst(true, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseRInst (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //RInsts ::=
        try {
            //pyc
            expect(true, TokenType.SYM_SEMICOLON);
            
            //Inst
            parseInst(true, Attributes.DEFAULT);
            
            //RInsts
            parseRInst(true, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseInst (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //Insts ::=
        try {
            LocatedToken readToken =
                expect(
                    true, TokenType.IDENTIFIER, TokenType.RW_IN, TokenType.RW_OUT, TokenType.RW_SWAP1,
                    TokenType.RW_SWAP2);
            
            switch (readToken.getToken().getType())
            
            { //ident asig Expr
                case IDENTIFIER:
                    expect(true, TokenType.SYM_ASIGNATION);
                    parseExpr(true, Attributes.DEFAULT);
                    
                    break;
                
                //in lpar ident rpar
                case RW_IN:
                    expect(true, TokenType.SYM_PAR_LEFT);
                    expect(true, TokenType.IDENTIFIER);
                    expect(true, TokenType.SYM_PAR_RIGHT);
                    
                    break;
                
                //out lpar Expr rpar
                case RW_OUT:
                    expect(true, TokenType.SYM_PAR_LEFT);
                    parseExpr(true, Attributes.DEFAULT);
                    expect(true, TokenType.SYM_PAR_RIGHT);
                    
                    break;
                
                //swap1 lpar rpar
                case RW_SWAP1:
                    expect(true, TokenType.SYM_PAR_LEFT);
                    expect(true, TokenType.SYM_PAR_RIGHT);
                    
                    break;
                
                //swap2 lpar rpar
                case RW_SWAP2:
                    expect(true, TokenType.SYM_PAR_LEFT);
                    expect(true, TokenType.SYM_PAR_RIGHT);
                    
                    break;
            
            }
            ;
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    /*
     * private Attributes parseProgram (Attributes attrs) throws IOException {
     * 
     * boolean b = lexer.hasNextToken(RW_PROGRAM);
     * 
     * return attrs; }
     */
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
    
    public static void main (String[] args) throws Exception {
        String code =
            "program: helloWorld {\n" + "\tvar-consts {\n" + "\t\t@Variable de entrada\n"
                + "\t\tvar integer entrada;\n" + "\t}\n" + "\tinstructions {\n" + "\t}\n" + "}\n";
        
        Debugger.INSTANCE.setLoggingEnabled(true);
        Debugger.INSTANCE.setDebugEnabled(true);
        
        Parser parser = new Parser(new Lexer(new StringReader(code)));
        parser.parse();
    }
}
