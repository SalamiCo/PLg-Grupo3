******** RAUL - MARINA *********









******** DANI - ARTURO *********
SInsts → instructions illave Insts fllave
Insts → Inst pyc Insts | 𝛆
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



SParams → SParam | ɛ
SParam → SParam coma Param | Param
Param → TypeDesc ident | TypeDesc mul ident



//Esto es para añadir en el léxico
subprograms ≡ "subprograms"
subprogram ≡ "subprogram:"
coma ≡ ","
