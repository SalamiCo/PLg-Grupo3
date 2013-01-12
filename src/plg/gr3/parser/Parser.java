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
    
    private Attributes parseSDecs (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //SDecs ::=
        try {
            // varconsts illave
            expect(last, TokenType.RW_VARCONSTS);
            expect(last, TokenType.SYM_PAR_LEFT);
            
            // Decs
            parseDecs(last, Attributes.DEFAULT);
            
            // fllave
            expect(last, TokenType.SYM_PAR_RIGHT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseDecs (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Decs ::=
        try {
            // Dec
            Attributes attrDec = parseDec(last, Attributes.DEFAULT);
            if (attrDec == null) {
                return null;
            }
            
            // RDecs
            symbolTable.putIdentifier(
                attrDec.getIdentifier(), attrDec.getType(), attrDec.getConstant(), attrDec.getAddress(),
                attrDec.getValue());
            
            parseRDecs(last, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
        
    }
    
    private Attributes parseRDecs (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // RDecs ::=
        try {
            // pyc
            expect(last, TokenType.SYM_SEMICOLON);
            // Dec
            Attributes attrDec = parseDec(last, Attributes.DEFAULT);
            // RDecs
            symbolTable.putIdentifier(
                attrDec.getIdentifier(), attrDec.getType(), attrDec.getConstant(), attrDec.getAddress(),
                attrDec.getValue());
            parseRDecs(last, Attributes.DEFAULT);
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseDec (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Dec ::=
        try {
            // var || const
            LocatedToken lt = expect(last, TokenType.RW_VAR, TokenType.RW_CONST);
            switch (lt.getToken().getType()) {
                case RW_VAR: {
                    //Type
                    Attributes attrType = parseType(last, Attributes.DEFAULT);
                    //ident
                    LocatedToken id = expect(last, TokenType.IDENTIFIER);
                    
                    attrb.constant(false).type(attrType.getType()).identifier(id.getToken().getLexeme());
                }
                    break;
                
                case RW_CONST: {
                    //Type
                    Attributes attrType = parseType(last, Attributes.DEFAULT);
                    //ident
                    LocatedToken id = expect(last, TokenType.IDENTIFIER);
                    //dpigual
                    expect(last, TokenType.SYM_CONST_ASIGNATION);
                    //Lit
                    Attributes attrLit = parseLit(last, Attributes.DEFAULT);
                    
                    attrb.constant(true).type(attrType.getType()).identifier(id.getLexeme()).value(attrLit.getValue());
                }
                    break;
            
            }
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
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
    
    private Attributes parseType (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            LocatedToken token =
                expect(
                    last, TokenType.RW_BOOLEAN, TokenType.RW_NATURAL, TokenType.RW_INTEGER, TokenType.RW_FLOAT,
                    TokenType.RW_CHARACTER);
            
            switch (token.getToken().getType()) {
                case RW_BOOLEAN:
                    attrb.type(Type.BOOLEAN);
                    break;
                
                case RW_NATURAL:
                    attrb.type(Type.NATURAL);
                    break;
                
                case RW_INTEGER:
                    attrb.type(Type.INTEGER);
                    break;
                
                case RW_FLOAT:
                    attrb.type(Type.FLOAT);
                    break;
                
                case RW_CHARACTER:
                    attrb.type(Type.CHARACTER);
                    break;
            }
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseCast (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            LocatedToken token =
                expect(last, TokenType.RW_NAT, TokenType.RW_INT, TokenType.RW_FLOAT, TokenType.RW_CHAR);
            
            switch (token.getToken().getType()) {
                case RW_NAT:
                    attrb.type(Type.NATURAL);
                    break;
                
                case RW_INT:
                    attrb.type(Type.INTEGER);
                    break;
                
                case RW_FLOAT:
                    attrb.type(Type.FLOAT);
                    break;
                
                case RW_CHAR:
                    attrb.type(Type.CHARACTER);
                    break;
            }
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
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
    
    private Attributes parseExpr (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            Attributes attrTerm = parseTerm(last, Attributes.DEFAULT);
            Attributes attrInhFExpr = new Attributes.Builder().type(attrTerm.getType()).create();
            Attributes attrFExpr = parseFExpr(last, attrInhFExpr);
            
            return attrb.type(attrFExpr.getType()).create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseFExpr (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            Attributes attrOp0 = parseOp0(last, Attributes.DEFAULT);
            if (attrOp0 != null) {
                Attributes attrTerm = parseTerm(last, attrOp0);
                attrb.type(attrTerm.getType());
            } else {
                attrb.type(attr.getType());
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseTerm (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            // TODO llamada a la función tipoOpIgu(a, b)
            Attributes attrFact = parseFact(last, Attributes.DEFAULT);
            Attributes attrInhRTerm = new Attributes.Builder().type(attrFact.getType()).create();
            Attributes attrRTerm = parseRTerm(true, attrInhRTerm);
            
            return attrb.type(attrRTerm.getType()).create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseRTerm (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            Attributes attrOp1 = parseOp1(last, Attributes.DEFAULT);
            if (attrOp1 != null) {
                Attributes attrInhFact = new Attributes.Builder().type(attrOp1.getType()).create();
                Attributes attrFact = parseFact(last, attrInhFact);
                if (attrFact != null) {
                    Attributes attrRTerm = parseRTerm(last, attrFact);
                    attrb.type(attrRTerm.getType());
                } else {
                    attrb.type(attrFact.getType());
                }
            } else {
                attrb.type(attrOp1.getType());
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp0 (boolean last, Attributes attr) {
        try {
            TokenType token =
                expect(
                    last, TokenType.SYM_EQUAL, TokenType.SYM_NOT_EQUAL, TokenType.SYM_LOWER, TokenType.SYM_GREATER,
                    TokenType.SYM_LOWER_OR_EQUAL, TokenType.SYM_GREATER_OR_EQUAL);
            
            Attributes.Builder attrb = new Attributes.Builder();
            switch (token.getType()) {
                case TokenType.SYM_EQUAL:
                    attrb.operator("igual");
                    break;
                case TokenType.SYM_NOT_EQUAL:
                    attrb.operator("igual");
                    break;
                case TokenType.SYM_LOWER:
                    attrb.operator("igual");
                    break;
                case TokenType.SYM_GREATER:
                    attrb.operator("igual");
                    break;
                case TokenType.SYM_LOWER_OR_EQUAL:
                    attrb.operator("igual");
                    break;
                case TokenType.SYM_GREATER_OR_EQUAL:
                    attrb.operator("igual");
                    break;
                default:
                    attrb.operator("nullako");
                    break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp1 (boolean last, Attributes attr) {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            TokenType token = expect(last, TokenType.RW_OR, TokenType.SYM_MINUS, TokenType.SYM_PLUS);
            switch (token.getType()) {
                case TokenType.RW_OR:
                    attrb.operator("or");
                    break;
                case TokenType.SYM_MINUS:
                    attrb.operator("menos");
                    break;
                case TokenType.SYM_PLUS:
                    attrb.operator("mas");
                    break;
                default:
                    attrb.operator("nullako");
                    break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp2 (boolean last, Attributes attr) {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            TokenType token =
                expect(last, TokenType.RW_AND, TokenType.SYM_MODULO, TokenType.SYM_DIV, TokenType.SYM_MULT);
            switch (token.getType()) {
                case TokenType.RW_AND:
                    attrb.operator("and");
                    break;
                case TokenType.SYM_MODULO:
                    attrb.operator("modulo");
                    break;
                case TokenType.SYM_DIV:
                    attrb.operator("div");
                    break;
                case TokenType.SYM_MULT:
                    attrb.operator("mult");
                    break;
                default:
                    attrb.operator("nullako");
                    break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp3 (boolean last, Attributes attr) {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            TokenType token = expect(last, TokenType.SYM_SHIFT_LEFT, TokenType.SYM_SHIFT_RIGHT);
            switch (token.getType()) {
                case TokenType.SYM_SHIFT_LEFT:
                    attrb.operator("rsh");
                    break;
                case TokenType.SYM_SHIFT_RIGHT:
                    attrb.operator("lsh");
                    break;
                default:
                    attrb.operator("nullako");
                    break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp4 (boolean last, Attributes attr) {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            TokenType token = expect(last, TokenType.RW_NOT, TokenType.SYM_MINUS);
            switch (token.getType()) {
                case TokenType.RW_NOT:
                    attrb.operator("not");
                    break;
                case TokenType.SYM_MINUS:
                    attrb.operator("minus");
                    break;
                default:
                    attrb.operator("nullako");
                    break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
