package plg.gr3.tests;

import org.junit.Assert;
import org.junit.Test;

import plg.gr3.lexer.Token;
import plg.gr3.lexer.TokenType;

public class TokenTest {

    @Test
    public void testMatches () {
        Token token = new Token(TokenType.LIT_NATURAL, "123");

        Assert.assertTrue(token.getLexeme().matches(token.getType().getPattern().pattern()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotMatches () {
        new Token(TokenType.LIT_NATURAL, "-123");
    }

    @Test(expected = NullPointerException.class)
    public void testTypeNotNull () {
        new Token(null, "abcd");
    }

    @Test(expected = NullPointerException.class)
    public void testLexemeNotNull () {
        new Token(TokenType.IDENTIFIER, null);
    }
}
