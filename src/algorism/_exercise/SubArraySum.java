package algorism._exercise;

/**
 * 找到子数组其和等于目标值
 * 是否都是正数？
 * Created by liumengjun on 2023-04-15.
 */
public class SubArraySum {
    public static void main(String[] args) {
        int[] nums = {1, 2, 2, 3, 4, 5, 6, 7, 8, 9};
        int sum = 8;
        for (int i = 0; i < nums.length; i++) {
            // calc sub array sum
            int temp = 0;
            for (int j = i; j < nums.length; j++) {
                temp += nums[j];
                if (temp == sum) {
                    // output
                    for (int k = i; k <= j; k++) {
                        System.out.print(nums[k] + " ");
                    }
                    System.out.println();
                    break; // or remove this line
                }
                // if all greater than 0，break ahead
                if (temp > sum) {
                    break;
                }
            }
        }
    }
}
