package algorism.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by liumengjun on 05/06/2017.
 */
public class KindsBagTest {

	@Test
	public void testConstructor() {
		KindsBag<String, String> bag = new KindsBag<>();
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Red Fuji");
		assertEquals(bag.size(), 2);
	}

	@Test
	public void testConstructorBag() {
		KindsBag<String, String> bag = new KindsBag<>(new HashMap<>());
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Red Fuji");
		assertEquals(bag.size(), 2);

		KindsBag<String, String> bag2 = new KindsBag<>(new TreeMap<>());
		bag2.add("Pear", "Crown");
		bag2.add("Pear", "YaLi");
		assertEquals(bag2.size(), 2);
	}

	@Test
	public void testConstructorBagAndCollectionMaker() {
		KindsBag<String, String> bag = new KindsBag<>(new HashMap<>(), () -> new HashSet<>());
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Marshal");
		assertEquals(bag.size(), 1);

		KindsBag<String, String> bag2 = new KindsBag<>(new TreeMap<>(), () -> new TreeSet<>());
		bag2.add("Pear", "Crown");
		bag2.add("Pear", "Crown");
		bag2.add("Pear", "YaLi");
		assertEquals(bag2.size(), 2);
	}

	@Test
	public void testAdd() {
		KindsBag<String, String> bag = new KindsBag<>();
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Red Fuji");
		bag.add("Pear", "Crown");
		bag.add("Pear", "YaLi");
		System.out.println(bag.toString());
		assertEquals(bag.size(), 4);
	}

	@Test
	public void testRemove() {
		KindsBag<String, String> bag = new KindsBag<>();
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Red Fuji");
		bag.add("Pear", "Crown");
		bag.add("Pear", "YaLi");
		bag.remove("Apple", "Red Fuji");
		bag.remove("Pear", "Crown");
		System.out.println(bag.toString());
		assertEquals(bag.size(), 2);
	}

	@Test
	public void testRemoveKey() {
		KindsBag<String, String> bag = new KindsBag<>();
		bag.add("Apple", "Marshal");
		bag.add("Apple", "Red Fuji");
		bag.add("Pear", "Crown");
		bag.add("Pear", "YaLi");
		bag.remove("Apple");
		System.out.println(bag.toString());
		assertEquals(bag.size(), 2);
	}
}
