
public class IntegerTest {
	public static void main(String[] args) {
		Integer integer = new Integer(0);
		System.out.println(integer.toString());
		integer++;
		System.out.println(integer);
		increase(integer);
		System.out.println(integer);
		int i=0;
		for(;i<0;i++){
			System.out.println("执行了一次");
		}
		int[] array = new int[0];
		for(i=0;i<array.length;i++){
			System.out.println("array["+i+"]="+i);
		}
	}
	
	public static void increase(Integer integer){
		integer+=5;
		System.out.println("in increase():"+integer);
	}
}
