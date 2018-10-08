package crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Random;

/** */


/**
 * RSAEncrypt
 *
 * @author maqujun
 * @see
 */
public class RSAAESEncrypt {

    /**
     * Main method for RSAEncrypt.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String home = System.getProperty("user.home");
            RSAPrivateKey privateKey = OpenSSHRSAKeyReader.readSSHRSAPrivateKey(new File(home + "/.ssh/id_rsa"));
            RSAPublicKey publicKey = OpenSSHRSAKeyReader.readSSHRSAPublicKey(new File(home + "/.ssh/id_rsa.pub"));

            String plainText = "你好！who are you？";
            System.out.println(plainText);
            byte[] encBytes = encrypt(publicKey, plainText.getBytes(StandardCharsets.UTF_8));
            System.out.println(Base64.getEncoder().encodeToString(encBytes));

            byte[] decBytes = decrypt(privateKey, encBytes);
            String passedText = new String(decBytes, StandardCharsets.UTF_8);
            assert plainText.equals(passedText);
            System.out.println(passedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Encrypt String.
     *
     * @return byte[]
     */
    protected static byte[] encrypt(RSAPublicKey publicKey, byte[] textBytes) {
        try {
            // generate random AES keyBytes
            byte[] keyBytes = new byte[16];
            Random random = new Random();
            random.nextBytes(keyBytes);
            System.out.println(Base64.getEncoder().encodeToString(keyBytes));
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);

            // enc AES keyBytes and write into stream
            Cipher cipherRSA = Cipher.getInstance(RSA);
            cipherRSA.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedKeyBytes = cipherRSA.doFinal(keyBytes);

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOut);
            dos.writeInt(encryptedKeyBytes.length);
            dos.write(encryptedKeyBytes);

            // enc textBytes
            Cipher cipherAES = Cipher.getInstance(AES_TRANSFORMATION);
            cipherAES.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(new byte[16])); // 初始化向量为空
            byte[] encryptedTextBytes = cipherAES.doFinal(textBytes);
            dos.write(encryptedTextBytes);

            return byteOut.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Basic decrypt method
     *
     * @return byte[]
     */
    protected static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherBytes) {
        try {
            // read encrypted AES keyBytes and decrypt
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(cipherBytes));
            int encryptedKeyBytesLen = dis.readInt();
            byte[] encryptedKeyBytes = new byte[encryptedKeyBytesLen];
            dis.readFully(encryptedKeyBytes);

            Cipher cipherRSA = Cipher.getInstance(RSA);
            cipherRSA.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] keyBytes = cipherRSA.doFinal(encryptedKeyBytes);
            System.out.println(Base64.getEncoder().encodeToString(keyBytes));
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, AES);

            // read real cipherBytes, and decrypt
            byte[] encryptedTextBytes = new byte[cipherBytes.length - 4 - encryptedKeyBytesLen];
            dis.readFully(encryptedTextBytes);

            Cipher cipherAES = Cipher.getInstance(AES_TRANSFORMATION);
            cipherAES.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(new byte[16])); // 初始化向量为空
            return cipherAES.doFinal(encryptedTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String RSA = "RSA";
    private static final String AES = "AES";
    private static final String AES_TRANSFORMATION = "AES/CFB/PKCS5Padding";
}
