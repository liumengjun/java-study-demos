
public class StringArrayTest {
	public static void main(String[] args) {
		String[] strs = new String[3];
		for(int i=0;i<strs.length;i++){
			if(strs[i]==null){
				strs[i]="";
				System.out.println();
				//System.out.println(Integer.parseInt(strs[i]));
			}else
				System.out.println(strs[i]);
		}
		
		setNewValue(strs);
		for(int i=0;i<strs.length;i++){
			if(strs[i]==null){
				strs[i]="";
				System.out.println();
				//System.out.println(Integer.parseInt(strs[i]));
			}else{
				System.out.println(strs[i]);
				System.out.println(Integer.parseInt(strs[i]));
			}
		}
	}
	
	public static void setNewValue(String[] strs){
		for(int i=0;i<strs.length;i++){
			strs[i] = String.valueOf(Math.random()*23);
		}
	}
}
