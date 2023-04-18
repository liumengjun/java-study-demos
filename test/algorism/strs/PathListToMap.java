package algorism.strs;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathListToMap {
    public static void main(String[] args) {
        List<String> pathList = Arrays.asList(
                "/etc/hosts",
                "/etc/kubernetes/ssl/certs",
                "/root"
        );
        Map<String, Map> mapTree = pathListToMap(pathList);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(mapTree));
    }

    private static Map<String, Map> pathListToMap(List<String> pathList) {
        Map<String, Map> mapTree = new HashMap<>();
        for (String str : pathList) {
            Map<String, Map> tree = mapTree;
            String[] words = str.split("/");
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }
                if (tree.get(word) == null) {
                    Map<String, Map> subTree = new HashMap<>();
                    tree.put(word, subTree);
                }
                tree = tree.get(word);
            }
        }
        return mapTree;
    }
}
