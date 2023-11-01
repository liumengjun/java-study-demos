package algorism.math;

import algorism.util.BitSet;
import algorism.util.IntNode;
import algorism.util.IntPair;
import algorism.util.IntegerList;

public class PrimeNumber {
	
	public static void main(String[] args) {
		/*
		int a = 122, b = 234;
		
		int[] prime1 = getPrimeLessEqual(a);
		System.out.print("小于等于 "+a+" 的质数:");
		for(int i=0; i<prime1.length; i++){
			System.out.print("    "+prime1[i]);
		}
		System.out.println();
		
		int[] prime2 = getPrimeLessEqual(b);
		System.out.print("小于等于 "+b+" 的质数:");
		for(int i=0; i<prime2.length; i++){
			System.out.print("    "+prime2[i]);
		}
		System.out.println();
		
		System.out.println(a+"和"+b+"的最大公约数是:\t"+greatCommonDivisor(a, b));
		System.out.println(a+"和"+b+"的最小公倍数是:\t"+leastCommomMultiple(a, b));
		System.out.println(a+" = "+primeFactorization(a));
		System.out.println(b+" = "+primeFactorization(b));
		*/
		int n = 2000000;
		int[] prime1 = getPrimeLessEqual(n);
		System.out.println("小于等于 "+n+" 的质数:");
		for(int i=0; i<prime1.length; i++){
			System.out.println(i+"\t"+prime1[i]);
		}
		System.out.println();
		IntPair[] f = primeFactorization(n,prime1);
		System.out.print(n+" = ");
		for(int i=0; i<f.length - 1; i++)
			System.out.print(f[i].getData1()+"^"+f[i].getData2()+" * ");
		System.out.print(f[f.length - 1].getData1()+"^"+f[f.length - 1].getData2());
	}
	
	/**
	 * getPrimeLessEqual():求小于等于 n 的所有prime（质数、素数）<br>
	 * 		1,对于小于等于10的情况，直接返回<br>
	 * 		2,大于10时:首先得到 小于等于sqrt(n)的质数 a[];<br>
	 * 			在判断 (sqrt(n) < m <= n] 中的数是否是质数:<br>
	 * 			判断 m 能否被 a[i] 整除;<br>
	 * @param n int
	 * @return int[] 所有prime
	 */
	public static int[] getPrimeLessEqual(int n){
		if(n <= 1) return null;
		if(n <= 10){	//10以内的prime
			IntegerList list = new IntegerList();
			if(2 <= n) list.add(2);
			if(3 <= n) list.add(3);
			if(5 <= n) list.add(5);
			if(7 <= n) list.add(7);
			return list.toArray();
		}else{			// (n >= 11) && (n <= Integer.MAX_VALUE)
			/*
			 * 首先得到 小于等于sqrt(n)的质数 a[];
			 * 在判断 (sqrt(n) < m <= n] 中的数是否是质数:
			 * 		判断 m 能否被 a[i] 整除;
			 */
			int sqrt = (int)Math.ceil(Math.sqrt((int)n));
			int[] a = getPrimeLessEqual(sqrt);	//得到小于等于sqrt(n)的prime
			IntegerList list = new IntegerList(a);
			boolean isPrime,flag;				//标志变量
			for(int m = sqrt+1; m <= n; m++){	//遍历(sqrt(n) < m <= n]
				isPrime = true;
				/* 下面测试数组a[]每个质数 */
				for(int i=0; (i<a.length) && isPrime; i++){
					flag = (m % a[i] != 0);		//符合prime条件吗?
					isPrime = isPrime && flag;
				}
				if(isPrime){	//如果是prime，则加入list
					list.add(m);
				}
			}
			return list.toArray();
		}
	}
	
	/**
	 * getPrimeLessEqual2():求小于等于 n 的所有prime（质数、素数）<br>
	 * @param n int
	 * @return int[] 所有prime
	 */
	public static int[] getPrimeLessEqual2(int n){
		IntegerList list = new IntegerList();
		if(2<n)list.add(2);
		if(3<n)list.add(3);
		if(n > 4){
			int end, m, listSize;
			IntNode head;
			boolean isPrime, flag;
			m = 5;
			end = 16;
			if(end > n) end = n;
			while(true){
				while(m <= end){
					isPrime = true;
					head = list.getHead();
					listSize = list.getSize();
					while(listSize>0 && isPrime){
						flag = (m % head.data != 0);		//符合prime条件吗?
						isPrime = isPrime && flag;
						listSize--;
						head = head.nextNode;
					}
					if(isPrime){	//如果是prime，则加入list
						list.add(m);
					}
					m++;
				}
				if(end == n)
					break;
				end = end*end;
				if(end > n) end = n;
			}
		}
		int[] a=list.toArray();
		if(a == null)
			return null;
		return a;
	}
	
	/**
	 * sieve():求小于等于 n 的所有prime（质数、素数）<br>
	 * @param n int
	 * @return int[] 所有prime
	 */
	public static int[] sieve(int n){
		BitSet b = new BitSet(n+1);
		IntegerList list = new IntegerList();
		int count = 0;
		int i;
		for (i = 2; i <= n; i++)
			b.set(i);
		i = 2;
		while (i * i <= n) {
			if (b.get(i)) {
				count++;
				list.add(i);
				int k = 2 * i;
				while (k <= n) {
					b.clear(k);
					k += i;
				}
			}
			i++;
		}
		while (i <= n) {
			if (b.get(i)){
				count++;
				list.add(i);
			}
			i++;
		}
		return list.toArray();
	}
	
	/**
	 * sieve2():求小于等于 n 的所有prime（质数、素数）<br>
	 * @param n int
	 * @return int[] 所有prime
	 */
	public static int[] sieve2(int n){
		boolean[] b = new boolean[n+1];
		IntegerList list = new IntegerList();
		int count = 0;
		int i = 2;
		//用false表示为prime
		//用true表示为composite
		while (i * i <= n) {
			if ( !b[i] ) {
				count++;
				list.add(i);
				int k = 2 * i;
				while (k <= n) {
					b[k]=true;
					k += i;
				}
			}
			i++;
		}
		while (i <= n) {
			if (!b[i]){
				count++;
				list.add(i);
			}
			i++;
		}
		return list.toArray();
	}
	
	/**
	 * 返回 a 和 b 的最大公约数(great common divisor)<br>
	 * Euclid算法:辗转相除法－求最大公因子的算法
	 * 
	 * @param a
	 *            第一个数
	 * @param b
	 *            第二个数
	 * @return gcd(a,b)
	 */
	public static int greatCommonDivisor(int a, int b){
		if(a<=0 && b<=0)
			return 1;
		if ((a==0) || (b==0))
			return a+b;
		else
			return greatCommonDivisor(a%b, b%a);
	}
	
	/**
	 * 返回 a 和 b 的最小公倍数(least common multiple)<br>
	 * 		lcm = a * b / gcd(a,b);
	 * @param a int 第一个数
	 * @param b int 第二个数
	 * @return lcm(a,b)
	 */
	public static int leastCommomMultiple(int a, int b){
		int gcd = greatCommonDivisor(a, b);
		int lcm = a*b/gcd;
		return lcm;
	}
	
	/**
	 * 对 n 进行质因数分解
	 * @param n int 要分解的数
	 * @return IntPair[] <P,E>[N] 表示 Π(Pi^Ei) i=[0:n-1] 
	 */
	public static IntPair[] primeFactorization(int n){
		int[] prime = getPrimeLessEqual(n);	//求出小于等于n的所有prime（质数）
		return primeFactorization(n, prime);
	}
	
	/**
	 * 对 n 进行质因数分解
	 * @param n int 要分解的数
	 * @param primes int[] 小于等于 n 的所有prime（质数、素数）
	 * @return IntPair[] <P,E>[N] 表示 Π(Pi^Ei) i=[0:n-1] 
	 */
	public static IntPair[] primeFactorization(int n, int[] primes){
		int len = primes.length;
		int[] exp = new int[len];
		int quotient = n;
		/* 求可以整除n的质数,以及指数 */
		for(int i=0; i<len; i++){
			while(quotient%primes[i] == 0){
				quotient /= primes[i];
				exp[i]++;
			}
		}
		/* 统计 */
		int count = 0;
		for(int i=0; i<len; i++){
			if(exp[i]>0) count++;
		}
		/* 组合factor字符串 */
		IntPair[] pairs = new IntPair[count];
		int index = 0;
		for(int i=0; i<len; i++){
			if(exp[i]>0){
				pairs[index] = new IntPair(primes[i],exp[i]);
				index++;
				if(index == count)
					break;
			}
		}
		return pairs;
	}

}
