package date;

public class DataSwapTest {
	public static void main(String[] args) {
		int[] a={1,2};
		int[] b={3,4};
		
		System.out.println(a);
		for(int i=0;i<a.length;i++){
			System.out.print("\t"+a[i]);
		}
		System.out.println();
		System.out.println(b);
		for(int i=0;i<b.length;i++){
			System.out.print("\t"+b[i]);
		}
		System.out.println();
		
		swap(a,b);
		
		System.out.println(a);
		for(int i=0;i<a.length;i++){
			System.out.print("\t"+a[i]);
		}
		System.out.println();
		System.out.println(b);
		for(int i=0;i<b.length;i++){
			System.out.print("\t"+b[i]);
		}
		System.out.println();
	}
	
	public static void swap(int[] a, int[] b){
		int[] c=a;
		a=b;
		System.out.println(a.toString());
		b=c;
		System.out.println(b.hashCode());
	}
	
}
