Program ::= PROGRAM IDENT ILLAVE SConsts STypes SVars SSubprogs SInsts FLLAVE
    {$$ = program_R1($4, $5, $6, $7, $8);}


SConsts ::= CONSTS ILLAVE Consts:consts FLLAVE{:
    RESULT = attr.sConsts_R1(consts);
:}
          | {:
    RESULT = attr.sConsts_R2();
:};

Consts ::= Consts:consts_1 PYC Const:cons {:
    RESULT = attr.consts_R1(consts_1, cons);
:}
         | Const:cons {:
    RESULT = attr.consts_R2(cons);
:};

Const ::= CONST TPrim:tPrim IDENT:ident ASIG ConstLit:lit {:
    RESULT = attr.const_R1(tPrim, (new Lexeme(ident, identleft, identright)), lit);
:}
        | {:
    RESULT = attr.const_R2();
:};

ConstLit ::= Lit:lit {:
    RESULT = attr.constLit_R1(lit);
:}
        | MENOS Lit:lit {:
    RESULT = attr.constLit_R2(lit);
:};


STypes ::= TIPOS ILLAVE Types:types FLLAVE {:
    RESULT = attr.sTypes_R1(types);
:}
         | {:
    RESULT = attr.sTypes_R2();
:};

Types ::= Types:types_1 PYC Type:type {:
    RESULT = attr.types_R1(types_1, type);
:}
        | Type:type {:
    RESULT = attr.types_R2(type);
:};

Type ::= TIPO TypeDesc:typeDesc IDENT:ident {:
    RESULT = attr.type_R1(typeDesc, new Lexeme(ident, identleft, identright));
:}
       | {:
    RESULT = attr.type_R2();
:};


SVars ::= VARS ILLAVE Vars:vars FLLAVE {:
    RESULT = attr.sVars_R1(vars);
:}
        | {:
    RESULT = attr.sVars_R2();
:};

Vars ::= Vars:vars_1 PYC Var:var {:
    RESULT = attr.vars_R1(vars_1, var);
:}
       | Var:var {:
    RESULT = attr.vars_R2(var);
:};

Var ::= VAR TypeDesc:typeDesc IDENT:ident {:
    RESULT = attr.var_R1(typeDesc, (new Lexeme(ident, identleft, identright)));
:}
      | {:
    RESULT = attr.var_R2();
:};

TypeDesc ::= TPrim:tPrim {:
    RESULT = attr.typeDesc_R1(tPrim);
:}
           | TArray:tArray {:
    RESULT = attr.typeDesc_R2(tArray);
:}
           | TTupla:tTupla {:
    RESULT = attr.typeDesc_R3(tTupla);
:}
           | IDENT:ident {:
    RESULT = attr.typeDesc_R4((new Lexeme(ident, identleft, identright)));
:};


TPrim ::= NATURAL {:
    RESULT = attr.tPrim_R1();
:}
        | INTEGER {:
    RESULT = attr.tPrim_R2();
:}
        | FLOAT {:
    RESULT = attr.tPrim_R3();
:}
        | BOOLEAN {:
    RESULT = attr.tPrim_R4();
:}
        | CHARACTER {:
    RESULT = attr.tPrim_R5();
:};

Cast ::= CHAR {:
    RESULT = attr.cast_R1();
:}
       | INT {:
    RESULT = attr.cast_R2();
:}
       | NAT {:
    RESULT = attr.cast_R3();
:}
       | FLOAT {:
    RESULT = attr.cast_R4();
:};


TArray ::= TypeDesc:typeDesc ICORCHETE IDENT:ident FCORCHETE {:
    RESULT = attr.tArray_R1(typeDesc, (new Lexeme(ident, identleft, identright)));
:}
         | TypeDesc:typeDesc ICORCHETE LITNAT:litnat FCORCHETE {:
    RESULT = attr.tArray_R2(typeDesc, new Lexeme(litnat, litnatleft, litnatright));
:};


TTupla ::= IPAR Tupla:tupla FPAR {:
    RESULT = attr.tTupla_R1(tupla);
:}
         | IPAR FPAR {:
    RESULT = attr.tTupla_R2();
:};

Tupla ::= TypeDesc:typeDesc COMA Tupla:tupla_1 {:
    RESULT = attr.tupla_R1(typeDesc, tupla_1);
:}
        | TypeDesc:typeDesc {:
    RESULT = attr.tupla_R2(typeDesc);
:};


SInsts ::= INSTRUCTIONS ILLAVE Insts:insts FLLAVE {:
    RESULT = attr.sInsts_R1(insts);
:};

Insts ::= Insts:insts_1 PYC Inst:inst {:
    RESULT = attr.insts_R1(insts_1, inst);
:}
        | Inst:inst {:
    RESULT = attr.insts_R2(inst);
:};

Inst ::= Desig:desig ASIG:asig Expr:expr {:
    RESULT = attr.inst_R1(desig, expr, new Lexeme(asig, asigleft, asigright));
:}
       | IN IPAR Desig:desig FPAR {:
    RESULT = attr.inst_R2(desig);
:}
       | OUT IPAR Expr:expr FPAR {:
    RESULT = attr.inst_R3(expr);
:}
       | SWAP1 IPAR FPAR {:
    RESULT = attr.inst_R4();
:}
       | SWAP2 IPAR FPAR {:
    RESULT = attr.inst_R5();
:}
       | IF Expr:expr THEN Insts:insts ElseIf:elseIf {:
    RESULT = attr.inst_R6(expr, insts, elseIf);
:}
       | WHILE Expr:expr DO Insts:insts ENDWHILE {:
    RESULT = attr.inst_R7(expr, insts);
:}
       | InstCall:instCall {:
    RESULT = attr.inst_R8(instCall);
:}
       | {:
    RESULT = attr.inst_R9();
:};


ElseIf ::= ELSE Insts:insts ENDIF {:
    RESULT = attr.elseIf_R1(insts);
:}
         | ENDIF {:
    RESULT = attr.elseIf_R2();
:};

InstCall ::= CALL IDENT:ident IPAR SRParams:srParams FPAR {:
    RESULT = attr.instCall_R1((new Lexeme(ident, identleft, identright)), srParams);
:};


SRParams ::= RParams:rParams {:
    RESULT = attr.srParams_R1(rParams);
:}
       | {:
    RESULT = attr.srParams_R2();
:};

RParams ::= RParams:rParams_1 COMA RParam:rParam {:
    RESULT = attr.rParams_R1(rParams_1, rParam);
:}
          | RParam:rParam {:
    RESULT = attr.rParams_R2(rParam);
:};

RParam ::= IDENT:ident ASIG Expr:expr {:
    RESULT = attr.rParam_R1((new Lexeme(ident, identleft, identright)), expr);
:};


SSubprogs ::= SUBPROGRAMS ILLAVE Subprogs:subprogs FLLAVE {:
    RESULT = attr.sSubprogs_R1(subprogs);
:}
            | SUBPROGRAMS ILLAVE FLLAVE {:
    RESULT = attr.sSubprogs_R2();
:}
            | {:
    RESULT = attr.sSubprogs_R3();
:};

Subprogs ::= Subprogs:subprogs_1 Subprog:subprog {:
    RESULT = attr.subprogs_R1(subprogs_1, subprog);
:}
           | Subprog:subprog {:
    RESULT = attr.subprogs_R2(subprog);
:};

Subprog ::= SUBPROGRAM IDENT:ident IPAR SFParams:sfParams FPAR ILLAVE SVars:sVars SInsts:sInsts FLLAVE {:
    RESULT = attr.subprog_R1((new Lexeme(ident, identleft, identright)), sfParams, sVars, sInsts);
:};


SFParams ::= FParams:fParams {:
    RESULT = attr.sfParams_R1(fParams);
:}
           | {:
    RESULT = attr.sfParams_R2();
:};

FParams ::= FParams:fParams_1 COMA FParam:fParam {:
    RESULT = attr.fParams_R1(fParams_1, fParam);
:}
          | FParam:fParam {:
    RESULT = attr.fParams_R2(fParam);
:};

FParam ::= TypeDesc:typedesc IDENT:ident {:
    RESULT = attr.fParam_R1(typedesc, (new Lexeme(ident, identleft, identright)));
:}
         | TypeDesc:typedesc MUL IDENT:ident {:
    RESULT = attr.fParam_R2(typedesc, (new Lexeme(ident, identleft, identright)));
:};


Desig ::= IDENT:ident {:
    RESULT = attr.desig_R1((new Lexeme(ident, identleft, identright)));
:}
        | Desig:desig_1 ICORCHETE Expr:expr FCORCHETE {:
    RESULT = attr.desig_R2(desig_1, expr);
:}
        | Desig:desig_1 BARRABAJA LITNAT:litnat {:
    RESULT = attr.desig_R3(desig_1, new Lexeme(litnat, litnatleft, litnatright));
:};


Expr ::= Term:term_1 Op0:op0 Term:term_2 {:
    RESULT = attr.expr_R1(term_1, op0, term_2);
:}
       | Term:term {:
    RESULT = attr.expr_R2(term);
:};


Term ::= Term:term_1 Op1:op1 Fact:fact {:
    RESULT = attr.term_R1(term_1, op1, fact);
:}
       | Term:term_1 OR Fact:fact {:
    RESULT = attr.term_R2(term_1, fact);
:}
       | Fact:fact {:
    RESULT = attr.term_R3(fact);
:};

Fact ::= Fact:fact_1 Op2:op3 Shft:shft {:
    RESULT = attr.fact_R1(fact_1, op3, shft);
:}
       | Fact:fact_1 AND Shft:shft {:
    RESULT = attr.fact_R2(fact_1, shft);
:}
       | Shft:shft {:
    RESULT = attr.fact_R3(shft);
:};

Shft ::= Unary:unary Op3:op3 Shft:shft {:
    RESULT = attr.shft_R1(unary, op3, shft);
:}
       | Unary:unary {:
    RESULT = attr.shft_R2(unary);
:};

Unary ::= Op4:op4 Unary:unary_1 {:
    RESULT = attr.unary_R1(op4, unary_1);
:}
        | IPAR Cast:cast FPAR Paren:paren {:
    RESULT = attr.unary_R2(cast, paren);
:}
        | Paren:paren {:
    RESULT = attr.unary_R3(paren);
:};

Paren ::= IPAR Expr:expr FPAR {:
    RESULT = attr.paren_R1(expr);
:}
        | Lit:lit {:
    RESULT = attr.paren_R2(lit);
:}
        | Desig:desig {:
    RESULT = attr.paren_R3(desig);
:};


Op0 ::= IGUAL {:
    RESULT = attr.op0_R1();
:}
      | NOIGUAL {:
    RESULT = attr.op0_R2();
:}
      | MEN {:
    RESULT = attr.op0_R3();
:}
      | MAY {:
    RESULT = attr.op0_R4();
:}
      | MENOIG {:
    RESULT = attr.op0_R5();
:}
      | MAYOIG {:
    RESULT = attr.op0_R6();
:};

Op1 ::= MENOS {:
    RESULT = attr.op1_R1();
:}
      | MAS {:
    RESULT = attr.op1_R2();
:};

Op2 ::= MOD {:
    RESULT = attr.op2_R1();
:}
      | DIV {:
    RESULT = attr.op2_R2();
:}
      | MUL {:
    RESULT = attr.op2_R3();
:};

Op3 ::= LSH {:
    RESULT = attr.op3_R1();
:}
      | RSH {:
    RESULT = attr.op3_R2();
:};

Op4 ::= NOT {:
    RESULT = attr.op4_R1();
:}
      | MENOS {:
    RESULT = attr.op4_R2();
:};


Lit ::= LitBool:litBool {:
    RESULT = attr.lit_R1(litBool);
:}
      | LitNum:litNum {:
    RESULT = attr.lit_R2(litNum);
:}
      | LITCHAR:litchar {:
    RESULT = attr.lit_R3(new Lexeme(litchar, litcharleft, litcharright));
:};

LitBool ::= TRUE {:
    RESULT = attr.litBool_R1();
:}
          | FALSE {:
    RESULT = attr.litBool_R2();
:};

LitNum ::= LITNAT:litnat {:
    RESULT = attr.litNum_R1(new Lexeme(litnat, litnatleft, litnatright));
:}
//| MENOS LITNAT {::}
         | LITFLOAT:litfloat {:
    RESULT = attr.litNum_R2(new Lexeme(litfloat,litfloatleft, litfloatright));
:};
//| MENOS LITFLOAT {::};