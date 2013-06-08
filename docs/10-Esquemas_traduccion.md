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


SFParams ::= FParams {$$ = sfParams_R1($1);}
SFParams ::= {$$ = sfParams_R2();}

FParams ::= FParams COMA FParam {$$ = fParams_R1($1, $3);}
FParams ::= FParam {$$ = fParams_R2($1);}

FParam ::= TypeDesc IDENT {$$ = fParam_R1($1, $2.lex);}
FParam ::= TypeDesc MUL IDENT {$$ = fParam_R2($1, $3.lex);}

Desig ::= IDENT {$$ = desig_R1($1.lex)));}
Desig ::= Desig ICORCHETE Expr FCORCHETE {$$ = desig_R2($1, $3);}
Desig ::= Desig BARRABAJA LITNAT {$$ = desig_R3($1, $3.lex);}

Expr ::= Term Op0 Term {$$ = expr_R1($1, $2, $3);}
Expr ::= Term {$$ = expr_R2($1);}

Term ::= Term Op1 Fact {$$ = term_R1($1, $2, $3);}
Term ::= Term OR Fact {$$ = term_R2($1, $3);}
Term ::= Fact {$$ = term_R3($1);}

Fact ::= Fact Op2 Shft {$$ = fact_R1($1, $2, $3);}
Fact ::= Fact AND Shft {$$ = fact_R2($1, $3);}
Fact ::= Shft {$$ = fact_R3($1);}

Shft ::= Unary Op3 Shft {$$ = shft_R1($1, $2, $3);}
Shft ::= Unary {$$ = shft_R2($1);}

Unary ::= Op4 Unary {$$ = unary_R1($1, $2);}
Unary ::= IPAR Cast FPAR Paren {$$ = unary_R2($2, $4);}
Unary ::= Paren {$$ = unary_R3($1);}

Paren ::= IPAR Expr FPAR {$$ = paren_R1($2);}
Paren ::= Lit {$$ = paren_R2($1);}
Paren ::= Desig {$$ = paren_R3($1);}


Op0 ::= IGUAL {$$ = op0_R1();}
Op0 ::= NOIGUAL {$$ = op0_R2();}
Op0 ::= MEN {$$ = op0_R3();}
Op0 ::= MAY {$$ = op0_R4();}
Op0 ::= MENOIG {$$ = op0_R5();}
Op0 ::= MAYOIG {$$ = op0_R6();}

Op1 ::= MENOS {$$ = op1_R1();}
Op1 ::= MAS {$$ = op1_R2();}

Op2 ::= MOD {$$ = op2_R1();}
Op2 ::= DIV {$$ = op2_R2();}
Op2 ::= MUL {$$ = op2_R3();}

Op3 ::= LSH {$$ = op3_R1();}
Op3 ::= RSH {$$ = op3_R2();}

Op4 ::= NOT {$$ = op4_R1();}
Op4 ::= MENOS {$$ = op4_R2();}


Lit ::= LitBool {$$ = lit_R1($1);}
Lit ::= LitNum {$$ = lit_R2($1);}
Lit ::= LITCHAR {$$ = lit_R3($1.lex));}

LitBool ::= TRUE {$$ = litBool_R1();}
LitBool ::= FALSE {$$ = litBool_R2();}

LitNum ::= LITNAT {$$ = litNum_R1($1.lex);}
LitNum ::= LITFLOAT {$$ = litNum_R2($1.lex);}