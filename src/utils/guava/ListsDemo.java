package utils.guava;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumengjun on 2017-08-29.
 */
public class ListsDemo {

    public static void main(String[] args) {
        batches();
    }

    static void batches() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add(i);
        }

        List<List<Integer>> batches = Lists.partition(data, 30);
        for (List<Integer> batch : batches) {
            System.out.println(batch);
        }
    }
}
