package _exercise;  // 使用`jmh`必须有包

import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.IO.println;


/// 各个方法性能分情况而不同
///
/// - 数据集很小(比如10000以内): 不要用多线程, 用普通方法或 UnionFind 都可以，普通方法时间更稳定。
/// - 数据规模中等时(比如20000~50000以内): 各方法性能差不多, 速度相差几秒。多线程优势开始显现, UnionFind 也不错, 普通方法表现不佳。
/// - 只有数据集特别大时(50000+): 多线程方法, 联结度越高(子线程结果集越小), 多线程效果越好; 如果很分散则效果反而不好。UnionFind中规中矩, 普通方法很差不可取。
/// - 数据集再大时, 都很慢, 选啥都不在乎了; 数据集极大时, 单机可能都跑不了了。
/// * 总之, 普通方法在数据集很小时用; 一般用 UnionFind 方法比较好, 如果联结度较高, 多线程方法好。
///
/// (1)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 26;
/// NAME_LEN = 2;
/// PAIRS_COUNT = 1000;
/// Benchmark                                                    Mode  Cnt   Score    Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  42.058 ± 87.390  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  11.250 ±  9.869  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  13.462 ±  9.547  ms/op
///
/// (2)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 26;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 10000;
/// Benchmark                                                    Mode  Cnt     Score      Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  4241.257 ±  768.931  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  3408.043 ± 3716.080  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  2593.042 ± 1140.117  ms/op
///
/// (3)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 26;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 20000;
/// Benchmark                                                    Mode  Cnt      Score      Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  10565.459 ± 5987.829  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  11807.955 ± 9300.289  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  13170.790 ± 4336.158  ms/op
///
/// (4)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 30;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 20000;
/// Benchmark                                                    Mode  Cnt      Score      Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  12182.172 ± 3578.090  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  13800.118 ± 7755.257  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  16804.287 ± 9455.984  ms/op
///
/// (5)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 40;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 30000;
/// Benchmark                                                      Mode  Cnt      Score       Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread    avgt    3  35061.882 ± 10119.449  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThreadUF  avgt    3  35786.120 ± 33275.911  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind      avgt    3  28581.028 ± 31373.087  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon           avgt    3  30341.218 ± 28485.548  ms/op
///
/// (6)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 40;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 40000;
/// Benchmark                                                    Mode  Cnt      Score       Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  42745.308 ± 10339.791  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  48902.025 ± 11980.506  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  55661.903 ± 22277.066  ms/op
///
/// (7)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 30;
/// NAME_LEN = 3;
/// PAIRS_COUNT = 60000;
/// Benchmark                                                    Mode  Cnt      Score       Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    6  36280.186 ± 13358.555  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    6  66528.104 ± 13421.020  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    6  92497.742 ± 30524.437  ms/op
///
/// (?)
/// UnitePairsToGroups
/// NAME_CHARSET_LENGTH = 20;
/// NAME_LEN = 4;
/// PAIRS_COUNT = 80000;
/// Benchmark                                                    Mode  Cnt       Score         Error  Units
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByMultiThread  avgt    3  434122.825 ±   53078.778  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsByUnionFind    avgt    3  380295.379 ± 2618382.886  ms/op
/// UnitePairsToGroupsBenchmark.unitePairsToGroupsCommon         avgt    3  319808.313 ± 1829890.433  ms/op
///
//@Fork(value = 2, jvmArgsPrepend = "-Xmx512m")
@Fork(value = 1, jvmArgsPrepend = "-Xmx512m")
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class UnitePairsToGroupsBenchmark {
    static Constructor<?> workerConstructor;
    static Method m_readPairs;
    static Method m_genPairs;
    static Method m_unitePairsToGroups;
    static Method m_unitePairsToGroupsByUnionFind;
    static Method m_unitePairsToGroupsMultiThread;
    final static Object[] readPairsArgs = new Object[]{null};
//    final static Object[] readPairsArgs = new Object[]{new String[]{"resources/pairs.list3.txt"}};

    static {
        try {
            println(StringUtils.center("-static-", 80, '$'));
            Class<?> cls = Class.forName("UnitePairsToGroups");
            workerConstructor = cls.getDeclaredConstructor();
            workerConstructor.setAccessible(true);
            Method[] methods = cls.getDeclaredMethods();
            m_readPairs = Arrays.stream(methods).filter(
                    m -> m.getName().equals("readPairs")).findFirst().get();
            m_readPairs.setAccessible(true);
            m_genPairs = Arrays.stream(methods).filter(
                    m -> m.getName().equals("genPairs")).findFirst().get();
            m_genPairs.setAccessible(true);
            m_unitePairsToGroups = Arrays.stream(methods).filter(
                    m -> m.getName().equals("unitePairsToGroups")).findFirst().get();
            m_unitePairsToGroups.setAccessible(true);
            m_unitePairsToGroupsByUnionFind = Arrays.stream(methods).filter(
                    m -> m.getName().equals("unitePairsToGroupsByUnionFind")).findFirst().get();
            m_unitePairsToGroupsByUnionFind.setAccessible(true);
            m_unitePairsToGroupsMultiThread = Arrays.stream(methods).filter(
                    m -> m.getName().equals("unitePairsToGroupsMultiThread")).findFirst().get();
            m_unitePairsToGroupsMultiThread.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws RunnerException, ReflectiveOperationException {
        println(StringUtils.center("-main-", 80, '@'));
        genData(40, 3, 30000);
        Object obj = workerConstructor.newInstance();
        m_readPairs.invoke(obj, readPairsArgs);

        Options opt = new OptionsBuilder()
                .include(UnitePairsToGroupsBenchmark.class.getSimpleName())
                .detectJvmArgs()
                .build();
        new Runner(opt).run();
    }

    @Setup
    public void setup() throws ReflectiveOperationException {
        println(StringUtils.center("-setup-", 80, '!'));
    }

    private static void genData(int charsetLen, int nameLen, int pairsCount) throws ReflectiveOperationException {
        Object obj = workerConstructor.newInstance();
        m_genPairs.invoke(obj, true, true, charsetLen, nameLen, pairsCount, null);
    }

    @Benchmark
    public void unitePairsToGroupsCommon() throws ReflectiveOperationException {
        Object obj = workerConstructor.newInstance();
        List list = (List) m_readPairs.invoke(obj, readPairsArgs);
        m_unitePairsToGroups.invoke(obj, list);
    }

    @Benchmark
    public void unitePairsToGroupsByUnionFind() throws ReflectiveOperationException {
        Object obj = workerConstructor.newInstance();
        List list = (List) m_readPairs.invoke(obj, readPairsArgs);
        m_unitePairsToGroupsByUnionFind.invoke(obj, list);
    }

    @Benchmark
    public void unitePairsToGroupsByMultiThread() throws ReflectiveOperationException {
        Object obj = workerConstructor.newInstance();
        List list = (List) m_readPairs.invoke(obj, readPairsArgs);
        m_unitePairsToGroupsMultiThread.invoke(obj, list, false);
    }

    @Benchmark
    public void unitePairsToGroupsByMultiThreadUF() throws ReflectiveOperationException {
        Object obj = workerConstructor.newInstance();
        List list = (List) m_readPairs.invoke(obj, readPairsArgs);
        m_unitePairsToGroupsMultiThread.invoke(obj, list, true);
    }
}
