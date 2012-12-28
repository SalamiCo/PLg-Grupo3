package plg.gr3.tests;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import plg.gr3.lexer.LocatedToken;
import plg.gr3.lexer.Token;
import plg.gr3.lexer.TokenType;

public class LocatedTokenTest {

    private Token validToken;

    private int validLine;

    private int validColumn;

    private LocatedToken lt1;

    private LocatedToken lt2;

    @Before
    public void setUp () {
        validToken = new Token(TokenType.IDENTIFIER, "ident");
        validLine = 21;
        validColumn = 43;

        lt1 = new LocatedToken(validToken, validLine, validColumn);
        lt2 = new LocatedToken(validToken, validLine, validColumn);
    }

    @Test(expected = NullPointerException.class)
    public void testTokenNotNull () {
        new LocatedToken(null, validLine, validColumn);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLineNotNegative () {
        new LocatedToken(validToken, -23, validColumn);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLineNotZero () {
        new LocatedToken(validToken, 0, validColumn);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumnNotNegative () {
        new LocatedToken(validToken, validLine, -12);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testColumnNotZero () {
        new LocatedToken(validToken, validLine, 0);
        Assert.fail();
    }

    @Test
    public void testEquals () {
        Assert.assertEquals(lt1, lt2);
    }

    @Test
    public void testHashCode () {
        Assert.assertEquals(lt1.hashCode(), lt2.hashCode());
    }

}
