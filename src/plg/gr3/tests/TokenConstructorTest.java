package plg.gr3.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.Token;
import plg.gr3.lexer.TokenType;

public class TokenConstructorTest {

    /** Array con naturales correctos e incorrectos */
    private String[][] naturals;

    /** Array con enteros correctos e incorrectos */
    private String[][] integers;

    /** Array con decimales correctos e incorrectos */
    private String[][] floats;

    /** Array con caracteres correctos e incorrectos */
    private String[][] chars;

    /** Array con identificadores correctos e incorrectos */
    private String[][] idents;

    @Before
    public void setUp () {
        String[] validNats = {"0", "12", "3456", "7890"};
        String[] invalidNats = {"00", "014", "-123"};
        naturals = new String[][] {validNats, invalidNats};

        String[] validInts = {"0", "12", "3456", "7890", "-143", "-934"};
        String[] invalidInts = {"00", "014", "-0123", "-0"};
        integers = new String[][] {validInts, invalidInts};

        String[] validFloats = {"0", "12", "-143", "-93", "0.123", "0.0", "123.0", "12e10", "4e-24", "1E45", "18.2E-1"};
        String[] invalidFloats = {"00", "014", "-0123", "-0", "12.10", "12.160e1"};
        floats = new String[][] {validFloats, invalidFloats};

        String[] validChars = {"'a'", "'0'", "'B'"};
        String[] invalidChars = {"'.'", "'", "b", "'ab'"};
        chars = new String[][] {validChars, invalidChars};

        String[] validIdents = {"assert", "affect", "peim", "asdfGhj123"};
        String[] invalidIdents = {"12meh", "MaxPower", "under_score"};
        idents = new String[][] {validIdents, invalidIdents};
    }

    /**
     * Metodo que evalua la validez de un Token
     * 
     * @param type
     *            Categoria lexica del Token
     * @param lexeme
     *            Lexema del Token
     * @return Si no lanza ninguna excepcion consideramos al Token bien construido
     * */
    private static final boolean isValidToken (TokenType type, String lexeme) {
        try {
            Token validToken;
            validToken = new Token(type, lexeme);
            return (validToken != null);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Testea la construccion de Tokens con lexemas validos e invalidos
     * 
     * @param type
     *            Categoria lexica del Token
     * @param valid
     *            Array de lexemas validos para dicha categoria lexica
     * @param invalid
     *            Array de lexemas invalidos para dicha categoria lexica
     * */
    private static final void constructTokens (TokenType type, String[] valid, String[] invalid) {
        for (String lexeme : valid) {
            Assert.assertTrue(lexeme, isValidToken(type, lexeme));
        }
        for (String lexeme : invalid) {
            Assert.assertFalse(lexeme, isValidToken(type, lexeme));
        }
    }

    @Test
    public void validNaturals () {
        constructTokens(TokenType.LIT_NATURAL, naturals[0], naturals[1]);
    }

    @Test
    public void validIntegers () {
        constructTokens(TokenType.LIT_INTEGER, integers[0], integers[1]);
    }

    @Test
    public void validFloats () {
        constructTokens(TokenType.LIT_FLOAT, floats[0], floats[1]);
    }

    @Test
    public void validChars () {
        constructTokens(TokenType.LIT_CHARACTER, chars[0], chars[1]);
    }

    @Test
    public void validIdents () {
        constructTokens(TokenType.IDENTIFIER, idents[0], idents[1]);
    }

}
