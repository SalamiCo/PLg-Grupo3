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
import plg.gr3.data.BooleanValue;
import plg.gr3.data.CharacterValue;
import plg.gr3.data.FloatValue;
import plg.gr3.data.IntegerValue;
import plg.gr3.data.NaturalValue;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.data.Value;
import plg.gr3.debug.Debugger;
import plg.gr3.errors.compile.AssignationToConstantError;
import plg.gr3.errors.compile.AssignationTypeError;
import plg.gr3.errors.compile.CastingError;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.errors.compile.OperatorError;
import plg.gr3.errors.compile.UndefinedIdentifierError;
import plg.gr3.errors.compile.UnexpectedTokenError;
import plg.gr3.errors.compile.UnrecognizedTokenError;
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
    
    /** Token no reconocido leído */
    private String unrecognizedToken;
    
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
        
        // Si sigue siendo null, es que no ha conseguido nada reconocible
        if (token == null) {
            for (TokenType category : categories) {
                expected.add(category);
            }
            unrecognizedToken = lexer.nextStringToken();
            
        } else {
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
    
    /** @return Tabla de símbolos obtenida */
    
    public SymbolTable getSymbolTable () {
        return symbolTable;
    }
    
    /**
     * @return <tt>true</tt> si se ha reconocido el lenguaje sin errores, <tt>false</tt> en caso contratrio
     * @throws IOException si ocurre un error de E/S
     */
    public boolean parse () throws IOException {
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
            codeWriter.inhibit();
            
            if (token != null) {
                errors.add(new UnexpectedTokenError(token, expected));
                
            } else if (unrecognizedToken != null) {
                errors.add(new UnrecognizedTokenError(unrecognizedToken, lexer.getLine(), lexer.getColumn(), expected));
            }
        }
        
        for (CompileError error : errors) {
            error.print();
        }
        
        return errors.isEmpty();
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
            Attributes attrSDecs = parseSDecs(true, Attributes.DEFAULT);
            if (attrSDecs == null) {
                return null;
            }
            
            Attributes attrSInsts = parseSInsts(true, Attributes.DEFAULT);
            if (attrSInsts == null) {
                return null;
            }
            
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
            if (parseDecs(last, Attributes.DEFAULT) == null) {
                return null;
            }
            
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
                return Attributes.DEFAULT;
            }
            
            // Se supone que en esta produccion se crea la tabla de símbolos. Así que no me tengo que preocupar si el
            // identificador ya ha sido declarado porque es el 1º
            
            // RDecs
            symbolTable.putIdentifier(
                attrDec.getIdentifier(), attrDec.getType(), attrDec.getConstant(), attrDec.getAddress(),
                attrDec.getValue());
            
            Attributes attrInhDecs = new Attributes.Builder().address(attrDec.getAddress()).create();
            parseRDecs(last, attrInhDecs);
            
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
            Attributes attrInhDec = new Attributes.Builder().address(attr.getAddress()).create();
            Attributes attrDec = parseDec(last, attrInhDec);
            if (attrDec == null) {
                return null;
            }
            
            if (symbolTable.hasIdentifier(attrDec.getIdentifier())) {
                DuplicateIdentifierError error =
                    new DuplicateIdentifierError(attrDec.getIdentifier(), lexer.getLine(), lexer.getColumn());
                errors.add(error);
                codeWriter.inhibit();
                
            }
            // RDecs
            symbolTable.putIdentifier(
                attrDec.getIdentifier(), attrDec.getType(), attrDec.getConstant(), attrDec.getAddress(),
                attrDec.getValue());
            
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
                    if (attrType == null) {
                        return null;
                    }
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
            return null;
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
            if (parseInsts(last, Attributes.DEFAULT) == null) {
                return null;
            }
            
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
            if (parseInst(last, Attributes.DEFAULT) == null) {
                return Attributes.DEFAULT;
            }
            
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
            if (attrInst == null) {
                return null;
            }
            
            // RInsts1.codh = RInsts0.codh || Inst.cod
            codeWriter.write(attrInst.getInstructions());
            
            // RInsts
            parseRInsts(last, Attributes.DEFAULT);
            
        } catch (NoSuchElementException exc) {
            return null;
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
                    
                    expect(last, TokenType.SYM_ASIGNATION);
                    
                    // Comprobamos que el identificador existe
                    if (!symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                        UndefinedIdentifierError errorIDExist =
                            new UndefinedIdentifierError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        errors.add(errorIDExist);
                        codeWriter.inhibit();
                        
                        // Comprobamos que no estamos asignando la expresion a una constante
                    } else if (symbolTable.isIdentifierConstant(tokenRead.getLexeme())) {
                        AssignationToConstantError errorConstant =
                            new AssignationToConstantError(tokenRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        errors.add(errorConstant);
                        codeWriter.inhibit();
                    }
                    
                    Type asigType = symbolTable.getIdentfierType(tokenRead.getLexeme());
                    Attributes attrInhExpr = new Attributes.Builder().asignationType(asigType).create();
                    Attributes exprAttributes = parseExpr(last, attrInhExpr);
                    if (exprAttributes == null) {
                        return null;
                    }
                    
                    /*
                     * Comprobamos que el tipo de la expresion y del identificador son compatibles para la asignacion
                     */
                    Type identType = symbolTable.getIdentfierType(tokenRead.getLexeme());
                    Type exprType = exprAttributes.getType();
                    if (exprType == null) {
                        return null;
                    }
                    
                    if (!Type.assignable(identType, exprType)) {
                        AssignationTypeError error = new AssignationTypeError(exprType, identType, tokenRead);
                        errors.add(error);
                        codeWriter.inhibit();
                    }
                    
                    // Inst.cod = Expr.cod || desapila-dir(Inst.tsh[ident.lex].dir) }
                    if (!codeWriter.isInhibited()) {
                        codeWriter.write(exprAttributes.getInstructions());
                        codeWriter.write(new StoreInstruction(symbolTable.getIdentifierAddress(tokenRead.getLexeme())));
                    }
                
                break;
                
                // in lpar ident rpar
                case RW_IN:
                    
                    expect(last, TokenType.SYM_PAR_LEFT);
                    LocatedToken identRead = expect(last, TokenType.IDENTIFIER);
                    
                    /* Comprobamos que el identificador existe */
                    if (!this.symbolTable.hasIdentifier(identRead.getLexeme())) {
                        UndefinedIdentifierError error =
                            new UndefinedIdentifierError(identRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        errors.add(error);
                        
                    } else if (this.symbolTable.isIdentifierConstant(identRead.getLexeme())) {
                        AssignationToConstantError error =
                            new AssignationToConstantError(identRead.getLexeme(), lexer.getLine(), lexer.getColumn());
                        errors.add(error);
                    }
                    
                    // Inst.cod = in(Inst.tsh[ident.lex].type) || desapila-dir(Inst.tsh[ident.lex].dir) }
                    if (!codeWriter.isInhibited()) {
                        codeWriter.write(new InputInstruction(symbolTable.getIdentfierType(identRead.getLexeme())));
                        codeWriter.write(new StoreInstruction(symbolTable.getIdentifierAddress(identRead.getLexeme())));
                    }
                    
                    expect(last, TokenType.SYM_PAR_RIGHT);
                
                break;
                
                // out lpar Expr rpar
                case RW_OUT:
                    expect(last, TokenType.SYM_PAR_LEFT);
                    Attributes attrExpr = parseExpr(last, Attributes.DEFAULT);
                    if (attrExpr == null) {
                        return null;
                    }
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
            Attributes attrTerm = parseTerm(last, attr);
            if (attrTerm == null) {
                return null;
            }
            
            Attributes attrInhFExpr =
                new Attributes.Builder().type(attrTerm.getType()).asignationType(attr.getAsigType()).create();
            Attributes attrFExpr = parseFExpr(last, attrInhFExpr);
            if (attrFExpr == null) {
                return null;
            }
            
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
            Attributes attrOp0 = parseOp0(last, attr);
            if (attrOp0 != null) {
                Attributes attrTerm = parseTerm(last, attrOp0);
                if (attrTerm == null) {
                    return null;
                }
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = attrOp0.getOperator(BinaryOperator.class);
                
                if (!op.canApply(attr.getType(), attrTerm.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrTerm.getType(), attrOp0.getOperator(), actLine, actColumn);
                    errors.add(error);
                }
                
                // FExpr.cod = Term.cod || Op0.op }
                codeWriter.write(attrTerm.getInstructions());
                codeWriter.write(new BinaryOperatorInstruction(op));
                
                attrb.type(attrTerm.getType());
            } else {
                attrb.type(attr.getType());
            }
            
        } catch (NoSuchElementException e) {
            return null;
        }
        return attrb.create();
    }
    
    private Attributes parseTerm (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            Attributes attrFact = parseFact(last, attr);
            if (attrFact == null) {
                return null;
            }
            
            Attributes attrInhRTerm =
                new Attributes.Builder().type(attrFact.getType()).asignationType(attr.getAsigType()).create();
            Attributes attrRTerm = parseRTerm(true, attrInhRTerm);
            if (attrRTerm == null) {
                return null;
            }
            
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
            Attributes attrOp1 = parseOp1(last, attr);
            if (attrOp1 != null) {
                Attributes attrInhFact =
                    new Attributes.Builder().type(attrOp1.getType()).asignationType(attr.getAsigType()).create();
                Attributes attrFact = parseFact(last, attrInhFact);
                if (attrFact != null) {
                    Type t = attrOp1.getOperator(BinaryOperator.class).getApplyType(attrFact.getType(), attr.getType());
                    Attributes attrInhRTerm =
                        new Attributes.Builder().type(t).asignationType(attr.getAsigType()).create();
                    Attributes attrRTerm = parseRTerm(last, attrInhRTerm);
                    if (attrRTerm == null) {
                        return null;
                    }
                    
                    // Comprobamos que podamos aplicar el operador (que los tipos casen)
                    BinaryOperator op = (BinaryOperator) attrOp1.getOperator();
                    
                    if (!op.canApply(attr.getType(), attrRTerm.getType())) {
                        OperatorError error =
                            new OperatorError(
                                attr.getType(), attrRTerm.getType(), attrOp1.getOperator(), actLine, actColumn);
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
            return null;
        }
        
        return attrb.create();
    }
    
    // Fact
    private Attributes parseFact (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Fact ::=
        try {
            // Shft
            Attributes shftSynAttr = parseShft(true, attr);
            // Rfact
            if (shftSynAttr == null) {
                return null;
            }
            Attributes rfactInhAttr =
                new Attributes.Builder().type(shftSynAttr.getType()).asignationType(attr.getAsigType()).create();
            Attributes rfactSynAttr = parseRFact(true, rfactInhAttr);
            
            attrb.type(rfactSynAttr.getType());
        } catch (NoSuchElementException exc) {
            return null;
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
            Attributes attrOp2 = parseOp2(true, attr);
            
            if (attrOp2 != null) {
                // Shft
                Attributes attrInhShft =
                    new Attributes.Builder().type(attrOp2.getType()).asignationType(attr.getAsigType()).create();
                Attributes attrShft = parseShft(last, attrInhShft);
                
                if (attrShft == null) {
                    return null;
                }
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = attrOp2.getOperator(BinaryOperator.class);
                
                if (!op.canApply(attr.getType(), attrShft.getType())) {
                    OperatorError error =
                        new OperatorError(attr.getType(), attrShft.getType(), attrOp2.getOperator(), actLine, actColumn);
                    errors.add(error);
                }
                
                if (attrShft != null) {
                    Type t = attrOp2.getOperator(BinaryOperator.class).getApplyType(attrShft.getType(), attr.getType());
                    Attributes attrInhRFact =
                        new Attributes.Builder().type(t).asignationType(attr.getAsigType()).create();
                    Attributes attrRFact = parseRFact(last, attrInhRFact);
                    
                    // RFact1.codh = RFact0.codh || Shft.cod || Op2.op }
                    codeWriter.write(attrShft.getInstructions());
                    codeWriter.write(new BinaryOperatorInstruction(op));
                    
                    attrb.type(attrRFact.getType());
                }
            } else {
                // Epsilon
                attrb.type(attr.getType());
            }
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    // Shft
    private Attributes parseShft (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        // Shft ::=
        try {
            // Unary
            Attributes unarySynAttr = parseUnary(true, attr);
            if (unarySynAttr == null) {
                return null;
            }
            
            // FShft
            Attributes fshftInhAttr =
                new Attributes.Builder().type(unarySynAttr.getType()).asignationType(attr.getAsigType()).create();
            Attributes fshftSynAttr = parseFShft(true, fshftInhAttr);
            if (fshftSynAttr == null) {
                return null;
            }
            
            attrb.type(fshftSynAttr.getType());
            
            // Shft.cod = Unary.cod || FShft.cod
            
            codeWriter.write(unarySynAttr.getInstructions());
            codeWriter.write(fshftSynAttr.getInstructions());
            
        } catch (NoSuchElementException exc) {
            return null;
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
            Attributes op3SynAttr = parseOp3(true, attr);
            
            if (op3SynAttr != null) {
                // Shft
                Attributes shftSynAttr = parseShft(true, attr);
                if (shftSynAttr == null) {
                    return null;
                }
                
                // Comprobamos que podamos aplicar el operador (que los tipos casen)
                BinaryOperator op = (BinaryOperator) op3SynAttr.getOperator();
                
                if (!op.canApply(attr.getType(), shftSynAttr.getType())) {
                    OperatorError error =
                        new OperatorError(
                            attr.getType(), shftSynAttr.getType(), op3SynAttr.getOperator(), actLine, actColumn);
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
            return null;
        }
        
        return attrb.create();
    }
    
    // XXX Inicio de la puta mierda
    
    private Attributes parseUnary (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int col = lexer.getColumn();
        int line = lexer.getLine();
        
        // Unary ::=
        try {
            // Op4
            Attributes attrOp4 = parseOp4(last, attr);
            
            if (attrOp4 != null) {
                // Unary
                Attributes attrUnary = parseUnary(last, attr);
                if (attrUnary == null) {
                    return null;
                }
                
                // Comprobamos que el operador unario se puede aplicar (que los tipos casan)
                UnaryOperator op = (UnaryOperator) attrOp4.getOperator();
                
                if (!op.canApply(attrUnary.getType())) {
                    OperatorError error = new OperatorError(attrUnary.getType(), attrOp4.getOperator(), line, col);
                    errors.add(error);
                }
                
                // Unary0.cod = Unary1.cod || Op4.op
                codeWriter.write(new UnaryOperatorInstruction(op)); // FIXME
                
                attrb.type(attrUnary.getType());
            } else {
                
                Attributes attrParen2 = parseParen2(last, attr);
                if (attrParen2 != null) {
                    attrb.type(attrParen2.getType());
                    
                } else {
                    expect(last, TokenType.SYM_PAR_LEFT);
                    Attributes attrFUnary = parseFUnary(last, attr);
                    if (attrFUnary == null) {
                        return null;
                    }
                    
                    attrb.type(attrFUnary.getType());
                }
                
            }
            return attrb.create();
            
        } catch (NoSuchElementException exc) {
            return null;
        }
    }
    
    private Attributes parseFUnary (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int col = lexer.getColumn();
        int line = lexer.getLine();
        
        try {
            Attributes attrFParen = parseFParen(last, attr);
            if (attrFParen != null) {
                attrb.type(attrFParen.getType());
                
            } else {
                Attributes attrCast = parseCast(last, attr);
                if (attrCast == null) {
                    return null;
                }
                
                expect(last, TokenType.SYM_PAR_RIGHT);
                
                Attributes attrParen = parseParen(last, attr);
                if (attrParen == null) {
                    return null;
                }
                
                if (!Type.canCast(attrCast.getType(), attrParen.getType())) {
                    CastingError error = new CastingError(attrCast.getType(), attrParen.getType(), line, col);
                    errors.add(error);
                }
                
                attrb.type(attrCast.getType());
                
                codeWriter.write(attrCast.getInstructions());
                codeWriter.write(new CastInstruction(attrCast.getType()));
            }
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseFParen (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            Attributes attrExpr = parseExpr(last, attr);
            if (attrExpr == null) {
                return null;
            }
            
            expect(last, TokenType.SYM_PAR_RIGHT);
            
            attrb.type(attrExpr.getType());
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseParen (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        
        try {
            
            Attributes attrParen2 = parseParen2(last, attr);
            
            if (attrParen2 != null) {
                attrb.type(attrParen2.getType());
            } else {
                expect(last, TokenType.SYM_PAR_LEFT);
                Attributes attrFParen = parseFParen(last, attr);
                if (attrFParen == null) {
                    return null;
                }
                attrb.type(attrFParen.getType());
                
            }
            
        } catch (NoSuchElementException exc) {
            return null;
        }
        
        return attrb.create();
    }
    
    private Attributes parseParen2 (boolean last, Attributes attr) throws IOException {
        Attributes.Builder attrb = new Attributes.Builder();
        int line = lexer.getLine();
        int column = lexer.getColumn();
        
        // Paren ::=
        try {
            // Lit
            Attributes litAttributes = parseLit(last, attr);
            
            if (litAttributes == null) {
                LocatedToken tokenRead = expect(last, TokenType.IDENTIFIER);
                
                /* Comprobamos que el identificador existe */
                if (!symbolTable.hasIdentifier(tokenRead.getLexeme())) {
                    UndefinedIdentifierError error = new UndefinedIdentifierError(tokenRead.getLexeme(), line, column);
                    errors.add(error);
                    
                    attrb.type(Type.ERROR);
                } else {
                    attrb.type(symbolTable.getIdentfierType(tokenRead.getLexeme()));
                }
                
                /*
                 * Paren.cod = Si Paren.tsh[ident.lex].const = true apila(Paren.tsh[ident.lex].value) Si no
                 * apila-dir(Paren.tsh[ident.lex].dir)
                 */
                if (!codeWriter.isInhibited()) {
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
                
            } else {
                attrb.type(litAttributes.getType());
                // Paren.cod = apilar(Lit.value)
                PushInstruction inst = new PushInstruction(litAttributes.getValue());
                codeWriter.write(inst);
            }
            
        } catch (NoSuchElementException exc) {
            return null;
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
            LocatedToken readToken =
                expect(last, TokenType.RW_AND, TokenType.SYM_MODULO, TokenType.SYM_DIV, TokenType.SYM_MULT);
            switch (readToken.getType()) {
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
            Attributes attrSynLitBool = parseLitBool(last, attr);
            if (attrSynLitBool != null) {
                attrb.type(attrSynLitBool.getType()).value(attrSynLitBool.getValue());
                return attrb.create();
            }
            
            Attributes attrSynLitNum = parseLitNum(last, attr);
            if (attrSynLitNum != null) {
                attrb.type(attrSynLitNum.getType()).value(attrSynLitNum.getValue());
                return attrb.create();
            }
            
            LocatedToken token = expect(last, TokenType.LIT_CHARACTER);
            attrb.type(Type.CHARACTER).value(CharacterValue.valueOf(token.getLexeme()));
            
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
                    attrb.value(BooleanValue.TRUE);
                break;
                
                // false
                case RW_FALSE:
                    attrb.type(Type.BOOLEAN);
                    attrb.value(BooleanValue.FALSE);
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
            
            Value val = null;
            switch (token.getToken().getType()) {
            
            // litnat
                case LIT_NATURAL:
                    attrb.type(Type.NATURAL);
                    val = IntegerValue.valueOf(token.getLexeme());
                break;
                
                // litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT);
                    val = FloatValue.valueOf(token.getLexeme());
                break;
                
                // menos
                case SYM_MINUS:
                    // FLitNum
                    Attributes attrFLitNum = parseFLitNum(last, Attributes.DEFAULT);
                    attrb.type(attrFLitNum.getType());
                    val = UnaryOperator.MINUS.apply(attrFLitNum.getValue());
                break;
            }
            
            // Convert the value
            Type asigType = attr.getAsigType();
            if (asigType != null && asigType.isNumeric()) {
                if (asigType == Type.NATURAL) {
                    val = val.toNaturalValue();
                    
                } else if (asigType == Type.INTEGER) {
                    val = val.toIntegerValue();
                    
                } else if (asigType == Type.FLOAT) {
                    val = val.toFloatValue();
                }
            }
            
            return attrb.value(val).create();
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
                    attrb.type(Type.INTEGER).value(NaturalValue.valueOf(token.getLexeme()));
                break;
                
                // litfloat
                case LIT_FLOAT:
                    attrb.type(Type.FLOAT).value(NaturalValue.valueOf(token.getLexeme()));
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
