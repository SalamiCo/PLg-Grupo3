******** RAUL - MARINA *********









******** DANI - ARTURO *********









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