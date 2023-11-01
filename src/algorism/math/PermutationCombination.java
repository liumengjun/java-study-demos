package algorism.math;

public class PermutationCombination {
	/*排列组合计数*/
	private static int numOfPermuCombo = 0;
	
	/**
	 * main test
	 * @param args
	 */
	public static void main(String[] args) {
		char[] charSet = {'1','2','3','4','5'};
		int n = charSet.length;
		System.out.println("length(Set):"+n);
		/*生成组合*/
		for(int k=1;k<=n;k++){
			System.out.println();
			System.out.println("C("+k+","+n+"):");
			numOfPermuCombo = 0;
			genCombination(charSet, k);
			System.out.println("组合C("+k+","+n+")计数:"+numOfPermuCombo);
		}
		/*生成排列*/
		for(int k=1;k<=n;k++){
			System.out.println();
			System.out.println("P("+k+","+n+"):");
			numOfPermuCombo = 0;
			genPermutation(charSet, k);
			System.out.println("排列P("+k+","+n+")计数:"+numOfPermuCombo);
		}
		/*生成排列,可重复*/
		for(int k=1;k<=7;k++){
			System.out.println();
			System.out.println("A("+k+","+n+"):");
			numOfPermuCombo = 0;
			genPermutationDuplicate(charSet, k);
			System.out.println("全排列A("+k+","+n+")计数:"+numOfPermuCombo);
		}
	}
	
	/**
	 * 生成n个元素set的k组合
	 * @param nSet 非空字符集
	 * @param k 1<=k<=length(nSet)
	 */
	public static void genCombination(char[] nSet, int k){
		if(nSet==null) return;
		int n = nSet.length;
		if(n<1 || k<1 || k>n){//界限约束条件
			return;
		}
		char[] resultSet = new char[k];
		genCombinationInner(nSet, k, resultSet, 1);
	}
	
	/**
	 * 生成n个元素set的k组合的内部私有调用方法
	 * @param nSet 非空字符集
	 * @param k 1<=k<=length(nSet)
	 * @param resultSet 结果集
	 * @param depth 调用深度：从1到k
	 */
	private static void genCombinationInner(char[] nSet, int k, char[] resultSet, int depth){
		int n = nSet.length;
		for(int i=0;i<n;i++){
			resultSet[depth-1] = nSet[i];
			if( depth == k ){
				numOfPermuCombo++;
				System.out.println(new String(resultSet));
				continue;
			}
			char[] remainSet = new char[n-1-i];
			for(int r=0;r<remainSet.length;r++){
				remainSet[r] = nSet[r+i+1];
			}
			genCombinationInner(remainSet, k, resultSet, depth+1);
		}
	}
	
	/**
	 * 生成n个元素set的k排列
	 * @param nSet 非空字符集
	 * @param k 1<=k<=length(nSet)
	 */
	public static void genPermutation(char[] nSet, int k){
		if(nSet==null) return;
		int n = nSet.length;
		if(n<1 || k<1 || k>n){//界限约束条件
			return;
		}
		char[] resultSet = new char[k];
		genPermutationInner(nSet, k, resultSet, 1);
	}
	
	/**
	 * 生成n个元素set的k排列的内部私有调用方法
	 * @param remainSet nSet除去resultSet中字符
	 * @param k 1<=k<=length(nSet)
	 * @param resultSet 结果集
	 * @param depth 调用深度：从1到k
	 */
	private static void genPermutationInner(char[] remainSet, int k, char[] resultSet, int depth){
		int n = remainSet.length;
		for(int i=0;i<remainSet.length;i++){
			resultSet[depth-1] = remainSet[i];
			if( depth == k ){
				numOfPermuCombo++;
				System.out.println(new String(resultSet));
				continue;
			}
			char[] newRemainSet = new char[n-1];
			for(int s=0,r=0;s<n;s++){
				char setChar = remainSet[s];
				boolean notUse = true;
				for(int t=0;notUse&&t<depth;t++){
					notUse &= (setChar != resultSet[t]);
				}
				if(notUse){
					newRemainSet[r++] = setChar;
				}
			}
			genPermutationInner(newRemainSet, k, resultSet, depth+1);
		}
	}
	
	/**
	 * 生成n个元素set的可重复k排列
	 * @param nSet 非空字符集
	 * @param k 1<=k<=256（性能限制）
	 */
	public static void genPermutationDuplicate(char[] nSet, int k){
		if(nSet==null) return;
		if(k<1 || k>256){//限定字符个数
			return;
		}
		char[] resultSet = new char[k];
		genPermutationDuplicateInner(nSet, k, resultSet, 1);
	}
	
	/**
	 * 生成n个元素set的可重复k排列的内部私有调用方法
	 * @param nSet 非空字符集
	 * @param k 1<=k<=256（性能限制）
	 * @param resultSet 结果集
	 * @param depth 调用深度：从1到k
	 */
	private static void genPermutationDuplicateInner(char[] nSet, int k, char[] resultSet, int depth){
		int n = nSet.length;
		for(int i=0; i<n; i++){
			resultSet[depth-1] = nSet[i];
			if( depth==k ){
				numOfPermuCombo++;
				System.out.println(new String(resultSet));
				continue;
			}
			genPermutationDuplicateInner(nSet, k, resultSet, depth+1);
		}
	}
}
