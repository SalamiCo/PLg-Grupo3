### 4.3 Atributos semánticos

* op: atributo que indica cuál es el operador usado.
* ts: tabla de símbolos. Se crea en la parte de declaraciones.
* tsh: tabla de símbolos heredada. Se hereda en la parte de instrucciones.
* err: atributo que indica si se ha detectado algún error. Es un atributo de tipo booleano.
* nparams: contador que cuenta cuántos parámetros se han pasado en la llamada (call) a un subprograma
* nombresubprog: lleva el identificador el subprograma. Se usa para las restricciones contextuales en el paso de parámetros a funciones