package me.duanbn.snailfish.core.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * hmac
 * 
 * @author shanwei
 *
 */
public class HMAC {

	private static final String HMAC_SHA1 = "HmacSHA1";

	public static String encrypt(String data, String key) {
		try {
			SecretKeySpec secret = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(secret);
			byte[] doFinal = mac.doFinal(data.getBytes());
			return new Base64().encodeToString(doFinal);
		} catch (Exception e) {
			throw new RuntimeException("encrypt err", e);
		}
	}

}
