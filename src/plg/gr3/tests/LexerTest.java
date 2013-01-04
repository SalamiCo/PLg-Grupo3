package plg.gr3.tests;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.Lexer;
import plg.gr3.lexer.TokenType;

public class LexerTest {
    
    private Lexer lexer, lexerEof;
    
    @Before
    public void setUp () {
        lexer = new Lexer(new StringReader("45356 var { ) ghdjsfa 'a'  "));
        lexerEof = new Lexer(new StringReader("  \t\n @fgdgf\n "));
    }
    
    @Test
    public void testHasNextToken () throws IOException {
        Assert.assertTrue(lexer.hasNextToken(TokenType.LIT_INTEGER));
    }
    
    @Test
    public void testHasNextEof () throws IOException {
        Assert.assertTrue(lexerEof.hasNextToken(TokenType.EOF));
    }
    
    @Test
    public void testNextToken () throws IOException {
        Assert.assertEquals(TokenType.LIT_INTEGER, lexer.nextToken(TokenType.LIT_INTEGER).getToken().getType());
        Assert.assertEquals(TokenType.RW_VAR, lexer.nextToken(TokenType.RW_VAR).getToken().getType());
        Assert.assertEquals(TokenType.SYM_CURLY_LEFT, lexer.nextToken(TokenType.SYM_CURLY_LEFT).getToken().getType());
        Assert.assertEquals(TokenType.SYM_PAR_RIGHT, lexer.nextToken(TokenType.SYM_PAR_RIGHT).getToken().getType());
        Assert.assertEquals(TokenType.IDENTIFIER, lexer.nextToken(TokenType.IDENTIFIER).getToken().getType());
        Assert.assertEquals(TokenType.LIT_CHARACTER, lexer.nextToken(TokenType.LIT_CHARACTER).getToken().getType());
        Assert.assertEquals(TokenType.EOF, lexer.nextToken(TokenType.EOF).getToken().getType());
    }
}
