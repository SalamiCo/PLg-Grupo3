package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import plg.gr3.AssignError;
import plg.gr3.AssignToConstantError;
import plg.gr3.BinaryOperator;
import plg.gr3.CastingError;
import plg.gr3.CompileError;
import plg.gr3.OperatorError;
import plg.gr3.UnaryOperator;
import plg.gr3.UndefinedIdentError;
import plg.gr3.UnexpectedTokenError;
import plg.gr3.Util;
import plg.gr3.code.CodeWriter;
import plg.gr3.code.instructions.LoadInstruction;
import plg.gr3.code.instructions.PushInstruction;
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
    
    /** Coleccion de errores de compilacion */
    private final Collection<CompileError> errors = new ArrayList<>();
    
    /** Generador de código */
    
    private final CodeWriter codeWriter;
    
    /**
     * @param lexer
     *            Analizador léxico que será utilizado por este analizador sintáctico
     */
    public Parser (Lexer lexer, CodeWriter codeGenerator) {
        this.lexer = lexer;
        this.codeWriter = codeGenerator;
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
            CompileError error = new UnexpectedTokenError(token, expected);
            error.print();
            errors.add(error);
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
            
            //TODO comprobar que el identificador no esta repetido
            
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
            LocatedToken tokenRead =
                expect(
                    last, TokenType.IDENTIFIER, TokenType.RW_IN, TokenType.RW_OUT, TokenType.RW_SWAP1,
                    TokenType.RW_SWAP2);
            
            switch (tokenRead.getToken().getType())
            
            { //ident asig Expr
                case IDENTIFIER:
                    
                    expect(last, TokenType.SYM_ASIGNATION);
                    Attributes exprAttributes = parseExpr(last, Attributes.DEFAULT);
                    
                    /* Comprobamos que el identificador existe */
                    if (!this.symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                        UndefinedIdentError error =
                            new UndefinedIdentError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        error.print();
                        errors.add(error);
                        
                    } else {
                        
                        /* Comprobamos que no estamos asignando la expresion a una constante */
                        if (this.symbolTable.isIdentifierConstant(tokenRead.getLexeme())) {
                            AssignToConstantError error =
                                new AssignToConstantError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                            error.print();
                            errors.add(error);
                        } else {
                            /*
                             * Comprobamos que el tipo de la expresion y del identificador Son compatibles para la
                             * asignacion
                             */
                            Type identType = this.symbolTable.getIdentfierType(tokenRead.getLexeme());
                            Type exprType = exprAttributes.getType();
                            
                            if (!exprType.typeMatch(exprType, identType)) {
                                AssignError error = new AssignError(identType, exprType, tokenRead);
                                error.print();
                                errors.add(error);
                            }
                        }
                        
                    }
                
                break;
                
                //in lpar ident rpar
                case RW_IN:
                    
                    expect(last, TokenType.SYM_PAR_LEFT);
                    LocatedToken identRead = expect(last, TokenType.IDENTIFIER);
                    
                    /* Comprobamos que el identificador existe */
                    if (!this.symbolTable.hasIdentifier(identRead.getLexeme())) {
                        UndefinedIdentError error =
                            new UndefinedIdentError(identRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        error.print();
                        errors.add(error);
                        
                    } else {
                        
                        /* Comprobamos que no estamos asignando la expresion a una constante */
                        if (this.symbolTable.isIdentifierConstant(identRead.getLexeme())) {
                            AssignToConstantError error =
                                new AssignToConstantError(identRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                            error.print();
                            errors.add(error);
                        }
                    }
                    
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
            
            // Expr.cod = Term.cod || FExpr.cod }
            codeWriter.write(attrTerm.getInstructions());
            codeWriter.write(attrFExpr.getInstructions());
            
            return attrb.type(attrFExpr.getType()).create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseFExpr (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        try {
            Attributes attrOp0 = parseOp0(last, Attributes.DEFAULT);
            if (attrOp0 != null) {
                Attributes attrTerm = parseTerm(last, attrOp0);
                
                //Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = (BinaryOperator) attrOp0.getOperator();
                
                if (!op.canApply(attr.getType(), attrTerm.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrTerm.getType(), attrOp0.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                // FExpr.cod = Term.cod || Op0.op }
                codeWriter.write(attrTerm.getInstructions());
                codeWriter.write(attrOp0.getInstructions());
                
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
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        try {
            Attributes attrOp1 = parseOp1(last, Attributes.DEFAULT);
            if (attrOp1 != null) {
                Attributes attrInhFact = new Attributes.Builder().type(attrOp1.getType()).create();
                Attributes attrFact = parseFact(last, attrInhFact);
                if (attrFact != null) {
                    Type t = attrOp1.getOperator(BinaryOperator.class).getApplyType(attrFact.getType(), attr.getType());
                    Attributes attrInhRTerm = new Attributes.Builder().type(t).create();
                    Attributes attrRTerm = parseRTerm(last, attrFact);
                    
                    //Comprobamos que podamos aplicar el operador (que los tipos casen)
                    BinaryOperator op = (BinaryOperator) attrOp1.getOperator();
                    
                    if (!op.canApply(attr.getType(), attrRTerm.getType())) {
                        OperatorError error =
                            new OperatorError(
                                attr.getType(), attrRTerm.getType(), attrOp1.getOperator(), actLine, actColumn);
                        error.print();
                        errors.add(error);
                    }
                    
                    //  RTerm1.codh = RTerm0.codh || Fact.cod || Op1.op }
                    codeWriter.write(attrFact.getInstructions());
                    
                    //FIXME Cambiar el concreteInstruction, y pasarle los argumentos correctos
                    ConcreteInstruction inst = new ConcreteInstruction();
                    codeWriter.write(inst);
                    
                    attrb.type(attrRTerm.getType());
                } else {
                    return null;
                }
            } else {
                attrb.type(attr.getType());
            }
            
        } catch (NoSuchElementException e) {
            attrb.type(attr.getType());
        }
        
        return attrb.create();
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
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        //Rfact
        try {
            //Op2
            Attributes attrOp2 = parseOp2(true, Attributes.DEFAULT);
            
            if (attrOp2 != null) {
                //Shft
                Attributes attrInhShft = new Attributes.Builder().type(attrOp2.getType()).create();
                Attributes attrShft = parseShft(last, attrInhShft);
                
                //Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = (BinaryOperator) attrOp2.getOperator();
                
                if (!op.canApply(attr.getType(), attrShft.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrShft.getType(), attrOp2.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                if (attrShft != null) {
                    
                    Type t = attrOp2.getOperator(BinaryOperator.class).getApplyType(attrShft.getType(), attr.getType());
                    Attributes attrInhRFact = new Attributes.Builder().type(t).create();
                    Attributes attrRFact = parseRTerm(last, attrRFact);
                    
                    // RFact1.codh = RFact0.codh || Shft.cod || Op2.op }
                    //FIXME Cambiar el concreteInstruction, y pasarle los argumentos correctos
                    ConcreteInstruction inst = new ConcreteInstructions();
                    codeWriter.write(inst);
                    
                } else {
                    return null;
                }
            } else {
                attrb.type(attr.getType());
            }
        } catch (NoSuchElementException exc) {
            attrb.type(attr.getType());
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
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        //FShft
        try {
            //Op3
            Attributes op3SynAttr = parseOp3(true, Attributes.DEFAULT);
            
            if (op3SynAttr != null) {
                //Shft            
                Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
                
                //Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = (BinaryOperator) op3SynAttr.getOperator();
                
                if (!op.canApply(attr.getType(), shftSynAttr.getType())) {
                    OperatorError error =
                        new OperatorError(
                            attr.getType(), shftSynAttr.getType(), op3SynAttr.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
            } else {
                //Epsilon
                attrb.type(attr.getType());
            }
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseUnary (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        //Unary ::=
        try {
            //Op4
            Attributes attrOp4 = parseOp4(last, Attributes.DEFAULT);
            if (attrOp4 != null) {
                //Unary
                Attributes attrUnary = parseUnary(last, Attributes.DEFAULT);
                
                //Comprobamos que el operador unario se puede aplicar (que los tipos casan)
                UnaryOperator op = (UnaryOperator) attrOp4.getOperator();
                
                if (!op.canApply(attrUnary.getType())) {
                    OperatorError error =
                        new OperatorError(attrUnary.getType(), attrOp4.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
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
                    Attributes attrCast = parseCast(last, Attributes.DEFAULT);
                    //rpar
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    //Paren
                    Attributes attrParen2 = parseParen(last, Attributes.DEFAULT);
                    
                    /* Comprobamos que se puede aplicar el tipo del casting al tipo casteado */
                    if (!attrParen2.getType().typeCasting(attrCast.getType(), attrParen2.getType())) {
                        CastingError error =
                            new CastingError(
                                attrCast.getType(), attrParen2.getType(), lexer.getLine(), lexer.getColumn());
                        error.print();
                        errors.add(error);
                    }
                }
            }
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
    
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
                    case SYM_PAR_LEFT: {
                        parseExpr(last, Attributes.DEFAULT);
                        expect(last, TokenType.SYM_PAR_RIGHT);
                        PushInstruction inst = new PushInstruction(tokenRead.getToken().getType());
                        codeWriter.write(inst);
                    }
                    break;
                    
                    // ident
                    case IDENTIFIER: {
                        
                        /* Comprobamos que el identificador existe */
                        if (!this.symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                            UndefinedIdentError error =
                                new UndefinedIdentError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                            error.print();
                            errors.add(error);
                            
                        } else {
                            attrb.type(this.symbolTable.getIdentfierType(tokenRead.getLexeme()));
                        }
                        
                        // Paren.cod = apila-dir(Paren.tsh[ident.lex].dir) }
                        int addr = symbolTable.getIdentifierAddress(tokenRead.getLexeme());
                        LoadInstruction inst = new LoadInstruction(addr);
                        codeWriter.write(inst);
                    }
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
    
    private Attributes parseOp0 (boolean last, Attributes attr) throws IOException {
        try {
            LocatedToken token =
                expect(
                    last, TokenType.SYM_EQUAL, TokenType.SYM_NOT_EQUAL, TokenType.SYM_LOWER, TokenType.SYM_GREATER,
                    TokenType.SYM_LOWER_OR_EQUAL, TokenType.SYM_GREATER_OR_EQUAL);
            
            Attributes.Builder attrb = new Attributes.Builder();
            switch (token.getType()) {
                case SYM_EQUAL:
                    attrb.operator(BinaryOperator.EQUALS);
                break;
                case SYM_NOT_EQUAL:
                    attrb.operator(BinaryOperator.NOT_EQUALS);
                break;
                case SYM_LOWER:
                    attrb.operator(BinaryOperator.LOWER_THAN);
                break;
                case SYM_GREATER:
                    attrb.operator(BinaryOperator.GREATER_THAN);
                break;
                case SYM_LOWER_OR_EQUAL:
                    attrb.operator(BinaryOperator.LOWER_EQUAL);
                break;
                case SYM_GREATER_OR_EQUAL:
                    attrb.operator(BinaryOperator.GREATER_EQUALS);
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
                    attrb.operator(BinaryOperator.OR);
                break;
                case SYM_MINUS:
                    attrb.operator(BinaryOperator.SUBTRACTION);
                break;
                case SYM_PLUS:
                    attrb.operator(BinaryOperator.ADDITION);
                break;
            }
            
            return attrb.create();
            
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp2 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token =
                expect(last, TokenType.RW_AND, TokenType.SYM_MODULO, TokenType.SYM_DIV, TokenType.SYM_MULT);
            switch (token.getType()) {
                case RW_AND:
                    attrb.operator(BinaryOperator.AND);
                break;
                case SYM_MODULO:
                    attrb.operator(BinaryOperator.MODULO);
                break;
                case SYM_DIV:
                    attrb.operator(BinaryOperator.DIVISION);
                break;
                case SYM_MULT:
                    attrb.operator(BinaryOperator.PRODUCT);
                break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return Attributes.DEFAULT;
        }
        
    }
    
    private Attributes parseOp3 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token = expect(last, TokenType.SYM_SHIFT_LEFT, TokenType.SYM_SHIFT_RIGHT);
            switch (token.getType()) {
                case SYM_SHIFT_LEFT:
                    attrb.operator(BinaryOperator.SHIFT_LEFT);
                break;
                case SYM_SHIFT_RIGHT:
                    attrb.operator(BinaryOperator.SHIFT_RIGHT);
                break;
            }
            
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    private Attributes parseOp4 (boolean last, Attributes attr) throws IOException {
        try {
            Attributes.Builder attrb = new Attributes.Builder();
            LocatedToken token = expect(last, TokenType.RW_NOT, TokenType.SYM_MINUS);
            switch (token.getType()) {
                case RW_NOT:
                    attrb.operator(UnaryOperator.NOT);
                break;
                case SYM_MINUS:
                    attrb.operator(UnaryOperator.MINUS);
                break;
            }
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
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
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
}
