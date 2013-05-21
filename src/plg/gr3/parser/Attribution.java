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
    
    public void program_R1 (
        TAtributos sConsts, TAtributos sTypes, TAtributos sVars, TAtributos sSubprogs, TAtributos sInsts)
    {
    }
    
    // SConsts
    
    public TAtributos sConsts_R1 (TAtributos consts) {
        return null;
    }
    
    public TAtributos sConsts_R2 () {
        return null;
    }
    
    // Consts
    
    public TAtributos consts_R1 (TAtributos consts_1, TAtributos cons) {
        return null;
    }
    
    public TAtributos consts_R2 (TAtributos cons) {
        return null;
    }
    
    // Const
    
    public TAtributos const_R1 (TAtributos tPrim, String ident, TAtributos lit) {
        return null;
    }
    
    public TAtributos const_R2 () {
        return null;
    }
    
    // STypes
    
    public TAtributos sTypes_R1 (TAtributos types) {
        return null;
    }
    
    public TAtributos sTypes_R2 () {
        return null;
    }
    
    // Types
    
    public TAtributos types_R1 (TAtributos types_1, TAtributos type) {
        return null;
    }
    
    public TAtributos types_R2 (TAtributos type) {
        return null;
    }
    
    // Type
    
    public TAtributos type_R1 (TAtributos typeDesc, String ident) {
        return null;
    }
    
    public TAtributos type_R2 () {
        return null;
    }
    
    // SVars
    
    public TAtributos sVars_R1 (TAtributos vars) {
        return null;
    }
    
    public TAtributos sVars_R2 () {
        return null;
    }
    
    // Vars
    
    public TAtributos vars_R1 (TAtributos vars_1, TAtributos var) {
        return null;
    }
    
    public TAtributos vars_R2 (TAtributos var) {
        return null;
    }
    
    // Var
    
    public TAtributos var_R1 (TAtributos typeDesc, String ident) {
        return null;
    }
    
    public TAtributos var_R2 () {
        return null;
    }
    
    // TypeDesc
    
    public TAtributos typeDesc_R1 (TAtributos tPrim) {
        return null;
    }
    
    public TAtributos typeDesc_R2 (TAtributos tArray) {
        return null;
    }
    
    public TAtributos typeDesc_R3 (TAtributos tTupla) {
        return null;
    }
    
    public TAtributos typeDesc_R4 (String ident) {
        return null;
    }
    
    // TPrim
    
    public TAtributos tPrim_R1 () {
        return null;
    }
    
    public TAtributos tPrim_R2 () {
        return null;
    }
    
    public TAtributos tPrim_R3 () {
        return null;
    }
    
    public TAtributos tPrim_R4 () {
        return null;
    }
    
    public TAtributos tPrim_R5 () {
        return null;
    }
    
    // Cast
    
    public TAtributos cast_R1 () {
        return null;
    }
    
    public TAtributos cast_R2 () {
        return null;
    }
    
    public TAtributos cast_R3 () {
        return null;
    }
    
    public TAtributos cast_R4 () {
        return null;
    }
    
    // TArray
    
    public TAtributos tArray_R1 (TAtributos typeDesc, String ident) {
        return null;
    }
    
    public TAtributos tArray_R2 (TAtributos typeDesc, String litnat) {
        return null;
    }
    
    // TTupla
    
    public TAtributos tTupla_R1 (TAtributos tupla) {
        return null;
    }
    
    public TAtributos tTupla_R2 () {
        return null;
    }
    
    // Tupla
    
    public TAtributos tupla_R1 (TAtributos typeDesc, TAtributos tupla_1) {
        return null;
    }
    
    public TAtributos tupla_R2 (TAtributos typeDesc) {
        return null;
    }
    
    // SInsts
    
    public TAtributos sInsts_R1 (TAtributos insts) {
        return null;
    }
    
    // Insts
    
    public TAtributos insts_R1 (TAtributos insts_1, TAtributos inst) {
        return null;
    }
    
    public TAtributos insts_R2 (TAtributos inst) {
        return null;
    }
    
    // Inst
    
    public TAtributos inst_R1 (TAtributos desig, TAtributos expr) {
        return null;
    }
    
    public TAtributos inst_R2 (TAtributos desig) {
        return null;
    }
    
    public TAtributos inst_R3 (TAtributos expr) {
        return null;
    }
    
    public TAtributos inst_R4 () {
        return null;
    }
    
    public TAtributos inst_R5 () {
        return null;
    }
    
    public TAtributos inst_R6 (TAtributos expr, TAtributos insts, TAtributos elseIf) {
        return null;
    }
    
    public TAtributos inst_R7 (TAtributos expr, TAtributos insts) {
        return null;
    }
    
    public TAtributos inst_R8 (TAtributos instCall) {
        return null;
    }
    
    public TAtributos inst_R9 () {
        return null;
    }
    
    // ElseIf
    
    public TAtributos elseIf_R1 (TAtributos insts) {
        return null;
    }
    
    public TAtributos elseIf_R2 () {
        return null;
    }
    
    // InstCall
    
    public TAtributos instCall_R1 (String ident, TAtributos srParams) {
        return null;
    }
    
    // SRParams
    
    public TAtributos srParams_R1 (TAtributos rParams) {
        return null;
    }
    
    public TAtributos srParams_R2 () {
        return null;
    }
    
    // RParams
    
    public TAtributos rParams_R1 (TAtributos rParams_1, TAtributos rParams) {
        return null;
    }
    
    public TAtributos rParams_R2 (TAtributos rParam) {
        return null;
    }
    
    // RParam
    
    public TAtributos rParam_R1 (String ident, TAtributos expr) {
        return null;
    }
    
    // SSubprogs
    
    public TAtributos sSubprogs_R1 (TAtributos subprogs) {
        return null;
    }
    
    public TAtributos sSubprogs_R2 () {
        return null;
    }
    
    public TAtributos sSubprogs_R3 () {
        return null;
    }
    
    // Subprogs
    
    public TAtributos subprogs_R1 (TAtributos subprogs_1, TAtributos subprog) {
        return null;
    }
    
    public TAtributos subprogs_R2 (TAtributos subprog) {
        return null;
    }
    
    // Subprog
    
    public TAtributos subprog_R1 (String ident, TAtributos sfParams, TAtributos sVars, TAtributos sInsts) {
        return null;
    }
    
    // SFParams
    
    public TAtributos sfParams_R1 (TAtributos fParams) {
        return null;
    }
    
    public TAtributos sfParams_R2 () {
        return null;
    }
    
    // FParams
    
    public TAtributos fParams_R1 (TAtributos fParams_1, TAtributos fParam) {
        return null;
    }
    
    public TAtributos fParams_R2 (TAtributos fParam) {
        return null;
    }
    
    // FParam
    
    public TAtributos fParam_R1 (TAtributos typeDesc, String ident) {
        return null;
    }
    
    public TAtributos fParam_R2 (TAtributos typeDesc, String ident) {
        return null;
    }
    
    // Desig
    
    public TAtributos desig_R1 (String ident) {
        return null;
    }
    
    public TAtributos desig_R2 (TAtributos desig_1) {
        return null;
    }
    
    public TAtributos desig_R3 (TAtributos desig_1, String litnat) {
        return null;
    }
    
    // Expr
    
    public TAtributos expr_R1 (TAtributos term_1, TAtributos op0, TAtributos term_2) {
        return null;
    }
    
    public TAtributos expr_R2 (TAtributos term) {
        return null;
    }
    
    // Term
    
    public TAtributos term_R1 (TAtributos term_1, TAtributos op1, TAtributos fact) {
        return null;
    }
    
    public TAtributos term_R2 (TAtributos fact) {
        return null;
    }
    
    // Fact
    
    public TAtributos fact_R1 (TAtributos fact_1, TAtributos op2, TAtributos shft) {
        return null;
    }
    
    public TAtributos fact_R2 (TAtributos shft) {
        return null;
    }
    
    // Shft
    
    public TAtributos shft_R1 (TAtributos unary, TAtributos op3, TAtributos shft_1) {
        return null;
    }
    
    public TAtributos shft_R2 (TAtributos unary) {
        return null;
    }
    
    // Unary
    
    public TAtributos unary_R1 (TAtributos op4, TAtributos unary_1) {
        return null;
    }
    
    public TAtributos unary_R2 (TAtributos cast, TAtributos paren) {
        return null;
    }
    
    public TAtributos unary_R3 (TAtributos paren) {
        return null;
    }
    
    // Paren
    
    public TAtributos paren_R1 (TAtributos expr) {
        return null;
    }
    
    public TAtributos paren_R2 (TAtributos lit) {
        return null;
    }
    
    public TAtributos paren_R3 (TAtributos desig) {
        return null;
    }
    
    // Op0
    
    public TAtributos op0_R1 () {
        return null;
    }
    
    public TAtributos op0_R2 () {
        return null;
    }
    
    public TAtributos op0_R3 () {
        return null;
    }
    
    public TAtributos op0_R4 () {
        return null;
    }
    
    public TAtributos op0_R5 () {
        return null;
    }
    
    public TAtributos op0_R6 () {
        return null;
    }
    
    // Op1
    
    public TAtributos op1_R1 () {
        return null;
    }
    
    public TAtributos op1_R2 () {
        return null;
    }
    
    public TAtributos op1_R3 () {
        return null;
    }
    
    // Op2
    
    public TAtributos op2_R1 () {
        return null;
    }
    
    public TAtributos op2_R2 () {
        return null;
    }
    
    public TAtributos op2_R3 () {
        return null;
    }
    
    public TAtributos op2_R4 () {
        return null;
    }
    
    // Op3
    
    public TAtributos op3_R1 () {
        return null;
    }
    
    public TAtributos op3_R2 () {
        return null;
    }
    
    // Op4
    
    public TAtributos op4_R1 () {
        return null;
    }
    
    public TAtributos op4_R2 () {
        return null;
    }
    
    // Lit
    
    public TAtributos lit_R1 (TAtributos litBool) {
        return null;
    }
    
    public TAtributos lit_R2 (TAtributos litNum) {
        return null;
    }
    
    public TAtributos lit_R3 (String litchar) {
        return null;
    }
    
    // LitBool
    
    public TAtributos litBool_R1 () {
        return null;
    }
    
    public TAtributos litBool_R2 () {
        return null;
    }
    
    // LitNum
    
    public TAtributos litNum_R1 (String litnat) {
        return null;
    }
    
    public TAtributos litNum_R2 (String litfloat) {
        return null;
    }
}
