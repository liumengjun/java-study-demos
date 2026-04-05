package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.IO.println;


/**
 * 类似 python collections.Counter
 * <p>
 * Created by liumengjun on 2026-04-03.
 */
public class Counter {
    Map<Object, Integer> countMap = new ConcurrentHashMap<>();

    public void add(Object obj) {
        countMap.merge(obj, 1, Integer::sum);
    }

    public void addAll(Collection<Object> objs) {
        for (Object obj : objs) {
            this.add(obj);
        }
    }

    public int size() {
        return countMap.size();
    }

    public int getCount(Object obj) {
        return countMap.getOrDefault(obj, 0);
    }

    public int total() {
        return countMap.values().stream().reduce(0, (a, b) -> a + b);
    }

    /**
     * 个数最多的n个元素
     *
     * @param n 1,2,,,size() 越界会自动修正
     * @return List&lt;Map.Entry&gt;
     */
    public List<Map.Entry<Object, Integer>> mostCommon(int n) {
        n = Math.max(1, n);
        n = Math.min(n, countMap.size());
        ArrayList<Map.Entry<Object, Integer>> entries = new ArrayList<>(countMap.entrySet());

        entries.sort((e1, e2) -> {
            return e2.getValue() - e1.getValue();
//            if (e1.getValue().equals(e2.getValue())) {
//                return e1.getKey().compareTo(e2.getKey());
//            } else {
//                return e2.getValue() - e1.getValue();
//            }
        });
        return entries.subList(0, n);
    }

    @Override
    public String toString() {
        return "Counter(%s)".formatted(countMap);
    }

    @Override
    public int hashCode() {
        return countMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Counter)) {
            return false;
        }
        return countMap.equals(((Counter) obj).countMap);
    }

    static void main() {
        var counter = new Counter();
        counter.add("Apple");
        counter.add("Banana");
        counter.add("cherry");
        counter.add("Apple");
        println(counter);
        println("Kinds: %d, Total: %d".formatted(counter.size(), counter.total()));
        println("Apple count: " + (counter.getCount("Apple")));
        println(counter.mostCommon(99));
        println(counter.mostCommon(0));
        println(counter.mostCommon(3));

        println("-".repeat(80));

        counter = new Counter();
        counter.addAll(List.of(1, 2, 3, 4, 5, 6, 2, 3, 5, 6, 4, 7, 0, 2, 6, 4, 6, 3, 4, 2, 6));
        println(counter);
        println("Kinds: %d, Total: %d".formatted(counter.size(), counter.total()));
        println("Apple count: " + (counter.getCount("Apple")));
        println(counter.mostCommon(99));
        println(counter.mostCommon(0));
        println(counter.mostCommon(3));
    }
}
