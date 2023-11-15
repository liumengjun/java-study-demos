package _test;

import org.junit.Test;

/**
 * java编译器对循环拼接字符串优化不好(java21依然)，只有一条拼接语句用+就可以，循环时用builder。
 */
public class StringPlusOrBuilderTest {
    /**
     * 使用+拼接字符串
     */
    public String concatStringByPlus(String prefix, int i) {
        return prefix + "-" + i;
    }

    /**
     * 使用StringBuilder拼接字符串
     */
    public String concatStringByStringBuilder(String prefix, int i) {
        return new StringBuilder().append(prefix).append("-").append(i).toString();
    }

    /**
     * 测试使用+拼接字符串耗时
     */
    @Test
    public void testConcat01ByPlus() {
        long startTime = System.currentTimeMillis();
        int count = 100000;
        for (int i = 0; i < count; i++) {
            String str = concatStringByPlus("testConcat01ByStringBuilder:", i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("testConcat01ByPlus，+拼接字符串" + count + "次，花费" + (endTime - startTime) + "ms");
    }


    /**
     * 测试使用StringBuilder拼接字符串耗时
     */
    @Test
    public void testConcat02ByStringBuilder() {
        long startTime = System.currentTimeMillis();
        int count = 100000;
        for (int i = 0; i < count; i++) {
            String str = concatStringByStringBuilder("testConcat02ByStringBuilder:", i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("testConcat02ByStringBuilder，拼接字符串" + count + "次，花费" + (endTime - startTime) + "ms");
    }

    public String loopConcatByPlus() {
        int count = 10000;
        String str = "testLoopConcatByPlus:";
        for (int i = 0; i < count; i++) {
            str += "-" + i;
        }
        return str;
    }

    public String loopConcatByStringBuilder() {
        int count = 100000;
        StringBuilder stringBuilder = new StringBuilder("testLoopConcatByStringBuilder:");
        for (int i = 0; i < count; i++) {
            stringBuilder.append("-");
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    /**
     * 循环使用+拼接字符串
     */
    @Test
    public void testLoopConcat03ByPlus() {
        long startTime = System.currentTimeMillis();
        String str = loopConcatByPlus();
        long endTime = System.currentTimeMillis();
        System.out.println("testLoopConcatByPlus，+拼接字符串，花费" + (endTime - startTime) + "ms");
        System.out.println(str);
    }

    /**
     * 测试循环使用StringBuilder拼接字符串耗时
     */
    @Test
    public void testLoopConcat04ByStringBuilder() {
        long startTime = System.currentTimeMillis();
        String str = loopConcatByStringBuilder();
        long endTime = System.currentTimeMillis();
        System.out.println("testLoopConcatByStringBuilder，拼接字符串，花费" + (endTime - startTime) + "ms");
        System.out.println(str);
    }

}
