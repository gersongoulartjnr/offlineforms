package br.unifesp.maritaca.business.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class MaritacaEncode {
	
	private static Logger logger = Logger.getLogger(MaritacaEncode.class);
	
    public static String MD5	= "MD5";
    public static String SHA1	= "SHA-1";
    
	public static String toHexadecimal(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
	
	public static String getStringMessageDigest(String message, String algorithm) {
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            logger.error(ex.getMessage());
        }
        return toHexadecimal(digest);
    }
}