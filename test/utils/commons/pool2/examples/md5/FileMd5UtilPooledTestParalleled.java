package utils.commons.pool2.examples.md5;

import org.junit.Test;
import org.junit.runner.RunWith;
import runners.ParallelTestClassRunner;

/**
 * Created by liumengjun on 08/07/2017.
 * <p>并行执行所有的测试方法，可知{@link FileMd5UtilSingleton}不支持多线程
 */
@RunWith(ParallelTestClassRunner.class)
public class FileMd5UtilPooledTestParalleled extends AbstractFileMd5UtilTest {

    @Override
    FileMd5Util newFileMd5Util() {
        return new FileMd5UtilPooled();
    }

    @Test
    public void testCalcFileMd5_0() throws Exception {
        this.testCalcFileMd5Random();
    }

    @Test
    public void testCalcFileMd5_1() throws Exception {
        this.testCalcFileMd5Random();
    }

    @Test
    public void testCalcFileMd5_2() throws Exception {
        this.testCalcFileMd5Random();
    }

    @Test
    public void testCalcFileMd5_3() throws Exception {
        this.testCalcFileMd5Random();
    }
}
