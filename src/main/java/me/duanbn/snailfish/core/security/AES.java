// package me.duanbn.snailfish.core.security;
//
// import java.security.Security;
//
// import javax.crypto.Cipher;
// import javax.crypto.spec.SecretKeySpec;
//
// import org.apache.commons.codec.binary.Base64;
// import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
/// **
// * 32位密钥AES对称加密.
// *
// * @author shanwei
// *
// */
// public class AES {
//
// private static final String CipherMode = "AES/ECB/PKCS7Padding";
//
// private static String create32Key(String sKey) {
// StringBuffer sb = new StringBuffer(32);
// sb.append(sKey);
// while (sb.length() < 32) {
// sb.append("0");
// }
// if (sb.length() > 32) {
// sb.setLength(32);
// }
// return sb.toString();
// }
//
// // 加密
// public static String encrypt(String content, String sKey) {
// sKey = create32Key(sKey);
//
// try {
// byte[] raw = sKey.getBytes("utf-8");
//
// Security.addProvider(new BouncyCastleProvider());
// SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
// Cipher cipher = Cipher.getInstance(CipherMode);
// cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
// byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
//
// return new Base64().encodeToString(encrypted);
// } catch (Exception e) {
// throw new RuntimeException(e);
// }
// }
//
// // 解密
// public static String decrypt(String content, String sKey) {
// sKey = create32Key(sKey);
//
// try {
// byte[] raw = sKey.getBytes("utf-8");
//
// Security.addProvider(new BouncyCastleProvider());
// SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
// Cipher cipher = Cipher.getInstance(CipherMode);
// cipher.init(Cipher.DECRYPT_MODE, skeySpec);
// byte[] encrypted1 = new Base64().decodeBase64(content);
// byte[] original = cipher.doFinal(encrypted1);
// String originalString = new String(original, "utf-8");
// return originalString;
// } catch (Exception e) {
// throw new RuntimeException(e);
// }
// }
//
// }
