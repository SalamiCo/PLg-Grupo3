package plg.gr3.parser;

import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%cup 
%line 
%column

%{
	   int yyline() { return yyline+1; }
	   int yycolumn() { return yycolumn+1; }
%}

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

{litnat}   {return new Symbol(sym.LITNAT, yyline(), yycolumn(), yytext());}
{litfloat} {return new Symbol(sym.LITFLOAT, yyline(), yycolumn(), yytext());}
{litchar}  {return new Symbol(sym.LITCHAR, yyline(), yycolumn(), yytext());}
{true}     {return new Symbol(sym.TRUE, yyline(), yycolumn(), yytext());}
{false}    {return new Symbol(sym.FALSE, yyline(), yycolumn(), yytext());}

{program}      {return new Symbol(sym.PROGRAM, yyline(), yycolumn(), yytext());}
{subprogram}   {return new Symbol(sym.SUBPROGRAM, yyline(), yycolumn(), yytext());}
{vars}         {return new Symbol(sym.VARS, yyline(), yycolumn(), yytext());}
{tipos}        {return new Symbol(sym.TIPOS, yyline(), yycolumn(), yytext());}
{consts}       {return new Symbol(sym.CONSTS, yyline(), yycolumn(), yytext());}
{subprograms}  {return new Symbol(sym.SUBPROGRAMS, yyline(), yycolumn(), yytext());}
{instructions} {return new Symbol(sym.INSTRUCTIONS, yyline(), yycolumn(), yytext());}
{var}       {return new Symbol(sym.VAR, yyline(), yycolumn(), yytext());}
{tipo}      {return new Symbol(sym.TIPO, yyline(), yycolumn(), yytext());}
{const}     {return new Symbol(sym.CONST, yyline(), yycolumn(), yytext());}
{in}        {return new Symbol(sym.IN, yyline(), yycolumn(), yytext());}
{out}       {return new Symbol(sym.OUT, yyline(), yycolumn(), yytext());}
{call}      {return new Symbol(sym.CALL, yyline(), yycolumn(), yytext());}
{swap1}     {return new Symbol(sym.SWAP1, yyline(), yycolumn(), yytext());}
{swap2}     {return new Symbol(sym.SWAP2, yyline(), yycolumn(), yytext());}
{float}     {return new Symbol(sym.FLOAT, yyline(), yycolumn(), yytext());}
{integer}   {return new Symbol(sym.INTEGER, yyline(), yycolumn(), yytext());}
{natural}   {return new Symbol(sym.NATURAL, yyline(), yycolumn(), yytext());}
{character} {return new Symbol(sym.CHARACTER, yyline(), yycolumn(), yytext());}
{boolean}   {return new Symbol(sym.BOOLEAN, yyline(), yycolumn(), yytext());}
{int}       {return new Symbol(sym.INT, yyline(), yycolumn(), yytext());}
{nat}       {return new Symbol(sym.NAT, yyline(), yycolumn(), yytext());}
{char}      {return new Symbol(sym.CHAR, yyline(), yycolumn(), yytext());}
{if}        {return new Symbol(sym.IF, yyline(), yycolumn(), yytext());}
{then}      {return new Symbol(sym.THEN, yyline(), yycolumn(), yytext());}
{else}      {return new Symbol(sym.ELSE, yyline(), yycolumn(), yytext());}
{endif}     {return new Symbol(sym.ENDIF, yyline(), yycolumn(), yytext());}
{while}     {return new Symbol(sym.WHILE, yyline(), yycolumn(), yytext());}
{do}        {return new Symbol(sym.DO, yyline(), yycolumn(), yytext());}
{endwhile}  {return new Symbol(sym.ENDWHILE, yyline(), yycolumn(), yytext());}

{asig}    {return new Symbol(sym.ASIG, yyline(), yycolumn(), yytext());}
{ipar}    {return new Symbol(sym.IPAR, yyline(), yycolumn(), yytext());}
{fpar}    {return new Symbol(sym.FPAR, yyline(), yycolumn(), yytext());}
{illave}  {return new Symbol(sym.ILLAVE, yyline(), yycolumn(), yytext());}
{fllave}  {return new Symbol(sym.FLLAVE, yyline(), yycolumn(), yytext());}
{icorchete} {return new Symbol(sym.ICORCHETE, yyline(), yycolumn(), yytext());}
{fcorchete} {return new Symbol(sym.FCORCHETE, yyline(), yycolumn(), yytext());}
{pyc}     {return new Symbol(sym.PYC, yyline(), yycolumn(), yytext());}
{coma}    {return new Symbol(sym.COMA, yyline(), yycolumn(), yytext());}
{barrabaja} {return new Symbol(sym.BARRABAJA, yyline(), yycolumn(), yytext());}
{men}     {return new Symbol(sym.MEN, yyline(), yycolumn(), yytext());}
{may}     {return new Symbol(sym.MAY, yyline(), yycolumn(), yytext());}
{menoig}  {return new Symbol(sym.MENOIG, yyline(), yycolumn(), yytext());}
{mayoig}  {return new Symbol(sym.MAYOIG, yyline(), yycolumn(), yytext());}
{igual}   {return new Symbol(sym.IGUAL, yyline(), yycolumn(), yytext());}
{noigual} {return new Symbol(sym.NOIGUAL, yyline(), yycolumn(), yytext());}
{mas}     {return new Symbol(sym.MAS, yyline(), yycolumn(), yytext());}
{menos}   {return new Symbol(sym.MENOS, yyline(), yycolumn(), yytext());}
{mul}     {return new Symbol(sym.MUL, yyline(), yycolumn(), yytext());}
{div}     {return new Symbol(sym.DIV, yyline(), yycolumn(), yytext());}
{mod}     {return new Symbol(sym.MOD, yyline(), yycolumn(), yytext());}
{and}     {return new Symbol(sym.AND, yyline(), yycolumn(), yytext());}
{or}      {return new Symbol(sym.OR, yyline(), yycolumn(), yytext());}
{not}     {return new Symbol(sym.NOT, yyline(), yycolumn(), yytext());}
{lsh}     {return new Symbol(sym.LSH, yyline(), yycolumn(), yytext());}
{rsh}     {return new Symbol(sym.RSH, yyline(), yycolumn(), yytext());}

{ident}    {return new Symbol(sym.IDENT, yyline(), yycolumn(), yytext());}
[^]       {throw new RuntimeException("Caracter no reconocido: "+yytext());}


