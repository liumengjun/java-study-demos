package algorism.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Created by liumengjun on 05/06/2017.
 */
public class KindsBag<K, E> {
	private static final ReentrantLock lock = new ReentrantLock();
	private Map<K, Set<E>> bag;
	private Supplier<Set<E>> setMaker;

	public KindsBag() {
		bag = new ConcurrentHashMap<K, Set<E>>();
		setMaker = (() -> new CopyOnWriteArraySet<E>());
	}

	public KindsBag(Map<K, Set<E>> bag) {
		this.bag = bag;
		setMaker = (() -> new CopyOnWriteArraySet<E>());
	}

	public KindsBag(Map<K, Set<E>> bag, Supplier<Set<E>> setMaker) {
		this.bag = bag;
		this.setMaker = setMaker;
	}

	public Map<K, Set<E>> getBag() {
		return this.bag;
	}

	public void add(K key, E ele) {
		Set<E> names = bag.get(key);
		if (names == null) {
			lock.lock();
			Set<E> tmp = bag.get(key);
			// 防止还没put进去呢，另一个线程就进入lock区了，后来的不用new
			if (tmp != null) {
				names = tmp;
			} else {
				names = setMaker.get();
				bag.put(key, names);
			}
			lock.unlock();
		}
		names.add(ele);
	}

	public void put(K key, E ele) {
		this.add(key, ele);
	}

	public void remove(K key, E ele) {
		Collection<E> names = bag.get(key);
		if (names == null) {
			return;
		}
		names.remove(ele);
	}

	public void remove(K key) {
		Collection<E> names = bag.get(key);
		if (names == null) {
			return;
		}
		names.clear();
	}

	public int size() {
//		return bag.reduceValues(1, v -> v.size(), Integer::sum);
		class IntWrapper {
			int i;
		}
		IntWrapper s = new IntWrapper();
		bag.forEach((k, v) -> s.i += v.size());
		return s.i;
	}

	public String toString() {
		return bag.toString();
	}
}
