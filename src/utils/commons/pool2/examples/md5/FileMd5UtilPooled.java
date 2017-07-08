package utils.commons.pool2.examples.md5;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liumengjun on 08/07/2017.
 */
public class FileMd5UtilPooled implements FileMd5Util {
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static ObjectPool<MessageDigest> pool;

    static {
        pool = new GenericObjectPool(new Md5MessageDigestFactory());
    }

    public String calcFileMd5(String filename) {
        try {
            return this.getFileMd5(filename);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileMd5(String fileName) throws Exception {
        File file = new File(fileName);
        System.out.println("File: " + file.getName());
        //System.out.println("Path: " + file.getAbsolutePath());
        //System.out.println("Size: " + file.length() + "B");

        MessageDigest md5 = pool.borrowObject();
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = in.read(bytes)) != -1) {
            md5.update(bytes, 0, len);
        }
        byte[] md5Bytes = md5.digest();
        pool.returnObject(md5);
        return bytesToHex(md5Bytes);
    }

    private static String bytesToHex(byte bytes[]) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            byte bt = bytes[i];
            sb.append(HEX_DIGITS[(bt & 0xf0) >> 4]);
            sb.append(HEX_DIGITS[bt & 0xf]);
        }
        return sb.toString();
    }

    static class Md5MessageDigestFactory extends BasePooledObjectFactory<MessageDigest> {
        /**
         * pooled md5, create instance by clone the private md5 instance
         */
        private MessageDigest md5;

        public Md5MessageDigestFactory() {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MessageDigest create() throws Exception {
            return (MessageDigest) md5.clone();
        }

        @Override
        public PooledObject<MessageDigest> wrap(MessageDigest obj) {
            return new DefaultPooledObject<MessageDigest>(obj);
        }
    }
}
