package plg.gr3.parser;

import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%cup 
%line 
%column

/* Comentarios */
_comment = "@" [^\r\n]* (\r|\n|\r\n)

/*Expresiones auxiliares*/
minus    = [a-z]
mayus    = [A-Z]
letra    = {minus} | {mayus}
digit    = [0-9]
digitn0  = [1-9]
alfanum  = {letra} | {digit}
partedec = \. (0 |  {digit}* {digitn0}) 
parteexp = ("e" | "E") \-? {litnat}

/*Literales e identificadores*/
litnat   = 0 | {digitn0} {digit}*
litfloat = {litnat} ({partedec} {parteexp}? | {parteexp})
litchar  = \'[^\']*\'
ident    = {letra} {alfanum}*
true     = "true"
false    = "false"

/*Palabras reservadas*/
program    = "program:"
subprogram = "subprogram:"
vars   = "vars"
tipos  = "tipos"
consts = "consts"
subprograms  = "subprograms"
instructions = "instructions"
var   = "var"
const = "const"
tipo  = "tipo"
in  = "in"
out = "out"
call = "call"
swap1 = "swap1"
swap2 = "swap2"
float = "float"
integer = "integer"
natural = "natural"
character = "character"
boolean = "boolean"
int   = "int"
nat   = "nat"
char  = "char"
if    = "if"
then  = "then"
else  = "else"
endif = "endif"
while = "while"
do	  = "do"
endwhile = "endwhile"

/*Simbolos y operadores*/
asig = "="
ipar = "("
fpar = ")"
illave = "{"
fllave = "}"
icorchete = "["
fcorchete = "]"
pyc = ";"
coma = ","
barrabaja = "_"
men = "<"
may = ">"
menoig = "<="
mayoig = ">="
igual = "=="
noigual = "!="
mas = "+"
menos = "-"
mul = "*"
div = "/"
mod = "%"
and = "and"
or = "or"
not = "not"
lsh = "<<"
rsh = ">>"

%%

[\t\r\n ] {}
#[^\n]*   {}
{_comment} {}

{litnat}   {return new Symbol(sym.LITNAT, yytext());}
{litfloat} {return new Symbol(sym.LITFLOAT, yytext());}
{litchar}  {return new Symbol(sym.LITCHAR, yytext());}
{true}     {return new Symbol(sym.TRUE);}
{false}    {return new Symbol(sym.FALSE);}

{program}      {return new Symbol(sym.PROGRAM);}
{subprogram}   {return new Symbol(sym.SUBPROGRAM);}
{vars}         {return new Symbol(sym.VARS);}
{tipos}        {return new Symbol(sym.TIPOS);}
{consts}       {return new Symbol(sym.CONSTS);}
{subprograms}  {return new Symbol(sym.SUBPROGRAMS);}
{instructions} {return new Symbol(sym.INSTRUCTIONS);}
{var}       {return new Symbol(sym.VAR);}
{tipo}      {return new Symbol(sym.TIPO);}
{const}     {return new Symbol(sym.CONST);}
{in}        {return new Symbol(sym.IN);}
{out}       {return new Symbol(sym.OUT);}
{call}      {return new Symbol(sym.CALL);}
{swap1}     {return new Symbol(sym.SWAP1);}
{swap2}     {return new Symbol(sym.SWAP2);}
{float}     {return new Symbol(sym.FLOAT);}
{integer}   {return new Symbol(sym.INTEGER);}
{natural}   {return new Symbol(sym.NATURAL);}
{character} {return new Symbol(sym.CHARACTER);}
{boolean}   {return new Symbol(sym.BOOLEAN);}
{int}       {return new Symbol(sym.INT);}
{nat}       {return new Symbol(sym.NAT);}
{char}      {return new Symbol(sym.CHAR);}
{if}        {return new Symbol(sym.IF);}
{then}      {return new Symbol(sym.THEN);}
{else}      {return new Symbol(sym.ELSE);}
{endif}     {return new Symbol(sym.ENDIF);}
{while}     {return new Symbol(sym.WHILE);}
{do}        {return new Symbol(sym.DO);}
{endwhile}  {return new Symbol(sym.ENDWHILE);}

{asig}    {return new Symbol(sym.ASIG);}
{ipar}    {return new Symbol(sym.IPAR);}
{fpar}    {return new Symbol(sym.FPAR);}
{illave}  {return new Symbol(sym.ILLAVE);}
{fllave}  {return new Symbol(sym.FLLAVE);}
{icorchete} {return new Symbol(sym.ICORCHETE);}
{fcorchete} {return new Symbol(sym.FCORCHETE);}
{pyc}     {return new Symbol(sym.PYC);}
{coma}    {return new Symbol(sym.COMA);}
{barrabaja} {return new Symbol(sym.BARRABAJA);}
{men}     {return new Symbol(sym.MEN);}
{may}     {return new Symbol(sym.MAY);}
{menoig}  {return new Symbol(sym.MENOIG);}
{mayoig}  {return new Symbol(sym.MAYOIG);}
{igual}   {return new Symbol(sym.IGUAL);}
{noigual} {return new Symbol(sym.NOIGUAL);}
{mas}     {return new Symbol(sym.MAS);}
{menos}   {return new Symbol(sym.MENOS);}
{mul}     {return new Symbol(sym.MUL);}
{div}     {return new Symbol(sym.DIV);}
{mod}     {return new Symbol(sym.MOD);}
{and}     {return new Symbol(sym.AND);}
{or}      {return new Symbol(sym.OR);}
{not}     {return new Symbol(sym.NOT);}
{lsh}     {return new Symbol(sym.LSH);}
{rsh}     {return new Symbol(sym.RSH);}

{ident}    {return new Symbol(sym.IDENT, yytext());}
[^]       {throw new Error("Caracter no reconocido:"+yytext());}

