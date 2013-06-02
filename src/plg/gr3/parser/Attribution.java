package plg.gr3.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java_cup.runtime.Symbol;
import plg.gr3.data.Type;
import plg.gr3.data.UnaryOperator;
import plg.gr3.data.Value;
import plg.gr3.errors.compile.CompileError;
import plg.gr3.errors.compile.DuplicateIdentifierError;
import plg.gr3.errors.compile.OperatorError;
import plg.gr3.parser.semfun.CheckDuplicateIdentifierFun;
import plg.gr3.parser.semfun.IncrementFun;
import plg.gr3.vm.instr.Instruction;
import plg.gr3.vm.instr.JumpInstruction;
import plg.gr3.vm.instr.OutputInstruction;
import plg.gr3.vm.instr.StopInstruction;
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

    /** Función semántica para asignar valores */
    private static final SemFun SEMFUN_ASIGNATION = new SemFun() {

        @Override
        public Object eval (Atributo... args) {
            return (args.length == 0 || args[0] == null) ? null : args[0].valor();
        }
    };

    /** Función semántica para concatenar errores */
    private static final SemFun SEMFUN_ERRORS = new SemFun() {

        @SuppressWarnings("unchecked")
        @Override
        public List<CompileError> eval (Atributo... attrs) {
            List<CompileError> errors = Collections.checkedList(new ArrayList<CompileError>(), CompileError.class);

            for (Atributo attr : attrs) {
                Object obj = (attr == null) ? null : attr.valor();
                if (obj instanceof CompileError) {
                    errors.add((CompileError) obj);

                } else if (obj instanceof List<?>) {
                    errors.addAll((List<CompileError>) obj);

                }
            }
            return Collections.unmodifiableList(errors);
        }

    };

    /** Función semántica para concatenar código */
    private static final SemFun SEMFUN_CONCAT = new SemFun() {
        @SuppressWarnings("unchecked")
        public List<Instruction> eval (Atributo... attrs) {
            List<Instruction> code = Collections.checkedList(new ArrayList<Instruction>(), Instruction.class);

            for (Atributo attr : attrs) {
                Object obj = (attr == null) ? null : attr.valor();
                if (obj instanceof Instruction) {
                    code.add((Instruction) obj);

                } else if (obj instanceof List<?>) {
                    code.addAll((List<Instruction>) obj);

                } else if (obj instanceof Instruction[]) {
                    code.addAll(Arrays.asList((Instruction[]) obj));

                } else if (obj instanceof Atributo[]) {
                    code.addAll(eval((Atributo[]) obj));
                }
            }

            return Collections.unmodifiableList(code);
        }
    };

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
        TAtributos attr = atributosPara("Program", "tsh", "err", "cod", "dirh");

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
        calculo(attr.a("err"), SEMFUN_ERRORS);

        // Program.cod
        dependencias(attr.a("cod"), sSubprogs.a("etq"), sSubprogs.a("cod"), sInsts.a("cod"));
        calculo(attr.a("cod"), new SemFun() {
            @Override
            public Object eval (Atributo... attrs) {
                return SEMFUN_CONCAT.eval(
                    a(new JumpInstruction((Integer) (attrs[0].valor()))),
                    a(Arrays.asList(Arrays.copyOfRange(attrs, 1, attrs.length))), a(new StopInstruction()));
            }
        });

        // SConsts.tsh
        dependencias(sConsts.a("tsh"), attr.a("tsh"));
        calculo(sConsts.a("tsh"), SEMFUN_ASIGNATION);

        // STypes.tsh
        dependencias(sTypes.a("tsh"), sConsts.a("ts"));
        calculo(sTypes.a("tsh"), SEMFUN_ASIGNATION);

        // SVars.tsh
        dependencias(sVars.a("tsh"), sTypes.a("ts"));
        calculo(sVars.a("tsh"), SEMFUN_ASIGNATION);

        // SSubprogs.tsh
        dependencias(sSubprogs.a("tsh"), sVars.a("ts"));
        calculo(sSubprogs.a("tsh"), SEMFUN_ASIGNATION);

        // SInsts.tsh
        dependencias(sInsts.a("tsh"), sSubprogs.a("ts"));
        calculo(sInsts.a("tsh"), SEMFUN_ASIGNATION);

        // Program.ts
        dependencias(attr.a("ts"), sInsts.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        // SSubprogs.etqh
        dependencias(sSubprogs.a("etqh"), a(1));
        calculo(sSubprogs.a("etqh"), SEMFUN_ASIGNATION);

        // SInsts.etqh
        dependencias(sInsts.a("etqh"), sSubprogs.a("etq"));
        calculo(sInsts.a("etqh"), SEMFUN_ASIGNATION);

        // Program.dirh = 2
        dependencias(attr.a("dirh"), a(2));
        calculo(attr.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(sVars.a("dirh"), attr.a("dirh"));
        calculo(sVars.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(sSubprogs.a("dirh"), sVars.a("dir"));
        calculo(sSubprogs.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(sInsts.a("dirh"), sSubprogs.a("dir"));
        calculo(sInsts.a("dirh"), SEMFUN_ASIGNATION);

        // Program.err
        dependencias(
            attr.a("err"), sConsts.a("err"), sTypes.a("err"), sVars.a("err"), sSubprogs.a("err"), sInsts.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // SConsts

    public TAtributos sConsts_R1 (TAtributos consts) {
        regla("SConst -> CONSTS ILLAVE Consts FLLAVE");
        TAtributos attr = atributosPara("SConst", "tsh", "ts", "err");

        dependencias(consts.a("tsh"), attr.a("tsh"));
        calculo(consts.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), consts.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), consts.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos sConsts_R2 () {
        regla("SConst -> $");
        TAtributos attr = atributosPara("SConst", "ts", "err", "etqh");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // Consts

    public TAtributos consts_R1 (TAtributos consts_1, TAtributos cons) {
        regla("Consts -> Consts PYC Const");
        TAtributos attr = atributosPara("Consts", "tsh", "ts", "err");

        dependencias(consts_1.a("tsh"), attr.a("tsh"));
        calculo(consts_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(cons.a("tsh"), consts_1.a("ts"));
        calculo(cons.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), cons.a("ts"), cons.a("id"), cons.a("valor"), cons.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {

                SymbolTable table = (SymbolTable) args[0].valor();
                Lexeme ident = (Lexeme) args[1].valor();
                Value value = (Value) args[2].valor();
                Type type = (Type) args[3].valor();

                table.putConstant(ident.getLexeme(), type, value);

                return table;
            }
        });

        dependencias(attr.a("err"), cons.a("ts"), cons.a("id"));
        calculo(attr.a("err"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable st = (SymbolTable) args[0].valor();
                Symbol ident = (Symbol) args[1].valor();

                String str = (String) ident.value;

                if (st.hasIdentifier(str)) {
                    return new DuplicateIdentifierError(str, -1, -1);
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
        calculo(cons.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), cons.a("ts"), cons.a("id"), cons.a("valor"), cons.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                SymbolTable st = (SymbolTable) args[0].valor();
                Symbol ident = (Symbol) args[1].valor();
                Value value = (Value) args[2].valor();
                Type type = (Type) args[3].valor();

                st.putConstant((String) ident.value, type, value);

                return st;
            }
        });

        dependencias(attr.a("err"), cons.a("ts"), cons.a("id"));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Const

    public TAtributos const_R1 (TAtributos tPrim, Symbol ident, TAtributos lit) {
        regla("Const -> CONST TPrim IDENT ASIG ConstLit");
        TAtributos attr = atributosPara("Const", "tsh", "ts", "id", "tipo", "err", "valor");
        LAtributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), SEMFUN_ASIGNATION);

        dependencias(attr.a("tipo"), tPrim.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);

        dependencias(attr.a("valor"), tPrim.a("valor"));
        calculo(attr.a("valor"), SEMFUN_ASIGNATION);

        // TODO para dani, hacer el error
        // Const.err = ¬(compatibles(TPrim.tipo, ConstLit.tipo))

        return attr;
    }

    public TAtributos const_R2 () {
        regla("Const -> $");
        TAtributos attr = atributosPara("Const", "ts", "tsh", "err", "dir", "dirh");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // ConstLit

    public TAtributos constLit_R1 (TAtributos lit) {
        regla("ConstLit -> Lit");
        TAtributos attr = atributosPara("ConstLit", "valor", "tipo", "err");

        dependencias(attr.a("valor"), lit.a("valor"));
        calculo(attr.a("valor"), SEMFUN_ASIGNATION);

        dependencias(attr.a("tipo"), lit.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos constLit_R2 (TAtributos lit) {
        regla("ConstLit -> MENOS Lit");
        TAtributos attr = atributosPara("ConstLit", "tipo", "valor", "err");

        dependencias(attr.a("tipo"), lit.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);

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
        calculo(attr.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(types.a("tsh"), attr.a("tsh"));
        calculo(types.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), types.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), types.a("dir"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), types.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos sTypes_R2 () {
        regla("STypes -> $");
        TAtributos attr = atributosPara("STypes", "tsh", "ts", "dir", "dirh", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // Types

    public TAtributos types_R1 (TAtributos types_1, TAtributos type) {
        regla("Types -> Types PYC Type");
        TAtributos attr = atributosPara("Types", "tsh", "ts", "dir", "dirh", "err");

        dependencias(types_1.a("tsh"), attr.a("tsh"));
        calculo(types_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(types_1.a("dirh"), attr.a("dirh"));
        calculo(types_1.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(type.a("tsh"), attr.a("ts"));
        calculo(type.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(type.a("dirh"), types_1.a("dir"));
        calculo(type.a("dirh"), SEMFUN_ASIGNATION);

        // DANI for Daniel Escoz
        // Types0.dir = Type.dir + desplazamiento(Type.tipo, Types.id)

        dependencias(attr.a("ts"), types_1.a("ts"), type.a("id"), type.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                ts.putType((String) args[1].valor(), (Type) args[2].valor());

                return ts;
            }
        });

        dependencias(attr.a("err"), types_1.a("ts"), type.a("id"));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    public TAtributos types_R2 (TAtributos type) {
        regla("Types -> Type");
        TAtributos attr = atributosPara("Types", "tsh", "dirh", "ts", "dir", "err");

        dependencias(type.a("tsh"), attr.a("tsh"));

        dependencias(attr.a("ts"), type.a("ts"), type.a("id"), type.a("tipo"));
        dependencias(type.a("err"), type.a("ts"), type.a("id"));

        calculo(type.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(type.a("dirh"), attr.a("dirh"));
        calculo(type.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), type.a("ts"), type.a("id"), type.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                ts.putType((String) args[1].valor(), (Type) args[2].valor());

                return ts;
            }
        });

        // DANI for Daniel Escoz Solana
        // Types.dir = Type.dir + desplazamiento(Type.tipo, Type.id)

        dependencias(type.a("err"), type.a("ts"), type.a("id"));
        calculo(type.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Type

    public TAtributos type_R1 (TAtributos typeDesc, Symbol ident) {
        regla("Type -> TIPO TypeDesc IDENT");
        TAtributos attr = atributosPara("Type", "ts", "dir", "dirh", "id", "clase", "nivel", "tipo");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        Atributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);
        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), SEMFUN_ASIGNATION);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);
        // Type.tipo = <t:TypeDesc.tipo, tipo:obtieneCTipo(TypeDesc), tam:desplazamiento(obtieneCTipo(TypeDesc),
        // Type.id)>

        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos type_R2 () {
        regla("Type -> $");
        TAtributos attr = atributosPara("Type", "ts", "tsh", "dir", "dirh", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // SVars

    public TAtributos sVars_R1 (TAtributos vars) {
        regla("SVars -> VARS ILLAVE Vars FLLAVE");
        TAtributos attr = atributosPara("SVars", "tsh", "ts", "dirh", "dir", "err");

        dependencias(vars.a("tsh"), attr.a("tsh"));
        calculo(vars.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), vars.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(vars.a("dirh"), attr.a("dirh"));
        calculo(vars.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), vars.a("dir"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), vars.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos sVars_R2 () {
        regla("SVars -> $");
        TAtributos attr = atributosPara("SVars", "ts", "tsh", "dir", "dirh", "err");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // Vars

    public TAtributos vars_R1 (TAtributos vars_1, TAtributos var) {
        regla("Vars -> Vars PYC Var");
        TAtributos attr = atributosPara("Vars", "tsh", "ts", "err", "dir", "dirh");

        dependencias(vars_1.a("tsh"), attr.a("tsh"));
        calculo(vars_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(var.a("tsh"), vars_1.a("ts"));
        calculo(var.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), var.a("ts"), var.a("id"), var.a("nivel"), attr.a("dir"), var.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                String ident = (String) args[1].valor();
                Scope scope = (Scope) args[2].valor();
                int address = (int) args[3].valor();
                Type type = (Type) args[4].valor();
                ts.putVariable(ident, scope, address, type);

                return ts;
            }
        });

        dependencias(vars_1.a("dirh"), attr.a("dirh"));
        calculo(vars_1.a("dirh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), vars_1.a("dir"), var.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    public TAtributos vars_R2 (TAtributos var) {
        regla("Vars -> Var");
        TAtributos attr = atributosPara("Vars", "tsh", "ts", "err", "dir", "dirh");

        dependencias(var.a("tsh"), attr.a("tsh"));
        calculo(var.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), attr.a("dirh"), var.a("tipo"));
        calculo(attr.a("dir"), new SemFun() {
            @Override
            public Object eval (Atributo... args) {
                int varDir = (Integer) args[0].valor();
                Type type = (Type) args[1].valor();

                return varDir + type.getSize();
            }
        });

        dependencias(attr.a("ts"), var.a("ts"), var.a("id"), var.a("nivel"), var.a("dir"), var.a("tipo"));
        calculo(attr.a("ts"), new SemFun() {

            @Override
            public Object eval (Atributo... args) {
                SymbolTable ts = (SymbolTable) args[0].valor();
                String ident = (String) args[1].valor();
                Scope scope = (Scope) args[2].valor();
                int address = (int) args[3].valor();
                Type type = (Type) args[4].valor();
                ts.putVariable(ident, scope, address, type);

                return ts;
            }
        });

        dependencias(attr.a("err"), var.a("ts"), var.a("id"), var.a("nivel"));
        calculo(attr.a("err"), CheckDuplicateIdentifierFun.INSTANCE);

        return attr;
    }

    // Var

    public TAtributos var_R1 (TAtributos typeDesc, Symbol ident) {
        regla("Var -> VAR TypeDesc IDENT");
        TAtributos attr = atributosPara("Var", "ts", "tsh", "id", "nivel", "tipo");

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        Atributo lexIdent = atributoLexicoPara("IDENT", "lex", ident);
        dependencias(attr.a("id"), lexIdent);
        calculo(attr.a("id"), SEMFUN_ASIGNATION);

        dependencias(attr.a("nivel"), a(Scope.GLOBAL));
        calculo(attr.a("nivel"), SEMFUN_ASIGNATION);

        dependencias(attr.a("tipo"), typeDesc.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);
        // Var.tipo = (si (TypeDesc.tipo == TPrim) {<t:TypeDesc.tipo, tam:1>}
        // si no {<t:ref, id:Var.id, tam: desplazamiento(TypeDesc.tipo, Var.id)>} )

        return attr;
    }

    public TAtributos var_R2 () {
        regla("Var -> $");
        TAtributos attr = atributosPara("Var", "ts", "tsh", "err");
        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // TypeDesc

    public TAtributos typeDesc_R1 (TAtributos tPrim) {
        regla("TypeDesc -> TPrim");
        TAtributos attr = atributosPara("TypeDesc");

        return attr;
    }

    public TAtributos typeDesc_R2 (TAtributos tArray) {
        regla("TypeDesc -> TArray");
        TAtributos attr = atributosPara("TypeDesc");

        return attr;
    }

    public TAtributos typeDesc_R3 (TAtributos tTupla) {
        regla("TypeDesc -> TTupla");
        TAtributos attr = atributosPara("TypeDesc");

        return attr;
    }

    public TAtributos typeDesc_R4 (Symbol ident) {
        regla("TypeDesc -> IDENT");
        TAtributos attr = atributosPara("TypeDesc");

        return attr;
    }

    // TPrim

    public TAtributos tPrim_R1 () {
        regla("TPrim -> NATURAL");
        TAtributos attr = atributosPara("TPrim");

        return attr;
    }

    public TAtributos tPrim_R2 () {
        regla("TPrim -> INTEGER");
        TAtributos attr = atributosPara("TPrim");

        return attr;
    }

    public TAtributos tPrim_R3 () {
        regla("TPrim -> FLOAT");
        TAtributos attr = atributosPara("TPrim");

        return attr;
    }

    public TAtributos tPrim_R4 () {
        regla("TPrim -> BOOLEAN");
        TAtributos attr = atributosPara("TPrim");

        return attr;
    }

    public TAtributos tPrim_R5 () {
        regla("TPrim -> CHARACTER");
        TAtributos attr = atributosPara("TPrim");

        return attr;
    }

    // Cast

    public TAtributos cast_R1 () {
        regla("Cast -> CHAR");
        TAtributos attr = atributosPara("Cast");

        return attr;
    }

    public TAtributos cast_R2 () {
        regla("Cast -> INT");
        TAtributos attr = atributosPara("Cast");

        return attr;
    }

    public TAtributos cast_R3 () {
        regla("Cast -> NAT");
        TAtributos attr = atributosPara("Cast");

        return attr;
    }

    public TAtributos cast_R4 () {
        regla("Cast -> FLOAT");
        TAtributos attr = atributosPara("Cast");

        return attr;
    }

    // TArray

    public TAtributos tArray_R1 (TAtributos typeDesc, Symbol ident) {
        regla("TArray -> TypeDesc ICORCHETE IDENT FCORCHETE");
        TAtributos attr = atributosPara("TArray");

        return attr;
    }

    public TAtributos tArray_R2 (TAtributos typeDesc, Symbol litnat) {
        regla("TArray -> TypeDesc ICORCHETE LITNAT FCORCHETE");
        TAtributos attr = atributosPara("TArray");

        return attr;
    }

    // TTupla

    public TAtributos tTupla_R1 (TAtributos tupla) {
        regla("TTupla -> IPAR Tupla FPAR");
        TAtributos attr = atributosPara("TTupla");

        return attr;
    }

    public TAtributos tTupla_R2 () {
        regla("TTupla -> IPAR FPAR");
        TAtributos attr = atributosPara("TTupla");

        return attr;
    }

    // Tupla

    public TAtributos tupla_R1 (TAtributos typeDesc, TAtributos tupla_1) {
        regla("Tupla -> TypeDesc COMA Tupla");
        TAtributos attr = atributosPara("Tupla");

        return attr;
    }

    public TAtributos tupla_R2 (TAtributos typeDesc) {
        regla("Tupla -> TypeDesc");
        TAtributos attr = atributosPara("Tupla");

        return attr;
    }

    // SInsts

    public TAtributos sInsts_R1 (TAtributos insts) {
        regla("SInsts -> INSTRUCTIONS ILLAVE Insts FLLAVE");
        TAtributos attr = atributosPara("SInsts", "cod", "etq", "etqh", "tsh", "err");

        dependencias(insts.a("tsh"), attr.a("tsh"));
        calculo(insts.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), insts.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), insts.a("cod"));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(insts.a("etqh"), attr.a("etqh"));
        calculo(insts.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), insts.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    // Insts

    public TAtributos insts_R1 (TAtributos insts_1, TAtributos inst) {
        regla("Insts -> Insts PYC Inst");
        TAtributos attr = atributosPara("Insts", "cod", "etqh", "etq", "tsh", "err");

        dependencias(insts_1.a("tsh"), attr.a("tsh"));
        calculo(insts_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(inst.a("tsh"), attr.a("tsh"));
        calculo(inst.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), insts_1.a("err"), inst.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), insts_1.a("cod"), inst.a("cod"));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(insts_1.a("etqh"), attr.a("etqh"));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(inst.a("etqh"), insts_1.a("etq"));
        calculo(inst.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), inst.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos insts_R2 (TAtributos inst) {
        regla("Insts -> Inst");
        TAtributos attr = atributosPara("Insts", "cod", "etqh", "etq", "tsh", "err");

        dependencias(inst.a("tsh"), attr.a("tsh"));
        calculo(inst.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), inst.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), inst.a("cod"));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(inst.a("etqh"), attr.a("etqh"));
        calculo(inst.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), inst.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    // Inst

    public TAtributos inst_R1 (TAtributos desig, TAtributos expr) {
        regla("Inst -> Desig ASIG Expr");
        TAtributos attr = atributosPara("Inst", "cod", "etqh", "etq", "tsh", "err");

        dependencias(desig.a("tsh"), attr.a("tsh"));
        calculo(desig.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), SEMFUN_ASIGNATION);


        // TODO for Daniel Escoz Solana. Producción:
        //     Inst.err = (¬asignacionValida(Desig.tipo, Expr.tipo)) ∨ Expr.err ∨ Desig.err

        // TODO for Daniel Escoz Solana.
        // dependencias(attr.a("cod"), expr.a("cod"), desig.a("dir"), a(new IndirectStoreInstruction()), a(new LoadInstruction(desig.a("dir").valor())));
        // calculo(attr.a("cod"), );

        dependencias(desig.a("etqh"), attr.a("etqh"));
        calculo(desig.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(expr.a("etqh"), desig.a("etq"));
        calculo(expr.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), expr.a("etq"));
        
        //TODO pal pecho de Dani
        calculo(attr.a("etq"), new SemFunWhatever!);

        return attr;
    }

    public TAtributos inst_R2 (TAtributos desig) {
        regla("Inst -> IN IPAR Desig FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "tsh", "err");

        dependencias(desig.a("tsh"), attr.a("tsh"));
        calculo(desig.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), desig.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), desig.a("tipo"), desig.a("dir"));
        // TODO
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(desig.a("etqh"), attr.a("etq"));
        // TODO calculo

        dependencias(attr.a("etq"), desig.a("etq"));
        // TODO

        return attr;
    }

    public TAtributos inst_R3 (TAtributos expr) {
        regla("Inst -> OUT IPAR Expr FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "tsh", "err");

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), expr.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), expr.a("cod"), a(new OutputInstruction()));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), expr.a("etqh"));
        // TODO sumar

        return attr;
    }

    public TAtributos inst_R4 () {
        regla("Inst -> SWAP1 IPAR FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "err");

        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), a(new Swap1Instruction()));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), attr.a("etqh"));
        // TODO sumar

        return attr;
    }

    public TAtributos inst_R5 () {
        regla("Inst -> SWAP2 IPAR FPAR");
        TAtributos attr = atributosPara("Inst", "cod", "etq", "etqh", "err");

        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), a(new Swap2Instruction()));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), attr.a("etqh"));
        // TODO suma

        return attr;
    }

    public TAtributos inst_R6 (TAtributos expr, TAtributos insts, TAtributos elseIf) {
        regla("Inst -> IF Expr THEN Insts ElseIf");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh");

        return attr;
    }

    public TAtributos inst_R7 (TAtributos expr, TAtributos insts) {
        regla("Inst -> WHILE Expr DO Insts ENDWHILE");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh");

        return attr;
    }

    public TAtributos inst_R8 (TAtributos instCall) {
        regla("Inst -> InstCall");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh");

        return attr;
    }

    public TAtributos inst_R9 () {
        regla("Inst -> $");
        TAtributos attr = atributosPara("Inst", "etqh", "etq", "tsh");

        return attr;
    }

    // ElseIf

    public TAtributos elseIf_R1 (TAtributos insts) {
        regla("ElseIf -> ELSE Insts ENDIF");
        TAtributos attr = atributosPara("ElseIf", "tsh", "err", "cod", "etq", "etqh");

        dependencias(insts.a("tsh"), attr.a("tsh"));
        calculo(insts.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), insts.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), insts.a("cod"));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(insts.a("etqh"), attr.a("etqh"));
        calculo(insts.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), insts.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos elseIf_R2 () {
        regla("ElseIf -> ENDIF");
        TAtributos attr = atributosPara("ElseIf", "err", "cod", "etq", "etqh");

        calculo(attr.a("err"), SEMFUN_ERRORS);

        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etqh"), SEMFUN_ASIGNATION);

        return attr;
    }

    // InstCall

    public TAtributos instCall_R1 (Symbol ident, TAtributos srParams) {
        regla("InstCall -> CALL IDENT IPAR SRParams FPAR");
        TAtributos attr = atributosPara("InstCall");

        return attr;
    }

    // SRParams

    public TAtributos srParams_R1 (TAtributos rParams) {
        regla("SRParams -> RParams");
        TAtributos attr =
            atributosPara("SRParams", "tsh", "err", "cod", "etq", "etqh", "nparams", "nparamsh", "nombresubprogh");

        dependencias(rParams.a("tsh"), attr.a("tsh"));
        calculo(rParams.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), rParams.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), rParams.a("cod"));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(rParams.a("etqh"), attr.a("etqh"));
        calculo(rParams.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), rParams.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParams.a("nparamsh"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams.a("nombresubprogh"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos srParams_R2 () {
        regla("SRParams -> $");
        TAtributos attr = atributosPara("SRParams", "err", "cod", "etqh", "etq", "nparamsh", "nparams");

        calculo(attr.a("err"), SEMFUN_ERRORS);

        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        dependencias(attr.a("nparams"), attr.a("nparamsh"));
        calculo(attr.a("nparams"), SEMFUN_ASIGNATION);

        return attr;
    }

    // RParams

    public TAtributos rParams_R1 (TAtributos rParams_1, TAtributos rParams) {
        regla("RParams -> RParams COMA RParam");
        TAtributos attr =
            atributosPara("RParams", "tsh", "err", "cod", "nparamsh", "nparams", "nombresubprogh", "etqh", "etq");

        dependencias(rParams_1.a("tsh"), attr.a("tsh"));
        calculo(rParams_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("tsh"), rParams_1.a("ts"));
        calculo(rParams.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), rParams.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), rParams_1.a("err"), rParams.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        calculo(attr.a("cod"), SEMFUN_CONCAT); // TODO Preguntar como se hacian las cod para concatenar

        dependencias(rParams_1.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParams_1.a("nparamsh"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("nparamsh"), rParams_1.a("nparams"));
        calculo(rParams.a("nparams"), SEMFUN_ASIGNATION);

        dependencias(attr.a("nparams"), rParams.a("nparams"));
        calculo(attr.a("nparams"), SEMFUN_ASIGNATION);

        dependencias(rParams_1.a("etqh"), attr.a("etqh"));
        calculo(rParams_1.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("etqh"), rParams_1.a("etq"));
        calculo(rParams.a("etq"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), rParams.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        dependencias(rParams_1.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams_1.a("nombresubprogh"), SEMFUN_ASIGNATION);

        dependencias(rParams.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParams.a("nombresubprogh"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos rParams_R2 (TAtributos rParam) {
        regla("RParams -> RParam");
        TAtributos attr =
            atributosPara("RParams", "tsh", "err", "cod", "etq", "etqh", "nparams", "nparamsh", "nombresubprogh");

        dependencias(rParam.a("tsh"), attr.a("tsh"));
        calculo(rParam.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("cod"), rParam.a("cod"));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(attr.a("err"), rParam.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(rParam.a("etqh"), attr.a("etqh"));
        calculo(rParam.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), rParam.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        dependencias(rParam.a("nparamsh"), attr.a("nparamsh"));
        calculo(rParam.a("nparamsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("nparams"), rParam.a("nparams"));
        calculo(attr.a("nparams"), SEMFUN_ASIGNATION);

        dependencias(rParam.a("nombresubprogh"), attr.a("nombresubprogh"));
        calculo(rParam.a("nombresubprogh"), SEMFUN_ASIGNATION);

        return attr;
    }

    // RParam

    public TAtributos rParam_R1 (Symbol ident, TAtributos expr) {
        regla("RParam -> IDENT ASIG Expr");
        TAtributos attr = atributosPara("RParam", "tsh", "cod", "etq", "etqh", "nparams", "nparamsh");

        dependencias(expr.a("tsh"), attr.a("tsh"));
        calculo(expr.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), expr.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS); // TODO ver como se hacen el atb error

        dependencias(expr.a("etqh"), attr.a("etqh"));
        calculo(expr.a("etqh"), new IncrementFun(6));

        dependencias(attr.a("etq"), expr.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        dependencias(attr.a("nparams"), attr.a("nparams")); // TODO lo de sumar las cosas
        calculo(attr.a("nparams"), SEMFUN_ASIGNATION);

        return attr;
    }

    // SSubprogs

    public TAtributos sSubprogs_R1 (TAtributos subprogs) {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE Subprogs FLLAVE");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh");

        dependencias(subprogs.a("tsh"), attr.a("tsh"));
        calculo(subprogs.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), subprogs.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), subprogs.a("cod"));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(subprogs.a("etqh"), attr.a("etqh"));
        calculo(subprogs.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos sSubprogs_R2 () {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE FLLAVE");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh");

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos sSubprogs_R3 () {
        regla("SSubprogs -> $");
        TAtributos attr = atributosPara("SSubprogs", "etqh", "etq", "tsh");

        calculo(attr.a("err"), SEMFUN_ERRORS);

        // TODO iniciar a lista vacía:
        // SSubprogs.cod = []
        // calculo(attr.a("cod"), );

        dependencias(attr.a("etq"), attr.a("etqh"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    // Subprogs

    public TAtributos subprogs_R1 (TAtributos subprogs_1, TAtributos subprog) {
        regla("Subprogs -> Subprogs Subprog");
        TAtributos attr = atributosPara("Subprogs");

        dependencias(subprogs_1.a("tsh"), attr.a("tsh"));
        calculo(subprogs_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(subprog.a("tsh"), attr.a("tsh"));
        calculo(subprog.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), subprogs_1.a("err"), subprog.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), subprogs_1.a("cod"), subprog.a("cod"));
        calculo(attr.a("cod"), SEMFUN_CONCAT);

        dependencias(subprogs_1.a("etqh"), attr.a("etqh"));
        calculo(subprogs_1.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(subprog.a("etqh"), subprogs_1.a("etq"));
        calculo(subprog.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), subprog.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    public TAtributos subprogs_R2 (TAtributos subprog) {
        regla("Subprogs -> Subprog");
        TAtributos attr = atributosPara("Subprogs");

        dependencias(subprog.a("tsh"), attr.a("tsh"));
        calculo(subprog.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), subprog.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        dependencias(attr.a("cod"), subprog.a("cod"));
        calculo(attr.a("cod"), SEMFUN_ASIGNATION);

        dependencias(subprog.a("etqh"), attr.a("etqh"));
        calculo(subprog.a("etqh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("etq"), subprog.a("etq"));
        calculo(attr.a("etq"), SEMFUN_ASIGNATION);

        return attr;
    }

    // Subprog

    public TAtributos subprog_R1 (Symbol ident, TAtributos sfParams, TAtributos sVars, TAtributos sInsts) {
        regla("Subprog -> SUBPROGRAM IDENT IPAR SFParams FPAR ILLAVE SVars SInsts FLLAVE");
        TAtributos attr = atributosPara("Subprog");

        // TODOTODOTODO ODOTODOTOD TODOTODOTO ODOTODOTOD
        // TODO TODO TODO TODO TODO TODO TODO
        // TODO TODO TODO TODO TODO TODO TODO
        // TODO TODO TODO TODO TODO TODO TODO
        // TODO ODOTODOTOD TODOTODOTO ODOTODOTOD

        // PD: Dani, paaaayoh, ta tocao.

        return attr;
    }

    // SFParams

    public TAtributos sfParams_R1 (TAtributos fParams) {
        regla("SFParams -> FParams");
        TAtributos attr = atributosPara("SFParams", "tsh", "ts", "dir", "err");

        // FParams
        dependencias(fParams.a("tsh"), attr.a("tsh"));
        calculo(fParams.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(attr.a("ts"), fParams.a("ts"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("dir"), fParams.a("dir"));
        calculo(attr.a("dir"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), fParams.a("err"));
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos sfParams_R2 () {
        regla("SFParams -> $");
        TAtributos attr = atributosPara("SFParams", "ts", "tsh", "err");

        // sfParams
        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    // FParams
    public TAtributos fParams_R1 (TAtributos fParams_1, TAtributos fParam) {
        regla("FParams -> FParams COMA FParam");
        TAtributos attr = atributosPara("FParams", "tsh", "ts", "err", "dir", "id", "clase", "tipo");

        dependencias(fParams_1.a("tsh"), attr.a("tsh"));
        calculo(fParams_1.a("tsh"), SEMFUN_ASIGNATION);

        dependencias(fParam.a("tsh"), fParams_1.a("tsh"));
        calculo(fParam.a("tsh"), SEMFUN_ASIGNATION);

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

    public TAtributos fParams_R2 (TAtributos fParam) {
        regla("FParams -> FParam");
        TAtributos attr = atributosPara("FParams", "tsh", "ts", "id", "dir", "tipo", "clase", "err");

        dependencias(fParam.a("tsh"), attr.a("tsh"));
        calculo(fParam.a("tsh"), SEMFUN_ASIGNATION);

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

    public TAtributos fParam_R1 (TAtributos typeDesc, Symbol ident) {
        regla("FParam -> TypeDesc IDENT");
        TAtributos attr = atributosPara("FParams", "ts", "tsh", "id", "clase", "tipo");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("id"), identLex);
        calculo(attr.a("id"), SEMFUN_ASIGNATION);

        dependencias(attr.a("clase"), a(ClassDec.PARAM_VALUE));
        calculo(attr.a("clase"), SEMFUN_ASIGNATION);

        // dependencias(attr.a("tipo"), /*TODO*/);

        return attr;
    }

    public TAtributos fParam_R2 (TAtributos typeDesc, Symbol ident) {
        regla("FParam -> TypeDesc MUL IDENT");
        TAtributos attr = atributosPara("FParams", "ts", "tsh", "id", "clase", "tipo");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("ts"), attr.a("tsh"));
        calculo(attr.a("ts"), SEMFUN_ASIGNATION);

        dependencias(attr.a("id"), identLex);
        calculo(attr.a("id"), SEMFUN_ASIGNATION);

        dependencias(attr.a("clase"), a(ClassDec.VARIABLE));
        calculo(attr.a("clase"), SEMFUN_ASIGNATION);

        // dependencias(attr.a("tipo"), /*TODO*/);

        return attr;
    }

    // Desig

    public TAtributos desig_R1 (Symbol ident) {
        regla("Desig -> IDENT");
        TAtributos attr = atributosPara("Desig", "tipo", "err", "tsh", "etq", "cod");
        Atributo identLex = atributoLexicoPara("IDENT", "lex", ident);

        dependencias(attr.a("tipo"), attr.a("tsh"), identLex);
        // DANI calculo(attr.a("tipo"), );

        dependencias(attr.a("err"), identLex, attr.a("tsh"));
        // DANI calculo(attr.a("err"), );

        // DANI dependencias y calculo de cod

        return attr;
    }

    public TAtributos desig_R2 (TAtributos desig_1, TAtributos expr) {
        regla("Desig -> Desig ICORCHETE Expr FCORCHETE");
        TAtributos attr = atributosPara("Desig", "tipo", "err");

        dependencias(attr.a("tipo"), desig_1.a("tipo"));
        calculo(attr.a("tipo"), SEMFUN_ASIGNATION);

        dependencias(attr.a("err"), desig_1.a("err"), expr.a("err")); // TODO Falta comprobar poner lo de
                                                                      // tamañoCorrecto()
        calculo(attr.a("err"), SEMFUN_ERRORS);

        return attr;
    }

    public TAtributos desig_R3 (TAtributos desig_1, Symbol litnat) {
        regla("Desig -> Desig BARRABAJA LITNAT");
        TAtributos attr = atributosPara("Desig");

        return attr;
    }

    // Expr

    public TAtributos expr_R1 (TAtributos term_1, TAtributos op0, TAtributos term_2) {
        regla("Expr -> Term Op0 Term");
        TAtributos attr = atributosPara("Expr");

        return attr;
    }

    public TAtributos expr_R2 (TAtributos term) {
        regla("Expr -> Term");
        TAtributos attr = atributosPara("Expr");

        return attr;
    }

    // Term

    public TAtributos term_R1 (TAtributos term_1, TAtributos op1, TAtributos fact) {
        regla("Term -> Term Op1 Fact");
        TAtributos attr = atributosPara("Term");

        return attr;
    }

    public TAtributos term_R2 (TAtributos term_1, TAtributos fact) {
        regla("Term -> Term OR Fact");
        TAtributos attr = atributosPara("Term");

        return attr;
    }

    public TAtributos term_R3 (TAtributos fact) {
        regla("Term -> Fact");
        TAtributos attr = atributosPara("Term");

        return attr;
    }

    // Fact

    public TAtributos fact_R1 (TAtributos fact_1, TAtributos op2, TAtributos shft) {
        regla("Fact -> Fact Op2 Shft");
        TAtributos attr = atributosPara("Fact");

        return attr;
    }

    public TAtributos fact_R2 (TAtributos fact_1, TAtributos shft) {
        regla("Fact -> Fact AND Shft");
        TAtributos attr = atributosPara("Fact");

        return attr;
    }

    public TAtributos fact_R3 (TAtributos shft) {
        regla("Fact -> Shft");
        TAtributos attr = atributosPara("Fact");

        return attr;
    }

    // Shft

    public TAtributos shft_R1 (TAtributos unary, TAtributos op3, TAtributos shft_1) {
        regla("Shft -> Unary Op3 Shft");
        TAtributos attr = atributosPara("Shft");

        return attr;
    }

    public TAtributos shft_R2 (TAtributos unary) {
        regla("Shft -> Unary");
        TAtributos attr = atributosPara("Shft");

        return attr;
    }

    // Unary

    public TAtributos unary_R1 (TAtributos op4, TAtributos unary_1) {
        regla("Unary -> Op4 Unary");
        TAtributos attr = atributosPara("Unary");

        return attr;
    }

    public TAtributos unary_R2 (TAtributos cast, TAtributos paren) {
        regla("Unary -> IPAR Cast FPAR Paren");
        TAtributos attr = atributosPara("Unary");

        return attr;
    }

    public TAtributos unary_R3 (TAtributos paren) {
        regla("Unary -> Paren");
        TAtributos attr = atributosPara("Unary");

        return attr;
    }

    // Paren

    public TAtributos paren_R1 (TAtributos expr) {
        regla("Paren -> IPAR Expr FPAR");
        TAtributos attr = atributosPara("Paren");

        return attr;
    }

    public TAtributos paren_R2 (TAtributos lit) {
        regla("Paren -> Lit");
        TAtributos attr = atributosPara("Paren");

        return attr;
    }

    public TAtributos paren_R3 (TAtributos desig) {
        regla("Paren -> Desig");
        TAtributos attr = atributosPara("Paren");

        return attr;
    }

    // Op0

    public TAtributos op0_R1 () {
        regla("Op0 -> IGUAL");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    public TAtributos op0_R2 () {
        regla("Op0 -> NOIGUAL");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    public TAtributos op0_R3 () {
        regla("Op0 -> MAY");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    public TAtributos op0_R4 () {
        regla("Op0 -> MEN");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    public TAtributos op0_R5 () {
        regla("Op0 -> MENOIG");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    public TAtributos op0_R6 () {
        regla("Op0 -> MAYOIG");
        TAtributos attr = atributosPara("Op0");

        return attr;
    }

    // Op1

    public TAtributos op1_R1 () {
        regla("Op1 -> MENOS");
        TAtributos attr = atributosPara("Op1");

        return attr;
    }

    public TAtributos op1_R2 () {
        regla("Op1 -> MAS");
        TAtributos attr = atributosPara("Op1");

        return attr;
    }

    // Op2

    public TAtributos op2_R1 () {
        regla("Op2 -> MOD");
        TAtributos attr = atributosPara("Op2");

        return attr;
    }

    public TAtributos op2_R2 () {
        regla("Op2 -> DIV");
        TAtributos attr = atributosPara("Op2");

        return attr;
    }

    public TAtributos op2_R3 () {
        regla("Op2 -> MUL");
        TAtributos attr = atributosPara("Op2");

        return attr;
    }

    // Op3

    public TAtributos op3_R1 () {
        regla("Op3 -> LSH");
        TAtributos attr = atributosPara("Op3");

        return attr;
    }

    public TAtributos op3_R2 () {
        regla("Op3 -> RSH");
        TAtributos attr = atributosPara("Op3");

        return attr;
    }

    // Op4

    public TAtributos op4_R1 () {
        regla("Op4 -> NOT");
        TAtributos attr = atributosPara("Op4");

        return attr;
    }

    public TAtributos op4_R2 () {
        regla("Op4 -> MENOS");
        TAtributos attr = atributosPara("Op4");

        return attr;
    }

    // Lit

    public TAtributos lit_R1 (TAtributos litBool) {
        regla("Lit -> LitBool");
        TAtributos attr = atributosPara("Lit");

        return attr;
    }

    public TAtributos lit_R2 (TAtributos litNum) {
        regla("Lit -> LitNum");
        TAtributos attr = atributosPara("Lit");

        return attr;
    }

    public TAtributos lit_R3 (Symbol litchar) {
        regla("Lit -> LITCHAR");
        TAtributos attr = atributosPara("Lit");

        return attr;
    }

    // LitBool

    public TAtributos litBool_R1 () {
        regla("LitBool -> TRUE");
        TAtributos attr = atributosPara("LitBool");

        return attr;
    }

    public TAtributos litBool_R2 () {
        regla("LitBool -> FALSE");
        TAtributos attr = atributosPara("LitBool");

        return attr;
    }

    // LitNum

    public TAtributos litNum_R1 (Symbol litnat) {
        regla("LitNum -> LITNAT");
        TAtributos attr = atributosPara("LitNum");

        return attr;
    }

    public TAtributos litNum_R2 (Symbol litfloat) {
        regla("LitNum -> LITFLOAT");
        Atributo litfloatLex = atributoLexicoPara("LITFLOAT", "lex", litfloat);
        TAtributos attr = atributosPara("LitNum");

        return attr;
    }

}
