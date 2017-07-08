package utils.commons.pool2.examples.md5;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileMd5UtilSingleton implements FileMd5Util {
    private static final char HEX_DIGITS[] =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    /*
     * singleton md5 instance
     */
    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = in.read(bytes)) != -1) {
            md5.update(bytes, 0, len);
        }
        byte[] md5Bytes = md5.digest();
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

}
