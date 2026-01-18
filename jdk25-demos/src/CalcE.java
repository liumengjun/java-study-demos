/**
 * 计算自然常数(e)小数点后128位数值。
 * <p>
 * e = ∑{n=0->∞}1/n!
 * <p>
 * 前几天写了一个python版的, 这里是java版的, 对比一下。
 * [python版的](https://gitee.com/liumengjun/script_exercise/blob/master/py/power_series/calc_e.py)
 * math.e 的数值只保留到小数点后15位, 对于双精度浮点数足够了。
 * 不过 java 中 double 输出了小数点后16位, 可是第16位不准确了。
 * 同样如果把 context 精度 precision 改成`129`或更大时, 可以看到之前计算的第128位错了。
 * <p>
 * <p>
 * Note：
 * <p>
 * 双精度浮点数：
 * S EEEEEEEEEEE FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
 * 二进制： 符号位(sign), 11 位指数(exponent)(或称阶码), 52 位有效数(fraction)(或称分数值, 尾数)
 * value = (-1)^_sign * 1.fraction * 2^(E-127)  // E有特殊情况此处不表
 * <p>
 * java BigDecimal:
 * // 十进制： intVal 是表数值, scale 是阶码
 * bigDecimal = intVal * 10^(-scale);  // intVal 是 BigInteger, 通过unscaledValue()方法获得。intVal=null时由long转化来scale=0。
 * <p>
 * python decimal:
 * # 十进制： _int 是表数值, _exp 是阶码
 * # python 中 int 相当于 java 中 BigInteger
 * decimal = (-1)^_sign * _int * 10^_exp
 * <p>
 * Created by liumengjun on 2026-01-18.
 */


final MathContext MY_DECIMAL128 = new MathContext(128, RoundingMode.HALF_UP);

/*
 * 原始类型
 */
long factorial(int n) {
    long factorial = 1;
    for (int i = 1; i <= n; i++) {
        factorial *= i;
    }
    return factorial;
}

private double _f1n(int n) {
    return 1D / factorial(n);
}


double calcByPri() {
    return calcByPri(100);
}

double calcByPri(int count) {
    double sum = 0;
    for (var n = 0; n <= count; n++) {
        sum += _f1n(n);
    }
    return sum;
}

/*
 * BigDecimal
 */

BigInteger bigFactorial(int n) {
    BigInteger factorial = BigInteger.ONE;
    for (int i = 1; i <= n; i++) {
        factorial = factorial.multiply(BigInteger.valueOf(i));
    }
    return factorial;
}

private BigDecimal _decF1n(int n) {
    return BigDecimal.ONE.divide(new BigDecimal(bigFactorial(n)), MY_DECIMAL128);
}

BigDecimal calcByDec() {
    return calcByDec(100);
}

BigDecimal calcByDec(int count) {
    BigDecimal sum = BigDecimal.ZERO;
    for (var n = 0; n <= count; n++) {
        sum = sum.add(_decF1n(n), MY_DECIMAL128);
    }
    return sum;
}

void main() {
    IO.println("math.e:" + Math.E);

    IO.println();
    IO.println("=".repeat(20) + "1, calculate e by primitive type" + "=".repeat(20));
    IO.println("calculate e by primitive type, result = " + calcByPri());
    IO.println("primitive type double float precision in radix 10 is: " + Math.log10(2) * 52);
    IntStream.rangeClosed(0, 30).forEachOrdered(c -> {  // 这种写法语法有些生硬
        var e = calcByPri(c);
        IO.println("N: " + c + " " + e + ", " + Double.toHexString(e));
    });

    IO.println();
    IO.println("=".repeat(20) + "2, calculate e by decimal" + "=".repeat(20));
    IO.println("decimal's Context: " + MY_DECIMAL128);
    IO.println("calculate e by decimal:\n" + calcByDec());
    for (var c = 0; c < 100; c++) {
        IO.println("N: " + c + " " + calcByDec(c));
    }

    IO.println();
    IO.println("=".repeat(30) + "note" + "=".repeat(30));
    var dec_e = calcByDec();
    IO.println(dec_e);
    IO.println("intVal: " + dec_e.unscaledValue() + "\n" +
            "scale: " + dec_e.scale());
}
