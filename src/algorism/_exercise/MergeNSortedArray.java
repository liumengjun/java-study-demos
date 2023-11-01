package algorism._exercise;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class MergeNSortedArray {
    public static int[] mergeNSortedArray(int[][] arrays) {
        PriorityQueue<IdxInfo> stage = new PriorityQueue<>(arrays.length);
        int sumLen = 0;
        for (int i = 0; i < arrays.length; i++) {
            sumLen += arrays[i].length;
            stage.add(new IdxInfo(arrays[i][0], i, 0, arrays[i].length));
        }
        int[] merged = new int[sumLen];
        int s = 0;
        while (!stage.isEmpty()) {
            IdxInfo minInfo = stage.poll();
            merged[s++] = minInfo.val;
            if (minInfo.idx < minInfo.len - 1) {
                minInfo.idx++;
                minInfo.val = arrays[minInfo.seqInN][minInfo.idx];
                stage.add(minInfo);
            }
        }
        return merged;
    }

    static class IdxInfo implements Comparable<IdxInfo> {
        int val;
        int seqInN;
        int idx;
        int len;

        public IdxInfo(int val, int seqInN, int idx, int len) {
            this.val = val;
            this.seqInN = seqInN;
            this.idx = idx;
            this.len = len;
        }

        @Override
        public int compareTo(final IdxInfo other) {
            return this.val - other.val;
        }
    }

    public static void main(String[] args) {
        final int N = 5;
        int[][] arrays = new int[N][];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            int m = 10 + rand.nextInt(5);
            arrays[i] = new int[m];
            for (int j = 0; j < m; j++) {
                arrays[i][j] = -10 + rand.nextInt(100);
            }
            // System.out.println(Arrays.toString(arrays[i]));
            Arrays.sort(arrays[i]);
            System.out.println(Arrays.toString(arrays[i]));
        }

        int[] merged = mergeNSortedArray(arrays);
        System.out.println(Arrays.toString(merged));
    }
}
