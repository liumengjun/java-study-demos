import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetTest {
	public static void main(String[] args) {
		test2();
	}

	static void test1() {
		SortedSet<Pojo> set1 = new TreeSet<SortedSetTest.Pojo>();
		set1.add(new Pojo(1, "a", 1));
		set1.add(new Pojo(2, "b", 4));
		set1.add(new Pojo(3, "c", 2));
		set1.add(new Pojo(4, "d", 3));
		set1.add(new Pojo(5, "e", 3));
		for (Pojo o : set1) {
			System.out.println(o);
		}
	}

	static void test2() {
		SortedSet<Pojo2> set1 = new TreeSet<SortedSetTest.Pojo2>(RankedComparator.SINGLETON);
		set1.add(new Pojo2(1, "a", 1));
		set1.add(new Pojo2(2, "b", 4));
		set1.add(new Pojo2(3, "c", 2));
		set1.add(new Pojo2(3, "c2", 2));
		set1.add(new Pojo2(4, "d", 3));
		set1.add(new Pojo2(5, "e", 3));
		for (Pojo2 o : set1) {
			System.out.println(o);
		}
	}

	private static class Pojo implements Comparable<Pojo> {
		int		id;
		String	name;
		int		rank;

		public Pojo(int id, String name, int rank) {
			this.id = id;
			this.name = name;
			this.rank = rank;
		}

		@Override
		public int compareTo(Pojo o) {
			return this.rank - o.rank;
		}

		public String toString() {
			return "{" + this.id + ":" + this.name + ":" + this.rank + "}";
		}
	}

	private static interface RankedPojo {
		public int getId();

		public int getRank();
	}

	private static enum RankedComparator implements Comparator<RankedPojo> {
		SINGLETON;

		@Override
		public int compare(RankedPojo o1, RankedPojo o2) {
			int r = o1.getRank() - o2.getRank();
			if (r == 0) {
				int i = o1.getId() - o2.getId();
				if (i == 0) {
					return 1;
				}
				return i;
			}
			return r;
		}

	}

	private static class Pojo2 implements RankedPojo {
		int		id;
		String	name;
		int		rank;

		public Pojo2(int id, String name, int rank) {
			this.id = id;
			this.name = name;
			this.rank = rank;
		}

		public String toString() {
			return "{" + this.id + ":" + this.name + ":" + this.rank + "}";
		}

		@Override
		public int getId() {
			return this.id;
		}

		@Override
		public int getRank() {
			return this.rank;
		}
	}
}
