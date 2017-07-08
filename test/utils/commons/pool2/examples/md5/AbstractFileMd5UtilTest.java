package utils.commons.pool2.examples.md5;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

/**
 * Created by liumengjun on 08/07/2017.
 */
public abstract class AbstractFileMd5UtilTest {

    private Random rand = ThreadLocalRandom.current();
    private FileMd5Util fileMd5Util;

    public AbstractFileMd5UtilTest() {
        this.fileMd5Util = this.newFileMd5Util();
    }

    abstract FileMd5Util newFileMd5Util();

    public final void testCalcFileMd5Random() throws Exception {
        String md5 = null;
        switch (rand.nextInt() % 3) {
            case 0:
                md5 = fileMd5Util.calcFileMd5("text/text_file.txt");
                assertEquals(Constant.TEXT_FILE_MD5, md5);
                break;
            case 1:
                md5 = fileMd5Util.calcFileMd5("text/text_file2.txt");
                assertEquals(Constant.TEXT_FILE2_MD5, md5);
                break;
            case 2:
                md5 = fileMd5Util.calcFileMd5("audio/yfdjj.mp3");
                assertEquals(Constant.UDIO_YFDJJ_MD5, md5);
                break;
        }
    }

    public final void testCalcFileMd5All() throws Exception {
        String md5 = null;
        md5 = fileMd5Util.calcFileMd5("text/text_file.txt");
        assertEquals(Constant.TEXT_FILE_MD5, md5);
        md5 = fileMd5Util.calcFileMd5("text/text_file2.txt");
        assertEquals(Constant.TEXT_FILE2_MD5, md5);
        md5 = fileMd5Util.calcFileMd5("audio/yfdjj.mp3");
        assertEquals(Constant.UDIO_YFDJJ_MD5, md5);
    }
}
