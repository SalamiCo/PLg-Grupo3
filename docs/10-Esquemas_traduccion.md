Program ::= PROGRAM IDENT ILLAVE SConsts STypes SVars SSubprogs SInsts FLLAVE
    {$$ = program_R1($4, $5, $6, $7, $8);}

SConsts ::= CONSTS ILLAVE Consts FLLAVE
    {$$ = sConsts_R1($3);}
SConsts ::=
    {$$ = sConsts_R1();}

Consts ::= Consts PYC Const
    {consts_R1($1, $3);}
Consts ::= Const
    {$$ = const_R2($1);}


Const ::= CONST TPrim IDENT ASIG ConstLit
    {$$ = const_R1($1, $3.lex, $5);}
Const ::= 
    {$$ = const_R2();}

ConstLit ::= Lit
    {$$ = constLit_R1($1);}
ConstLit ::= MENOS Lit
    {$$ = constLit_R1($1);}

STypes ::= TIPOS ILLAVE Types FLLAVE
    {$$ = sTypes_R1($3);}
STypes ::= 
    {$$ = sTypes_R2();}

Types ::= Types PYC Type
    {$$ = types_R1($1, $3);}
Types ::= Type
    {$$ = types_R2($1);}

Type ::= TIPO TypeDesc IDENT
    {$$ = type_R1($2, $3.lex);}
Type ::=
    {$$ = type_R2();}

SVars ::= VARS ILLAVE Vars FLLAVE
    {$$ = sVars_R1($3);}
SVars ::=
    {$$ = sVars_R2();}

Vars ::= Vars:vars_1 PYC Var
    {$$ = vars_R1($1, $3);}
Vars ::=
    {$$ = vars_R2($1);}

Var ::= VAR TypeDesc IDENT
    {$$ = var_R1($1, $2.lex);}
Var ::=
    {$$ = var_R2();}

TypeDesc ::= TPrim
    {$$ = typeDesc_R1($1);}
TypeDesc ::= TArray
    {$$ = typeDesc_R2($1);}
TypeDesc ::= TTupla
    {$$ = typeDesc_R3($1);}
TypeDesc ::= IDENT
    {$$ = typeDesc_R4($1.lex);}

TPrim ::= NATURAL
    {$$ = tPrim_R1();}
TPrim ::= INTEGER
    {$$ = tPrim_R2();}
TPrim ::= FLOAT
    {$$ = tPrim_R3();}
TPrim ::= BOOLEAN
    {$$ = tPrim_R4();}
TPrim ::= CHARACTER
    {$$ = tPrim_R5();}

Cast ::= CHAR
    {$$ = cast_R1();}
Cast ::= INT
    {$$ = cast_R2();}
Cast ::= NAT
    {$$ = cast_R3();}
Cast ::= FLOAT
    {$$ = cast_R4();}

TArray ::= TypeDesc ICORCHETE IDENT FCORCHETE
    {$$ = tArray_R1($1, $3.lex);}
TArray ::= TypeDesc ICORCHETE LITNAT FCORCHETE
    {$$ = tArray_R2($1, $3.lex);}

TTupla ::= IPAR Tupla FPAR
    {$$ = tTupla_R1($2);}
TTupla ::= IPAR FPAR
    {$$ = tTupla_R2();}

Tupla ::= TypeDesc COMA Tupla
    {$$ = tupla_R1($1, $3);}
Tupla ::= TypeDesc
    {$$ = tupla_R2($1);}

SInsts ::= INSTRUCTIONS ILLAVE Insts FLLAVE
    {$$ = sInsts_R1($3);}

Insts ::= InstsPYC Inst
    {$$ = insts_R1($1, $3);}
Insts ::= Inst
    {$$ = insts_R2($1);}

Inst ::= Desig ASIG Expr
    {$$ = inst_R1($1, $3, $2.lex);}
Inst ::= IN PAR Desig FPAR
    {$$ = inst_R2($3);}
Inst ::= OUT IPAR Expr FPAR
    {$$ = inst_R3($3);}
Inst ::= SWAP1 IPAR FPAR
    {$$ = inst_R4();}
Inst ::= SWAP2 IPAR FPAR
    {$$ = inst_R5();}
Inst ::= IF Expr THEN Insts ElseIf
    {$$ = inst_R6($2, $4, $5);}
Inst ::= WHILE Expr DO Insts ENDWHILE
    {$$ = inst_R7($2, $4);}
Inst ::= InstCall
    {$$ = inst_R8($1);}
Inst ::=
    {$$ = inst_R9();}

ElseIf ::= ELSE Insts ENDIF
    {$$ = elseIf_R1($2);}
ElseIf ::= ENDIF
    {$$ = elseIf_R2();}

InstCall ::= CALL IDENT IPAR SRParams FPAR
    {$$ = instCall_R1($2.lex, $4);}

SRParams ::= RParams
    {$$ = srParams_R1($1);}
SRParams ::=
    {$$ = srParams_R2();}

RParams ::= RParams COMA RParam
    {$$ = rParams_R1($1, $3);}
RParams ::= RParam
    {$$ = rParams_R2($1);}

RParam ::= IDENT ASIG Expr
    {$$ = rParam_R1($1.lex, $3);}

SSubprogs ::= SUBPROGRAMS ILLAVE Subprogs FLLAVE
    {$$ = sSubprogs_R1($3);}
SSubprogs ::= SUBPROGRAMS ILLAVE FLLAVE
    {$$ = sSubprogs_R2();}
SSubprogs ::=
    {$$ = sSubprogs_R3();}

Subprogs ::= Subprogs Subprog
    {$$ = subprogs_R1($1, $2);}
Subprogs ::= Subprog
    {$$ = subprogs_R2($1);}

Subprog ::= SUBPROGRAM IDENT IPAR SFParams FPAR ILLAVE SVars SInsts FLLAVE
    {$$ = subprog_R1($2.lex, $4, $7, $8);}

SFParams ::= FParams 
    {$$ = sfParams_R1($1);}
SFParams ::= 
    {$$ = sfParams_R2();}

FParams ::= FParams COMA FParam 
    {$$ = fParams_R1($1, $3);}
FParams ::= FParam 
    {$$ = fParams_R2($1);}

FParam ::= TypeDesc IDENT 
    {$$ = fParam_R1($1, $2.lex);}
FParam ::= TypeDesc MUL IDENT 
    {$$ = fParam_R2($1, $3.lex);}

Desig ::= IDENT 
    {$$ = desig_R1($1.lex)));}
Desig ::= Desig ICORCHETE Expr FCORCHETE 
    {$$ = desig_R2($1, $3);}
Desig ::= Desig BARRABAJA LITNAT 
    {$$ = desig_R3($1, $3.lex);}

Expr ::= Term Op0 Term 
    {$$ = expr_R1($1, $2, $3);}
Expr ::= Term 
    {$$ = expr_R2($1);}

Term ::= Term Op1 Fact 
    {$$ = term_R1($1, $2, $3);}
Term ::= Term OR Fact 
    {$$ = term_R2($1, $3);}
Term ::= Fact 
    {$$ = term_R3($1);}

Fact ::= Fact Op2 Shft 
    {$$ = fact_R1($1, $2, $3);}
Fact ::= Fact AND Shft 
    {$$ = fact_R2($1, $3);}
Fact ::= Shft 
    {$$ = fact_R3($1);}

Shft ::= Unary Op3 Shft 
    {$$ = shft_R1($1, $2, $3);}
Shft ::= Unary 
    {$$ = shft_R2($1);}

Unary ::= Op4 Unary 
    {$$ = unary_R1($1, $2);}
Unary ::= IPAR Cast FPAR Paren 
    {$$ = unary_R2($2, $4);}
Unary ::= Paren 
    {$$ = unary_R3($1);}

Paren ::= IPAR Expr FPAR 
    {$$ = paren_R1($2);}
Paren ::= Lit 
    {$$ = paren_R2($1);}
Paren ::= Desig 
    {$$ = paren_R3($1);}

Op0 ::= IGUAL 
    {$$ = op0_R1();}
Op0 ::= NOIGUAL 
    {$$ = op0_R2();}
Op0 ::= MEN 
    {$$ = op0_R3();}
Op0 ::= MAY 
    {$$ = op0_R4();}
Op0 ::= MENOIG 
    {$$ = op0_R5();}
Op0 ::= MAYOIG 
    {$$ = op0_R6();}

Op1 ::= MENOS 
    {$$ = op1_R1();}
Op1 ::= MAS 
    {$$ = op1_R2();}

Op2 ::= MOD 
    {$$ = op2_R1();}
Op2 ::= DIV 
    {$$ = op2_R2();}
Op2 ::= MUL 
    {$$ = op2_R3();}

Op3 ::= LSH 
    {$$ = op3_R1();}
Op3 ::= RSH 
    {$$ = op3_R2();}

Op4 ::= NOT 
    {$$ = op4_R1();}
Op4 ::= MENOS 
    {$$ = op4_R2();}

Lit ::= LitBool 
    {$$ = lit_R1($1);}
Lit ::= LitNum 
    {$$ = lit_R2($1);}
Lit ::= LITCHAR 
    {$$ = lit_R3($1.lex));}

LitBool ::= TRUE 
    {$$ = litBool_R1();}
LitBool ::= FALSE 
    {$$ = litBool_R2();}

LitNum ::= LITNAT 
    {$$ = litNum_R1($1.lex);}
LitNum ::= LITFLOAT 
    {$$ = litNum_R2($1.lex);}
