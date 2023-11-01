package algorism.util;

public class IntegerList {
	private IntNode head;
	private IntNode tail;
	private int size;

	public static void main(String[] args) {
		IntegerList list = new IntegerList();
		list.add(2);
		list.add(3);
		list.add(5);
		list.add(7);
		int[] num = list.toArray();
		for(int i=0; i<num.length; i++){
			System.out.println(i+": "+num[i]);
		}
		System.out.println();
		IntegerList list2 = new IntegerList(num);
		int[] num2 = list2.toArray();
		for(int i=0; i<num2.length; i++){
			System.out.println(i+": "+num2[i]);
		}
	}
	
	public IntegerList(){
		head = null;
		tail = null;
		size = 0;
	}
	
	public IntegerList(int[] array){
		head = null;
		tail = null;
		size = 0;
		if(array == null){
			return;
		}else{
			for(int i=0; i<array.length; i++)
				this.add(array[i]);
		}
	}
	
	public void add(int number){
		if(head == null){
			head = new IntNode(number);
			tail = head;
			size++;
		}else{
			IntNode newNode = new IntNode(number);
			tail.nextNode = newNode;
			tail = newNode;
			size++;
		}
	}
	
	public int[] toArray(){
		if(size == 0)
			return null;
		else{
			int[] a = new int[size];
			IntNode current = head;
			for(int i = 0; i<size; i++){
				a[i] = current.data;
				current = current.nextNode;
			}
			return a;
		}
	}

	public IntNode getHead() {
		return head;
	}

	public int getSize() {
		return size;
	}
	
}
