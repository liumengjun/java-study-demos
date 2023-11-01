package algorism._exercise;

import algorism.util.BasicTool;

public class Knapsack {

	//设n=5，p=[6,3,5,4,6],w=[2,2,6,5,4],且c=10
	static int N = 5;
	static int[] p = {6,3,5,4,6};
	static int[] w = {4,2,6,5,2};
	
	public static void main(String[] args) {
		int sum=0;
		int c = 10;
		
		String str;
		String[] name = {"a","b","c","d","e"};
		int[] x = knapsack(p, w, name, c);
		str="应该选择:";
		for(int i=0; i<N; i++){
			sum += x[i]*p[i];
			str += name[i] + "," + x[i] + "个;    ";
		}
		System.out.println(str + "\n    总价值:"+sum);
		
		/*
		sum = f(0,c);
		System.out.println("  总价值:"+sum);
		*/
	}
	
	/**
	 * knapsack（背包）问题：有N中物品<br>
	 * 		约束条件:	sum(weight[i]*count[i]) <= capacity;<br>
	 * 		求:		max{sum(weight[i]*profit[i])};<br>
	 * 		方法中会对输入数组重排序
	 * @param profit int[N] 单个物品的价值 
	 * @param weight int[N] 单个物品的重量
	 * @param name String[N] 每种物体的名字
	 * @param capacity int 最大容量
	 * @return count int[N] 存放结果：各个物品应选数量
	 */
	public static int[] knapsack(int [] profit, int[] weight,
			String[] name, int capacity){
		int N = profit.length;
		int i;
		if(N != weight.length || N != name.length){
			return null;
		}
		int[] count = new int[5];	//储存结果，各个商品的数量
		double[] price = new double[N];	//profit per weight:单价
		for(i = 0; i < N; i++){
			price[i] = profit[i]/weight[i];
		}
		/* 按price重排序:由大到小 */
		int j, indexOfNextMax;
		double nextMax;
		for(i = 0; i < N-1; i++){
			indexOfNextMax = i;
			nextMax = price[i];
			for(j = i+1; j < N; j++){
				if(price[j] > nextMax){
					indexOfNextMax = j;
					nextMax = price[j];
				}
			}
			if(i != indexOfNextMax){
				BasicTool.swap(price, i, indexOfNextMax);
				BasicTool.swap(profit, i, indexOfNextMax);
				BasicTool.swap(weight, i, indexOfNextMax);
				BasicTool.swap(name, i, indexOfNextMax);
			}
		}
		/* 计算各个商品的数量 */
		int limit,sum = 0, remaining = capacity;
		for(i = 0; i < N; i++){
			limit = remaining/weight[i];
			count[i] = limit;
			sum += limit * profit[i];
			remaining -= limit * weight[i];
		}
		return count;//返回结果
	}
	
	public static int f(int index, int remaining){
		if(remaining <= 0)
			return 0;
		int limit = remaining / w[index];
		if(index == N - 1){
			return limit * p[index];
		}
		int max = 0,temp = 0;
		for(int i=0; i<=limit;i++){
			temp = f(index+1,remaining-w[index]*i) + i*p[index];
			System.out.println("index="+index+":profit="+p[index]+",weight="+w[index]+",upper limit="+limit+",remaining="+remaining
					+"\n\t"+i+"个,possible profit="+temp+",max="+max+",remaining="+(remaining-w[index]*i));
			if(temp > max){
				max = temp;
			}
		}
		return max;
	}
}
