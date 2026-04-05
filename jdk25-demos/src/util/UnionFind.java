package util;

import java.util.*;

import static java.lang.IO.println;


/**
 * 并查集（union-find disjoint sets）。
 *
 * <p>
 * 最初都是单独的元素, 被 union 的元素属于同一棵树(集合/连通分量), 所有的组成一片森林。
 *
 * <p>
 * Created by liumengjun on 2026-04-03.
 */
public class UnionFind {

    /**
     * 集合数
     */
    int count;

    /**
     * 每个数所属的集合(树), id[i] = j, 在森林树中表示 i 的父节点为 j, 最好情况为树的根结点
     */
    int[] id;

    /**
     * 子树大小
     */
    int[] sz;

    public UnionFind(int N) {
        count = N;
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    public int getCount() {
        return count;
    }

    public int get(int i) {
        return id[i];
    }

    /**
     * 查找元素所在的集合，即根节点。
     */
    public int find(int p) {
        while (p != id[p]) {
            id[p] = id[id[p]]; // 压缩路径
            p = id[p]; // 若找不到，则一直往根root回溯
        }
        return p;
    }

    /**
     * 将两个元素所在的集合合并(连接)为一个集合。
     */
    public void union(int p, int q) {
        int pID = find(p);
        int qID = find(q);
        if (pID == qID)
            return;

        if (sz[pID] < sz[qID]) {    // 通过结点数量，判断树的大小并将小树并到大树下
            id[pID] = qID;
            sz[qID] += sz[pID];
        } else {
            id[qID] = pID;
            sz[pID] += sz[qID];
        }
        count--;
    }

    /**
     * 判断p,q是否连接，即是否属于同一集合
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * 不返回 {@link #id} 引用, 以 List&lt;Map.Entry&gt; 形式返回
     */
    public List<Map.Entry<Integer, Integer>> getIdsAsEntryList() {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            entries.add(new AbstractMap.SimpleEntry<>(i, id[i]));
        }
        return entries;
    }

    /**
     * 收集结果为一系列 {num -> Set} 映射关系的 Map
     */
    public Map<Integer, Set<Integer>> collectNumSetResultMap() {
        Map<Integer, Set<Integer>> numToSet = new HashMap<>();
        for (int i = 0; i < id.length; i++) {
            int num = find(i);
            Set<Integer> set = numToSet.get(num);
            if (set == null) {
                set = new HashSet<>();
                numToSet.put(num, set);
            }
            set.add(i);
        }
        return numToSet;
    }

    static void main() {
        Scanner input = new Scanner("""
                8
                2 3
                3 2
                1 0
                0 4
                5 7
                6 2
                """);
        int N = input.nextInt(); // 数据里的最大整数
        UnionFind uf = new UnionFind(N);
        while (input.hasNext()) {
            int p = input.nextInt();
            int q = input.nextInt();
            if (uf.connected(p, q))
                continue; // 若p,q已属于同一集合不再连接，则故直接跳过
            uf.union(p, q);
            println(p + "-" + q);
        }
        println("总集合数：" + uf.count);
        uf.getIdsAsEntryList().forEach(IO::println);

        // 收集集合
        println(uf.collectNumSetResultMap());
    }
}
