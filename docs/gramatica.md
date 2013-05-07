******** RAUL - MARINA *********









******** DANI - ARTURO *********









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