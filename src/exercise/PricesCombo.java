package exercise;

import java.util.*;

/**
 * 总额 <-> 价格组合，类似找零钱
 */
public class PricesCombo {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int amount = sc.nextInt();
//        Set<Integer> priceset = new HashSet<Integer>();
//        while (sc.hasNext()) {
//            String priceStr = sc.next();
//            String tmpStr = priceStr.replaceAll("[\\[\\], ]", "");
////             System.out.println(tmpStr);
//            int price = Integer.valueOf(tmpStr);
//            priceset.add(price);
//            if (priceStr.lastIndexOf(']') > 0) {
//                break;
//            }
//        }
//        List<Integer> prices = new ArrayList(priceset);
        int amount = 1000;
        List<Integer> prices = Arrays.asList(500, 200, 300);
//         System.out.println(amount);
//         System.out.println(prices);
        List<List<Integer>> combos = new ArrayList<List<Integer>>();
        List<Integer> combo = new ArrayList<Integer>();
        findCombo(combos, combo, amount, prices);
        System.out.println(combos);
    }

    public static void findCombo(List<List<Integer>> combos,
                                 List<Integer> combo,
                                 int amount,
                                 List<Integer> prices) {
        for (int i = 0; i < prices.size(); i++) {
            int tmpPrice = prices.get(i);
            if (amount < tmpPrice) {
                continue;
            }
            int n = amount / tmpPrice;
            if (amount % tmpPrice == 0) {
                List<Integer> tmpCombo = new ArrayList<Integer>(combo);
                for (int k = 0; k < n; k++) {
                    tmpCombo.add(tmpPrice);
                }
                // 成功一组
                combos.add(tmpCombo);
//                 continue;
            }
            for (int j = n; j >= 1; j--) {
                if (amount % tmpPrice == 0 && j == 0) {
                    continue;
                }
                List<Integer> tmpCombo = new ArrayList<Integer>(combo);
                for (int k = 0; k < j; k++) {
                    tmpCombo.add(tmpPrice);
                }
                int remainAmount = amount - tmpPrice * j;
                if (remainAmount <= 0) {
                    continue;
                }
//                 System.out.println(remainAmount);
                List<Integer> newPrices = new ArrayList<Integer>(prices.subList(i + 1, prices.size()));
//                 newPrices.remove(new Integer(tmpPrice));
                findCombo(combos, tmpCombo, remainAmount, newPrices);
            }
        }
    }
}
