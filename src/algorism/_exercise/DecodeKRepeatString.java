package algorism._exercise;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Given an encoded string, return its decoded string.
 * <p>
 * The encoding rule is: `k[encoded_string]`, where the `encoded_string`
 * inside the square brackets is being repeated exactly `k` times.
 * Note that `k` is guaranteed to be a positive integer.
 * <p>
 * You may assume that the input string is always valid; there are no extra white spaces,
 * square brackets are well-formed, etc. Furthermore, you may assume that the original data
 * does not contain any digits and that digits are only for those repeat numbers, `k`.
 * For example, there will not be input like `3a` or `2[4]`.
 * The test cases are generated so that the length of the output will never exceed `105`.
 * <p>
 * <p>
 * **Example 1:**
 * <p>
 * **Input:** s = "3[a]2[bc]"**Output:** "aaabcbc"
 * <p>
 * **Example 2:**
 * <p>
 * **Input:** s = "3[a2[c]]"**Output:** "accaccacc"
 * <p>
 * **Example 3:**
 * <p>
 * **Input:** s = "2[abc]3[cd]ef"**Output:** "abcabccdcdcdef"
 * <p>
 * <p>
 * <p>
 * **Constraints:**
 * <p>
 * - `1 <= s.length <= 30`
 * - `s` consists of lowercase English letters, digits, and square brackets `'[]'`.
 * - `s` is guaranteed to be **a valid** input.
 * - All the integers in `s` are in the range `[1, 300]`.
 */
public class DecodeKRepeatString {
    public static void main(String[] args) {
        String s = "3[a]2[bc]";
        System.out.println(decode(s));  // aaabcbc
        s = "3[a2[c]]";
        System.out.println(decode(s));  // accaccacc
        s = "2[abc]3[cd]ef";
        System.out.println(decode(s));  // abcabccdcdcdef
        System.out.println(decode("abcd"));
        System.out.println(decode("a2[bc]d"));
        System.out.println(decode("a3[b2[cd]]"));
        System.out.println(decode("a3[2[bc]d]")); // bad, fail
    }

    public static String decode(String s) {
        StringBuilder resultBuf = new StringBuilder();
        Deque stack = new ArrayDeque();
        int pos = 0;
        while (pos < s.length()) {
            char c = s.charAt(pos);
            pos++;
            if (c != ']') {
                stack.push(c);
                continue;
            }
            if (stack.isEmpty()) {
                // 嵌套时连续多个']'时stack为空跳过不做解析
                continue;
            }
            // 遇到 ']' 解析stack中的字符
            String seg = decodeSeg(stack, null);
            resultBuf.append(seg);
        }
        if (!stack.isEmpty()) {
            // 结尾无k编码的字符串
            resultBuf.append(appendTail(stack));
        }
        return resultBuf.toString();
    }

    public static String decodeSeg(Deque stack, String suffix) {
        // 解析[]中的 encoded_string 记为 item
        StringBuilder itemBuf = new StringBuilder();
        while (!stack.isEmpty()) {
            char c = (char) stack.pop();
            if (c != '[') {
                itemBuf.append(c);
            } else {
                break;
            }
        }
        String item = itemBuf.reverse().toString();
        if (suffix != null) {
            // 嵌套时, 拼接上嵌套部分
            item += suffix;
        }
        if (stack.isEmpty()) {
            return item;
        }
        // 解析 k [1, 300]
        StringBuilder kBuf = new StringBuilder();
        while (!stack.isEmpty()) {
            char c = (char) stack.peek();
            if (Character.isDigit(c)) {
                kBuf.append(stack.pop());
            } else {
                break;
            }
        }
        int k = Integer.parseInt(kBuf.reverse().toString());
        // 重复, java8 String没有repeat方法, java21可用string.repeat()
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < k; i++) {
            buf.append(item);
        }
        String seg = buf.toString();
        if (!stack.isEmpty()) {
            // 含有嵌套
            return decodeSeg(stack, seg);
        }
        return seg;
    }

    private static String appendTail(Deque stack) {
        StringBuilder buf = new StringBuilder();
        while (!stack.isEmpty()) {
            buf.append(stack.pop());
        }
        return buf.reverse().toString();
    }

}
