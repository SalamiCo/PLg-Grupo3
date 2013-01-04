package plg.gr3.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.Token;
import plg.gr3.lexer.TokenType;

public class TokenTest {
    
    private Token t1;
    
    private Token t2;
    
    @Before
    public void setUp () {
        // Iguales, pero no el mismo
        t1 = new Token(TokenType.IDENTIFIER, "ident");
        t2 = new Token(TokenType.IDENTIFIER, "ident");
    }
    
    @Test
    public void testConstructor () {
        TokenType type = TokenType.LIT_NATURAL;
        String lexeme = "123";
        
        Token token = new Token(type, lexeme);
        Assert.assertTrue(token.getLexeme().matches(token.getType().getPattern().pattern()));
        Assert.assertEquals(type, token.getType());
        Assert.assertEquals(lexeme, token.getLexeme());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNotMatches () {
        new Token(TokenType.LIT_NATURAL, "-123");
        Assert.fail();
    }
    
    @Test(expected = NullPointerException.class)
    public void testTypeNotNull () {
        new Token(null, "abcd");
        Assert.fail();
    }
    
    @Test(expected = NullPointerException.class)
    public void testLexemeNotNull () {
        new Token(TokenType.IDENTIFIER, null);
        Assert.fail();
    }
    
    @Test
    public void testEquals () {
        Assert.assertEquals(t1, t2);
    }
    
    @Test
    public void testHashCode () {
        Assert.assertEquals(t1.hashCode(), t2.hashCode());
    }
}
