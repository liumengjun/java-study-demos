
public class test {
	public static void main(String[] args) {
		Object obj = new Object();
		System.out.println("obj.hash:"+obj.hashCode());
		
		String str = new String("00");
		int h = str.hashCode();
		System.out.println("str.hash:"+h);
		
		test tt = new test();
		System.out.println(tt.hashCode());
		tt.testBoyHash();
		tt.testGirlHash();
	}
	
	void testBoyHash(){
		TheBoy boy = new TheBoy();
		System.out.println(boy.hashCode());
	}
	
	void testGirlHash(){
		TheGirl girl = new TheGirl("");
		System.out.println(girl.hashCode());
	}
	
	class TheBoy{
		public TheBoy(){
			
		}
	}
	
	class TheGirl{
		private String name = "I'm a girl!";
		public TheGirl(String name){
			this.name = name;
		}
	}
}
