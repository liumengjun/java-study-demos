package crypt;

import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import javax.annotation.Nonnull;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class OpenSSHRSAKeyReader {

    public static void main(String[] args) throws Exception {
        RSAPrivateKey privateKey = readSSHRSAPrivateKey(rsaPrivateKeyText);
        RSAPublicKey publicKey = readSSHRSAPublicKey(rsaPublicKeyText);
        assert privateKey.getModulus().equals(publicKey.getModulus());
        System.out.println(privateKey);
        System.out.println(privateKey.getModulus());
        System.out.println(publicKey);

        String home = System.getProperty("user.home");
        privateKey = readSSHRSAPrivateKey(new File(home + "/.ssh/id_rsa"));
        publicKey = readSSHRSAPublicKey(new File(home + "/.ssh/id_rsa.pub"));
        assert privateKey.getModulus().equals(publicKey.getModulus());
        System.out.println(privateKey);
        System.out.println(privateKey.getModulus());
        System.out.println(publicKey);
    }

    /**
     * 读取ssh-keygen生成的私钥(from file)
     */
    public static RSAPrivateKey readSSHRSAPrivateKey(@Nonnull File rsaPrivateKeyFile)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] bytes = Files.readAllBytes(rsaPrivateKeyFile.toPath());
        return readSSHRSAPrivateKey(new String(bytes));
    }

    /**
     * 读取ssh-keygen生成的私钥(from file text)
     * 格式RSA Private Key file (PKCS#1), 参考 rfc8017#appendix-A.1.2
     */
    public static RSAPrivateKey readSSHRSAPrivateKey(@Nonnull String rsaPrivateKeyText)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        StringBuilder keyInfoSb = new StringBuilder(rsaPrivateKeyText.length());
        BufferedReader br = new BufferedReader(new StringReader(rsaPrivateKeyText));
        // 去掉文件头尾的注释信息
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("---"))
                continue;
            keyInfoSb.append(line);
        }
        return readSSHRSAPrivateKeyBody(keyInfoSb.toString());
    }

    /**
     * 读取ssh-keygen生成的私钥(from body)
     * 格式RSA Private Key file (PKCS#1), 参考 rfc8017#appendix-A.1.2
     */
    public static RSAPrivateKey readSSHRSAPrivateKeyBody(@Nonnull String rsaPrivateKeyBody)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // 密钥信息用 BASE64 编码了，需要先解码
        byte[] decodeKeyInfo = Base64.getDecoder().decode(rsaPrivateKeyBody);
        // 使用 DerInputStream 读取密钥信息
        DerInputStream dis = new DerInputStream(decodeKeyInfo);
        // 密钥不含 otherPrimeInfos 信息，故只有 9 段
        DerValue[] ders = dis.getSequence(9);
        // 依次读取 RSA 因子信息
        // int version = ders[0].getBigInteger().intValue(); // it's 0, ignore
        BigInteger modulus = ders[1].getBigInteger();
        BigInteger publicExponent = ders[2].getBigInteger();
        BigInteger privateExponent = ders[3].getBigInteger();
        BigInteger primeP = ders[4].getBigInteger();
        BigInteger primeQ = ders[5].getBigInteger();
        BigInteger primeExponentP = ders[6].getBigInteger();
        BigInteger primeExponentQ = ders[7].getBigInteger();
        BigInteger crtCoefficient = ders[8].getBigInteger();
        // generate private key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent,
                primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
        PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
        return (RSAPrivateKey) privateKey;
    }


    /**
     * 读取ssh-keygen生成的公钥(from file)
     */
    public static RSAPublicKey readSSHRSAPublicKey(@Nonnull File rsaPublicKeyFile)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(rsaPublicKeyFile.toPath());
        return readSSHRSAPublicKey(new String(bytes));
    }

    /**
     * 读取ssh-keygen生成的公钥(from file text)
     * 参考 rfc4716#section-3.4, rfc4253#section-6.6
     */
    public static RSAPublicKey readSSHRSAPublicKey(@Nonnull String rsaPublicKeyText)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // 只支持'ssh-rsa'格式
        final String tag = "ssh-rsa";
        if (!rsaPublicKeyText.startsWith(tag)) {
            throw new IllegalArgumentException("Only support 'ssh-rsa' format!");
        }
        int startPos = tag.length() + 1;
        int endPos = rsaPublicKeyText.lastIndexOf(' ');
        String keyInfoStr = rsaPublicKeyText.substring(startPos, endPos);
        return readSSHRSAPublicKeyBody(keyInfoStr);
    }

    /**
     * 读取ssh-keygen生成的公钥(from body)
     * 参考 rfc4716#section-3.4, rfc4253#section-6.6
     */
    public static RSAPublicKey readSSHRSAPublicKeyBody(@Nonnull String rsaPublicKeyBody)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // 密钥信息用 BASE64 编码了，需要先解码
        byte[] decodeKeyInfo = Base64.getDecoder().decode(rsaPublicKeyBody);
        // 使用 DataInputStream 读取密钥信息
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decodeKeyInfo));
        // RSA 公钥的语法结构包含(string "ssh-rsa", mpint e, mpint n)
        BigInteger formatId = readBigInt(dis); // integer form of string "ssh-rsa" 0x7373682d727361
        if (!formatId.toString(16).equals("7373682d727361")) {
            throw new IllegalArgumentException("Only support 'ssh-rsa' format!");
        }
        BigInteger publicExponent = readBigInt(dis);
        BigInteger modulus = readBigInt(dis);
        //generate public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);
        return (RSAPublicKey) publicKey;
    }

    /**
     * 读入前 4 个字节获得元素长度，然后读取该元素字节信息并转换为 BigInteger
     */
    private static BigInteger readBigInt(DataInputStream dis) throws IOException {
        int len = dis.readInt();
        byte[] tmpBytes = new byte[len];
        dis.readFully(tmpBytes);
        return new BigInteger(1, tmpBytes);
    }

    private final static String rsaPrivateKeyText = "-----BEGIN RSA PRIVATE KEY-----\n"
            + "MIIEowIBAAKCAQEAw0VfV2OExGmW6G7EyvnX+mSHxjO7ibBhsNUD1gWq5LAQbaDi\n"
            + "3Ow80+h9SI5ltp2cr9ASxgWgUlwdCQgwjvpei6XPB86eIKwuCQ9l6T1anIx0REBQ\n"
            + "xrvKVv2R5X51S5y6rBr/8NPqr/t/Q2U+OWJ91iqf2wFYO1e3HoOO+I89yybyV/rj\n"
            + "EE/XZgYUUxmkLWAgOj5Rmk1WCjHSHpMVgKtExZ3nOVCWpaiMGD27tlsGyyp9Foc+\n"
            + "3JWOR9pnJC4wavDo/9FXIKOnhN1xK0mBhB5sAJdSigSonGM1aLyST49JDQAKI9l8\n"
            + "1wrb67nSJjq6WSG1/JG5++GZ2NMjWaTAipAHHwIDAQABAoIBAGW+KJ4c4VmBdrqm\n"
            + "tGzZ3vm9Fa1k2Qs11sDPTI9gJpm/TuAAGxRnFo6HLXf3kfGjj7dfX9jDp7QiAJLr\n"
            + "LHni5A45F3UG6Ac4D0hzOHNdn2/AEvhY1n2l7RiESFdeG3mJMQ5phv+RO0/0Z2vT\n"
            + "PaeitNQ5I092FOXenzAL9ht2bwkA2W+vTJHTbV36i35yyoKpW8havGUaZkC2Ki6H\n"
            + "bEABdTxsFo5NSlQh1Y8ElWyChQluv7ufLIU9BwyA9pA9hlfm9A5uPokJLRa4l6K4\n"
            + "C+2lwGbPelJCtUrAdvf+pGb0EaxaioJyS836kds37kkiNcHMZMiG2W/qvqIyl63x\n"
            + "/aOU8xkCgYEA70CBsWPw3wzoRs/QlOamJb8twT6ZwS9diNg0Q/rgPpsdUmMUoTSD\n"
            + "nmQ7XhOY3LtRRmSPAoPZR0RcHpqOzrYm6D7mp/D1ajxbH8rMVJdixhrmh9Eyfhaa\n"
            + "3KpPHdYxQsyPbyoE92J1pvrMRDrztFBmH0vCYc0rYf56ESTGQxNeRfMCgYEA0PC1\n"
            + "WwSxcXGvSyHxNczJ7cDCqhqmzc0ZXzLKi2KPMjv5sCU/CQk0Y8CDXuyv0KcvO6Pz\n"
            + "c5qZmBjh0/cZwDPiKoQ4AjS2mPy/LQOeWEg8e8wgcZWo6dmbvdo4ywer+TgBDv32\n"
            + "ZA/MQizHMD5a/xOrdU6f361uo+SBMitwustUKSUCgYA2MoiupJMmFAhyMhVwBafR\n"
            + "mTNGOWYfKxmnLEL6u1QdGopT5XoA4qKGOPJx50KTjiNlLNZwnWmVCVlM3XpIPV8m\n"
            + "8UjS9nTBshZ6ZzhOWVTYx7M9RRiJE5nVLwdpHOJf3XMInZ4uZDF+uAWZ1zjpueTI\n"
            + "ZiMMgKu5MSp5w7jd7P9IHQKBgHRXU+RWfKd6ce0X/UPDwDyOtNYa+P5nUdVcZsOu\n"
            + "IaYLF0EpvCu/4Fga/guBJ3S7YuWGqSyZzSSP1k88rC4Ftc2OydEQoQwW0Cy0cxsd\n"
            + "PfCA/R/CAcgUz0+rjyweHXgr4wsCBow477d5T4BheDHINhSfrw3ReGXx+q6A2chp\n"
            + "J3exAoGBAOswTFWBddUs4VXTLKowvQZ5mKPoL9LcrX+q7/uYkfzKYtBJ5Hu5f3p/\n"
            + "1lDNhji/ubDzhJlJMT5vCRMnGCpJy+WDrEOPZnsGISDNnnzqScXgJNNpy6+GCjCy\n"
            + "OUhZ+dyo3z9TAR1iXO20ZSD+hZ0+5TWf2xUZGT2XT1iloEPq8oX/\n"
            + "-----END RSA PRIVATE KEY-----\n";

    private final static String rsaPublicKeyText = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDDRV9XY4TEaZbobsTK+df6ZIfGM7uJsGGw1QPWBarksBBtoOLc7DzT6H1IjmW2nZyv0BLGBaBSXB0JCDCO+l6Lpc8Hzp4grC4JD2XpPVqcjHREQFDGu8pW/ZHlfnVLnLqsGv/w0+qv+39DZT45Yn3WKp/bAVg7V7ceg474jz3LJvJX+uMQT9dmBhRTGaQtYCA6PlGaTVYKMdIekxWAq0TFnec5UJalqIwYPbu2WwbLKn0Whz7clY5H2mckLjBq8Oj/0Vcgo6eE3XErSYGEHmwAl1KKBKicYzVovJJPj0kNAAoj2XzXCtvrudImOrpZIbX8kbn74ZnY0yNZpMCKkAcf scott@localhost";
}
