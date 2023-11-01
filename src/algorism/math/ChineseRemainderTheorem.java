package algorism.math;

public class ChineseRemainderTheorem {
	public static void main(String[] args) {
		int[] a = new int[]{2,3};
		int[] m = new int[]{3,5};
		int x = sovle(a, m);
		System.out.println(x);
	}
	public static int sovle(int[] a, int[] m) {
		if (a==null || m==null || a.length==1 || m.length!=a.length) {
			return 0;
		}
		final int N = a.length;
		int _m=1, M[]=new int[N], y[]=new int[N];
		// _m=∏(m[k]), M[k]=_m/m[k]
		for(int v: m) {
			_m*=v;
		}
		for(int k=0; k<N; k++) {
			M[k] = _m/m[k];
		}
		// M[k]*y[k]≡1(mod m[k])
		for(int k=0; k<N; k++) {
			for (int _=M[k]/m[k], t=(1>_?1:_);true;t++) {
				int p = t*m[k]+1;
				if (p%M[k]==0) {
					y[k]=p/M[k];
					break;
				}
			}
		}
		// s=∑(a[k]*M[k]*y[k])
		int s=0;
		for (int k=0;k<N;k++) {
			s+=a[k]*M[k]*y[k];
		}
		
		return s%_m;
	}
}
