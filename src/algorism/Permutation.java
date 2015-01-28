package algorism;
/**
 * 全排列
 * @author zhonglijunyi<br>
 * 方法：nextLargestPermutation(char[])
 * 		按字典序,产生下一个最大的排列<br>
 * 有个例子
 */
public class Permutation {
	
	/**
	 * 有个题目：求1,2,2,3,4,5的全排列，但是,4不在第三位，1、5不在一起
	 * @param args
	 */
	public static void main(String[] args) {
		char a[] = {'1','2','2','3','4','5'};
		String str;
		int i,count = 1;
		str = new String(a);
		System.out.println(str);
		for(i = 0; i < 720; i++){
			if(nextLargestPermutation(a)){//按字典序,产生下一个最大的排列
				if(check(a)){			//检查a的顺序是否符合要求
					count ++;
					str = new String(a);
					System.out.println(str);
				}
			}
		}
		System.out.println("符合条件的数有："+count+"个。");
	}
	
	/**
	 * 检查a的顺序是否符合要求：4不在第三位，1、5不在一起
	 * @param a 一个数组，标示序列
	 * @return 符合要求返回true
	 */
	private static boolean check(char[] a){
		if(a[2] == '4')//4在第三位了
			return false;
		int i = 0;
		//搜索第一个 1、5 出现的位置
		while((a[i] != '5') && (a[i]!='1')){
			i++;
		}
		//下一个是 1、5
		if(a[i+1] == '1' || a[i+1] == '5')
			return false;
		return true;
	}
	
	/**
	 * 按字典序,产生下一个最大的排列
	 * @param a 一个数组
	 * @return 如果有一个更大的则返回true;
	 */
	public static boolean nextLargestPermutation(char[] a){
		int j = a.length - 2;
		while(a[j] >= a[j+1]){
			j--;
			if(j < 0)
				return false;
		}
		//now j is the largest subscript(下标) with a[j] < a[j+1]
		int k = a.length - 1;
		while(a[j] >= a[k]){
			k--;
		}
		//a[k] is the smallest integer greater than a[j] to the right of a[j]
		//interchange a[j] and a[k]
		//System.out.println("j="+j+",k="+k);
		char temp;
		temp = a[j];
		a[j] = a[k];
		a[k] = temp;
		int r = a.length - 1;
		int s = j + 1;
		//System.out.println("r="+r+",s="+s);
		while(r > s){
			//interchange a[r] and a[s]
			temp = a[r];
			a[r] = a[s];
			a[s] = temp;
			r--;
			s++;
		}
		//this puts the tail end of the permutation after the j_th position
		//	increasing order
		return true;
	}
}
