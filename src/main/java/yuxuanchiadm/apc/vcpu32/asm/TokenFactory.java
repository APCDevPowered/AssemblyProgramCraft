package yuxuanchiadm.apc.vcpu32.asm;

import yuxuanchiadm.apc.vcpu32.asm.parser.Token;

public class TokenFactory
{
    public static Token newToken(int ofKind, String image)
    {
        return Token.newToken(ofKind, image);
    }
}