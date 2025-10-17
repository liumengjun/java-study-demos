/**
 * 探索java中String的最大长度
 * <p>
 * Created by liumengjun on 2025-10-17.
 */
final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
final char[] cnDigits = new char[]{'〇', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
final char[] mixedDigits = Arrays.copyOf(digits, digits.length + cnDigits.length);
//System.arraycopy(cnDigits, 0, mixedDigits, digits.length, cnDigits.length);
final int utf16MaxLen = 1073741823;

//static {
//    ``compact source file``里不允许写`static{}`
//}

///
/// - 对于 @see String.LATIN1, 最大长度是`Integer.MAX_VALUE - 2`(仅限目前我测试的机器，不排除某些平台上是`Integer.MAX_VALUE - 1`)
/// - 对于 @see String.UTF16, 最大长度是`StringUTF16.MAX_LENGTH - 1`
/// - 混合的按`UTF16`对待
///
void main() {
    System.arraycopy(cnDigits, 0, mixedDigits, digits.length, cnDigits.length);
    IO.println(Arrays.toString(digits));
    IO.println(Arrays.toString(cnDigits));
    IO.println(Arrays.toString(mixedDigits));
    IO.println();

    tryBuildNLenStr(0xffffL);
    tryBuildNLenStr(0xfffffL);
    tryBuildNLenStr(0xffffffL);
    tryBuildNLenStr(0xfffffffL);
    tryBuildNLenStr(0xffffffffL);
}

/**
 * java.lang.OutOfMemoryError: Requested array size exceeds VM limit
 * 准确的限制与平台有关
 */
boolean tryBuildNLenStr(long num) {
    IO.println("try num = " + num + ", hex(num) = 0x" + Long.toHexString(num));

    int n = (int) num;
    if (n < 0 || num > Integer.MAX_VALUE) {
//        n = Integer.MAX_VALUE; // Requested array size exceeds VM limit
//        n = Integer.MAX_VALUE - 1; // Requested array size exceeds VM limit
        n = Integer.MAX_VALUE - 2; // OK
        /**
         * -Xms5g -Xmx5g, 4g is not enough, or heap error.
         */
        IO.println("Int最大值了, revised n = " + n);
    }

    IO.println("build n = " + n + ", hex(n) = 0x" + Integer.toHexString(n));

    // arab digit
    buildNStr0(n, digits);

//    // mixed digit
//    if (n >= utf16MaxLen) {
////        n = utf16MaxLen;
//        n = utf16MaxLen - 1;
//        IO.println("revised, try n = " + n + ", hex(n) = 0x" + Integer.toHexString(n));
//    }
//    buildNStr0(n, mixedDigits);

    // chinese digit
    /**
     * java.lang.OutOfMemoryError:
     * UTF16 String size is 2147483645, should be less than 1073741823
     * @see StringUTF16.MAX_LENGTH
     */
    if (n >= utf16MaxLen) {
//        n = utf16MaxLen;
        n = utf16MaxLen - 1;
        IO.println("revised, try n = " + n + ", hex(n) = 0x" + Integer.toHexString(n));
    }
    buildNStr0(n, cnDigits);

    boolean ok = !(n < num);
    IO.println(ok ? "OK" : "Fail");
    IO.println();
    return ok;
}

private void buildNStr0(int n, char[] optChars) {
    IO.println("build length = " + n + " string via chars " + Arrays.toString(optChars));
    StringBuilder sder = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
        sder.append(optChars[(int) (Math.random() * optChars.length)]);
    }
    String ds = sder.toString();

    IO.println("ds[0:10] = " + ds.substring(0, 10));
    IO.println("ds[-10:] = " + ds.substring(ds.length() - 10));
    IO.println("len(ds) = " + ds.length());
    IO.println("done");
}
