package plg.gr3.lexer;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analizador léxico del lenguaje.
 * <p>
 * Esta clase realiza un análisis léxico del flujo de entrada bajo demanda. Esto quiere decir que el análisis sólo se
 * realizará al relizar llamadas a los métodos de la clase, en los cuales hemos de pasar la categoría léxica esperada,
 * puesto que múltiples categorías léxicas pueden ser ambiguas en su definición.
 * <p>
 * A nivel de implementación, esta clase realiza su análisis línea a línea. Cada vez que se llama a algún método de
 * análisis, la entrada se prepara de la siguiente manera:
 * <ol>
 * <li>Lee y descarta todos los blancos, así como cualquier comentario que exista en la entrada
 * <li>Si la línea actual se ha agotado, lee una nueva y vuelve al paso 1. En otro caso, termina
 * </ol>
 * Tras estos pasos, se tratará de reconocer el <i>token</i> pertinente de la forma especificada en cada método.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public final class Lexer implements Closeable {
    
    /** Patrón usado para ignorar los espacios y los comentarios */
    private static final Pattern PATTERN_IGNORE = Pattern.compile("#.*|\\s+(@.*$)?");
    
    /** Reader para obtener el programa línea a línea */
    private final BufferedReader reader;
    
    /** Reconocedor de tokens */
    private Matcher matcher;
    
    /** Línea en la que se encuentra el reconocedor */
    private int currentLine = 0;
    
    /** Columna en la que se encuentra el reconocedor */
    private int currentColumn = 0;
    
    /**
     * Crea un nuevo Lexer a partir de un <tt>reader</tt> dado
     * 
     * @param reader
     *            Flujo de caracteres del que obtener líneas
     */
    public Lexer (Reader reader) {
        this.reader = new BufferedReader(reader);
    }
    
    /**
     * Determina si existe un token del tipo dado en la entrada del analizador. Este método no modifica el estado actual
     * del analizador: múltiples llamadas consecutivas a este método devolverán lo mismo siempre que no se llame a
     * {@link #nextToken} entre ellas.
     * 
     * @param category
     *            Categoría léxica que buscamos reconocer
     * @return <tt>true</tt> si la entrada es capaz de reconocer <tt>category</tt> como siguiente token, <tt>false</tt>
     *         en otro caso.
     * @throws IOException
     *             si ocurre algún error de E/S
     */
    public boolean hasNextToken (TokenType category) throws IOException {
        prepareInput();
        
        // Si se ha llegado al final de fichero o intentamos reconocer un EOF...
        if (matcher == null || category == TokenType.EOF) {
            // ... devolvemos true si se hallegado al final del fichero _Y_ estamos reconociendo EOF
            return matcher == null && category == TokenType.EOF;
        }
        
        // Con el anterior if nos aseguramos de que en este punto:
        // * No hemos llegado al final del fichero (matcher no es null)
        // * No estamos tratando de reconocer EOF (lo cual haría que el resto del código no funcionara)
        
        // Probamos si el reconocedor es capaz de reconocer el token pedido
        matcher.usePattern(category.getPattern());
        return matcher.lookingAt();
    }
    
    /**
     * Devuelve el siguiente token disponible, siempre y cuando sea del tipo dado. En caso de no poder reconocer un
     * token del tipo dado, este método lanzará una excepción de tipo {@link NoSuchElementException}.
     * <p>
     * Si el método {@link #hasNextToken} devuelve <tt>true</tt>, este método retornará satisfactoriamente. De igual
     * manera, si devuelve <tt>false</tt>, este método lanzará una excepción.
     * 
     * @param category
     *            Categoría léxica que buscamos reconocer
     * @return Un token localizado con información acerca del lexema, la línea y la columna en que se encuentra
     * @throws IOException
     *             si ocurre algún error de E/S
     * @throws NoSuchElementException
     *             si no se pudo encontrar un token adecuado
     */
    public LocatedToken nextToken (TokenType category) throws IOException {
        prepareInput();
        
        // Comprobamos que el siguiente token es del tipo esperado
        if (!hasNextToken(category)) {
            throw new NoSuchElementException();
        }
        
        // Caso especial: Si estamos reconociendo el final de fichero, el reconocedor es null. Adelantamos el proceso
        // y evitamos problemas devolviendo directamente el token de fin de fichero
        if (category == TokenType.EOF) {
            return new LocatedToken(new Token(category, ""), currentLine, currentColumn);
        }
        
        int column = currentColumn;
        
        // Reconocemos el siguiente token
        // FIXME: ¿Es esto realmente neceario? Recordemos que en este punto venimos de una llamada a 'hasNextToken'
        //        que ha relizado exactamente las mismas acciones.
        matcher.usePattern(category.getPattern());
        matcher.lookingAt();
        
        // Avanzamos la posición del analizador y leemos el lexema
        currentColumn += matcher.end() - matcher.start();
        String lexeme = matcher.group();
        
        // Avanzamos el reconocedor
        matcher.region(matcher.end(), matcher.regionEnd());
        
        // Devolvemos el token localizado con toda la información necesaria
        return new LocatedToken(new Token(category, lexeme), currentLine, column);
    }
    
    /**
     * Prepara el analizador léxico para empezar a reconocer un token, saltándose espacios y comentarios así como
     * leyendo nuevas líneas si fuera necesario
     * 
     * @throws IOException
     *             si ocurre algún error de E/S
     */
    private void prepareInput () throws IOException {
        do {
            // Si el reconocedor no está creado o ha llegado al final de la línea...
            if (matcher == null || matcher.hitEnd()) {
                // Descartamos el reconocedor actual
                matcher = null;
                
                // Leemos una nueva línea
                String line = reader.readLine();
                
                // Si no hemos llegado al final del flujo...
                if (line != null) {
                    
                    // Pasamos a la siguiente línea
                    currentLine++;
                    currentColumn = 1;
                    
                    // Creamos un nuevo reconocedor sobre la línea nueva
                    matcher = PATTERN_IGNORE.matcher(line);
                }
            }
            
            // Si el reconocedor existe, es decir, no hemos agotado toda la entrada...
            if (matcher != null) {
                // Reconocemos los espacios y los comentarios
                matcher.usePattern(PATTERN_IGNORE);
                if (matcher.lookingAt()) {
                    // Nos los saltamos
                    currentColumn += matcher.end() - matcher.start();
                    matcher.region(matcher.end(), matcher.regionEnd());
                }
            }
            
        } while (matcher != null && matcher.hitEnd());
        
        // El procesose repite siempre que el reconocedor haya agotado una línea completa.
        // Si el reconocedor se ha parado a mitad de línea, significa quehay más tokens que leer. Si no, podemos tener
        // más espacios en líneas posteriores. De igual manera,siel reconocedor es null, significa que hemos llegado al
        // fin de fichero, por lo que hemos de parar el proceso.
        
        if (matcher == null) {
            // Cerramos la entrada en cuanto llegamos al final para evitar problemas
            reader.close();
        }
    }
    
    /**
     * Devuelve un conjunto con todas las categorías léxicas que el analizador es capaz de reconocer dado su estado
     * actual.
     * <p>
     * Aunque típicamente este método devolverá un conjunto unitario, en ocasiones un mismo token puede pertenecer a
     * varias categorías léxicas. Es el caso, por ejemplo, de los números naturales, que también se pueden reconocer
     * como enteros o decimales, o los enteros negativos, cuyo signo puede formar parte del token o formar una expresión
     * de negación unaria junto a un natural.
     * 
     * @return Un conjunto con todas las posibles categorías léxicas que encajarían con la entrada del analizador
     * @throws IOException
     *             si ocurre algún error de E/S
     */
    public Set<TokenType> nextTokenTypes () throws IOException {
        Set<TokenType> types = EnumSet.noneOf(TokenType.class);
        for (TokenType type : TokenType.values()) {
            if (hasNextToken(type)) {
                types.add(type);
            }
        }
        return types;
    }
    
    @Override
    public void close () throws IOException {
        reader.close();
    }
}
