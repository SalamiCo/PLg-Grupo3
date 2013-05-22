package plg.gr3.parser;

import es.ucm.fdi.plg.evlib.Atribucion;
import es.ucm.fdi.plg.evlib.TAtributos;

/**
 * Clase de los atributos del proyecto.
 * 
 * @author PLg Grupo 03 2012/2013
 */
@SuppressWarnings("javadoc")
public final class Attribution extends Atribucion {
    
    // Program
    
    public TAtributos program_R1 (
        TAtributos sConsts, TAtributos sTypes, TAtributos sVars, TAtributos sSubprogs, TAtributos sInsts)
    {
        regla("Program -> PROGRAM IDENT ILLAVE SConsts STypes SVars SSubprogs SInsts FLLAVE");
        return null;
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
        regla("Types -> TIPOS ILLAVE Types FLLAVE");
        return null;
    }
    
    public TAtributos sTypes_R2 () {
        regla("Types -> $");
        return null;
    }
    
    // Types
    
    public TAtributos types_R1 (TAtributos types_1, TAtributos type) {
        regla("Types -> Types PYC Type");
        return null;
    }
    
    public TAtributos types_R2 (TAtributos type) {
        regla("Types -> Type");
        return null;
    }
    
    // Type
    
    public TAtributos type_R1 (TAtributos typeDesc, String ident) {
        regla("Type -> TIPO TypeDesc IDENT");
        return null;
    }
    
    public TAtributos type_R2 () {
        regla("Type -> $");
        return null;
    }
    
    // SVars
    
    public TAtributos sVars_R1 (TAtributos vars) {
        regla("SVars -> VARS ILLAVE Vars FLLAVE");
        return null;
    }
    
    public TAtributos sVars_R2 () {
        regla("SVars -> $");
        return null;
    }
    
    // Vars
    
    public TAtributos vars_R1 (TAtributos vars_1, TAtributos var) {
        regla("Vars -> Vars PYC Var");
        return null;
    }
    
    public TAtributos vars_R2 (TAtributos var) {
        regla("Vars -> Var");
        return null;
    }
    
    // Var
    
    public TAtributos var_R1 (TAtributos typeDesc, String ident) {
        regla("Var -> VAR TypeDesc IDENT");
        return null;
    }
    
    public TAtributos var_R2 () {
        regla("Var -> $");
        return null;
    }
    
    // TypeDesc
    
    public TAtributos typeDesc_R1 (TAtributos tPrim) {
        regla("TypeDesc -> TPrim");
        return null;
    }
    
    public TAtributos typeDesc_R2 (TAtributos tArray) {
        regla("TypeDesc -> TArray");
        return null;
    }
    
    public TAtributos typeDesc_R3 (TAtributos tTupla) {
        regla("TypeDesc -> TTupla");
        return null;
    }
    
    public TAtributos typeDesc_R4 (String ident) {
        regla("TypeDesc -> IDENT");
        return null;
    }
    
    // TPrim
    
    public TAtributos tPrim_R1 () {
        regla("TPrim -> NATURAL");
        return null;
    }
    
    public TAtributos tPrim_R2 () {
        regla("TPrim -> INTEGER");
        return null;
    }
    
    public TAtributos tPrim_R3 () {
        regla("TPrim -> FLOAT");
        return null;
    }
    
    public TAtributos tPrim_R4 () {
        regla("TPrim -> BOOLEAN");
        return null;
    }
    
    public TAtributos tPrim_R5 () {
        regla("TPrim -> CHARACTER");
        return null;
    }
    
    // Cast
    
    public TAtributos cast_R1 () {
        regla("Cast -> CHAR");
        return null;
    }
    
    public TAtributos cast_R2 () {
        regla("Cast -> INT");
        return null;
    }
    
    public TAtributos cast_R3 () {
        regla("Cast -> NAT");
        return null;
    }
    
    public TAtributos cast_R4 () {
        regla("Cast -> FLOAT");
        return null;
    }
    
    // TArray
    
    public TAtributos tArray_R1 (TAtributos typeDesc, String ident) {
        regla("TArray -> TypeDesc ICORCHETE IDENT FCORCHETE");
        return null;
    }
    
    public TAtributos tArray_R2 (TAtributos typeDesc, String litnat) {
        regla("TArray -> TypeDesc ICORCHETE LITNAT FCORCHETE");
        return null;
    }
    
    // TTupla
    
    public TAtributos tTupla_R1 (TAtributos tupla) {
        regla("TTupla -> IPAR Tupla FPAR");
        return null;
    }
    
    public TAtributos tTupla_R2 () {
        regla("TTupla -> IPAR FPAR");
        return null;
    }
    
    // Tupla
    
    public TAtributos tupla_R1 (TAtributos typeDesc, TAtributos tupla_1) {
        regla("Tupla -> TypeDesc COMA Tupla");
        return null;
    }
    
    public TAtributos tupla_R2 (TAtributos typeDesc) {
        regla("Tupla -> TypeDesc");
        return null;
    }
    
    // SInsts
    
    public TAtributos sInsts_R1 (TAtributos insts) {
        regla("SInsts -> INSTRUCTIONS ILLAVE Insts FLLAVE");
        return null;
    }
    
    // Insts
    
    public TAtributos insts_R1 (TAtributos insts_1, TAtributos inst) {
        regla("Insts -> Insts PYC Inst");
        return null;
    }
    
    public TAtributos insts_R2 (TAtributos inst) {
        regla("Insts -> Inst");
        return null;
    }
    
    // Inst
    
    public TAtributos inst_R1 (TAtributos desig, TAtributos expr) {
        regla("Inst -> Desig ASIG Expr");
        return null;
    }
    
    public TAtributos inst_R2 (TAtributos desig) {
        regla("Inst -> IN IPAR Desig FPAR");
        return null;
    }
    
    public TAtributos inst_R3 (TAtributos expr) {
        regla("Inst -> OUT IPAR Expr FPAR");
        return null;
    }
    
    public TAtributos inst_R4 () {
        regla("Inst -> SWAP1 IPAR FPAR");
        return null;
    }
    
    public TAtributos inst_R5 () {
        regla("Inst -> SWAP2 IPAR FPAR");
        return null;
    }
    
    public TAtributos inst_R6 (TAtributos expr, TAtributos insts, TAtributos elseIf) {
        regla("Inst -> IF Expr THEN Insts ElseIf");
        return null;
    }
    
    public TAtributos inst_R7 (TAtributos expr, TAtributos insts) {
        regla("Inst -> WHILE Expr DO Insts ENDWHILE");
        return null;
    }
    
    public TAtributos inst_R8 (TAtributos instCall) {
        regla("Inst -> InstCall");
        return null;
    }
    
    public TAtributos inst_R9 () {
        regla("Inst -> $");
        return null;
    }
    
    // ElseIf
    
    public TAtributos elseIf_R1 (TAtributos insts) {
        regla("ElseIf -> ELSE Insts ENDIF");
        return null;
    }
    
    public TAtributos elseIf_R2 () {
        regla("ElseIf -> ENDIF");
        return null;
    }
    
    // InstCall
    
    public TAtributos instCall_R1 (String ident, TAtributos srParams) {
        regla("InstCall -> CALL IDENT IPAR SRParams FPAR");
        return null;
    }
    
    // SRParams
    
    public TAtributos srParams_R1 (TAtributos rParams) {
        regla("SRParams -> RParams");
        return null;
    }
    
    public TAtributos srParams_R2 () {
        regla("SRParams -> $");
        return null;
    }
    
    // RParams
    
    public TAtributos rParams_R1 (TAtributos rParams_1, TAtributos rParams) {
        regla("RParams -> RParams COMA RParam");
        return null;
    }
    
    public TAtributos rParams_R2 (TAtributos rParam) {
        regla("RParams -> RParam");
        return null;
    }
    
    // RParam
    
    public TAtributos rParam_R1 (String ident, TAtributos expr) {
        regla("RParam -> IDENT ASIG Expr");
        return null;
    }
    
    // SSubprogs
    
    public TAtributos sSubprogs_R1 (TAtributos subprogs) {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE Subprogs FLLAVE");
        return null;
    }
    
    public TAtributos sSubprogs_R2 () {
        regla("SSubprogs -> SUBPROGRAMS ILLAVE FLLAVE");
        return null;
    }
    
    public TAtributos sSubprogs_R3 () {
        regla("SSubprogs -> $");
        return null;
    }
    
    // Subprogs
    
    public TAtributos subprogs_R1 (TAtributos subprogs_1, TAtributos subprog) {
        regla("Subprogs -> Subprogs Subprog");
        return null;
    }
    
    public TAtributos subprogs_R2 (TAtributos subprog) {
        regla("Subprogs -> Subprog");
        return null;
    }
    
    // Subprog
    
    public TAtributos subprog_R1 (String ident, TAtributos sfParams, TAtributos sVars, TAtributos sInsts) {
        regla("Subprog -> SUBPROGRAM IDENT IPAR SFParams FPAR ILLAVE SVars SInsts FLLAVE");
        return null;
    }
    
    // SFParams
    
    public TAtributos sfParams_R1 (TAtributos fParams) {
        regla("SFParams -> FParams");
        return null;
    }
    
    public TAtributos sfParams_R2 () {
        regla("SFParams -> $");
        return null;
    }
    
    // FParams
    
    public TAtributos fParams_R1 (TAtributos fParams_1, TAtributos fParam) {
        regla("FParams -> FParams COMA FParam");
        return null;
    }
    
    public TAtributos fParams_R2 (TAtributos fParam) {
        regla("FParams -> FParam");
        return null;
    }
    
    // FParam
    
    public TAtributos fParam_R1 (TAtributos typeDesc, String ident) {
        regla("FParam -> TypeDesc IDENT");
        return null;
    }
    
    public TAtributos fParam_R2 (TAtributos typeDesc, String ident) {
        regla("FParams -> TypeDesc MUL IDENT");
        return null;
    }
    
    // Desig
    
    public TAtributos desig_R1 (String ident) {
        regla("Desig -> IDENT");
        return null;
    }
    
    public TAtributos desig_R2 (TAtributos desig_1, TAtributos expr) {
        regla("Desig -> Desig ICORCHETE Expr FCORCHETE");
        return null;
    }
    
    public TAtributos desig_R3 (TAtributos desig_1, String litnat) {
        regla("Desig -> Desig BARRABAJA LITNAT");
        return null;
    }
    
    // Expr
    
    public TAtributos expr_R1 (TAtributos term_1, TAtributos op0, TAtributos term_2) {
        regla("Expr -> Term Op0 Term");
        return null;
    }
    
    public TAtributos expr_R2 (TAtributos term) {
        regla("Expr -> Term");
        return null;
    }
    
    // Term
    
    public TAtributos term_R1 (TAtributos term_1, TAtributos op1, TAtributos fact) {
        regla("Term -> Term Op1 Fact");
        return null;
    }
    
    public TAtributos term_R2 (TAtributos fact) {
        regla("Term -> Fact");
        return null;
    }
    
    // Fact
    
    public TAtributos fact_R1 (TAtributos fact_1, TAtributos op2, TAtributos shft) {
        regla("Fact -> Fact Op2 Shft");
        return null;
    }
    
    public TAtributos fact_R2 (TAtributos shft) {
        regla("Fact -> Shft");
        return null;
    }
    
    // Shft
    
    public TAtributos shft_R1 (TAtributos unary, TAtributos op3, TAtributos shft_1) {
        regla("Shft -> Unary Op3 Shft");
        return null;
    }
    
    public TAtributos shft_R2 (TAtributos unary) {
        regla("Shft -> Unary");
        return null;
    }
    
    // Unary
    
    public TAtributos unary_R1 (TAtributos op4, TAtributos unary_1) {
        regla("Unary -> Op4 Unary");
        return null;
    }
    
    public TAtributos unary_R2 (TAtributos cast, TAtributos paren) {
        regla("Unary -> IPAR Cast FPAR Paren");
        return null;
    }
    
    public TAtributos unary_R3 (TAtributos paren) {
        regla("Unary -> Paren");
        return null;
    }
    
    // Paren
    
    public TAtributos paren_R1 (TAtributos expr) {
        regla("Paren -> IPAR Expr FPAR");
        return null;
    }
    
    public TAtributos paren_R2 (TAtributos lit) {
        regla("Paren -> Lit");
        return null;
    }
    
    public TAtributos paren_R3 (TAtributos desig) {
        regla("Paren -> Desig");
        return null;
    }
    
    // Op0
    
    public TAtributos op0_R1 () {
        regla("Op0 -> IGUAL");
        return null;
    }
    
    public TAtributos op0_R2 () {
        regla("Op0 -> NOIGUAL");
        return null;
    }
    
    public TAtributos op0_R3 () {
        regla("Op0 -> MAY");
        return null;
    }
    
    public TAtributos op0_R4 () {
        regla("Op0 -> MEN");
        return null;
    }
    
    public TAtributos op0_R5 () {
        regla("Op0 -> MENOIG");
        return null;
    }
    
    public TAtributos op0_R6 () {
        regla("Op0 -> MAYOIG");
        return null;
    }
    
    // Op1
    
    public TAtributos op1_R1 () {
        regla("Op1 -> OR");
        return null;
    }
    
    public TAtributos op1_R2 () {
        regla("Op1 -> MENOS");
        return null;
    }
    
    public TAtributos op1_R3 () {
        regla("Op1 -> MAS");
        return null;
    }
    
    // Op2
    
    public TAtributos op2_R1 () {
        regla("Op2 -> AND");
        return null;
    }
    
    public TAtributos op2_R2 () {
        regla("Op2 -> MOD");
        return null;
    }
    
    public TAtributos op2_R3 () {
        regla("Op2 -> DIV");
        return null;
    }
    
    public TAtributos op2_R4 () {
        regla("Op2 -> MUL");
        return null;
    }
    
    // Op3
    
    public TAtributos op3_R1 () {
        regla("Op3 -> LSH");
        return null;
    }
    
    public TAtributos op3_R2 () {
        regla("Op3 -> RSH");
        return null;
    }
    
    // Op4
    
    public TAtributos op4_R1 () {
        regla("Op4 -> NOT");
        return null;
    }
    
    public TAtributos op4_R2 () {
        regla("Op4 -> MENOS");
        return null;
    }
    
    // Lit
    
    public TAtributos lit_R1 (TAtributos litBool) {
        regla("Lit -> LitBool");
        return null;
    }
    
    public TAtributos lit_R2 (TAtributos litNum) {
        regla("Lit -> LitNum");
        return null;
    }
    
    public TAtributos lit_R3 (String litchar) {
        regla("Lit -> LITCHAR");
        return null;
    }
    
    // LitBool
    
    public TAtributos litBool_R1 () {
        regla("LitBool -> TRUE");
        return null;
    }
    
    public TAtributos litBool_R2 () {
        regla("LitBool -> FALSE");
        return null;
    }
    
    // LitNum
    
    public TAtributos litNum_R1 (String litnat) {
        regla("LitNum -> LITNAT");
        return null;
    }
    
    public TAtributos litNum_R2 (String litfloat) {
        regla("LitNum -> LITFLOAT");
        return null;
    }
}
