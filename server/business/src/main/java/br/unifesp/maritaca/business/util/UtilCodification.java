package br.unifesp.maritaca.business.util;

import java.security.MessageDigest;
import java.util.Random;

@Deprecated
public class UtilCodification {
	
	public static synchronized String generateCode() {
		String dictionary = new String("QAa0bcLdUK2eHfJgTP8XhiFj61DOklNm9nBoI5pGqYVrs3CtSuMZvwWx4yE7zR");
		StringBuffer helper = new StringBuffer();
		Random num = new Random();
		int gosh = 0;
		for(int i = 1; i <= 5; i++) {
			gosh = num.nextInt(62);
		    helper.append(dictionary.charAt(gosh));
		}
		return helper.toString();
	}

	public static String encryptToSha1(String value) {
		return encryptHex(value, "SHA1");
	}
	
	private static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

	/**
	 * 
	 * @param value string to encrypt
	 * @param type MD5 or SHA1
	 * @return string encrypted
	 */
    private static String encryptHex(String value, String type) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            return hex(md.digest(value.getBytes("CP1252"))); //Windows-1252
        } catch (Exception ex) {
        	throw new RuntimeException(ex);
        }
    }
}