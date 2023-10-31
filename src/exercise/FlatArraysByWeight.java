package exercise;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 按权重展平多维数组
 * Created by liumengjun on 2023-04-13.
 */
public class FlatArraysByWeight {
    public static void main(String[] args) {
        String[][] items = {
                {"财经1", "财经2", "财经3", "财经4", "财经5"},
                {"时政1", "时政2", "时政3", "时政4", "时政5"},
                {"体育I", "体育II", "体育III", "体育IV", "体育V", "体育VI", "体育VII"}
        };
        int[] weight = {2, 1, 3};
        System.out.println(Arrays.deepToString(items));
        System.out.println(Arrays.toString(weight));
        ArrayList<String> all = new ArrayList<>();
        int[] pos = new int[items.length];
        while (true) {
            boolean added = false;
            for (int i = 0; i < weight.length; i++) {
                int count = weight[i];
                String[] curItems = items[i];
                for (int k = pos[i], c = 0; c < count && k < curItems.length; k++, c++, pos[i]++) {
                    all.add(curItems[k]);
                    added = true;
                }
            }
            if (!added) {
                break;
            }
        }
        System.out.println(all);
    }
}
