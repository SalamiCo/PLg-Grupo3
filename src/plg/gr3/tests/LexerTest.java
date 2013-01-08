package plg.gr3.tests;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.Lexer;
import plg.gr3.lexer.TokenType;

public class LexerTest {
    
    private Lexer lexer, lexerEof, lexerKeywords, lexerNotKeywords;
    
    @Before
    public void setUp () {
        lexer = new Lexer(new StringReader("45356 var { ) ghdjsfa 'a'  "));
        lexerEof = new Lexer(new StringReader("  \t\n @fgdgf\n "));
        lexerKeywords =
            new Lexer(new StringReader("program: var-consts instructions var const natural nat boolean"
                                       + " integer int float character char in out swap1 swap2 and or not"));
        lexerNotKeywords =
            new Lexer(new StringReader("programa instructionsgf variable constant naturalfsd swap12 swap3"));
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
    
    @Test
    public void testNextKeyword () throws IOException {
        Assert.assertEquals(TokenType.RW_PROGRAM, lexerKeywords.nextToken(TokenType.RW_PROGRAM).getToken().getType());
        Assert.assertEquals(TokenType.RW_VARCONSTS, lexerKeywords.nextToken(TokenType.RW_VARCONSTS).getToken()
            .getType());
        Assert.assertEquals(TokenType.RW_INSTRUCTIONS, lexerKeywords.nextToken(TokenType.RW_INSTRUCTIONS).getToken()
            .getType());
        Assert.assertEquals(TokenType.RW_VAR, lexerKeywords.nextToken(TokenType.RW_VAR).getToken().getType());
        Assert.assertEquals(TokenType.RW_CONST, lexerKeywords.nextToken(TokenType.RW_CONST).getToken().getType());
        Assert.assertEquals(TokenType.RW_NATURAL, lexerKeywords.nextToken(TokenType.RW_NATURAL).getToken().getType());
        Assert.assertEquals(TokenType.RW_NAT, lexerKeywords.nextToken(TokenType.RW_NAT).getToken().getType());
        Assert.assertEquals(TokenType.RW_BOOLEAN, lexerKeywords.nextToken(TokenType.RW_BOOLEAN).getToken().getType());
        Assert.assertEquals(TokenType.RW_INTEGER, lexerKeywords.nextToken(TokenType.RW_INTEGER).getToken().getType());
        Assert.assertEquals(TokenType.RW_INT, lexerKeywords.nextToken(TokenType.RW_INT).getToken().getType());
        Assert.assertEquals(TokenType.RW_FLOAT, lexerKeywords.nextToken(TokenType.RW_FLOAT).getToken().getType());
        Assert.assertEquals(TokenType.RW_CHARACTER, lexerKeywords.nextToken(TokenType.RW_CHARACTER).getToken()
            .getType());
        Assert.assertEquals(TokenType.RW_CHAR, lexerKeywords.nextToken(TokenType.RW_CHAR).getToken().getType());
        Assert.assertEquals(TokenType.RW_IN, lexerKeywords.nextToken(TokenType.RW_IN).getToken().getType());
        Assert.assertEquals(TokenType.RW_OUT, lexerKeywords.nextToken(TokenType.RW_OUT).getToken().getType());
        Assert.assertEquals(TokenType.RW_SWAP1, lexerKeywords.nextToken(TokenType.RW_SWAP1).getToken().getType());
        Assert.assertEquals(TokenType.RW_SWAP2, lexerKeywords.nextToken(TokenType.RW_SWAP2).getToken().getType());
        Assert.assertEquals(TokenType.RW_AND, lexerKeywords.nextToken(TokenType.RW_AND).getToken().getType());
        Assert.assertEquals(TokenType.RW_OR, lexerKeywords.nextToken(TokenType.RW_OR).getToken().getType());
        Assert.assertEquals(TokenType.RW_NOT, lexerKeywords.nextToken(TokenType.RW_NOT).getToken().getType());
        Assert.assertEquals(TokenType.EOF, lexerKeywords.nextToken(TokenType.EOF).getToken().getType());
    }
    
    @Test
    public void testNextNotKeyword () throws IOException {
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_PROGRAM));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_INSTRUCTIONS));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_VAR));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_CONST));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_NATURAL));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_SWAP1));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
        Assert.assertFalse(lexerNotKeywords.hasNextToken(TokenType.RW_SWAP2));
        lexerNotKeywords.nextToken(TokenType.IDENTIFIER);
    }
    
    @Test
    public void testNextKeywordNotIdentifier () throws IOException {
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_PROGRAM);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_VARCONSTS);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_INSTRUCTIONS);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_VAR);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_CONST);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_NATURAL);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_NAT);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_BOOLEAN);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_INTEGER);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_INT);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_FLOAT);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_CHARACTER);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_CHAR);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_IN);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_OUT);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_SWAP1);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_SWAP2);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_AND);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_OR);
        
        Assert.assertFalse(lexerKeywords.hasNextToken(TokenType.IDENTIFIER));
        lexerKeywords.nextToken(TokenType.RW_NOT);
    }
}
