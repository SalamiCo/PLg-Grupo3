package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import plg.gr3.CompileError;
import plg.gr3.Util;
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
    
    /** Token leído sin consumir */
    private LocatedToken token;
    
    /** Categorías que se esperaban y no se encontraron */
    private final Set<TokenType> expected = new HashSet<>();
    
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
        // Si tenemos algún token pendiente, lo usamos. Si no, leemos uno nuevo
        if (token == null) {
            token = lexer.nextToken();
        }
        
        if (token != null) {
            for (TokenType category : categories) {
                expected.add(category);
                
                if (category.equals(token.getToken().getType())) {
                    expected.clear();
                    Debugger.INSTANCE.at(token.getLine(), token.getColumn()).debug("Reconocido " + token.getToken());
                    
                    LocatedToken ret = token;
                    token = null;
                    return ret;
                }
            }
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
        
        if (attr == null) {
            CompileError error = CompileError.newUnexpectedTokenError(token, expected);
            error.print();
        }
        
        return true;
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
            expect(last, TokenType.SYM_CURLY_LEFT);
            
            // Decs
            parseDecs(last, Attributes.DEFAULT);
            
            // fllave
            expect(last, TokenType.SYM_CURLY_RIGHT);
            
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
            expect(false, TokenType.SYM_SEMICOLON);
            
            // Dec
            Attributes attrDec = parseDec(last, Attributes.DEFAULT);
            if (attrDec == null) {
                return Attributes.DEFAULT;
            }
            
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
            expect(last, TokenType.RW_INSTRUCTIONS);
            expect(last, TokenType.SYM_CURLY_LEFT);
            
            //Insts
            parseInsts(last, Attributes.DEFAULT);
            
            //fllave
            expect(last, TokenType.SYM_CURLY_RIGHT);
            
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
            parseInst(last, Attributes.DEFAULT);
            
            //RInst 
            parseRInst(last, Attributes.DEFAULT);
            
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
            expect(last, TokenType.SYM_SEMICOLON);
            
            //Inst
            parseInst(last, Attributes.DEFAULT);
            
            //RInsts
            parseRInst(last, Attributes.DEFAULT);
            
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
                    last, TokenType.IDENTIFIER, TokenType.RW_IN, TokenType.RW_OUT, TokenType.RW_SWAP1,
                    TokenType.RW_SWAP2);
            
            switch (readToken.getToken().getType())
            
            { //ident asig Expr
                case IDENTIFIER:
                    
                    expect(last, TokenType.SYM_ASIGNATION);
                    parseExpr(last, Attributes.DEFAULT);
                
                break;
                
                //in lpar ident rpar
                case RW_IN:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    expect(last, TokenType.IDENTIFIER);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
                break;
                
                //out lpar Expr rpar
                case RW_OUT:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    parseExpr(last, Attributes.DEFAULT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
                break;
                
                //swap1 lpar rpar
                case RW_SWAP1:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
                break;
                
                //swap2 lpar rpar
                case RW_SWAP2:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
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
            
            switch (token.getType()) {
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
            // TODO llamada a la función tipoFunc(a, b)
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
    
    private Attributes parseLitNum (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            LocatedToken tokenRead = expect(last, TokenType.LIT_NATURAL, TokenType.LIT_FLOAT, TokenType.SYM_MINUS);
            
            switch (tokenRead.getToken().getType()) {
            
            //litnat
                case LIT_NATURAL:
                    attrb.type(Type.NATURAL).value(Util.stringToNatural(tokenRead.getToken().getLexeme()));
                break;
                
                //litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT).value(Util.stringToFloat(tokenRead.getToken().getLexeme()));
                break;
                
                //menos
                case SYM_MINUS:
                    //FLitNum
                    Attributes attrFLitNum = parseFLitNum(last, Attributes.DEFAULT);
                    attrb.type(attrFLitNum.getType()).value(attrFLitNum.getValue());
                break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp0 (boolean last, Attributes attr) throws IOException {
        try {
            LocatedToken token =
                expect(
                    last, TokenType.SYM_EQUAL, TokenType.SYM_NOT_EQUAL, TokenType.SYM_LOWER, TokenType.SYM_GREATER,
                    TokenType.SYM_LOWER_OR_EQUAL, TokenType.SYM_GREATER_OR_EQUAL);
            
            Attributes.Builder attrb = new Attributes.Builder();
            switch (token.getType()) {
                case SYM_EQUAL:
                    attrb.operator("igual");
                break;
                case SYM_NOT_EQUAL:
                    attrb.operator("igual");
                break;
                case SYM_LOWER:
                    attrb.operator("igual");
                break;
                case SYM_GREATER:
                    attrb.operator("igual");
                break;
                case SYM_LOWER_OR_EQUAL:
                    attrb.operator("igual");
                break;
                case SYM_GREATER_OR_EQUAL:
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
    
    private Attributes parseOp1 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token = expect(last, TokenType.RW_OR, TokenType.SYM_MINUS, TokenType.SYM_PLUS);
            switch (token.getType()) {
                case RW_OR:
                    attrb.operator("or");
                break;
                case SYM_MINUS:
                    attrb.operator("menos");
                break;
                case SYM_PLUS:
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
    
    private Attributes parseFLitNum (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            LocatedToken tokenRead = expect(last, TokenType.LIT_NATURAL, TokenType.LIT_FLOAT);
            
            switch (tokenRead.getToken().getType()) {
            
            //litnat
                case LIT_NATURAL:
                    attrb.type(Type.INTEGER).value(Util.stringToNatural(tokenRead.getToken().getLexeme()));
                break;
                
                //litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT).value(Util.stringToFloat(tokenRead.getToken().getLexeme()));
                break;
            }
            return attrb.create();
        } catch (NoSuchElementException e) {
            return Attributes.DEFAULT;
        }
        
    }
    
    private Attributes parseOp2 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token =
                expect(last, TokenType.RW_AND, TokenType.SYM_MODULO, TokenType.SYM_DIV, TokenType.SYM_MULT);
            switch (token.getType()) {
                case RW_AND:
                    attrb.operator("and");
                break;
                case SYM_MODULO:
                    attrb.operator("modulo");
                break;
                case SYM_DIV:
                    attrb.operator("div");
                break;
                case SYM_MULT:
                    attrb.operator("mult");
                break;
                default:
                    attrb.operator("nullako");
                break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return Attributes.DEFAULT;
        }
        
    }
    
    //Fact
    private Attributes parseFact (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Fact ::=
        try {
            //Shft
            Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
            //Rfact
            Attributes rfactInhAttr = new Attributes.Builder().type(shftSynAttr.getType()).create();
            Attributes rfactSynAttr = parseRFact(true, rfactInhAttr);
            
            attrb.type(rfactSynAttr.getType());
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    //Rfact
    private Attributes parseRFact (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        //Rfact
        try {
            //Op2
            Attributes op2SynAttr = parseOp2(true, Attributes.DEFAULT);
            
            if (op2SynAttr != null) {
                //Shft
                Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
                //TODO  RFact1.typeh = tipoFunc(RFact0.typeh, Op2.op, Shft.type)            
                
                //Rfact
                Attributes rfactSynAttr = parseRFact(true, Attributes.DEFAULT);
                
                attrb.type(rfactSynAttr.getType());
                
            } else {
                //Epsilon
                attrb.type(attr.getType());
            }
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    //Shft
    private Attributes parseShft (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Shft ::=
        try {
            //Unary
            Attributes unarySynAttr = parseUnary(true, Attributes.DEFAULT);
            //FShft
            Attributes fshftInhAttr = new Attributes.Builder().type(unarySynAttr.getType()).create();
            Attributes fshftSynAttr = parseFShft(true, fshftInhAttr);
            
            attrb.type(fshftSynAttr.getType());
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    //FShft
    private Attributes parseFShft (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        //FShft
        try {
            //Op3
            Attributes op3SynAttr = parseOp3(true, Attributes.DEFAULT);
            
            if (op3SynAttr != null) {
                //Shft            
                Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
                
                //TODO FShft.type = tipoFunc(FShft.typeh, Op3.op, Shft.type)
                /*
                 * attrb.type(tipoFunc(attr.getType(), op3SynAttr.getOperator(), shftSynAttr.getType() ));
                 */
            } else {
                //Epsilon
                attrb.type(attr.getType());
            }
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseLit (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            Attributes attrSynLitBool = parseLitBool(last, Attributes.DEFAULT);
            if (attrSynLitBool != null) {
                attrb.type(attrSynLitBool.getType()).value(attrSynLitBool.getValue());
                return attrb.create();
            }
            
            Attributes attrSynLitNum = parseLitNum(last, Attributes.DEFAULT);
            if (attrSynLitNum != null) {
                attrb.type(attrSynLitNum.getType()).value(attrSynLitNum.getValue());
                return attrb.create();
            }
            
            LocatedToken token = expect(last, TokenType.LIT_NATURAL);
            attrb.type(Type.NATURAL).value(Util.stringToNatural(token.getLexeme()));
            
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
    }
    
    private Attributes parseOp3 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token = expect(last, TokenType.SYM_SHIFT_LEFT, TokenType.SYM_SHIFT_RIGHT);
            switch (token.getType()) {
                case SYM_SHIFT_LEFT:
                    attrb.operator("rsh");
                break;
                case SYM_SHIFT_RIGHT:
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
    
    /*
     * private Attributes parseProgram (Attributes attrs) throws IOException {
     * 
     * boolean b = lexer.hasNextToken(RW_PROGRAM);
     * 
     * return attrs; }
     */
    
    private Attributes parseParen (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //Paren ::=
        try {
            //Lit
            Attributes litAttributes = parseLit(last, Attributes.DEFAULT);
            
            if (litAttributes == null) {
                
                LocatedToken tokenRead = expect(last, TokenType.SYM_PAR_LEFT, TokenType.IDENTIFIER);
                
                switch (tokenRead.getToken().getType()) {
                
                // lpar Expr rpar
                    case SYM_PAR_LEFT:
                        parseExpr(last, Attributes.DEFAULT);
                        expect(last, TokenType.SYM_PAR_RIGHT);
                    break;
                    
                    // ident
                    case IDENTIFIER:
                        attrb.type(this.symbolTable.getIdentfierType(tokenRead.getLexeme()));
                    break;
                
                }
                
            } else {
                attrb.type(litAttributes.getType());
            }
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseUnary (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //Unary ::=
        try {
            //Op4
            Attributes attrOp4 = parseOp4(last, Attributes.DEFAULT);
            if (attrOp4 != null) {
                //Unary
                parseUnary(last, Attributes.DEFAULT);
            } else {
                Attributes attrParen = parseParen(last, Attributes.DEFAULT);
                if (attrParen != null) {
                    //Paren
                    attrb.type(attrParen.getType());
                    parseParen(last, Attributes.DEFAULT);
                } else {
                    //lpar
                    expect(last, TokenType.SYM_PAR_LEFT);
                    //Cast
                    parseCast(last, Attributes.DEFAULT);
                    //rpar
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    //Paren
                    parseParen(last, Attributes.DEFAULT);
                }
            }
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
    
    private Attributes parseOp4 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token = expect(last, TokenType.RW_NOT, TokenType.SYM_MINUS);
            switch (token.getType()) {
                case RW_NOT:
                    attrb.operator("not");
                break;
                case SYM_MINUS:
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
    
    /*
     * Expr→ { Term.tsh = Expr.tsh } Term { FExpr.typeh = Term.type FExpr.tsh = Expr.tsh } FExpr { Expr.type =
     * FExpr.type Expr.cod = Term.cod || FExpr.cod }
     * 
     * FExpr→ Op0 { Term.tsh = FExpr.tsh } Term { FExpr.type = tipoOpIgu(FExpr.typeh,Term.type) FExpr.cod = Term.cod ||
     * Op0.op }
     * 
     * FExpr→ ɛ { FExpr.type = FExpr.typeh FExpr.cod = ɛ }
     * 
     * Term → { Fact.tsh = Term.tsh } Fact { RTerm.tsh = Fact.tsh RTerm.typeh = Fact.type RTerm.codh = Fact.cod } RTerm
     * { Term.type = RFact.type Term.cod = RFact.cod }
     * 
     * RTerm → Op1 { Fact.tsh = RTerm0.tsh } Fact { RTerm1.tsh = Fact.tsh RTerm1.typeh = tipoFunc(RTerm0.typeh, Op1.op,
     * Fact.type) RTerm1.codh = Term0.codh || Fact.cod || Op1.op } RTerm { RTerm0.type = RTerm1.type RTerm0.cod
     * =RTerm1.cod }
     * 
     * RTerm → ɛ { RTerm.type = RTerm.typeh RTerm.cod = RTerm.codh }
     */
    
    private Attributes parseLitBool (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        //LitBool ::=
        try {
            LocatedToken tokenRead = expect(last, TokenType.RW_TRUE, TokenType.RW_FALSE);
            
            switch (tokenRead.getToken().getType()) {
                case RW_TRUE:
                    attrb.type(Type.BOOLEAN);
                    attrb.value(true);
                break;
                
                //false
                case RW_FALSE:
                    attrb.type(Type.BOOLEAN);
                    attrb.value(false);
                break;
            
            }
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
}
