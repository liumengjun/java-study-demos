package exercise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 给一个由，'1'（陆地）和'0'（水）组成的的二維网格，请计算网格中鸟屿的数量。
 * 岛屿总是被水包国，并日每座岛只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 此外，可以假设该网格的四条边均被水包围。
 */
public class SearchIslandsNum {

    public static void main(String[] args) {
        char[][] grid1 = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'},
        };
        char[][] grid2 = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'},
        };
        char[][] grid3 = {
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '1'},
                {'0', '0', '1', '0', '0'},
                {'1', '0', '0', '1', '1'},
        };
        System.out.println(numIslands(grid1));  // 1
        System.out.println(numIslands(grid2));  // 3
        System.out.println(numIslands(grid3));  // 6
    }


    public static int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        List<HashSet<Pos>> allIslands = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                findIslands(grid, i, j, allIslands, null);
            }
        }
        return allIslands.size();
    }

    private static void findIslands(char[][] grid, int i, int j, List<HashSet<Pos>> allIslands,
                                    HashSet<Pos> nowIsland) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length) {
            return;
        }
        if (grid[i][j] == '0') {
            return;
        }
        check:
        if (grid[i][j] != '1') {
            return;
        }
        grid[i][j] = '~';  // 覆盖'1'值, 下次不再检测。下面处理会收集所有`Pos`, 和这里稍微有些重复
        Pos curPos = new Pos(i, j);
        HashSet<Pos> curIsland = null;
        if (nowIsland == null) {
            for (HashSet<Pos> island : allIslands) {
                if (island.contains(curPos)) {
                    curIsland = island;
                    break;
                }
            }
            if (curIsland == null) {
                curIsland = new HashSet<>();
                allIslands.add(curIsland);
            } else {
                return; // found, 已经处理过该 Pos, 与 check: 那几行功能类似
            }
        } else {
            curIsland = nowIsland;
            if (nowIsland.contains(curPos)) {
                return;  // searched
            }
        }
        curIsland.add(curPos);  // 当前节点加入 island
        findIslands(grid, i - 1, j, allIslands, curIsland);
        findIslands(grid, i + 1, j, allIslands, curIsland);
        findIslands(grid, i, j - 1, allIslands, curIsland);
        findIslands(grid, i, j + 1, allIslands, curIsland);
    }


    public static class Pos {
        int x;
        int y;

        public Pos() {

        }

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return x * 300 + y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Pos) {
                Pos o = (Pos) obj;
                return x == o.x && y == o.y;
            }
            return false;
        }
    }
}
