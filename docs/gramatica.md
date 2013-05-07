
******** RAUL - MARINA *********

Program → program ident illave SDecs SInsts fllave fin

SDecs → varconsts illave Decs fllave
Decs → Decs pyc Dec | Dec
Dec → var Type ident | const Type ident dpigual Lit | ɛ

program ident illave SConsts STypes SVars SSubProgs SInsts fllave fin

SConsts → const illave Consts fllave | ɛ
Consts → Consts Const | Const
Const → const Prim ident asig Lit pyc

STypes → tipos illave Types fllave | ɛ
Types → Types Type | Type
Type → tipo TypeDesc ident pyc

SVars → vars illave Vars fllave | ɛ
Vars → Vars Var | Var
Var → var TypeDesc ident pyc


TypeDesc → Prim | TArray | TTupla | ident

Prim → natural | integer | float | boolean | character

TArray → Prim icorchete ident fcorchete Array | Prim icorchete litnat fcorchete Array
Array → Array icorchete ident fcorchete | Array icorchete litnat fcorchete | ɛ

TTuplas → lpar TTupla rpar
TTupla → ident coma TTupla | ident | ɛ


TODO: cambiar Type por Prim (tipo primitivo)
TODO: lpar, fpar


******** DANI - ARTURO *********

SInsts → instructions illave Insts fllave
Insts → Inst pyc Insts | 훆
Inst → Desig asig Expr | 
       in lpar Desig rpar |
       out lpar Expr rpar |
       swap1 lpar rpar |
       swap2 lpar rpar | 
       InstIf | InstWhile
InstIf → if Expr then Insts ElseIf
ElseIf → else Insts endif | endif
InstWhile → while Expr do Insts endwhile


******** ANTONIO - PEDRO *********

SSubprogs → subprograms illave Subprogs fllave | ɛ
Subprogs → Subprogs Subprog | Subprog
Subprog → subprogram ident lpar SParams rpar illave SVars SInsts fllave

SFParams → FParam | ɛ
FParams → FParams coma FParam | FParam
FParam → TypeDesc ident | TypeDesc mul ident

Desig → ident | Desig icorchete Expr fcorchete | Desig barrabaja litnat

//Esto es para añadir en el léxico
subprograms ≡ "subprograms"
subprogram ≡ "subprogram:"
coma ≡ ","
barrabaja ≡ "_"
