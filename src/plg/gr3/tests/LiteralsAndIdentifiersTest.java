package plg.gr3.tests;

import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.TokenType;

/**
 * Tests para comprobar que las definiciones de literales e identificadores son correctas.
 * 
 * @author PLg Grupo 03 2012/2013
 */
public class LiteralsAndIdentifiersTest {
    
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
    
    private static final void matchValidInvalid (String regex, String[] valid, String[] invalid) {
        for (String str : valid) {
            Assert.assertTrue(str, Pattern.matches(regex, str));
        }
        for (String str : invalid) {
            Assert.assertFalse(str, Pattern.matches(regex, str));
        }
    }
    
    @Test
    public void validNaturals () {
        matchValidInvalid(TokenType.LIT_NATURAL.getPattern().pattern(), naturals[0], naturals[1]);
    }
    
    @Test
    public void validIntegers () {
        matchValidInvalid(TokenType.LIT_INTEGER.getPattern().pattern(), integers[0], integers[1]);
    }
    
    @Test
    public void validFloats () {
        matchValidInvalid(TokenType.LIT_FLOAT.getPattern().pattern(), floats[0], floats[1]);
    }
    
    @Test
    public void validChars () {
        matchValidInvalid(TokenType.LIT_CHARACTER.getPattern().pattern(), chars[0], chars[1]);
    }
    
    @Test
    public void validIdents () {
        matchValidInvalid(TokenType.IDENTIFIER.getPattern().pattern(), idents[0], idents[1]);
    }
}
