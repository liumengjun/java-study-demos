package algorism.math;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 2000001;
		int r1,r2;
		
		long start,end1,end2,end3,consumingTime1,consumingTime2,consumingTime3;
		while(true){
			start = System.currentTimeMillis();
			r1 = (int)Math.ceil(Math.sqrt((int)n));
			end1 = System.currentTimeMillis();
			r2 = 1;
			while(r2 * r2 < n)	r2++;
			end2 = System.currentTimeMillis();
			System.out.println("(int)Math.ceil(Math.sqrt((int)"+n+"))\t = "+r1);
			System.out.println("while(r2 * r2 < "+n+")	r2++\t = "+r2);
			consumingTime1 = end1-start;
			consumingTime2 = end2-end1;
			System.out.println("First time spended :"+consumingTime1+"(ms)");
			System.out.println("Secend time spended :"+consumingTime2+"(ms)");
			if(consumingTime1 > consumingTime2)
				n++;
			else
				break;
		}
		//用两种方法求素数
		start = System.currentTimeMillis();
		int[] prime1 = PrimeNumber.getPrimeLessEqual(n);
		end1 = System.currentTimeMillis();
		int[] prime2 = PrimeNumber.sieve(n);
		end2 = System.currentTimeMillis();
		int[] prime3 = PrimeNumber.sieve2(n);
		end3 = System.currentTimeMillis();
		consumingTime1 = end1-start;
		consumingTime2 = end2-end1;
		consumingTime3 = end3-end2;
		System.out.println("1:小于等于 "+n+" 的质数共有 "+(prime1==null?0:prime1.length)+" 个!");
		System.out.println("2:小于等于 "+n+" 的质数共有 "+(prime2==null?0:prime2.length)+" 个!");
		System.out.println("3:小于等于 "+n+" 的质数共有 "+(prime3==null?0:prime3.length)+" 个!");
		System.out.println("First time spended :"+consumingTime1+"(ms)");
		System.out.println("Secend time spended :"+consumingTime2+"(ms)");
		System.out.println("Third time spended :"+consumingTime3+"(ms)");
//		for(int i=0; i<prime.length; i++){
//			System.out.println(i+"\t"+prime[i]);
//		}
		System.out.println();
		
	}

}
