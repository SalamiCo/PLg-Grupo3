Formación de literales e identificadores
litnat ≡ _dign0 (_dig)* | "0" 
litfloat ≡ litnat _partedec (_parteexp)? | litnat _parteexp 
litchar ≡ "'"_alfanum1"'" 
ident ≡ _min (_alfanum1)*
true ≡ "true" 
false ≡ "false"

Palabras reservadas
program ≡ "program:" 
subprograms ≡ "subprograms"
subprogram ≡ "subprogram:"
varconsts ≡ "vars-consts" 
instructions ≡ "instructions" 
var ≡ "var" 
const ≡ "const" 
float ≡ "float" 
integer ≡ "integer" 
int ≡ "int" 
boolean ≡ "boolean" 
natural ≡ "natural" 
nat ≡ "nat" 
character ≡ "character" 
char ≡ "char" 
in ≡ "in" 
out ≡ "out" 
swap1 ≡ "swap1" 
swap2 ≡ "swap2"
call ≡ "call"

Símbolos y operadores
Asig ≡ "=" 
dpigual ≡ ":=" 
lpar ≡ "(" 
rpar ≡ ")" 
illave ≡ "{" 
fllave ≡ "}" 
pyc ≡ ";" 
men ≡ "<" 
menoig ≡ "<=" 
may ≡ ">" 
mayoig ≡ ">=" 
igual ≡ "=="
noigual ≡ "!=" 
mas ≡ "+" 
menos ≡ "-" 
mul ≡ "*" 
div ≡ "/" 
mod ≡ "%" 
and ≡ "and" 
or ≡ "or" 
not ≡ "not" 
lsh ≡ "<<" 
rsh ≡ ">>"
coma ≡ ","
barrabaja ≡ "_"

Expresiones auxiliares
_min ≡ ['a'-'z'] 
_may ≡ ['A'-'Z'] 
_letra ≡ _min | _may 
_dig ≡ ['0'-'9'] 
_dign0 ≡ ['1'-'9'] 
_alfanum1 ≡ _letra | _dig 
_partedec ≡ "." ((_dig)*_dign0 | "0") 
_parteexp ≡ ("e" | "E") "-"? litnat 
fin ≡ <end-of-file>
