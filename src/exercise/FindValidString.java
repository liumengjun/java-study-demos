package exercise;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 查找符合条件的字符串，条件较多一条一条堆
 */
public class FindValidString {
    private static Set<Character> cs = new HashSet<Character>();

    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        String src = sc.next();
//        String target = sc.next();
        String src = "owijacdmsoqieklfjsdf", target = "qoimlk";
        int targetCharCount = getUniqCharCount(target);
        System.out.println("limit: " + target + "," + targetCharCount);
        int maxSubCharCount = 0;
        String maxsub = null;
        String[] srcValids = src.replaceAll("[0-9]+", " ").replaceAll("[a-f]+", " ").split(" ");
        for (String tmp : srcValids) {
            if (tmp.isEmpty()) {
                continue;
            }
            int tmpCharCount = getUniqCharCount(tmp);
            System.out.println(tmp + "," + tmpCharCount);
            if (tmpCharCount > targetCharCount) {
                continue;
            }
            if (maxsub == null) {
                maxsub = tmp;
                maxSubCharCount = tmpCharCount;
                continue;
            }
            if (tmpCharCount < maxSubCharCount) {
                continue;
            }
            if (tmpCharCount > maxSubCharCount) {
                maxsub = tmp;
                maxSubCharCount = tmpCharCount;
                continue;
            }
            if (maxsub.compareTo(tmp) > 0) {
                maxsub = tmp;
            }
        }
        System.out.println(maxsub == null ? "Not Found" : maxsub);
    }

    private static int getUniqCharCount(String s) {
        cs.clear();
        for (int i = 0; i < s.length(); i++) {
            cs.add(s.charAt(i));
        }
        return cs.size();
    }
}
