import java.util.Stack;


public class BooleanArrayTest {
	public static final int And = 0;
	public static final int Or = 1;
	
	/**
	 * 
	 * @param relations
	 * @param AndOrs
	 * @return
	 */
	public static boolean testExpressions(boolean[] relations, int[] AndOrs){
		if(relations.length != (AndOrs.length+1)){
			//������Ӧ�ñȲ�������һ��
			return false;
		}
		if(relations.length==1){
			return relations[0];
		}
		Stack<Boolean> booleans = new Stack<Boolean>();
		Stack<Integer> ops = new Stack<Integer>();
		
		booleans.push(new Boolean(relations[0]));
		ops.push(new Integer(AndOrs[0]));
		
		int oldOp,newOp;
		boolean flag1,flag2,flag1OpFlag2;
		int i;
		for(i=1;i<AndOrs.length;i++){
			oldOp = ops.peek().intValue();	//�鿴ջ��������
			newOp = AndOrs[i];
			flag1 = booleans.peek().booleanValue();	//�鿴ջ��������
			flag2 = relations[i];
			if(newOp>=oldOp){
				//flag1 && flag2 || flag3�����
				//flag1 && flag2 && flag3�����
				//flag1 || flag2 || flag3�����
				booleans.pop();	//�Ƴ���ջ�����Ķ���flag1
				ops.pop();		//�Ƴ���ջ�����Ķ���oldOp
				//flag1 oldOp flag2
				if(oldOp==And)
					flag1OpFlag2 = flag1 && flag2;
				else//(oldOp==Or)
					flag1OpFlag2 = flag1 || flag2;
				booleans.push(new Boolean( flag1OpFlag2 ));
				ops.push(new Integer(newOp));
			}else{
				//flag1 || flag2 && flag3�����
				booleans.push(new Boolean( flag2 ));
				ops.push(new Integer(newOp));
			}
		}
		//System.out.println("now i="+i);
		booleans.push(new Boolean(relations[i]));//�����һ��������ѹջ
		
		//System.out.println(booleans.size());
		//System.out.println(ops.size());
		while(!ops.isEmpty()){
			oldOp = ops.pop().intValue();
			flag2 = booleans.pop().booleanValue();
			flag1 = booleans.pop().booleanValue();
			//flag1 oldOp flag2
			if(oldOp==And)
				flag1OpFlag2 = flag1 && flag2;
			else//(oldOp==Or)
				flag1OpFlag2 = flag1 || flag2;
			booleans.push(new Boolean( flag1OpFlag2 ));
		}
		//System.out.println(booleans.size());
		//System.out.println(ops.size());
		
		return booleans.pop().booleanValue();
	}
	
	/**
	 * ����
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 1;
		boolean[] bools = new boolean[n];
		int[] ops = new int[n-1];
		for (int j = 0; j < n; j++) {
			int i = 0;
			for (i = 0; i < bools.length; i++) {
				bools[i] = (int) (Math.random() * 2) == 1;
			}
			for (i = 0; i < ops.length; i++) {
				ops[i] = (int) (Math.random() * 2);
			}

			System.out.print("(");
			for (i = 0; i < n - 1; i++) {
				System.out.print(bools[i] + " "
						+ ((ops[i] == BooleanArrayTest.And) ? "And" : "Or")
						+ " ");
			}
			System.out.print(bools[i] + ")");

			System.out.println("�����:" + testExpressions(bools, ops));
		}
	}
	
}
