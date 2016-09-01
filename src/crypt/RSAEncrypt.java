package crypt;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import javax.crypto.Cipher;

import sun.security.rsa.RSAPrivateCrtKeyImpl;

/** */
/**
 * RSAEncrypt
 * 
 * @author maqujun
 * @see
 */
public class RSAEncrypt {
	/** */
	/**
	 * Main method for RSAEncrypt.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RSAEncrypt encrypt = new RSAEncrypt();
			String encryptText = "你好！who are you？";
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(512);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			// Generate keys
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			RSAPrivateCrtKeyImpl pcki = (RSAPrivateCrtKeyImpl) privateKey;
			System.out.println(privateKey);
			
			BigInteger p = pcki.getPrimeP();
			BigInteger q = pcki.getPrimeQ();
			System.out.println(p.bitCount());
			System.out.println(p.bitLength());
			byte[] bits = p.toByteArray();
			System.out.println(Arrays.toString(bits));
			BigInteger n = pcki.getModulus();
			BigInteger d = pcki.getPrivateExponent();
			BigInteger e = pcki.getPublicExponent();
			BigInteger pseudo_n = p.multiply(q);
			System.out.println(n);
			System.out.println(pseudo_n.equals(n));
			
			BigInteger negOne = BigInteger.valueOf(-1);
			BigInteger pseudo_m = p.add(negOne).multiply(q.add(negOne));
			BigInteger pseudo_m1 = pseudo_n.add(p.negate()).add(q.negate()).add(BigInteger.ONE);
			System.out.println(pseudo_m);
			System.out.println(pseudo_m.equals(pseudo_m1));
			
			BigInteger re = d.multiply(e).mod(pseudo_m);
			System.out.println(re);

			System.out.println(negOne.bitCount());
			System.out.println(negOne.bitLength());
			byte[] bits1 = negOne.toByteArray();
			System.out.println(Arrays.toString(bits1));
			System.out.println(re.bitCount());
			System.out.println(re.bitLength());
			byte[] bits2 = re.toByteArray();
			System.out.println(Arrays.toString(bits2));
			
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			System.out.println(publicKey);
			byte[] enc = encrypt.encrypt(publicKey, encryptText.getBytes());
			byte[] dec = encrypt.decrypt(privateKey, enc);
			System.out.println(new String(enc));
			System.out.println(new String(dec));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** */
	/**
	 * Change byte array to String.
	 * 
	 * @return byte[]
	 */
	protected String bytesToString(byte[] encrytpByte) {
		String result = "";
		for (Byte bytes : encrytpByte) {
			result += (char) bytes.intValue();
		}
		return result;
	}

	/** */
	/**
	 * Encrypt String.
	 * 
	 * @return byte[]
	 */
	protected byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
		if (publicKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				return cipher.doFinal(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/** */
	/**
	 * Basic decrypt method
	 * 
	 * @return byte[]
	 */
	protected byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
		if (privateKey != null) {
			try {
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				return cipher.doFinal(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
