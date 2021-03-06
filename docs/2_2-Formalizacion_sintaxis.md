## 2.2. Formalización de la sintaxis 

Program → program ident illave SConsts STypes SVars SSubprogs SInsts fllave fin

SConsts → consts illave Consts fllave | ɛ
Consts → Consts pyc Const | Const
Const → const TPrim ident asig ConstLit | ɛ

ConstLit → Lit | menos Lit

STypes → tipos illave Types fllave | ɛ
Types → Types pyc Type | Type
Type → tipo TypeDesc ident | ɛ

SVars → vars illave Vars fllave | ɛ
Vars → Vars pyc Var | Var
Var → var TypeDesc ident | ɛ

SSubprogs → subprograms illave Subprogs fllave | subprograms illave fllave | ɛ
Subprogs → Subprogs Subprog | Subprog
Subprog → subprogram ident ipar SParams fpar illave SVars SInsts fllave

SFParams → FParams | ɛ
FParams → FParams coma FParam | FParam
FParam → TypeDesc ident | TypeDesc mul ident

TypeDesc → TPrim | TArray | TTupla | ident

TPrim → natural | integer | float | boolean | character
Cast → char | int | nat | float

TArray → TypeDesc icorchete ident fcorchete | TypeDesc icorchete litnat fcorchete

TTupla → ipar Tupla fpar | ipar fpar
Tupla → TypeDesc coma Tupla | TypeDesc

SInsts → instructions illave Insts fllave
Insts → Insts pyc Inst | Inst
Inst → Desig asig Expr
     | in ipar Desig fpar
     | out ipar Expr fpar
     | swap1 ipar fpar
     | swap2 ipar fpar
     | if Expr then Insts ElseIf
     | while Expr do Insts endwhile
     | InstCall
     | ɛ
ElseIf → else Insts endif | endif
InstCall → call ident lpar SRParams rpar

SRParams → RParams | ɛ
RParams → RParams coma RParam | RParam
RParam → ident asig Expr

Desig → ident | Desig icorchete Expr fcorchete | Desig barrabaja litnat

Expr → Term Op0 Term | Term
Term → Term Op1 Fact | Term or Fact | Fact
Fact → Fact Op2 Shft | Fact and Shft |Shft
Shft → Unary Op3 Shft | Unary
Unary → Op4 Unary | lpar Cast rpar Paren | Paren
Paren → lpar Expr rpar | Lit | Desig

Op0 → igual | noigual | men | may | menoig | mayoig
Op1 →  menos | mas
Op2 →  mod | div | mul
Op3 → lsh | rsh
Op4 → not | menos

Lit → LitBool | LitNum | litchar
LitBool → true | false
LitNum → litnat | litfloat
