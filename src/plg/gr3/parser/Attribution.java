package plg.gr3.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import plg.gr3.data.ArrayType;
import plg.gr3.data.BinaryOperator;
import plg.gr3.data.BooleanValue;
import plg.gr3.data.CharacterValue;
import plg.gr3.data.FloatValue;
import plg.gr3.data.IntegerValue;
import plg.gr3.data.NaturalValue;
import plg.gr3.data.TupleType;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.data.Value;
import plg.gr3.errors.compile.AssignationTypeError;
import plg.gr3.errors.compile.BadIdentifierClassError;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.errors.compile.ExpectedDesignator;
import plg.gr3.errors.compile.MismatchNumberOfParameters;
import plg.gr3.errors.compile.OperatorError;
import plg.gr3.errors.compile.UndefinedIdentifierError;
import plg.gr3.parser.semfun.AndFun;
import plg.gr3.parser.semfun.AsignationFun;
import plg.gr3.parser.semfun.CheckDuplicateIdentifierFun;
import plg.gr3.parser.semfun.ConcatCodeFun;
import plg.gr3.parser.semfun.ConcatErrorsFun;
import plg.gr3.parser.semfun.IncrementFun;
import plg.gr3.vm.instr.BinaryOperatorInstruction;
import plg.gr3.vm.instr.BranchInstruction;
import plg.gr3.vm.instr.DropInstruction;
import plg.gr3.vm.instr.IndirectLoadInstruction;
import plg.gr3.vm.instr.IndirectStoreInstruction;
import plg.gr3.vm.instr.InputInstruction;
import plg.gr3.vm.instr.Instruction;
import plg.gr3.vm.instr.JumpInstruction;
import plg.gr3.vm.instr.LoadInstruction;
import plg.gr3.vm.instr.OutputInstruction;
import plg.gr3.vm.instr.PushInstruction;
import plg.gr3.vm.instr.ReturnInstruction;
import plg.gr3.vm.instr.StopInstruction;
import plg.gr3.vm.instr.StoreInstruction;
import plg.gr3.vm.instr.Swap1Instruction;
import plg.gr3.vm.instr.Swap2Instruction;
import es.ucm.fdi.plg.evlib.Atribucion;
import es.ucm.fdi.plg.evlib.Atributo;
import es.ucm.fdi.plg.evlib.LAtributo;
import es.ucm.fdi.plg.evlib.SemFun;
import es.ucm.fdi.plg.evlib.TAtributos;

/**
 * Clase de los atributos del proyecto.
 * 
 * @author PLg Grupo 03 2012/2013
 */
@SuppressWarnings("javadoc")
public final class Attribution extends Atribucion {

    /**
     * @param obj
     * @return Atributo generado al vuelo
     */
    private static Atributo a (Object obj) {
        return new LAtributo("", obj);
    }

    // Program

    public TAtributos program_R1 (
        TAtributos sConsts, TAtributos sTypes, TAtributos sVars, TAtributos sSubprogs, TAtributos sInsts)
    {
        regla("Program -> PROGRAM IDENT ILLAVE SConsts STypes SVars SSubprogs SInsts FLLAVE");
        TAtributos attr = atributosPara("Program", "etqh", "tsh", "err", "cod", "dirh", "ts");

        // Program.tsh
        calculo(attr.a("tsh"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                return new SymbolTable();
            }
        });

        // Program.err
        dependencias(
            attr.a("err"), sConsts.a("err"), sTypes.a("err"), sVars.a("err"), sSubprogs.a("err"), sInsts.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        // Program.cod
        dependencias(attr.a("cod"), sSubprogs.a("etq"), sSubprogs.a("cod"), sInsts.a("cod"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(
                    a(new JumpInstruction((Integer) (attrs[0].valor()))), attrs[1], attrs[2], a(new StopInstruction()));
            }
        });

        // SConsts.tsh
        dependencias(sConsts.a("tsh"), attr.a("tsh"));
        calculo(sConsts.a("tsh"), AsignationFun.INSTANCE);

        // STypes.tsh
        dependencias(sTypes.a("tsh"), sConsts.a("ts"));
        calculo(sTypes.a("tsh"), AsignationFun.INSTANCE);

        // SVars.tsh
        dependencias(sVars.a("tsh"), sTypes.a("ts"));
        calculo(sVars.a("tsh"), AsignationFun.INSTANCE);

        // SSubprogs.tsh
        dependencias(sSubprogs.a("tsh"), sVars.a("ts"));
        calculo(sSubprogs.a("tsh"), AsignationFun.INSTANCE);

        // SInsts.tsh
        dependencias(sInsts.a("tsh"), sSubprogs.a("ts"));
        calculo(sInsts.a("tsh"), AsignationFun.INSTANCE);

        // Program.ts
        dependencias(attr.a("ts"), sSubprogs.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        // SSubprogs.etqh
        dependencias(sSubprogs.a("etqh"), a(1));
        calculo(sSubprogs.a("etqh"), AsignationFun.INSTANCE);

        // SInsts.etqh
        dependencias(sInsts.a("etqh"), sSubprogs.a("etq"));
        calculo(sInsts.a("etqh"), AsignationFun.INSTANCE);

        // Program.dirh = 2
        dependencias(attr.a("dirh"), a(2));
        calculo(attr.a("dirh"), AsignationFun.INSTANCE);

        dependencias(sVars.a("dirh"), attr.a("dirh"));
        calculo(sVars.a("dirh"), AsignationFun.INSTANCE);

//        dependencias(sSubprogs.a("dirh"), sVars.a("dir"));
//        calculo(sSubprogs.a("dirh"), AsignationFun.INSTANCE);

//        dependencias(sInsts.a("dirh"), sSubprogs.a("dir"));
//        calculo(sInsts.a("dirh"), AsignationFun.INSTANCE);

        // Program.err
        dependencias(
            attr.a("err"), sConsts.a("err"), sTypes.a("err"), sVars.a("err"), sSubprogs.a("err"), sInsts.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // SConsts

    public TAtributos sConsts_R1 (TAtributos consts) {
        regla("SConst -> CONSTS ILLAVE Consts FLLAVE");
        TAtributos attr = atributosPara("SConst", "tsh", "ts", "err");

        dependencias(consts.a("tsh"), attr.a("tsh"));
        calculo(consts.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), consts.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), consts.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos sConsts_R2 () {
        regla("SConst -> $");
        TAtributos attr = atributosPara("SConst", "ts", "err", "etqh");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // Consts

    public TAtributos consts_R1 (TAtributos consts_1, TAtributos cons) {
        regla("Consts -> Consts PYC Const");
        TAtributos attr = atributosPara("Consts", "tsh", "ts", "err");

        dependencias(consts_1.a("tsh"), attr.a("tsh"));
        calculo(consts_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(cons.a("tsh"), consts_1.a("ts"));
        calculo(cons.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), cons.a("ts"), cons.a("id"), cons.a("valor"), cons.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {

                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Value value = (Value) args[2].valor();
                Type type = (Type) args[3].valor();

                if (ident != null) {
                    table.putConstant(ident.getLexeme(), type, value);
                }

                return table;
            }
        });

        dependencias(attr.a("err"), cons.a("ts"), cons.a("id"));
        calculo(attr.a("err"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable st = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                if (ident != null && st.hasIdentifier(ident.getLexeme())) {
                    return new DuplicateIdentifierError(ident.getLexeme(), ident.getLine(), ident.getColumn());
                } else {
                    return null;
                }
            }
        });

        return attr;
    }

    public TAtributos consts_R2 (TAtributos cons) {
        regla("Consts -> Const");
        TAtributos attr = atributosPara("Consts", "tsh", "ts", "err");

        dependencias(cons.a("tsh"), attr.a("tsh"));
        calculo(cons.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), cons.a("ts"), cons.a("id"), cons.a("valor"), cons.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Value value = (Value) args[2].valor();
                Type type = (Type) args[3].valor();

                if (ident != null) {
                    table.putConstant(ident.getLexeme(), type, value);
                }

                return table;
            }
        });

        dependencias(attr.a("err"), cons.a("ts"), cons.a("id"), a(Scope.GLOBAL));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Const

    public TAtributos const_R1 (TAtributos tPrim, Lexeme ident, TAtributos constLit) {
        regla("Const -> CONST TPrim IDENT ASIG ConstLit");
        TAtributos attr = atributosPara("Const", "tsh", "ts", "id", "tipo", "err", "valor");
        LAtributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), tPrim.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), constLit.a("valor"));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        // Const.err = ¬(compatibles(TPrim.tipo, ConstLit.tipo))
        dependencias(attr.a("err"), tPrim.a("tipo"), constLit.a("tipo"), lexIdent);
        calculo(attr.a("err"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Type left = (Type) args[0].valor();
                Type right = (Type) args[1].valor();
                Lexeme lex = (Lexeme) args[2].valor();

                if (!left.compatible(right)) {
                    return new AssignationTypeError(left, right, lex);
                }

                return null;
            }
        });

        return attr;
    }

    public TAtributos const_R2 () {
        regla("Const -> $");
        TAtributos attr = atributosPara("Const", "ts", "tsh", "id", "err", "dir", "dirh", "valor", "tipo");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // ConstLit

    public TAtributos constLit_R1 (TAtributos lit) {
        regla("ConstLit -> Lit");
        TAtributos attr = atributosPara("ConstLit", "valor", "tipo", "err");

        dependencias(attr.a("valor"), lit.a("valor"));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), lit.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos constLit_R2 (TAtributos lit) {
        regla("ConstLit -> MENOS Lit");
        TAtributos attr = atributosPara("ConstLit", "tipo", "valor", "err");

        dependencias(attr.a("tipo"), lit.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), lit.a("valor"), lit.a("tipo"));
        calculo(attr.a("valor"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Value value = (Value) args[0].valor();
                Type type = (Type) args[1].valor();

                return type.isNumeric() ? UnaryOperator.MINUS.apply(value) : null;
            }
        });

        dependencias(attr.a("err"), lit.a("tipo"));
        calculo(attr.a("err"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();

                return type.isNumeric() ? null : new OperatorError(type, UnaryOperator.MINUS, -1, -1);
            }
        });

        return attr;
    }

    // STypes

    public TAtributos sTypes_R1 (TAtributos types) {
        regla("STypes -> TIPOS ILLAVE Types FLLAVE");
        TAtributos attr = atributosPara("STypes", "tsh", "dirh", "ts", "dir", "err");

        dependencias(attr.a("tsh"), types.a("tsh"));
        calculo(attr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(types.a("tsh"), attr.a("tsh"));
        calculo(types.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), types.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), types.a("dir"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), types.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos sTypes_R2 () {
        regla("STypes -> $");
        TAtributos attr = atributosPara("STypes", "tsh", "ts", "dir", "dirh", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // Types

    public TAtributos types_R1 (TAtributos types_1, TAtributos type) {
        regla("Types -> Types PYC Type");
        TAtributos attr = atributosPara("Types", "tsh", "ts", "dir", "dirh", "tipo", "err");

        dependencias(types_1.a("tsh"), attr.a("tsh"));
        calculo(types_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(types_1.a("dirh"), attr.a("dirh"));
        calculo(types_1.a("dirh"), AsignationFun.INSTANCE);

        dependencias(type.a("tsh"), attr.a("ts"));
        calculo(type.a("tsh"), AsignationFun.INSTANCE);

        dependencias(type.a("dirh"), types_1.a("dir"));
        calculo(type.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), type.a("dir"), type.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(attr.a("ts"), types_1.a("ts"), type.a("id"), type.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Type type = (Type) args[2].valor();

                if (ident != null) {
                    ts.putType(ident.getLexeme(), type);
                }

                return ts;
            }
        });

        dependencias(attr.a("err"), types_1.a("ts"), type.a("id"), a(Scope.GLOBAL));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    public TAtributos types_R2 (TAtributos type) {
        regla("Types -> Type");
        TAtributos attr = atributosPara("Types", "tsh", "dirh", "ts", "dir", "tipo", "err");

        dependencias(type.a("tsh"), attr.a("tsh"));

        dependencias(attr.a("ts"), type.a("ts"), type.a("id"), type.a("tipo"));
        dependencias(type.a("err"), type.a("ts"), type.a("id"));

        calculo(type.a("tsh"), AsignationFun.INSTANCE);

        dependencias(type.a("dirh"), attr.a("dirh"));
        calculo(type.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), type.a("dir"), type.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(attr.a("ts"), type.a("ts"), type.a("id"), type.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Type type = (Type) args[2].valor();

                if (ident != null) {
                    ts.putType(ident.getLexeme(), type);
                }

                return ts;
            }
        });

        dependencias(attr.a("dir"), type.a("dir"), type.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(type.a("err"), type.a("ts"), type.a("id"), a(Scope.GLOBAL));
        calculo(type.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Type

    public TAtributos type_R1 (TAtributos typeDesc, Lexeme ident) {
        regla("Type -> TIPO TypeDesc IDENT");
        TAtributos attr =
            atributosPara("Type", "ts", "tsh", "id", "dir", "dirh", "id", "clase", "nivel", "tipo", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        Atributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);
        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);
        // Type.tipo = <t:TypeDesc.tipo, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc),
        // Type.id)>

        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos type_R2 () {
        regla("Type -> $");
        TAtributos attr = atributosPara("Type", "ts", "tsh", "id", "dir", "dirh", "err", "tipo", "clase", "nivel");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // SVars

    public TAtributos sVars_R1 (TAtributos vars) {
        regla("SVars -> VARS ILLAVE Vars FLLAVE");
        TAtributos attr = atributosPara("SVars", "tsh", "ts", "id", "dirh", "dir", "err");

        dependencias(vars.a("tsh"), attr.a("tsh"));
        calculo(vars.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), vars.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(vars.a("dirh"), attr.a("dirh"));
        calculo(vars.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), vars.a("dir"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), vars.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos sVars_R2 () {
        regla("SVars -> $");
        TAtributos attr = atributosPara("SVars", "ts", "tsh", "dir", "dirh", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // Vars

    public TAtributos vars_R1 (TAtributos vars_1, TAtributos var) {
        regla("Vars -> Vars PYC Var");
        TAtributos attr = atributosPara("Vars", "tsh", "ts", "err", "dir", "dirh");

        dependencias(vars_1.a("tsh"), attr.a("tsh"));
        calculo(vars_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(var.a("tsh"), vars_1.a("ts"));
        calculo(var.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), var.a("ts"), var.a("id"), var.a("nivel"), attr.a("dir"), var.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Scope scope = (Scope) args[2].valor();
                int address = (int) args[3].valor();
                Type type = (Type) args[4].valor();

                if (ident != null) {
                    ts.putVariable(ident.getLexeme(), scope, address, type);
                }

                return ts;
            }
        });

        dependencias(vars_1.a("dirh"), attr.a("dirh"));
        calculo(vars_1.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), vars_1.a("dir"), var.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                if (type != null) {
                    return varDir + type.getSize();
                } else {
                    return varDir;
                }
            }
        });

        dependencias(attr.a("err"), var.a("ts"), var.a("id"), var.a("nivel"));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    public TAtributos vars_R2 (TAtributos var) {
        regla("Vars -> Var");
        TAtributos attr = atributosPara("Vars", "tsh", "ts", "err", "dir", "dirh");

        dependencias(var.a("tsh"), attr.a("tsh"));
        calculo(var.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"), var.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(attr.a("ts"), var.a("ts"), var.a("id"), var.a("nivel"), attr.a("dirh"), var.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Scope scope = (Scope) args[2].valor();
                int address = (int) args[3].valor();
                Type type = (Type) args[4].valor();

                if (ident != null) {
                    ts.putVariable(ident.getLexeme(), scope, address, type);
                }

                return ts;
            }
        });

        dependencias(attr.a("err"), var.a("ts"), var.a("id"), var.a("nivel"));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Var

    public TAtributos var_R1 (TAtributos typeDesc, Lexeme ident) {
        regla("Var -> VAR TypeDesc IDENT");
        TAtributos attr = atributosPara("Var", "ts", "tsh", "id", "nivel", "tipo");
        Atributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), AsignationFun.INSTANCE);

        dependencias(attr.a("nivel"), a(Scope.GLOBAL));
        calculo(attr.a("nivel"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);
        // Var.tipo = (si (TypeDesc.tipo == TPrim) {<t:TypeDesc.tipo, tam:1>}
        // si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

        return attr;
    }

    public TAtributos var_R2 () {
        regla("Var -> $");
        TAtributos attr = atributosPara("Var", "ts", "tsh", "err", "id", "nivel", "tipo");
        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // TypeDesc

    public TAtributos typeDesc_R1 (TAtributos tPrim) {
        regla("TypeDesc -> TPrim");
        TAtributos attr = atributosPara("TypeDesc", "tipo", "tsh", "err");

        dependencias(attr.a("tipo"), tPrim.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos typeDesc_R2 (TAtributos tArray) {
        regla("TypeDesc -> TArray");
        TAtributos attr = atributosPara("TypeDesc", "tipo", "tsh", "err");

        dependencias(attr.a("tipo"), tArray.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(tArray.a("tsh"), attr.a("tsh"));
        calculo(tArray.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), tArray.a("err"));
        calculo(attr.a("err"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos typeDesc_R3 (TAtributos tTupla) {
        regla("TypeDesc -> TTupla");
        TAtributos attr = atributosPara("TypeDesc", "tipo", "tsh", "err");

        dependencias(attr.a("tipo"), tTupla.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(tTupla.a("tsh"), attr.a("tsh"));
        calculo(tTupla.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), tTupla.a("err"));
        calculo(attr.a("err"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos typeDesc_R4 (Lexeme ident) {
        regla("TypeDesc -> IDENT");
        TAtributos attr = atributosPara("TypeDesc", "tipo", "tsh", "err");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("tipo"), attr.a("tsh"), identLex);
        calculo(attr.a("tipo"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                if (table.hasIdentifier(ident.getLexeme())) {
                    return table.getIdentfierType(ident.getLexeme());
                }

                return Type.ERROR;
            }
        });

        dependencias(attr.a("err"), attr.a("tsh"), identLex);
        calculo(attr.a("err"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                if (table.hasIdentifier(ident.getLexeme())) {
                    return new UndefinedIdentifierError(ident.getLexeme(), ident.getLine(), ident.getColumn());
                }
                ClassDec cd = table.getIdentfierClassDec(ident.getLexeme());
                if (cd != ClassDec.TYPE) {
                    return (new BadIdentifierClassError(ident.getLexeme(), cd, ClassDec.TYPE, ident.getLine(), ident
                        .getColumn()));
                }
                return null;
            }
        });

        return attr;
    }

    // TPrim

    public TAtributos tPrim_R1 () {
        regla("TPrim -> NATURAL");
        TAtributos attr = atributosPara("TPrim", "tipo");

        dependencias(attr.a("tipo"), a(Type.NATURAL));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos tPrim_R2 () {
        regla("TPrim -> INTEGER");
        TAtributos attr = atributosPara("TPrim", "tipo");

        dependencias(attr.a("tipo"), a(Type.INTEGER));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos tPrim_R3 () {
        regla("TPrim -> FLOAT");
        TAtributos attr = atributosPara("TPrim", "tipo");

        dependencias(attr.a("tipo"), a(Type.FLOAT));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos tPrim_R4 () {
        regla("TPrim -> BOOLEAN");
        TAtributos attr = atributosPara("TPrim", "tipo");

        dependencias(attr.a("tipo"), a(Type.BOOLEAN));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos tPrim_R5 () {
        regla("TPrim -> CHARACTER");
        TAtributos attr = atributosPara("TPrim", "tipo");

        dependencias(attr.a("tipo"), a(Type.CHARACTER));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    // Cast

    public TAtributos cast_R1 () {
        regla("Cast -> CHAR");
        TAtributos attr = atributosPara("Cast", "tipo");

        dependencias(attr.a("tipo"), a(Type.CHARACTER));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos cast_R2 () {
        regla("Cast -> INT");
        TAtributos attr = atributosPara("Cast", "tipo");

        dependencias(attr.a("tipo"), a(Type.INTEGER));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos cast_R3 () {
        regla("Cast -> NAT");
        TAtributos attr = atributosPara("Cast", "tipo");

        dependencias(attr.a("tipo"), a(Type.NATURAL));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos cast_R4 () {
        regla("Cast -> FLOAT");
        TAtributos attr = atributosPara("Cast", "tipo");

        dependencias(attr.a("tipo"), a(Type.FLOAT));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        return attr;
    }

    // TArray

    public TAtributos tArray_R1 (TAtributos typeDesc, Lexeme ident) {
        regla("TArray -> TypeDesc ICORCHETE IDENT FCORCHETE");
        TAtributos attr = atributosPara("TArray", "tsh", "tipo", "err");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"), attr.a("tsh"), identLex);
        calculo(attr.a("tipo"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                SymbolTable table = (SymbolTable) args[1].valor();
                Lexeme ident = (Lexeme) args[2].valor();

                if (ident != null) {
                    String identStr = ident.getLexeme();
                    if (table.hasIdentifier(identStr) && table.getIdentfierClassDec(identStr) == ClassDec.CONSTANT
                        && table.getIdentfierType(identStr).compatible(Type.NATURAL))
                    {
                        NaturalValue val = table.getIdentifierValue(identStr, NaturalValue.class);

                        return new ArrayType(type, val.getValue());
                    }
                }

                return Type.ERROR;
            }
        });

        dependencias(attr.a("err"), attr.a("tsh"), identLex);
        calculo(attr.a("err"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                String identName = ident.getLexeme();
                if (!table.hasIdentifier(identName)) {
                    return new UndefinedIdentifierError(identName, ident.getLine(), ident.getColumn());
                }
                ClassDec cd = table.getIdentfierClassDec(identName);
                if (cd != ClassDec.CONSTANT) {
                    return new BadIdentifierClassError(identName, cd, ClassDec.CONSTANT, ident.getLine(), ident
                        .getColumn());
                }
                Type typeFound = table.getIdentfierType(identName);
                if (!typeFound.compatible(Type.NATURAL)) {
                    return new AssignationTypeError(typeFound, Type.NATURAL, ident);
                }
                return null;
            }
        });

        return attr;
    }

    public TAtributos tArray_R2 (TAtributos typeDesc, Lexeme litnat) {
        regla("TArray -> TypeDesc ICORCHETE LITNAT FCORCHETE");
        TAtributos attr = atributosPara("TArray", "tipo", "tsh");
        Atributo litnatLex = atributoLexicoPara("LITNAT", "lex", litnat);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), typeDesc.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"), litnatLex);
        calculo(attr.a("tipo"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                Lexeme litnat = (Lexeme) args[1].valor();

                return new ArrayType(type, Integer.parseInt(litnat.getLexeme(), 10));
            }
        });

        return attr;
    }

    // TTupla

    public TAtributos tTupla_R1 (TAtributos tupla) {
        regla("TTupla -> IPAR Tupla FPAR");
        TAtributos attr = atributosPara("TTupla", "tipo", "tsh", "err");

        dependencias(tupla.a("tsh"), attr.a("tsh"));
        calculo(tupla.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), tupla.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), tupla.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos tTupla_R2 () {
        regla("TTupla -> IPAR FPAR");
        TAtributos attr = atributosPara("TTupla", "tipo", "err");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // Tupla

    public TAtributos tupla_R1 (TAtributos typeDesc, TAtributos tupla_1) {
        regla("Tupla -> TypeDesc COMA Tupla");
        TAtributos attr = atributosPara("Tupla", "tipo", "tsh", "err");

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(tupla_1.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"), tupla_1.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                TupleType ttype = (TupleType) args[1].valor();

                List<Type> types = new ArrayList<>();
                types.add(type);
                types.addAll(ttype.getSubtypes());

                return new TupleType(types);
            }
        });

        dependencias(attr.a("err"), typeDesc.a("err"), tupla_1.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    public TAtributos tupla_R2 (TAtributos typeDesc) {
        regla("Tupla -> TypeDesc");
        TAtributos attr = atributosPara("Tupla", "tipo", "tsh", "err");

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();

                return new TupleType(Arrays.asList(type));
            }
        });

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), typeDesc.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // SInsts

    public TAtributos sInsts_R1 (TAtributos insts) {
        regla("SInsts -> INSTRUCTIONS ILLAVE Insts FLLAVE");
        TAtributos attr = atributosPara("SInsts", "cod", "etq", "etqh", "tsh", "err");

        dependencias(insts.a("tsh"), attr.a("tsh"));
        calculo(insts.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), insts.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), insts.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(insts.a("etqh"), attr.a("etqh"));
        calculo(insts.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), insts.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Insts

    public TAtributos insts_R1 (TAtributos insts_1, TAtributos inst) {
        regla("Insts -> Insts PYC Inst");
        TAtributos attr = atributosPara("Insts", "cod", "etqh", "etq", "tsh", "err");

        dependencias(insts_1.a("tsh"), attr.a("tsh"));
        calculo(insts_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(inst.a("tsh"), attr.a("tsh"));
        calculo(inst.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), insts_1.a("err"), inst.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), insts_1.a("cod"), inst.a("cod"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(insts_1.a("etqh"), attr.a("etqh"));
        calculo(insts_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(inst.a("etqh"), insts_1.a("etq"));
        calculo(inst.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), inst.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos insts_R2 (TAtributos inst) {
        regla("Insts -> Inst");
        TAtributos attr = atributosPara("Insts", "cod", "etqh", "etq", "tsh", "err");

        dependencias(inst.a("tsh"), attr.a("tsh"));
        calculo(inst.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), inst.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), inst.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(inst.a("etqh"), attr.a("etqh"));
        calculo(inst.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), inst.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Inst

    public TAtributos inst_R1 (TAtributos desig, TAtributos expr, Lexeme asig) {
        regla("Inst -> Desig ASIG Expr");
        TAtributos attr = atributosPara("Inst", "cod", "etqh", "etq", "tsh", "err");
        Atributo asigLex = atributoLexicoPara("ASIG", "lex", asig);

        dependencias(desig.a("tsh"), attr.a("tsh"));
        calculo(desig.a("tsh"), AsignationFun.INSTANCE);

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), AsignationFun.INSTANCE);

        // Inst.err = (¬asignacionValida(Desig.tipo, Expr.tipo)) ∨ Expr.err ∨ Desig.err
        dependencias(attr.a("err"), desig.a("tipo"), expr.a("tipo"), expr.a("err"), desig.a("err"), asigLex);
        calculo(attr.a("err"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                Type desigType = (Type) args[0].valor();
                Type exprType = (Type) args[1].valor();
                List<CompileError> exprErr = (List<CompileError>) args[2].valor();
                List<CompileError> desigErr = (List<CompileError>) args[3].valor();
                Lexeme lex = (Lexeme) args[4].valor();

                CompileError err =
                    (!desigType.compatible(exprType)) ? new AssignationTypeError(exprType, desigType, lex) : null;

                return ConcatErrorsFun.INSTANCE.eval(a(err), a(exprErr), a(desigErr));
            }
        });

        dependencias(attr.a("cod"), expr.a("cod"), desig.a("cod"), desig.a("tipo"));
        calculo(attr.a("cod"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<Instruction> codeExpr = (List<Instruction>) args[0].valor();
                List<Instruction> codeDesig = (List<Instruction>) args[1].valor();
                Type type = (Type) args[2].valor();

                return ConcatCodeFun.INSTANCE.eval(a(codeExpr), a(codeDesig), a(new IndirectStoreInstruction(type)));
            }
        });

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), AsignationFun.INSTANCE);

        dependencias(desig.a("etqh"), expr.a("etq"));
        calculo(desig.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), desig.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos inst_R2 (TAtributos desig) {
        regla("Inst -> IN IPAR Desig FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etqh", "etq", "tsh", "err");

        dependencias(desig.a("tsh"), attr.a("tsh"));
        calculo(desig.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), desig.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), desig.a("tipo"), desig.a("cod"));
        calculo(attr.a("cod"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                List<Instruction> code = (List<Instruction>) args[1].valor();

                return ConcatCodeFun.INSTANCE.eval(
                    a(new InputInstruction(type)), a(code), a(new IndirectStoreInstruction(type)));
            }
        });

        dependencias(desig.a("etqh"), attr.a("etq"));
        calculo(desig.a("etqh"), new IncrementFun(1));

        dependencias(attr.a("etq"), desig.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos inst_R3 (TAtributos expr) {
        regla("Inst -> OUT IPAR Expr FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "tsh", "err");

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), expr.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), expr.a("cod"), a(new OutputInstruction()));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), expr.a("etqh"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos inst_R4 () {
        regla("Inst -> SWAP1 IPAR FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "err");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), a(new Swap1Instruction()));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos inst_R5 () {
        regla("Inst -> SWAP2 IPAR FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "err");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), a(new Swap2Instruction()));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos inst_R6 (TAtributos expr, TAtributos insts, TAtributos elseIf) {
        regla("Inst -> IF Expr THEN Insts ElseIf");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh", "err", "cod");

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(insts.a("tsh"), attr.a("tsh"));
        calculo(insts.a("tsh"), AsignationFun.INSTANCE);

        dependencias(elseIf.a("tsh"), attr.a("tsh"));
        calculo(elseIf.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), expr.a("err"), insts.a("err"), elseIf.a("err"));
        calculo(attr.a("err"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<CompileError> exprErr = (List<CompileError>) args[0].valor();
                List<CompileError> instsErr = (List<CompileError>) args[1].valor();
                List<CompileError> elseIfErr = (List<CompileError>) args[2].valor();
                return ConcatErrorsFun.INSTANCE.eval(a(exprErr), a(instsErr), a(elseIfErr));
            }
        });

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), AsignationFun.INSTANCE);

        dependencias(insts.a("etqh"), expr.a("etq"));
        calculo(insts.a("etqh"), new IncrementFun(1));

        dependencias(attr.a("etq"), insts.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("cod"), expr.a("cod"), insts.a("cod"), elseIf.a("cod"), insts.a("etq"), elseIf.a("etq"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {

                return ConcatCodeFun.INSTANCE.eval(attrs[0], a(new BranchInstruction(
                    (Integer) attrs[3].valor() + 1, BooleanValue.FALSE)), attrs[1], a(new JumpInstruction(
                    (Integer) (attrs[4].valor()))), attrs[2]);
            }

        });

        return attr;
    }

    public TAtributos inst_R7 (TAtributos expr, TAtributos insts) {
        regla("Inst -> WHILE Expr DO Insts ENDWHILE");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh", "err", "cod");

        // FIXME Esto no es así
        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos inst_R8 (TAtributos instCall) {
        regla("Inst -> InstCall");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh", "err", "cod");

        // FIXME Esto no es así
        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos inst_R9 () {
        regla("Inst -> $");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh", "err", "cod");

        // FIXME Esto no es así
        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // ElseIf

    public TAtributos elseIf_R1 (TAtributos insts) {
        regla("ElseIf -> ELSE Insts ENDIF");
        TAtributos attr = atributosPara("ElseIf", "tsh", "err", "cod", "etq", "etqh");

        dependencias(insts.a("tsh"), attr.a("tsh"));
        calculo(insts.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), insts.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), insts.a("cod"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(insts.a("etqh"), attr.a("etqh"));
        calculo(insts.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), insts.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos elseIf_R2 () {
        regla("ElseIf -> ENDIF");
        TAtributos attr = atributosPara("ElseIf", "err", "cod", "etq", "etqh");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etqh"), AsignationFun.INSTANCE);

        return attr;
    }

    // InstCall

    public TAtributos instCall_R1 (Lexeme ident, TAtributos srParams) {
        regla("InstCall -> CALL IDENT IPAR SRParams FPAR");
        TAtributos attr =
            atributosPara(
                "InstCall", "tsh", "nparams", "nombresubprogh", "listaparamnombres", "err", "cod", "etqh", "etq");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(srParams.a("tsh"), attr.a("tsh"));
        calculo(srParams.a("tsh"), AsignationFun.INSTANCE);

        dependencias(srParams.a("nparams"));
        calculo(srParams.a("nparams"), new IncrementFun(0)); // FIXME srParams.nparams=0

        dependencias(srParams.a("nombresubprog"), identLex);
        calculo(srParams.a("nombresubprog"), AsignationFun.INSTANCE);

        dependencias(srParams.a("listaparamnombresh"), identLex);
        // calculo(srParams.a("listaparamnombresh"), ); //TODO hay que hacer: listaparamnombres=[]

        dependencias(attr.a("err"), srParams.a("err"), srParams.a("tsh"), identLex, srParams.a("nparams"));
        calculo(attr.a("err"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<CompileError> srparamsErr = (List<CompileError>) args[0].valor();

                SymbolTable ts = (SymbolTable) args[1].valor();
                Lexeme ident = (Lexeme) args[2].valor();
                // Comprobamos que el identificador exista en la tabla de símbolos
                CompileError err1 =
                    (ts.hasIdentifier(ident.getLexeme())) ? new UndefinedIdentifierError(ident.getLexeme(), ident
                        .getLine(), ident.getColumn()) : null;

                Integer nparams = (Integer) args[3].valor();
                Integer numParamsFormales = ts.getIdentifierParams(ident.getLexeme()).size();

                // Comprobamos que el numero de parametros con el qe llamamos a la función sea los mismos con los que
// esta declarado
                CompileError err2 =
                    (nparams != numParamsFormales) ? new MismatchNumberOfParameters(nparams, numParamsFormales, ident
                        .getLine(), ident.getColumn()) : null;

                return ConcatErrorsFun.INSTANCE.eval(a(err1), a(err2), a(srparamsErr));
            }
        });

        dependencias(attr.a("cod"), srParams.a("cod"));
        // calculo(attr.a("cod"), AsignationFun.INSTANCE); // TODO el codigo

        dependencias(srParams.a("etqh"), attr.a("etqh"));
        calculo(srParams.a("etqh"), new IncrementFun(13));

        dependencias(attr.a("etq"), srParams.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(6));

        return attr;
    }

    // SRParams

    public TAtributos srParams_R1 (TAtributos rParams) {
        regla("SRParams -> RParams");
        TAtributos attr =
            atributosPara(
                "SRParams", "tsh", "err", "cod", "etq", "etqh", "nparams", "nparamsh", "nombresubprog",
                "nombresubprogh", "listaparamnombresh");

        dependencias(rParams.a("tsh"), attr.a("tsh"));
        calculo(rParams.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), rParams.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), rParams.a("cod"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(rParams.a("etqh"), attr.a("etqh"));
        calculo(rParams.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), rParams.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(rParams.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParams.a("nparamsh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams.a("nombresubprogh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("listaparamnombresh"), attr.a("listaparamnombresh"));
        calculo(rParams.a("listaparamnombresh"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos srParams_R2 () {
        regla("SRParams -> $");
        TAtributos attr =
            atributosPara(
                "SRParams", "err", "cod", "etqh", "etq", "nparamsh", "nparams", "listaparamnombres",
                "listaparamnombresh", "nombresubprog", "nombresubprogh");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("nparams"), attr.a("nparamsh"));
        calculo(attr.a("nparams"), AsignationFun.INSTANCE);

        dependencias(attr.a("listaparamnombres"), attr.a("listaparamnombresh"));
        calculo(attr.a("listaparamnombres"), AsignationFun.INSTANCE);

        return attr;
    }

    // RParams

    public TAtributos rParams_R1 (TAtributos rParams_1, TAtributos rParams) {
        regla("RParams -> RParams COMA RParam");
        TAtributos attr =
            atributosPara(
                "RParams", "tsh", "err", "cod", "nparamsh", "nparams", "nombresubprogh", "etqh", "etq",
                "listaparamnombresh", "listaparamnombres");

        dependencias(rParams_1.a("tsh"), attr.a("tsh"));
        calculo(rParams_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("tsh"), attr.a("tsh"));
        calculo(rParams.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), rParams_1.a("err"), rParams.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE); // TODO Preguntar como se hacian las cod para concatenar

        dependencias(rParams_1.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParams_1.a("nparamsh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("nparamsh"), rParams_1.a("nparams"));
        calculo(rParams.a("nparams"), AsignationFun.INSTANCE);

        dependencias(attr.a("nparams"), rParams.a("nparams"));
        calculo(attr.a("nparams"), AsignationFun.INSTANCE);

        dependencias(rParams_1.a("etqh"), attr.a("etqh"));
        calculo(rParams_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("etqh"), rParams_1.a("etq"));
        calculo(rParams.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), rParams.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(rParams_1.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams_1.a("nombresubprogh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams.a("nombresubprogh"), AsignationFun.INSTANCE);

        dependencias(rParams_1.a("listaparamnombresh"), attr.a("listaparamnombresh"));
        calculo(rParams_1.a("listaparamnombresh"), AsignationFun.INSTANCE);

        dependencias(rParams.a("listaparamnombresh"), rParams_1.a("listaparamnombres"));
        calculo(rParams.a("listaparamnombresh"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos rParams_R2 (TAtributos rParam) {
        regla("RParams -> RParam");
        TAtributos attr =
            atributosPara(
                "RParams", "tsh", "err", "cod", "etq", "etqh", "nparams", "nparamsh", "nombresubprogh",
                "listaparamnombresh", "listaparamnombres");

        dependencias(rParam.a("tsh"), attr.a("tsh"));
        calculo(rParam.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), rParam.a("cod"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(attr.a("err"), rParam.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(rParam.a("etqh"), attr.a("etqh"));
        calculo(rParam.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), rParam.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(rParam.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParam.a("nparamsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("nparams"), rParam.a("nparams"));
        calculo(attr.a("nparams"), AsignationFun.INSTANCE);

        dependencias(rParam.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParam.a("nombresubprogh"), AsignationFun.INSTANCE);

        dependencias(rParam.a("listaparamnombresh"), attr.a("listaparamnombresh"));
        calculo(rParam.a("listaparamnombresh"), AsignationFun.INSTANCE);

        dependencias(attr.a("listaparamnombres"), rParam.a("listaparamnombres"));
        calculo(attr.a("listaparamnombres"), AsignationFun.INSTANCE);

        return attr;
    }

    // RParam

    public TAtributos rParam_R1 (Lexeme ident, TAtributos expr) {
        regla("RParam -> IDENT ASIG Expr");
        TAtributos attr =
            atributosPara(
                "RParam", "tsh", "cod", "etq", "etqh", "nparams", "nparamsh", "nombresubprog", "nombresubprogh",
                "tipo", "desig", "err", "listaparamnombres", "listaparamnombresh");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(
            attr.a("err"), expr.a("err"), expr.a("tsh"), identLex, attr.a("tsh"), attr.a("nombresubprogh"),
            expr.a("tipo"), expr.a("desig"), attr.a("listaparamnombresh"));
        calculo(attr.a("err"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<CompileError> exprErr = (List<CompileError>) args[0].valor();

                SymbolTable exprTsh = (SymbolTable) args[1].valor();
                Lexeme identParamReal = (Lexeme) args[2].valor();
                Lexeme identSubprog = (Lexeme) args[3].valor();

                List<Parameter> parametros = exprTsh.getIdentifierParams(identSubprog.getLexeme());

                // Comprobamos que el identificador del parámetro real esté declarado como parámetro en la tabla de
// símbolos
                CompileError err1 =
                    (!parametros.contains(identParamReal.getLexeme())) ? new UndefinedIdentifierError(identParamReal
                        .getLexeme(), identParamReal.getLine(), identParamReal.getColumn()) : null;

                // Comprobamos que el tipo del parámetro real se pueda asignar al tipo del parámetro formal declarado.
                Parameter paramFormal = null;
                // Parameter paramReal

                Iterator<Parameter> it = parametros.iterator();
                while (it.hasNext()) {
                    Parameter element = it.next();
                    if (element.getName().equals(identParamReal.getLexeme())) {
                        paramFormal = element;
                    }
                }
                if (paramFormal != null) {
                    Type exprT = (Type) args[5].valor();
                    Type paramT = paramFormal.getType();
                    CompileError err2 =
                        (!paramT.compatible(exprT)) ? new AssignationTypeError(paramT, exprT, identParamReal) : null;

                    ConcatErrorsFun.INSTANCE.eval(a(err2)); // FIXME Puedo hacerlo así??
                }

                // Comprobamos que la expresion sea un designador
                boolean esDesig = (boolean) args[6].valor();

                CompileError err3 =
                    (!esDesig) ? new ExpectedDesignator(
                        identParamReal.getLexeme(), identParamReal.getLine(), identParamReal.getColumn()) : null;

                // TODO el error de la lista ident ∈ listaparamnombresh. Pero las listas todavia no estan hechas

                return ConcatErrorsFun.INSTANCE.eval(a(exprErr), a(err1), a(err3));
            }
        });

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), new IncrementFun(6));

        dependencias(attr.a("etq"), expr.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("nparams"), attr.a("nparamsh"));
        calculo(attr.a("nparams"), new IncrementFun(1));

        dependencias(attr.a("listaparamnombres"), attr.a("listaparamnombres"));
        // calculo(attr.a("listaparamnombres"), new IncrementFun(1)); //TODO listaparamnombres ++ ident

        return attr;
    }

    // SSubprogs

    public TAtributos sSubprogs_R1 (TAtributos subprogs) {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE Subprogs FLLAVE");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh", "ts", "err", "cod");

        dependencias(subprogs.a("tsh"), attr.a("tsh"));
        calculo(subprogs.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), subprogs.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), subprogs.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(subprogs.a("etqh"), attr.a("etqh"));
        calculo(subprogs.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), subprogs.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos sSubprogs_R2 () {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE FLLAVE");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh", "ts", "cod");

        // SSublogos.cod = []
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos sSubprogs_R3 () {
        regla("SSubprogs -> $");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh", "ts", "err", "cod");

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        // SSublogos.cod = []
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Subprogs

    public TAtributos subprogs_R1 (TAtributos subprogs_1, TAtributos subprog) {
        regla("Subprogs -> Subprogs Subprog");
        TAtributos attr = atributosPara("Subprogs", "tsh", "ts", "err", "cod", "etq", "etqh");

        dependencias(subprogs_1.a("tsh"), attr.a("tsh"));
        calculo(subprogs_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(subprog.a("tsh"), subprogs_1.a("ts"));
        calculo(subprog.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), subprog.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), subprogs_1.a("err"), subprog.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), subprogs_1.a("cod"), subprog.a("cod"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(subprogs_1.a("etqh"), attr.a("etqh"));
        calculo(subprogs_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(subprog.a("etqh"), subprogs_1.a("etq"));
        calculo(subprog.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), subprog.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos subprogs_R2 (TAtributos subprog) {
        regla("Subprogs -> Subprog");
        TAtributos attr = atributosPara("Subprogs", "tsh", "ts", "err", "cod", "etq", "etqh");

        dependencias(subprog.a("tsh"), attr.a("tsh"));
        calculo(subprog.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), subprog.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), subprog.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("cod"), subprog.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(subprog.a("etqh"), attr.a("etqh"));
        calculo(subprog.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), subprog.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Subprog

    public TAtributos subprog_R1 (Lexeme ident, TAtributos sfParams, TAtributos sVars, TAtributos sInsts) {
        regla("Subprog -> SUBPROGRAM IDENT IPAR SFParams FPAR ILLAVE SVars SInsts FLLAVE");
        TAtributos attr = atributosPara("Subprog", "dir", "dirh", "tsh", "ts", "cod", "etq", "etqh", "err");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(sfParams.a("dirh"), a(0));
        calculo(sfParams.a("dirh"), AsignationFun.INSTANCE);

        dependencias(sfParams.a("tsh"), attr.a("tsh"));
        calculo(sfParams.a("tsh"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                return new SymbolTable(table);
            }
        });

        dependencias(sVars.a("tsh"), sfParams.a("ts"), identLex, sfParams.a("params"), attr.a("etqh"));
        calculo(sVars.a("tsh"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                List<Parameter> params = (List<Parameter>) args[2].valor();
                Integer address = (Integer) args[3].valor();

                table.putSubprogram(ident.getLexeme(), params, address);

                return table;
            }
        });

        dependencias(sVars.a("dirh"), sfParams.a("dir"));
        calculo(sVars.a("dirh"), AsignationFun.INSTANCE);

        dependencias(sInsts.a("tsh"), sVars.a("ts"));
        calculo(sInsts.a("tsh"), AsignationFun.INSTANCE);

        // TODO marina Subprog.err = existe(Subprog.tsh, ident) ∨ SParams.err ∨ SVars.err ∨ SInsts.err ∨
// parametrosNoRepetidos(SParams.ts, ident)
        // dependencias(attr.a("err"), attr.a("tsh"),)

        dependencias(attr.a("err"), sfParams.a("err"), sInsts.a("err"), sVars.a("err"), identLex, sfParams.a("ts"));
        calculo(attr.a("err"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<CompileError> sfparamsErr = (List<CompileError>) args[0].valor();
                List<CompileError> sInstsErr = (List<CompileError>) args[1].valor();
                List<CompileError> sVarsErr = (List<CompileError>) args[2].valor();

                SymbolTable ts = (SymbolTable) args[4].valor();
                Lexeme ident = (Lexeme) args[3].valor();
                // Comprobamos que el identificador del subprograma no exista previamente en la tabla de símbolos
                CompileError err1 =
                    (!ts.hasIdentifier(ident.getLexeme())) ? new DuplicateIdentifierError(ident.getLexeme(), ident
                        .getLine(), ident.getColumn()) : null;

                // Comprobamos que no haya parametros formales declarados repetidos
                List<Parameter> parametros = ts.getIdentifierParams(ident.getLexeme());

                Iterator<Parameter> it1 = parametros.iterator();
                while (it1.hasNext()) {
                    Parameter element1 = it1.next();
                    Iterator<Parameter> it2 = parametros.iterator();
                    while (it2.hasNext()) {
                        Parameter element2 = it2.next();
                        if (element1 != element2) {
                            if (element1.getName().equals(element2.getName())) {
                                CompileError err2 =
                                    new DuplicateIdentifierError(element2.getName(), ident.getLine(), ident.getColumn());
                                ConcatErrorsFun.INSTANCE.eval(a(err2));
                            }
                        }
                    }
                }

                return ConcatErrorsFun.INSTANCE.eval(a(sfparamsErr), a(sInstsErr), a(sVarsErr), a(err1));

            }
        });

        dependencias(
            attr.a("cod"), sInsts.a("cod"), a(new LoadInstruction(1, Type.INTEGER)), a(new PushInstruction(
                new IntegerValue(3))), a(new BinaryOperatorInstruction(BinaryOperator.SUBTRACTION)),
            a(new LoadInstruction(1, Type.INTEGER)), a(new IndirectLoadInstruction(Type.INTEGER)),
            a(new StoreInstruction(1, Type.INTEGER)), a(new DropInstruction()),
            a(new LoadInstruction(0, Type.INTEGER)), a(new PushInstruction(new IntegerValue(1))),
            a(new BinaryOperatorInstruction(BinaryOperator.ADDITION)), a(new IndirectLoadInstruction(Type.INTEGER)),
            a(new ReturnInstruction()));

        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(sInsts.a("etqh"), attr.a("etqh"));
        calculo(sInsts.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), sInsts.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(3));

        dependencias(attr.a("ts"), attr.a("tsh"), identLex, sfParams.a("params"), attr.a("etqh"));
        calculo(attr.a("ts"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                List<Parameter> params = (List<Parameter>) args[2].valor();
                Integer address = (Integer) args[3].valor();

                table.putSubprogram(ident.getLexeme(), params, address);

                return table;
            }
        });

        return attr;
    }

    // SFParams

    public TAtributos sfParams_R1 (TAtributos fParams) {
        regla("SFParams -> FParams");
        TAtributos attr = atributosPara("SFParams", "tsh", "ts", "dir", "dirh", "err", "params");

        // FParams
        dependencias(fParams.a("tsh"), attr.a("tsh"));
        calculo(fParams.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), fParams.a("ts"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), fParams.a("dir"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        dependencias(fParams.a("dirh"), attr.a("dirh"));
        calculo(fParams.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), fParams.a("err"));
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        dependencias(attr.a("params"), fParams.a("params"));
        calculo(attr.a("params"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos sfParams_R2 () {
        regla("SFParams -> $");
        TAtributos attr = atributosPara("SFParams", "ts", "tsh", "dir", "dirh", "err", "params");

        // sfParams
        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("params"), a(Collections.emptyList()));
        calculo(attr.a("params"), AsignationFun.INSTANCE);

        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        return attr;
    }

    // FParams
    public TAtributos fParams_R1 (TAtributos fParams_1, TAtributos fParam) {
        regla("FParams -> FParams COMA FParam");
        TAtributos attr = atributosPara("FParams", "tsh", "ts", "err", "dir", "dirh", "id", "clase", "tipo", "params");

        dependencias(fParams_1.a("tsh"), attr.a("tsh"));
        calculo(fParams_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(fParams_1.a("dirh"), attr.a("dirh"));
        calculo(fParams_1.a("dirh"), AsignationFun.INSTANCE);

        dependencias(fParam.a("tsh"), fParams_1.a("tsh"));
        calculo(fParam.a("tsh"), AsignationFun.INSTANCE);

        dependencias(fParam.a("dirh"), fParams_1.a("dir"));
        calculo(fParam.a("dirh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), fParams_1.a("dir"), fParam.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(attr.a("params"), fParams_1.a("params"), fParam.a("param"));
        calculo(attr.a("params"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... args) {
                List<Parameter> params = (List<Parameter>) args[0].valor();
                Parameter param = (Parameter) args[1].valor();

                List<Parameter> moreParams = new ArrayList<>(params);
                moreParams.add(param);

                return moreParams;
            }
        });

        dependencias(
            attr.a("ts"), fParam.a("ts"), fParam.a("id"), fParam.a("clase"), fParams_1.a("dir"), fParam.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                ClassDec cd = (ClassDec) args[2].valor();
                Integer address = (Integer) args[3].valor();
                Type type = (Type) args[4].valor();

                if (ident != null) {
                    table.putParam(ident.getLexeme(), address, type, cd == ClassDec.PARAM_REF);
                }

                return table;
            }
        });

        dependencias(attr.a("err"), fParam.a("ts"), fParam.a("id"), a(Scope.LOCAL));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    public TAtributos fParams_R2 (TAtributos fParam) {
        regla("FParams -> FParam");
        TAtributos attr = atributosPara("FParams", "tsh", "ts", "id", "dir", "dirh", "tipo", "clase", "err", "params");

        dependencias(fParam.a("tsh"), attr.a("tsh"));
        calculo(fParam.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), AsignationFun.INSTANCE);

        dependencias(attr.a("params"), fParam.a("param"));
        calculo(attr.a("params"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Parameter param = (Parameter) args[0].valor();

                return Arrays.asList(param);
            }
        });

        dependencias(attr.a("ts"), fParam.a("ts"), fParam.a("id"), fParam.a("clase"), fParam.a("dir"), fParam.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                String ident = (String) args[1].valor();
                ClassDec cd = (ClassDec) args[2].valor();
                int address = (int) args[3].valor();
                Type type = (Type) args[4].valor();

                ts.putParam(ident, address, type, cd == ClassDec.PARAM_REF);

                return ts;
            }
        });

        dependencias(attr.a("err"), fParam.a("ts"), fParam.a("id"), a(Scope.LOCAL));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // FParam

    public TAtributos fParam_R1 (TAtributos typeDesc, Lexeme ident) {
        regla("FParam -> TypeDesc IDENT");
        TAtributos attr = atributosPara("FParams", "ts", "tsh", "id", "clase", "tipo", "dir", "dirh", "param");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("id"), identLex);
        calculo(attr.a("id"), AsignationFun.INSTANCE);

        dependencias(attr.a("clase"), a(ClassDec.PARAM_VALUE));
        calculo(attr.a("clase"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("param"), typeDesc.a("tipo"), identLex);
        calculo(attr.a("param"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                return new Parameter(ident.getLexeme(), type, false);
            }
        });

        return attr;
    }

    public TAtributos fParam_R2 (TAtributos typeDesc, Lexeme ident) {
        regla("FParam -> TypeDesc MUL IDENT");
        TAtributos attr = atributosPara("FParams", "ts", "tsh", "id", "clase", "tipo", "dir", "dirh", "param");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(typeDesc.a("tsh"), attr.a("tsh"));
        calculo(typeDesc.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), AsignationFun.INSTANCE);

        dependencias(attr.a("id"), identLex);
        calculo(attr.a("id"), AsignationFun.INSTANCE);

        dependencias(attr.a("clase"), a(ClassDec.PARAM_REF));
        calculo(attr.a("clase"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("param"), typeDesc.a("tipo"), identLex);
        calculo(attr.a("param"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type type = (Type) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                return new Parameter(ident.getLexeme(), type, true);
            }
        });
        return attr;
    }

    // Desig

    public TAtributos desig_R1 (Lexeme ident) {
        regla("Desig -> IDENT");
        TAtributos attr = atributosPara("Desig", "tipo", "err", "tsh", "etqh", "etq", "cod");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("tipo"), attr.a("tsh"), identLex);
        calculo(attr.a("tipo"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                if (table.hasIdentifier(ident.getLexeme())) {
                    return table.getIdentfierType(ident.getLexeme());
                } else {
                    return Type.ERROR;
                }
            }
        });

        dependencias(attr.a("err"), attr.a("tsh"), identLex);
        calculo(attr.a("err"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();

                return table.hasIdentifier(ident.getLexeme()) ? new UndefinedIdentifierError(ident.getLexeme(), ident
                    .getLine(), ident.getColumn()) : null;
            }
        });

        // FIXME Esto no es así
        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        // DANI dependencias y calculo de cod

        return attr;
    }

    public TAtributos desig_R2 (TAtributos desig_1, TAtributos expr) {
        regla("Desig -> Desig ICORCHETE Expr FCORCHETE");
        TAtributos attr = atributosPara("Desig", "tsh", "tipo", "err", "cod", "etqh", "etq");

        dependencias(attr.a("tipo"), desig_1.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), desig_1.a("err"), expr.a("err")); // TODO Falta comprobar poner lo de
                                                                      // tamañoCorrecto()
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        // TODO Falta hacer todo lo del código

        dependencias(desig_1.a("etqh"), attr.a("etqh"));
        calculo(desig_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(expr.a("etqh"), desig_1.a("etq"));
        calculo(expr.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), expr.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(3));

        return attr;
    }

    public TAtributos desig_R3 (TAtributos desig_1, Lexeme litnat) {
        regla("Desig -> Desig BARRABAJA LITNAT");
        TAtributos attr = atributosPara("Desig", "tsh", "tipo", "err", "cod", "etqh", "etq");

        dependencias(attr.a("tipo"), desig_1.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("err"), desig_1.a("err")); // TODO Falta comprobar poner lo de
                                                       // tamañoCorrecto()
        calculo(attr.a("err"), ConcatErrorsFun.INSTANCE);

        // TODO Falta hacer todo lo del código

        dependencias(desig_1.a("etqh"), attr.a("etqh"));
        calculo(desig_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), desig_1.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(2));

        return attr;
    }

    // Expr

    public TAtributos expr_R1 (TAtributos term_1, TAtributos op0, TAtributos term_2) {
        regla("Expr -> Term Op0 Term");
        TAtributos attr = atributosPara("Expr", "desig", "tipo", "tsh", "err", "cod", "etqh", "etq");

        dependencias(attr.a("desig"), a(false));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), term_1.a("tipo"), op0.a("op"), term_2.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Type type1 = (Type) args[0].valor();
                BinaryOperator op = (BinaryOperator) args[1].valor();
                Type type2 = (Type) args[2].valor();

                return op.getApplyType(type1, type2);
            }
        });

        dependencias(term_1.a("tsh"), attr.a("tsh"));
        calculo(term_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(term_2.a("tsh"), attr.a("tsh"));
        calculo(term_2.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), term_1.a("desig"), term_2.a("desig"));
        calculo(attr.a("desig"), AndFun.INSTANCE);

        // FIXME Esto no es así
        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos expr_R2 (TAtributos term) {
        regla("Expr -> Term");
        TAtributos attr = atributosPara("Expr", "tipo", "tsh", "desig", "err", "cod", "etqh", "etq");

        dependencias(attr.a("tipo"), term.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(term.a("tsh"), attr.a("tsh"));
        calculo(term.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), a(false));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), term.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), term.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(term.a("etqh"), attr.a("etqh"));
        calculo(term.a("etqh"), AsignationFun.INSTANCE);

        return attr;
    }

    // Term

    public TAtributos term_R1 (TAtributos term_1, TAtributos op1, TAtributos fact) {
        regla("Term -> Term Op1 Fact");
        TAtributos attr = atributosPara("Term", "tipo", "tsh", "desig", "op", "etq", "etqh", "cod", "err");

        dependencias(attr.a("tipo"), term_1.a("tipo"), op1.a("op"), fact.a("tipo"));
        // TODO calculo(attr.a("tipo"),);

        dependencias(term_1.a("tsh"), attr.a("tsh"));
        calculo(term_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(fact.a("tsh"), attr.a("tsh"));
        calculo(fact.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), term_1.a("desig"), fact.a("desig"));
        // TODO calculo(attr.a("desig"),);

        dependencias(attr.a("cod"), term_1.a("cod"), fact.a("cod"), op1.a("op"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(term_1.a("etqh"), attr.a("etqh"));
        calculo(term_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(fact.a("etqh"), term_1.a("etq"));
        calculo(term_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), term_1.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos term_R2 (TAtributos term_1, TAtributos fact) {
        regla("Term -> Term OR Fact");
        TAtributos attr = atributosPara("Term", "tipo", "op", "tsh", "desig", "cod", "etq", "etqh", "err");

        dependencias(attr.a("tipo"), term_1.a("tipo"), fact.a("tipo"));
        // TODO la tipoFUnc con or calculo(attr.a("tipo"), );

        dependencias(term_1.a("tsh"), attr.a("tsh"));
        calculo(term_1.a("tipo"), AsignationFun.INSTANCE);

        dependencias(fact.a("tsh"), attr.a("tsh"));
        calculo(fact.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), term_1.a("desig"), fact.a("desig"));
        // TODO calculo(attr.a("desig"),);

        // TODO generacion de codigo
        // dependencias(attr.a("cod"),term_1.a("cod"),);
        // calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(term_1.a("etqh"), attr.a("etqh"));
        calculo(term_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(fact.a("etqh"), term_1.a("etq"));
        calculo(term_1.a("etqh"), new IncrementFun(3));

        dependencias(attr.a("etq"), term_1.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos term_R3 (TAtributos fact) {
        regla("Term -> Fact");
        TAtributos attr = atributosPara("Term", "tipo", "tsh", "desig", "cod", "etqh", "etq", "err", "op");

        dependencias(attr.a("tipo"), fact.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(fact.a("tsh"), attr.a("tsh"));
        calculo(fact.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), fact.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), fact.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(fact.a("etqh"), attr.a("etqh"));
        calculo(fact.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), fact.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Fact

    public TAtributos fact_R1 (TAtributos fact_1, TAtributos op2, TAtributos shft) {
        regla("Fact -> Fact Op2 Shft");
        TAtributos attr = atributosPara("Fact", "tipo", "tsh", "desig", "err", "cod", "etq", "etqh");

        dependencias(attr.a("tipo"), fact_1.a("tipo"), op2.a("op"), shft.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Type type1 = (Type) args[0].valor();
                BinaryOperator op = (BinaryOperator) args[1].valor();
                Type type2 = (Type) args[2].valor();

                return op.getApplyType(type1, type2);
            }
        });

        dependencias(fact_1.a("tsh"), attr.a("tsh"));
        calculo(fact_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(shft.a("tsh"), attr.a("tsh"));
        calculo(shft.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), fact_1.a("desig"), shft.a("desig"));
        // revisar el and:
        // Fact0.desig = Fact1.desig ˄ Shft.desig

        calculo(attr.a("desig"), AndFun.INSTANCE);

        return attr;
    }

    public TAtributos fact_R2 (TAtributos fact_1, TAtributos shft) {
        regla("Fact -> Fact AND Shft");
        TAtributos attr = atributosPara("Fact", "tipo", "tsh", "desig", "cod", "etq", "err", "etqh");

        // TODO
        // Fact → Fact and Shft
        // Fact0.tipo = tipoFunc(Fact1.tipo, and, Shft.tipo)
        // Fact1.tsh = Fact0.tsh
        // Shft.tsh = Fact0.tsh
        // Fact0.desig = Fact1.desig ˄ Shft.desig
        // Fact0.cod = Fact1.cod || copia || ir-f(Shft.etq ) || desapila || Shft.cod
        // Fact1.etqh = = Fact0.etqh
        // Shft.etqh = Fact1.etq + 3
        // Fact0.etq = Shft.etq

        return attr;
    }

    public TAtributos fact_R3 (TAtributos shft) {
        regla("Fact -> Shft");
        TAtributos attr = atributosPara("Fact", "tipo", "tsh", "desig", "cod", "etqh", "etq", "err");

        dependencias(attr.a("tipo"), shft.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(shft.a("tsh"), attr.a("tsh"));
        calculo(shft.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), shft.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), shft.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(shft.a("etqh"), attr.a("etqh"));
        calculo(shft.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), shft.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Shft

    public TAtributos shft_R1 (TAtributos unary, TAtributos op3, TAtributos shft_1) {
        regla("Shft -> Unary Op3 Shft");
        TAtributos attr = atributosPara("Shft", "tsh", "desig", "tipo", "cod", "etqh", "etq", "err");

        dependencias(attr.a("tipo"), unary.a("tipo"), op3.a("op"), shft_1.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                Type t1 = (Type) args[0].valor();
                Type t2 = (Type) args[2].valor();
                BinaryOperator op = (BinaryOperator) args[1].valor();
                if (!op.canApply(t1, t2)) {
                    return new OperatorError(t1, t2, op, -1, -1);
                }
                return op.getApplyType(t1, t2);
            }
        });

        dependencias(unary.a("tsh"), attr.a("tsh"));
        calculo(attr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("tsh"), shft_1.a("tsh"));
        calculo(attr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), unary.a("desig"), shft_1.a("desig"));
        calculo(attr.a("desig"), AndFun.INSTANCE);

        dependencias(attr.a("cod"), unary.a("cod"), shft_1.a("cod"), op3.a("op"));
        calculo(attr.a("cod"), ConcatCodeFun.INSTANCE);

        dependencias(unary.a("etqh"), attr.a("etqh"));
        calculo(unary.a("etqh"), AsignationFun.INSTANCE);

        dependencias(shft_1.a("etqh"), unary.a("etq"));
        calculo(shft_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), shft_1.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        return attr;
    }

    public TAtributos shft_R2 (TAtributos unary) {
        regla("Shft -> Unary");
        TAtributos attr = atributosPara("Shft", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(unary.a("tsh"), attr.a("tsh"));
        calculo(unary.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), unary.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), unary.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), unary.a("cod"));
        calculo(attr.a("cod"), AsignationFun.INSTANCE);

        dependencias(unary.a("etqh"), attr.a("etqh"));
        calculo(unary.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), unary.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        return attr;
    }

    // Unary

    public TAtributos unary_R1 (TAtributos op4, TAtributos unary_1) {
        regla("Unary -> Op4 Unary");
        TAtributos attr = atributosPara("Unary", "tsh", "tipo", "err", "desig", "cod", "etqh", "etq");

        dependencias(unary_1.a("tsh"), attr.a("tsh"));
        calculo(unary_1.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), unary_1.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(unary_1.a("etqh"), attr.a("etqh"));
        calculo(unary_1.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), unary_1.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("cod"), unary_1.a("cod"), op4.a("op"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(attrs[0], attrs[1]);
            }
        });

        dependencias(attr.a("tipo"), unary_1.a("tipo"), op4.a("op"));
        calculo(attr.a("tipo"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                Type t = (Type) attrs[0].valor();
                UnaryOperator op = (UnaryOperator) attrs[1].valor();

                return op.getApplyType(t);
            }
        });

        return attr;
    }

    public TAtributos unary_R2 (TAtributos cast, TAtributos paren) {
        regla("Unary -> IPAR Cast FPAR Paren");
        TAtributos attr = atributosPara("Unary", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(paren.a("tsh"), attr.a("tsh"));
        calculo(paren.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), paren.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(paren.a("etqh"), attr.a("etqh"));
        calculo(paren.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), paren.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("cod"), paren.a("cod"), cast.a("type"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(attrs[0], attrs[1]);
            }
        });

        dependencias(attr.a("tipo"), cast.a("tipo"), paren.a("tipo"));
        calculo(attr.a("tipo"), new SemFun() {
            @SuppressWarnings("unchecked")
            @Override
            public Object eval (Atributo... attrs) {
                Type tCast = (Type) attrs[0].valor();
                Type tParen = (Type) attrs[1].valor();

                return tCast.canCast(tCast, tParen);
            }
        });

        return attr;
    }

    public TAtributos unary_R3 (TAtributos paren) {
        regla("Unary -> Paren");
        TAtributos attr = atributosPara("Unary", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(paren.a("tsh"), attr.a("tsh"));
        calculo(paren.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), paren.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), paren.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(paren.a("etqh"), attr.a("etqh"));
        calculo(paren.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), paren.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), paren.a("cod"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(attrs[0]);
            }
        });

        return attr;
    }

    // Paren

    public TAtributos paren_R1 (TAtributos expr) {// TODO marina
        regla("Paren -> IPAR Expr FPAR");
        TAtributos attr = atributosPara("Paren", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), expr.a("desig"));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), expr.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), expr.a("etq"));
        calculo(attr.a("etq"), AsignationFun.INSTANCE);

        dependencias(attr.a("cod"), expr.a("cod"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(attrs[0]);
            }
        });

        return attr;
    }

    public TAtributos paren_R2 (TAtributos lit) {// TODO marina
        regla("Paren -> Lit");
        TAtributos attr = atributosPara("Paren", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(lit.a("tsh"), attr.a("tsh"));
        calculo(lit.a("tsh"), AsignationFun.INSTANCE);

        dependencias(attr.a("desig"), a(false));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(attr.a("tipo"), lit.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("cod"), lit.a("valor"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(a(new PushInstruction((Value) (attrs[0].valor()))));

            }
        });

        return attr;
    }

    public TAtributos paren_R3 (TAtributos desig) {// TODO marina
        regla("Paren -> Desig");
        TAtributos attr = atributosPara("Paren", "tsh", "tipo", "desig", "cod", "etqh", "etq", "err");

        dependencias(attr.a("desig"), a(true));
        calculo(attr.a("desig"), AsignationFun.INSTANCE);

        dependencias(desig.a("etqh"), attr.a("etqh"));
        calculo(desig.a("etqh"), AsignationFun.INSTANCE);

        dependencias(attr.a("etq"), desig.a("etq"));
        calculo(attr.a("etq"), new IncrementFun(1));

        dependencias(attr.a("cod"), desig.a("cod"), desig.a("tipo"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return ConcatCodeFun.INSTANCE.eval(attrs[0], a(new IndirectLoadInstruction((Type) attrs[1].valor())));

            }
        });

        return attr;
    }

    // Op0

    public TAtributos op0_R1 () {
        regla("Op0 -> IGUAL");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.EQUALS));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op0_R2 () {
        regla("Op0 -> NOIGUAL");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.NOT_EQUALS));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op0_R3 () {
        regla("Op0 -> MAY");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.GREATER_THAN));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op0_R4 () {
        regla("Op0 -> MEN");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.LOWER_THAN));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op0_R5 () {
        regla("Op0 -> MENOIG");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.LOWER_EQUAL));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op0_R6 () {
        regla("Op0 -> MAYOIG");
        TAtributos attr = atributosPara("Op0", "op");

        dependencias(attr.a("op"), a(BinaryOperator.GREATER_EQUALS));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    // Op1

    public TAtributos op1_R1 () {
        regla("Op1 -> MENOS");
        TAtributos attr = atributosPara("Op1", "op");

        dependencias(attr.a("op"), a(BinaryOperator.SUBTRACTION));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op1_R2 () {
        regla("Op1 -> MAS");
        TAtributos attr = atributosPara("Op1", "op");

        dependencias(attr.a("op"), a(BinaryOperator.ADDITION));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    // Op2

    public TAtributos op2_R1 () {
        regla("Op2 -> MOD");
        TAtributos attr = atributosPara("Op2", "op");

        dependencias(attr.a("op"), a(BinaryOperator.MODULO));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op2_R2 () {
        regla("Op2 -> DIV");
        TAtributos attr = atributosPara("Op2", "op");

        dependencias(attr.a("op"), a(BinaryOperator.DIVISION));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op2_R3 () {
        regla("Op2 -> MUL");
        TAtributos attr = atributosPara("Op2", "op");

        dependencias(attr.a("op"), a(BinaryOperator.PRODUCT));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    // Op3

    public TAtributos op3_R1 () {
        regla("Op3 -> LSH");
        TAtributos attr = atributosPara("Op3", "op");

        dependencias(attr.a("op"), a(BinaryOperator.SHIFT_LEFT));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op3_R2 () {
        regla("Op3 -> RSH");
        TAtributos attr = atributosPara("Op3", "op");

        dependencias(attr.a("op"), a(BinaryOperator.SHIFT_RIGHT));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    // Op4

    public TAtributos op4_R1 () {
        regla("Op4 -> NOT");
        TAtributos attr = atributosPara("Op4", "op");

        dependencias(attr.a("op"), a(UnaryOperator.NOT));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos op4_R2 () {
        regla("Op4 -> MENOS");
        TAtributos attr = atributosPara("Op4", "op");

        dependencias(attr.a("op"), a(UnaryOperator.MINUS));
        calculo(attr.a("op"), AsignationFun.INSTANCE);

        return attr;
    }

    // Lit

    public TAtributos lit_R1 (TAtributos litBool) {
        regla("Lit -> LitBool");
        TAtributos attr = atributosPara("Lit", "tipo", "valor");

        dependencias(attr.a("tipo"), a(Type.BOOLEAN));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), litBool.a("valor"));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos lit_R2 (TAtributos litNum) {
        regla("Lit -> LitNum");
        TAtributos attr = atributosPara("Lit", "tipo", "valor");

        dependencias(attr.a("tipo"), litNum.a("tipo"));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), litNum.a("valor"));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos lit_R3 (Lexeme litChar) {
        regla("Lit -> LITCHAR");
        TAtributos attr = atributosPara("Lit", "tipo", "valor");

        dependencias(attr.a("tipo"), a(Type.CHARACTER));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), a(litChar));
        calculo(attr.a("valor"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Lexeme lexeme = (Lexeme) args[0].valor();

                return CharacterValue.valueOf(lexeme.getLexeme());
            }
        });

        return attr;
    }

    // LitBool

    public TAtributos litBool_R1 () {
        regla("LitBool -> TRUE");
        TAtributos attr = atributosPara("LitBool", "valor");

        dependencias(attr.a("valor"), a(true));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        return attr;
    }

    public TAtributos litBool_R2 () {
        regla("LitBool -> FALSE");
        TAtributos attr = atributosPara("LitBool", "valor");

        dependencias(attr.a("valor"), a(false));
        calculo(attr.a("valor"), AsignationFun.INSTANCE);

        return attr;
    }

    // LitNum

    public TAtributos litNum_R1 (Lexeme litNat) {
        regla("LitNum -> LITNAT");
        Atributo litNatLex = atributoLexicoPara("LITNAT", "lex", litNat);
        TAtributos attr = atributosPara("LitNum", "valor", "tipo");

        dependencias(attr.a("tipo"), a(Type.NATURAL));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), litNatLex);
        calculo(attr.a("valor"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Lexeme lexeme = (Lexeme) args[0].valor();

                return NaturalValue.valueOf(lexeme.getLexeme());
            }
        });

        return attr;
    }

    public TAtributos litNum_R2 (Lexeme litFloat) {
        regla("LitNum -> LITFLOAT");
        Atributo litFloatLex = atributoLexicoPara("LITFLOAT", "lex", litFloat);
        TAtributos attr = atributosPara("LitNum", "valor", "tipo");

        dependencias(attr.a("tipo"), a(Type.FLOAT));
        calculo(attr.a("tipo"), AsignationFun.INSTANCE);

        dependencias(attr.a("valor"), litFloatLex);
        calculo(attr.a("valor"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                Lexeme lexeme = (Lexeme) args[0].valor();

                return FloatValue.valueOf(lexeme.getLexeme());
            }
        });

        return attr;
    }

}
