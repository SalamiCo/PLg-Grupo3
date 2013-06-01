package plg.gr3.parser;

import es.ucm.fdi.plg.evlib.Atribucion;
import es.ucm.fdi.plg.evlib.Atributo;
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
        public Atributo eval (Atributo... args) {
            if (args.length != 1) {
                throw new IllegalArgumentException();
            }
            return args[0];
        }
        
    };
    
    // Program
    
    public TAtributos program_R1 (
        TAtributos sConsts, TAtributos sTypes, TAtributos sVars, TAtributos sSubprogs, TAtributos sInsts)
    {
        regla("Program -> PROGRAM IDENT ILLAVE SConsts STypes SVars SSubprogs SInsts FLLAVE");
        
        TAtributos attr = atributosPara("Program", "tsh", "etqh", "err");
        calculo(attr.a("tsh"), new SemFun() {
            @Override
            public Atributo eval (Atributo... args) {
                return new SymbolTable();
            }
        });
        
        return attr;
    }
    
    // SConsts
    
    public TAtributos sConsts_R1 (TAtributos consts) {
        regla("SConst -> CONSTS ILLAVE Consts FLLAVE");
        return null;
    }
    
    public TAtributos sConsts_R2 () {
        regla("SConst -> $");
        return null;
    }
    
    // Consts
    
    public TAtributos consts_R1 (TAtributos consts_1, TAtributos cons) {
        regla("Const -> Consts PYC Const");
        return null;
    }
    
    public TAtributos consts_R2 (TAtributos cons) {
        regla("Consts -> Const");
        return null;
    }
    
    // Const
    
    public TAtributos const_R1 (TAtributos tPrim, String ident, TAtributos lit) {
        regla("Const -> CONST TPrim IDENT ASIG Lit");
        return null;
    }
    
    public TAtributos const_R2 () {
        regla("Const -> $");
        return null;
    }
    
    // STypes
    
    public TAtributos sTypes_R1 (TAtributos types) {
        regla("STypes -> TIPOS ILLAVE Types FLLAVE");
        
        TAtributos attr = atributosPara("STypes");
        return attr;
    }
    
    public TAtributos sTypes_R2 () {
        regla("STypes -> $");
        
        TAtributos attr = atributosPara("STypes");
        return attr;
    }
    
    // Types
    
    public TAtributos types_R1 (TAtributos types_1, TAtributos type) {
        regla("Types -> Types PYC Type");
        
        TAtributos attr = atributosPara("Types");
        return attr;
    }
    
    public TAtributos types_R2 (TAtributos type) {
        regla("Types -> Type");
        
        TAtributos attr = atributosPara("Types");
        return attr;
    }
    
    // Type
    
    public TAtributos type_R1 (TAtributos typeDesc, String ident) {
        regla("Type -> TIPO TypeDesc IDENT");
        
        TAtributos attr = atributosPara("Type");
        return attr;
    }
    
    public TAtributos type_R2 () {
        regla("Type -> $");
        
        TAtributos attr = atributosPara("Type");
        return attr;
    }
    
    // SVars
    
    public TAtributos sVars_R1 (TAtributos vars) {
        regla("SVars -> VARS ILLAVE Vars FLLAVE");
        
        TAtributos attr = atributosPara("SVars");
        return attr;
    }
    
    public TAtributos sVars_R2 () {
        regla("SVars -> $");
        
        TAtributos attr = atributosPara("SVars");
        return attr;
    }
    
    // Vars
    
    public TAtributos vars_R1 (TAtributos vars_1, TAtributos var) {
        regla("Vars -> Vars PYC Var");
        
        TAtributos attr = atributosPara("Vars");
        return attr;
    }
    
    public TAtributos vars_R2 (TAtributos var) {
        regla("Vars -> Var");
        
        TAtributos attr = atributosPara("Vars");
        return attr;
    }
    
    // Var
    
    public TAtributos var_R1 (TAtributos typeDesc, String ident) {
        regla("Var -> VAR TypeDesc IDENT");
        
        TAtributos attr = atributosPara("Var");
        return attr;
    }
    
    public TAtributos var_R2 () {
        regla("Var -> $");
        
        TAtributos attr = atributosPara("Var");
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
    
    public TAtributos typeDesc_R4 (String ident) {
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
    
    public TAtributos tArray_R1 (TAtributos typeDesc, String ident) {
        regla("TArray -> TypeDesc ICORCHETE IDENT FCORCHETE");
        
        TAtributos attr = atributosPara("TArray");
        return attr;
    }
    
    public TAtributos tArray_R2 (TAtributos typeDesc, String litnat) {
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
        
        TAtributos attr = atributosPara("SInsts");
        return attr;
    }
    
    // Insts
    
    public TAtributos insts_R1 (TAtributos insts_1, TAtributos inst) {
        regla("Insts -> Insts PYC Inst");
        
        TAtributos attr = atributosPara("Insts");
        return attr;
    }
    
    public TAtributos insts_R2 (TAtributos inst) {
        regla("Insts -> Inst");
        
        TAtributos attr = atributosPara("Insts");
        return attr;
    }
    
    // Inst
    
    public TAtributos inst_R1 (TAtributos desig, TAtributos expr) {
        regla("Inst -> Desig ASIG Expr");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R2 (TAtributos desig) {
        regla("Inst -> IN IPAR Desig FPAR");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R3 (TAtributos expr) {
        regla("Inst -> OUT IPAR Expr FPAR");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R4 () {
        regla("Inst -> SWAP1 IPAR FPAR");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R5 () {
        regla("Inst -> SWAP2 IPAR FPAR");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R6 (TAtributos expr, TAtributos insts, TAtributos elseIf) {
        regla("Inst -> IF Expr THEN Insts ElseIf");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R7 (TAtributos expr, TAtributos insts) {
        regla("Inst -> WHILE Expr DO Insts ENDWHILE");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R8 (TAtributos instCall) {
        regla("Inst -> InstCall");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    public TAtributos inst_R9 () {
        regla("Inst -> $");
        
        TAtributos attr = atributosPara("Inst");
        return attr;
    }
    
    // ElseIf
    
    public TAtributos elseIf_R1 (TAtributos insts) {
        regla("ElseIf -> ELSE Insts ENDIF");
        
        TAtributos attr = atributosPara("ElseIf");
        return attr;
    }
    
    public TAtributos elseIf_R2 () {
        regla("ElseIf -> ENDIF");
        
        TAtributos attr = atributosPara("ElseIf");
        return attr;
    }
    
    // InstCall
    
    public TAtributos instCall_R1 (String ident, TAtributos srParams) {
        regla("InstCall -> CALL IDENT IPAR SRParams FPAR");
        
        TAtributos attr = atributosPara("InstCall");
        return attr;
    }
    
    // SRParams
    
    public TAtributos srParams_R1 (TAtributos rParams) {
        regla("SRParams -> RParams");
        
        TAtributos attr = atributosPara("SRParams");
        return attr;
    }
    
    public TAtributos srParams_R2 () {
        regla("SRParams -> $");
        
        TAtributos attr = atributosPara("SRParams");
        return attr;
    }
    
    // RParams
    
    public TAtributos rParams_R1 (TAtributos rParams_1, TAtributos rParams) {
        regla("RParams -> RParams COMA RParam");
        
        TAtributos attr = atributosPara("RParams");
        return attr;
    }
    
    public TAtributos rParams_R2 (TAtributos rParam) {
        regla("RParams -> RParam");
        
        TAtributos attr = atributosPara("RParams");
        return attr;
    }
    
    // RParam
    
    public TAtributos rParam_R1 (String ident, TAtributos expr) {
        regla("RParam -> IDENT ASIG Expr");
        
        TAtributos attr = atributosPara("RParam");
        return attr;
    }
    
    // SSubprogs
    
    public TAtributos sSubprogs_R1 (TAtributos subprogs) {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE Subprogs FLLAVE");
        
        TAtributos attr = atributosPara("SSubprogs");
        return attr;
    }
    
    public TAtributos sSubprogs_R2 () {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE FLLAVE");
        
        TAtributos attr = atributosPara("SSubprogs");
        return attr;
    }
    
    public TAtributos sSubprogs_R3 () {
        regla("SSubprogs -> $");
        
        TAtributos attr = atributosPara("SSubprogs");
        return attr;
    }
    
    // Subprogs
    
    public TAtributos subprogs_R1 (TAtributos subprogs_1, TAtributos subprog) {
        regla("Subprogs -> Subprogs Subprog");
        
        TAtributos attr = atributosPara("Subprogs");
        return attr;
    }
    
    public TAtributos subprogs_R2 (TAtributos subprog) {
        regla("Subprogs -> Subprog");
        
        TAtributos attr = atributosPara("Subprogs");
        return attr;
    }
    
    // Subprog
    
    public TAtributos subprog_R1 (String ident, TAtributos sfParams, TAtributos sVars, TAtributos sInsts) {
        regla("Subprog -> SUBPROGRAM IDENT IPAR SFParams FPAR ILLAVE SVars SInsts FLLAVE");
        
        TAtributos attr = atributosPara("Subprog");
        return attr;
    }
    
    // SFParams
    
    public TAtributos sfParams_R1 (TAtributos fParams) {
        regla("SFParams -> FParams");
        
        TAtributos attr = atributosPara("SFParams");
        return attr;
    }
    
    public TAtributos sfParams_R2 () {
        regla("SFParams -> $");
        
        TAtributos attr = atributosPara("SFParams");
        return attr;
    }
    
    // FParams
    
    public TAtributos fParams_R1 (TAtributos fParams_1, TAtributos fParam) {
        regla("FParams -> FParams COMA FParam");
        
        TAtributos attr = atributosPara("FParams");
        return attr;
    }
    
    public TAtributos fParams_R2 (TAtributos fParam) {
        regla("FParams -> FParam");
        
        TAtributos attr = atributosPara("FParams");
        return attr;
    }
    
    // FParam
    
    public TAtributos fParam_R1 (TAtributos typeDesc, String ident) {
        regla("FParam -> TypeDesc IDENT");
        
        TAtributos attr = atributosPara("FParam");
        return attr;
    }
    
    public TAtributos fParam_R2 (TAtributos typeDesc, String ident) {
        regla("FParam -> TypeDesc MUL IDENT");
        
        TAtributos attr = atributosPara("FParams");
        return attr;
    }
    
    // Desig
    
    public TAtributos desig_R1 (String ident) {
        regla("Desig -> IDENT");
        
        TAtributos attr = atributosPara("Desig");
        return attr;
    }
    
    public TAtributos desig_R2 (TAtributos desig_1, TAtributos expr) {
        regla("Desig -> Desig ICORCHETE Expr FCORCHETE");
        
        TAtributos attr = atributosPara("Desig");
        return attr;
    }
    
    public TAtributos desig_R3 (TAtributos desig_1, String litnat) {
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
        return null;
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
    
    public TAtributos lit_R3 (String litchar) {
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
    
    public TAtributos litNum_R1 (String litnat) {
        regla("LitNum -> LITNAT");
        
        TAtributos attr = atributosPara("LitNum");
        return attr;
    }
    
    public TAtributos litNum_R2 (String litfloat) {
        regla("LitNum -> LITFLOAT");
        
        Atributo litfloatLex = atributoLexicoPara("LITFLOAT", "lex", litfloat);
        
        TAtributos attr = atributosPara("LitNum");
        return attr;
    }
}
