/**
 * 探索循环展开带来的性能提升
 * <p>按照一定的步数跳跃，每次循环处理多个对象，循环次数减少了，循环也变胖了。
 * <p>如果循环体内计算简单，性能提升显著，如果循环体很复杂，几乎没有提升。
 * <p>Created by liumengjun on 2023-12-28.
 */
public class LoopUnwindingTest {
    public static void main(String[] args) {
        int n = 1_0000_0000;
        // ---普通循环求和---
        long start = System.currentTimeMillis();
        long result = sum(n);
        long time = System.currentTimeMillis() - start;
        System.out.println("①Σ" + n + "= " + result + ", time: " + time);
        // ---循环展开求和---
        start = System.currentTimeMillis();
        long result2 = sum2(n);
        long time2 = System.currentTimeMillis() - start;
        System.out.println("②Σ" + n + "= " + result2 + ", time: " + time2);
        // ---对比---
        if (result2 != result) {
            System.out.printf("Oh god, second result is wrong, %s != %s!\n", result2, result);
            return;
        }
        System.out.printf("result ok, speed: %.2fx\n", (double) time / time2);
    }


    /**
     * 普通循环求和
     */
    private static long sum(int n) {
        long result = 0;
        for (int j = 1; j <= n; j++) {
            result += calc0(j);
        }
        return result;
    }

    /**
     * 循环展开求和
     * <p>按照一定的步数跳跃，每次循环处理多个对象，使循环次数减少，这样性能也许有一定提升。
     * <p>(区别于全部展开，如果n很小比如<10，也许全部展开不用循环就可以了)
     */
    private static long sum2(int n) {
        long result = 0;
        final int gap = 9;  // 尝试寻找最优的`gap`, 2～3性能不好，4以上都差不多。
        int m = n / gap * gap;
        for (int j = 1; j <= m; j += gap) {
            result += calc0(j);
            result += calc0(j + 1);
            result += calc0(j + 2);
            result += calc0(j + 3);
            result += calc0(j + 4);
            result += calc0(j + 5);
            result += calc0(j + 6);
            result += calc0(j + 7);
            result += calc0(j + 8);
//            result += calc0(j + 9);
        }
        for (int j = m + 1; j <= n; j++) {
            result += calc0(j);
        }
        return result;
    }

    /**
     * 对每次循环对象的处理函数，随着计算量的增大，循环展开的加成越来越小。
     * 如果计算太复杂，`N`太大的话就不知道什么时候结束了
     */
    private static long calc0(long j) {
//        return j;
//        return j * 3;
//        return Math.abs(j);
//        return (long) Math.floor(j);
//        return (long) Math.sqrt(j);
        return (long) Math.log1p(j);
//        return (long) (Math.sin(j) * 1000);
    }
}
