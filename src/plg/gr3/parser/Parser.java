package plg.gr3.parser;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import plg.gr3.code.CodeWriter;
import plg.gr3.data.BinaryOperator;
import plg.gr3.data.Natural;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.debug.Debugger;
import plg.gr3.errors.compile.AssignError;
import plg.gr3.errors.compile.AssignToConstantError;
import plg.gr3.errors.compile.CastingError;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.errors.compile.OperatorError;
import plg.gr3.errors.compile.UndefinedIdentifierError;
import plg.gr3.errors.compile.UnexpectedTokenError;
import plg.gr3.lexer.Lexer;
import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.TokenType;
import plg.gr3.vm.instr.BinaryOperatorInstruction;
import plg.gr3.vm.instr.CastInstruction;
import plg.gr3.vm.instr.InputInstruction;
import plg.gr3.vm.instr.LoadInstruction;
import plg.gr3.vm.instr.OutputInstruction;
import plg.gr3.vm.instr.PushInstruction;
import plg.gr3.vm.instr.StopInstruction;
import plg.gr3.vm.instr.StoreInstruction;
import plg.gr3.vm.instr.Swap1Instruction;
import plg.gr3.vm.instr.Swap2Instruction;
import plg.gr3.vm.instr.UnaryOperatorInstruction;

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
     * @param lexer Analizador léxico que será utilizado por este analizador sintáctico
     * @param codeWriter Escritor al que se escribirá todo el código
     */
    public Parser (Lexer lexer, CodeWriter codeWriter) {
        this.lexer = lexer;
        this.codeWriter = codeWriter;
        this.symbolTable = new SymbolTable();
    }
    
    /**
     * Intenta reconocer un token de alguna de las categorías pasadas como parámero
     * 
     * @param last Si al fallar debemos mostrar un mensaje de error o no
     * @param categories Categorías que queremos reconocer
     * @return El token que ha reconocido
     * @throws IOException si ocurre un error de E/S
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
    
    /**
     * @return <tt>true</tt> si se ha reconocido el lenguaje sin errores, <tt>false</tt> en caso contratrio
     * @throws IOException si ocurre un error de E/S
     */
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
            Attributes attrSInsts = parseSInsts(true, Attributes.DEFAULT);
            
            // fllave fin
            expect(true, TokenType.SYM_CURLY_RIGHT);
            expect(true, TokenType.EOF);
            
            // Program.cod = SInsts.cod || stop }
            codeWriter.write(attrSInsts.getInstructions());
            codeWriter.write(new StopInstruction());
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseSDecs (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // SDecs ::=
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
            
            // Se supone que en esta produccion se crea la tabla de símbolos. Así que no me tengo que preocupar si el
            // identificador ya ha sido declarado porque es el 1º
            
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
            
            if (symbolTable.hasIdentifier(attrDec.getIdentifier())) {
                DuplicateIdentifierError error =
                    new DuplicateIdentifierError(attrDec.getIdentifier(), lexer.getLine(), lexer.getColumn());
                errors.add(error);
                
            } else {
                // RDecs
                symbolTable.putIdentifier(
                    attrDec.getIdentifier(), attrDec.getType(), attrDec.getConstant(), attrDec.getAddress(),
                    attrDec.getValue());
            }
            
            Attributes inhAttr = new Attributes.Builder().address(attrDec.getAddress()).create();
            parseRDecs(last, inhAttr);
            
        } catch (NoSuchElementException exc) {
            return null;
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
                    // Type
                    Attributes attrType = parseType(last, Attributes.DEFAULT);
                    // ident
                    LocatedToken token = expect(last, TokenType.IDENTIFIER);
                    
                    attrb.constant(false).type(attrType.getType()).identifier(token.getToken().getLexeme())
                        .address(attr.getAddress() + 1);
                }
                break;
                
                case RW_CONST: {
                    // Type
                    Attributes attrType = parseType(last, Attributes.DEFAULT);
                    // ident
                    LocatedToken id = expect(last, TokenType.IDENTIFIER);
                    // dpigual
                    expect(last, TokenType.SYM_CONST_ASIGNATION);
                    // Lit
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
        
        // SInsts ::=
        try {
            // instructions illave
            expect(last, TokenType.RW_INSTRUCTIONS);
            expect(last, TokenType.SYM_CURLY_LEFT);
            
            // Insts
            parseInsts(last, Attributes.DEFAULT);
            
            // fllave
            expect(last, TokenType.SYM_CURLY_RIGHT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
        
    }
    
    private Attributes parseInsts (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Insts ::=
        try {
            // Inst
            parseInst(last, Attributes.DEFAULT);
            
            // RInsts
            parseRInsts(last, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseRInsts (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // RInsts ::=
        try {
            // pyc
            expect(last, TokenType.SYM_SEMICOLON);
            
            // Inst
            Attributes attrInst = parseInst(last, Attributes.DEFAULT);
            
            // RInsts1.codh = RInsts0.codh || Inst.cod
            codeWriter.write(attrInst.getInstructions());
            
            // RInsts
            parseRInsts(last, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    private Attributes parseInst (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Insts ::=
        try {
            LocatedToken tokenRead =
                expect(
                    last, TokenType.IDENTIFIER, TokenType.RW_IN, TokenType.RW_OUT, TokenType.RW_SWAP1,
                    TokenType.RW_SWAP2);
            
            switch (tokenRead.getToken().getType())
            
            { // ident asig Expr
                case IDENTIFIER:
                    
                    // FIXME Probablemente no haya que pasar default
                    expect(last, TokenType.SYM_ASIGNATION);
                    Attributes exprAttributes = parseExpr(last, Attributes.DEFAULT);
                    
                    // Comprobamos que el identificador existe
                    if (!symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                        UndefinedIdentifierError errorIDExist =
                            new UndefinedIdentifierError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        errors.add(errorIDExist);
                        
                        // Comprobamos que no estamos asignando la expresion a una constante
                        if (symbolTable.isIdentifierConstant(tokenRead.getLexeme())) {
                            AssignToConstantError errorConstant =
                                new AssignToConstantError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                            errors.add(errorConstant);
                        }
                        
                        /*
                         * Comprobamos que el tipo de la expresion y del identificador son compatibles para la
                         * asignacion
                         */
                        Type identType = symbolTable.getIdentfierType(tokenRead.getLexeme());
                        Type exprType = exprAttributes.getType();
                        
                        if (!Type.assignable(exprType, identType)) {
                            AssignError error = new AssignError(identType, exprType, tokenRead);
                            error.print();
                            errors.add(error);
                        }
                    }
                    
                    // Inst.cod = Expr.cod || desapila-dir(Inst.tsh[ident.lex].dir) }
                    codeWriter.write(exprAttributes.getInstructions());
                    codeWriter.write(new StoreInstruction(symbolTable.getIdentifierAddress(tokenRead.getLexeme())));
                
                break;
                
                // in lpar ident rpar
                case RW_IN:
                    
                    expect(last, TokenType.SYM_PAR_LEFT);
                    LocatedToken identRead = expect(last, TokenType.IDENTIFIER);
                    
                    /* Comprobamos que el identificador existe */
                    if (!this.symbolTable.hasIdentifier(identRead.getLexeme())) {
                        UndefinedIdentifierError error =
                            new UndefinedIdentifierError(identRead.getLexeme(), lexer.getLine(), lexer.getColumn());
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
                        
                        // Inst.cod = in(Inst.tsh[ident.lex].type) || desapila-dir(Inst.tsh[ident.lex].dir) }
                        codeWriter.write(new InputInstruction(symbolTable.getIdentfierType(identRead.getLexeme())));
                        codeWriter.write(new StoreInstruction(symbolTable.getIdentifierAddress(identRead.getLexeme())));
                    }
                    
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
                break;
                
                // out lpar Expr rpar
                case RW_OUT:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    Attributes attrExpr = parseExpr(last, Attributes.DEFAULT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    
                    // Inst.cod = Expr.cod || out}
                    codeWriter.write(attrExpr.getInstructions());
                    codeWriter.write(new OutputInstruction());
                
                break;
                
                // swap1 lpar rpar
                case RW_SWAP1:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    
                    // FIXME Comprobar que este bien
                    // Inst.cod = swap1
                    codeWriter.write(new Swap1Instruction());
                
                break;
                
                // swap2 lpar rpar
                case RW_SWAP2:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    
                    // Inst.cod = swap2
                    codeWriter.write(new Swap2Instruction());
                
                break;
            
            }
            
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
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = attrOp0.getOperator(BinaryOperator.class);
                
                if (!op.canApply(attr.getType(), attrTerm.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrTerm.getType(), attrOp0.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                // FExpr.cod = Term.cod || Op0.op }
                codeWriter.write(attrTerm.getInstructions());
                codeWriter.write(new BinaryOperatorInstruction(op));
                
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
                    
                    // Comprobamos que podamos aplicar el operador (que los tipos casen)
                    BinaryOperator op = (BinaryOperator) attrOp1.getOperator();
                    
                    if (!op.canApply(attr.getType(), attrRTerm.getType())) {
                        OperatorError error =
                            new OperatorError(
                                attr.getType(), attrRTerm.getType(), attrOp1.getOperator(), actLine, actColumn);
                        error.print();
                        errors.add(error);
                    }
                    
                    // RTerm1.codh = RTerm0.codh || Fact.cod || Op1.op }
                    codeWriter.write(attrFact.getInstructions());
                    codeWriter.write(new BinaryOperatorInstruction(op));
                    
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
    
    // Fact
    private Attributes parseFact (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Fact ::=
        try {
            // Shft
            Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
            // Rfact
            Attributes rfactInhAttr = new Attributes.Builder().type(shftSynAttr.getType()).create();
            Attributes rfactSynAttr = parseRFact(true, rfactInhAttr);
            
            attrb.type(rfactSynAttr.getType());
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    // Rfact
    private Attributes parseRFact (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        // Rfact
        try {
            // Op2
            Attributes attrOp2 = parseOp2(true, Attributes.DEFAULT);
            
            if (attrOp2 != null) {
                // Shft
                Attributes attrInhShft = new Attributes.Builder().type(attrOp2.getType()).create();
                Attributes attrShft = parseShft(last, attrInhShft);
                
                if (attrShft == null) {
                    return null;
                }
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = attrOp2.getOperator(BinaryOperator.class);
                
                if (!op.canApply(attr.getType(), attrShft.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrShft.getType(), attrOp2.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                if (attrShft != null) {
                    Type t = attrOp2.getOperator(BinaryOperator.class).getApplyType(attrShft.getType(), attr.getType());
                    Attributes attrInhRFact = new Attributes.Builder().type(t).create();
                    Attributes attrRFact = parseRFact(last, attrInhRFact);
                    
                    // RFact1.codh = RFact0.codh || Shft.cod || Op2.op }
                    codeWriter.write(attrShft.getInstructions());
                    codeWriter.write(new BinaryOperatorInstruction(op));
                }
            } else {
                // Epsilon
                attrb.type(attr.getType());
            }
        } catch (NoSuchElementException exc) {
            attrb.type(attr.getType());
        }
        
        return attrb.create();
    }
    
    // Shft
    private Attributes parseShft (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Shft ::=
        try {
            // Unary
            Attributes unarySynAttr = parseUnary(true, Attributes.DEFAULT);
            // FShft
            Attributes fshftInhAttr = new Attributes.Builder().type(unarySynAttr.getType()).create();
            Attributes fshftSynAttr = parseFShft(true, fshftInhAttr);
            
            attrb.type(fshftSynAttr.getType());
            
            // Shft.cod = Unary.cod || FShft.cod
            
            codeWriter.write(unarySynAttr.getInstructions());
            codeWriter.write(fshftSynAttr.getInstructions());
            
        } catch (NoSuchElementException exc) {
            return Attributes.DEFAULT;
        }
        
        return attrb.create();
    }
    
    // FShft
    private Attributes parseFShft (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int actColumn = lexer.getColumn();
        int actLine = lexer.getLine();
        
        // FShft
        try {
            // Op3
            Attributes op3SynAttr = parseOp3(true, Attributes.DEFAULT);
            
            if (op3SynAttr != null) {
                // Shft
                Attributes shftSynAttr = parseShft(true, Attributes.DEFAULT);
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = (BinaryOperator) op3SynAttr.getOperator();
                
                if (!op.canApply(attr.getType(), shftSynAttr.getType())) {
                    OperatorError error =
                        new OperatorError(
                            attr.getType(), shftSynAttr.getType(), op3SynAttr.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                // FShft.cod = Shft.cod || Op3.op
                codeWriter.write(shftSynAttr.getInstructions());
                codeWriter.write(new BinaryOperatorInstruction(op));
                
            } else {
                // Epsilon
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
        
        // Unary ::=
        try {
            // Op4
            Attributes attrOp4 = parseOp4(last, Attributes.DEFAULT);
            
            if (attrOp4 != null) {
                // Unary
                Attributes attrUnary = parseUnary(last, Attributes.DEFAULT);
                
                // Comprobamos que el operador unario se puede aplicar (que los tipos casan)
                UnaryOperator op = (UnaryOperator) attrOp4.getOperator();
                
                if (!op.canApply(attrUnary.getType())) {
                    OperatorError error =
                        new OperatorError(attrUnary.getType(), attrOp4.getOperator(), actLine, actColumn);
                    error.print();
                    errors.add(error);
                }
                
                // Unary0.cod = Unary1.cod || Op4.op
                codeWriter.write(new UnaryOperatorInstruction(op));
                
            } else {
                Attributes attrParen = parseParen(last, Attributes.DEFAULT);
                if (attrParen != null) {
                    // Paren
                    attrb.type(attrParen.getType());
                    parseParen(last, Attributes.DEFAULT);
                } else {
                    // lpar
                    expect(last, TokenType.SYM_PAR_LEFT);
                    // Cast
                    Attributes attrCast = parseCast(last, Attributes.DEFAULT);
                    // rpar
                    expect(last, TokenType.SYM_PAR_RIGHT);
                    // Paren
                    Attributes attrParen2 = parseParen(last, Attributes.DEFAULT);
                    
                    /* Comprobamos que se puede aplicar el tipo del casting al tipo casteado */
                    if (!Type.canCast(attrCast.getType(), attrParen2.getType())) {
                        CastingError error =
                            new CastingError(
                                attrCast.getType(), attrParen2.getType(), lexer.getLine(), lexer.getColumn());
                        error.print();
                        errors.add(error);
                    }
                    
                    // Unary.cod = Paren.cod || Cast.type
                    codeWriter.write(attrCast.getInstructions());
                    codeWriter.write(new CastInstruction(attrCast.getType()));
                }
            }
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
    
    private Attributes parseParen (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Paren ::=
        try {
            // Lit
            Attributes litAttributes = parseLit(last, Attributes.DEFAULT);
            
            if (litAttributes == null) {
                
                LocatedToken tokenRead = expect(last, TokenType.SYM_PAR_LEFT, TokenType.IDENTIFIER);
                
                switch (tokenRead.getToken().getType()) {
                
                // lpar Expr rpar
                    case SYM_PAR_LEFT: {
                        parseExpr(last, Attributes.DEFAULT);
                        expect(last, TokenType.SYM_PAR_RIGHT);
                        
                    }
                    break;
                    
                    // ident
                    case IDENTIFIER: {
                        
                        /* Comprobamos que el identificador existe */
                        if (!this.symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                            UndefinedIdentifierError error =
                                new UndefinedIdentifierError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                            error.print();
                            errors.add(error);
                            
                        } else {
                            attrb.type(this.symbolTable.getIdentfierType(tokenRead.getLexeme()));
                        }
                        
                        /*
                         * Paren.cod = Si Paren.tsh[ident.lex].const = true apila(Paren.tsh[ident.lex].value) Si no
                         * apila-dir(Paren.tsh[ident.lex].dir)
                         */
                        
                        if (symbolTable.isIdentifierConstant(tokenRead.getLexeme())) {
                            
                            PushInstruction inst =
                                new PushInstruction(symbolTable.getIdentifierValue(tokenRead.getLexeme()));
                            codeWriter.write(inst);
                            
                        } else {
                            int addr = symbolTable.getIdentifierAddress(tokenRead.getLexeme());
                            LoadInstruction inst = new LoadInstruction(addr);
                            codeWriter.write(inst);
                        }
                        
                    }
                    break;
                }
            } else {
                attrb.type(litAttributes.getType());
                // Paren.cod = apilar(Lit.value)
                PushInstruction inst = new PushInstruction(litAttributes.getValue());
                codeWriter.write(inst);
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
            return null;
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
            attrb.type(Type.NATURAL).value(Natural.parseNat(token.getLexeme()));
            
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
    }
    
    private Attributes parseLitBool (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // LitBool ::=
        try {
            LocatedToken tokenRead = expect(last, TokenType.RW_TRUE, TokenType.RW_FALSE);
            
            switch (tokenRead.getToken().getType()) {
                case RW_TRUE:
                    attrb.type(Type.BOOLEAN);
                    attrb.value(true);
                break;
                
                // false
                case RW_FALSE:
                    attrb.type(Type.BOOLEAN);
                    attrb.value(false);
                break;
            
            }
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseLitNum (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        try {
            LocatedToken token = expect(last, TokenType.LIT_NATURAL, TokenType.LIT_FLOAT, TokenType.SYM_MINUS);
            
            switch (token.getToken().getType()) {
            
            // litnat
                case LIT_NATURAL:
                    attrb.type(Type.NATURAL).value(Natural.parseNat(token.getLexeme()));
                break;
                
                // litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT).value(Float.valueOf(token.getLexeme()));
                break;
                
                // menos
                case SYM_MINUS:
                    // FLitNum
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
            
            // litnat
                case LIT_NATURAL:
                    attrb.type(Type.INTEGER).value(Natural.parseNat(token.getLexeme()));
                break;
                
                // litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT).value(Natural.parseNat(token.getLexeme()));
                break;
            }
            return attrb.create();
        } catch (NoSuchElementException e) {
            return null;
        }
        
    }
    
    @Override
    public void close () throws IOException {
        lexer.close();
    }
}
