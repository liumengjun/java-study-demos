package utils.commons.pool2.examples.md5;

import org.junit.Test;

/**
 * Created by liumengjun on 08/07/2017.
 */
public class FileMd5UtilTest extends AbstractFileMd5UtilTest {

    @Override
    FileMd5Util newFileMd5Util() {
        return new FileMd5UtilSingleton();
    }

    @Test
    public void testCalcFileMd5() throws Exception {
        this.testCalcFileMd5All();
    }

    @Test
    public void testCalcFileMd5_1() throws Exception {
        this.testCalcFileMd5Random();
    }

}
