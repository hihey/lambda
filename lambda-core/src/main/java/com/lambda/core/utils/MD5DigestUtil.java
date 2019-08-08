package com.lambda.core.utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import sun.misc.BASE64Encoder;

public class MD5DigestUtil {
	
	/**
	 * 加密
	 */
	public static String md5Digest(String seq) {
		try {
			MessageDigest md5Code = MessageDigest.getInstance("md5");
			byte[] bTmp = md5Code.digest(seq.getBytes());
			BASE64Encoder base64 = new BASE64Encoder();
			String str = base64.encode(bTmp);
			return str;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	};

	private static byte[] keybytes = { 0x31, 0x32, 0x33, 0x34, 0x35, 0x50, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };

	/** 
	 * @Title: encrypt 
	 * @Description: 加密
	 * @param @param value
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws 
	 */
	public static String encrypt(String value) {
		String s = null;
		int mode = Cipher.ENCRYPT_MODE;

		try {
			Cipher cipher = initCipher(mode);
			byte[] outBytes = cipher.doFinal(value.getBytes());

			s = String.valueOf(Hex.encodeHex(outBytes));
		} catch (Exception e) {
		}
		return s;
	}

	/** 
	 * @Title: initCipher 
	 * @Description: 初始化密码
	 * @param @param mode
	 * @param @throws NoSuchAlgorithmException
	 * @param @throws NoSuchPaddingException
	 * @param @throws InvalidKeyException 设定文件 
	 * @return Cipher 返回类型 
	 * @throws 
	 */
	private static Cipher initCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		Key key = new SecretKeySpec(keybytes, "AES");
		cipher.init(mode, key);
		return cipher;
	}

	/** 
	 * @Title: decrypt 
	 * @Description: 解密 
	 * @param @param value
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws 
	 */
	public static String decrypt(String value) {
		String s = null;
		int mode = Cipher.DECRYPT_MODE;
		try {
			Cipher cipher = initCipher(mode);
			byte[] outBytes = cipher.doFinal(Hex.decodeHex(value.toCharArray()));

			s = new String(outBytes);
		} catch (Exception e) {
		}
		return s;
	}

	public static void main(String[] args) {
		System.out.println(encrypt("root"));
		System.out.println(decrypt("c44b26492a8a74fcbd5780a411b885a7"));
	}
}