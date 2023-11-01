package algorism._exercise;

/**
 * 区间(或全部)反转单向链表
 */
public class ReverseLinkedList {
    public static void main(String[] args) {
        List list = new List(new int[]{0, 1, 2, 3, 4, 5, 6, 7});
        System.out.println(list.head);
        System.out.println(list.asString());
//        list.inverse(2, 5);
//        System.out.println(list.asString());
//        list.inverse(0, 5);
//        System.out.println(list.asString());
//        list.inverse(2, 7);
//        System.out.println(list.asString());
        list.inverse(0, 7);
        System.out.println(list.asString());
    }

    static class List {
        public Node head = null;

        public List(int[] array) {
            Node d = head;
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    d = head = new Node();
                    d.data = array[i];
                } else {
                    Node next = new Node();
                    next.data = array[i];
                    d.next = next;
                    d = next;
                }
            }
        }

        public void inverse(int from, int to) {
            //建立虚拟头节点，指向head，不用分开处理m==1和m!=1时的情况
            Node dummy = new Node(-1, head);
            Node d = dummy;
            for (int i = 0; i < from; i++) {
                if (d != null) {
                    d = d.next;
                } else {
                    return;
                }
            }
            Node firstSegEnd = d;
            Node segOrigFirst = firstSegEnd.next;
            Node pre = segOrigFirst;
            if (pre == null) {
                return;
            }
            Node cur = pre.next;
            for (int i = from; i < to; i++) {
                Node next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
                if (next == null) {
                    break;
                }
            }
            firstSegEnd.next = pre;
            segOrigFirst.next = cur;
            this.head = dummy.next;
        }

        public String asString() { // 如果命名为: toString()，写成死循环时，调试时不好观察(watch)变量
            StringBuilder buf = new StringBuilder();
            Node d = head;
            while (d != null) {
                buf.append(d.data);
                if (d.next != null) {
                    buf.append(" -> ");
                }
                d = d.next;
            }
            return buf.toString();
        }
    }

    static class Node {
        public int data = 0;
        public Node next = null;

        public Node() {

        }

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
//                    ", next=" + next + // 带着 next 会形成递归toString, 不好观察(watch)变量
                    '}';
        }
    }
}
