package algorism.util;

public class BasicTool {
	
	public static void swap(int[] array, int i, int j){
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public static void swap(double[] array, int i, int j){
		double temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public static void swap(String[] array, int i, int j){
		String temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
