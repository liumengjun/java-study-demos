package algorism._exercise;

import java.util.Arrays;

/**
 * 建一个学校，使到各个家庭距离之和最短
 */
public class SchoolLoc {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int N = sc.nextInt();
//        int[] locs = new int[N];
//        for (int i = 0; i < N; i++) {
//            locs[i] = sc.nextInt();
//        }
//        int N = 4;
        int[] locs = new int[]{0, 20, 40, 10};
//        int[] locs = new int[]{0, 20, 40, 10, 30};
        int N = locs.length;
        findLoc(N, locs);

        // 2nd impl
        Arrays.sort(locs);
        System.out.println(locs[(N + 1) / 2 - 1]);
    }

    private static void findLoc(int N, int[] locs) {
        int schollLoc = -1;
        int minDistanceSum = -1;
        for (int idx = 0; idx < N; idx++) {
            int tmpLoc = locs[idx];
            int tmpDisSum = 0;
            for (int j = 0; j < N; j++) {
                int tmpDis = locs[j] - tmpLoc;
                tmpDisSum += Math.abs(tmpDis);
            }
            if (minDistanceSum < 0 || tmpDisSum < minDistanceSum) {
                minDistanceSum = tmpDisSum;
                schollLoc = tmpLoc;
                continue;
            }
            if (tmpDisSum == minDistanceSum) {
                if (schollLoc > tmpLoc) {
                    schollLoc = tmpLoc;
                }
                continue;
            }
        }
        System.out.println(schollLoc);
    }
}
