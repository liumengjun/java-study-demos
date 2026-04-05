import util.Counter;
import util.UnionFind;

import static java.lang.IO.println;


/**
 * 生成一些名字 pair, 然后合并有相同名字的 pair 为一组。
 *
 * <p>
 * 调整 NAME_LEN 和 PAIRS_COUNT, 重新调用 genPairs(), 观察结果。
 * 小数据集验证, 大数据集探索调优。
 * <p>
 * <p>
 * 两种实现方法:
 * <p>
 * {@link #unitePairsToGroups(List)}
 * <p>
 * {@link #unitePairsToGroupsByUnionFind(List)} 性能更佳
 */
final int NAME_LEN = 3;
final int PAIRS_COUNT = 60000;
final String PAIRS_FILE_PATH = "temp/pairs.list.txt";
final Random random = new Random(System.currentTimeMillis());

int msgLevel = 0; // -1, 0, 1, 2, 3, 4; 数越大输出信息越多, 小于0只输出硬编码的输出信息。


void main() {
    genPairs(true);

    // read
    final List<Group> groups = readPairs();
    println("read pairs count: " + groups.size());
    printGroups(3, groups, "~", "Read pairs detail");

    // 方法1
    long startTime1 = System.currentTimeMillis();
    int cnt1 = unitePairsToGroupsByUnionFind(groups);
    long elapsedTime1 = System.currentTimeMillis() - startTime1;
    println("Processing groups elapsed time: %dms".formatted(elapsedTime1));
    printGroupsNumCounterStat(groups);
    println("Final merged group count: %d".formatted(cnt1));
    Set<Set<Integer>> numSetSet1 = buildGroupsNumSetSet(groups);

    println("*".repeat(80)); // 分割线, 下面换一种方法

    groups.forEach(Group::reset); // reset
    printGroups(4, groups, "~", "After reset");

    // 方法2
    long startTime2 = System.currentTimeMillis();
    int cnt2 = unitePairsToGroups(groups);
    long elapsedTime2 = System.currentTimeMillis() - startTime2;
    println("Processing groups elapsed time: %dms".formatted(elapsedTime2));
    printGroupsNumCounterStat(groups);
    println("Final merged group count: %d".formatted(cnt2));
    println("两个方法最终集合数量结果%s".formatted(cnt1 == cnt2 ? "相同" : "不同"));
    Set<Set<Integer>> numSetSet2 = buildGroupsNumSetSet(groups);
    boolean sameNumSets = Objects.equals(numSetSet1, numSetSet2);
    println("两个方法NumSets结果%s".formatted(sameNumSets ? "相同" : "不同"));
}


/**
 * 多个 pair 数据, 凡是有交集的 pair 就合并。
 *
 * <p>
 * 如: (A,B) (B,C) (D,E)
 * <p>
 * => (A,B,C) (D,E)
 * <p>
 * 通过遍历标记分组实现; 另一种通过{@link UnionFind}实现的版本见{@link #unitePairsToGroupsByUnionFind}。
 */
int unitePairsToGroups(List<Group> groups) {
    // 联结有交集的 pair, 转化为同一组。
    // 首先, 标记有交集的 pair 为相同的编号
    // 俩俩判断, n 个元素 2 的组合, 共判断 n*(n-1)/2 次
    for (int i = 1; i < groups.size(); i++) {
        Group iGroup = groups.get(i);
        boolean foundJoint = false;
        for (int f = 0; f < i; f++) {
            // f 在 i 的前面, 判断 i 和 f 是否有交集, 没则继续下一个, 有则标记归属
            Group fGroup = groups.get(f);
            if (!iGroup.hasJointWith(fGroup)) {
                continue;
            }
            foundJoint = true;
            if (msgLevel >= 3) {
                println("Found joint groups: %s and %s".formatted(iGroup, fGroup));
            }
            // 标记归属, 向前看齐, 设置 Group.num 为前面的 Group 的更小的 Group.num
            if (iGroup.num > fGroup.num) {
//                iGroup.num = fGroup.num;
                for (int a = 0; a <= i; a++) {
                    Group aGroup = groups.get(a);
                    if (aGroup.num == iGroup.num) {
                        aGroup.num = fGroup.num;
                    }
                }
            } else if (iGroup.num == fGroup.num) {
                // 不需要处理
            } else {
                // iGroup.num 更小, 已经判断得知 iGroup 和更靠前的 Group 有交集,
                // 回溯标记所有 fGroup.num 的 Group, 更改其 num 为 iGroup.num。
                int fNum = fGroup.num;
                for (int a = 0; a <= i; a++) {
                    Group aGroup = groups.get(a);
                    if (aGroup.num == fNum) {
                        aGroup.num = iGroup.num;
                    }
                }
            }
        }
        if (foundJoint) {
            printGroups(4, groups, "-", "After processing [" + i + "] Group");
        }
    }
    if (msgLevel < 4) { // 内部 print 了这里就不显示了
        printGroups(3, groups, "=", "processed each all");
    }

    // collect result, 上面只是标记，下面这里收集到 Group 集合中
    int groupCount = collectResultGroups(groups);
    printGroups(2, groups, "=", "Merged marked groups");
    return groupCount;
}

/**
 * 整理收集已标记联结的 Group 合并为大的 Group
 *
 * @deprecated use {@link #collectResultGroupsV2}
 */
int collectResultGroups(List<Group> groups) {
    int groupCount = 0;
    for (int i = 0; i < groups.size(); i++) {
        Group iGroup = groups.get(i);
        if (iGroup.merged) {
            continue;
        }
        iGroup.init();
        // u 在 i 的后面, 向前看齐合并
        for (int u = i + 1; u < groups.size(); u++) {
            Group uGroup = groups.get(u);
            if (uGroup.num == iGroup.num) {
                iGroup.add(uGroup);
                uGroup.merged = true;
//                uGroup.member.clear();
            }
        }
        groupCount++;
    }
    return groupCount;
}

/**
 * 合并有交集的 pair, 转化为同一组。
 * <p>
 * 用{@link UnionFind}实现; 普通遍历版本见{@link #unitePairsToGroups}。
 */
int unitePairsToGroupsByUnionFind(List<Group> groups) {
    // 用 UnionFind 联结有交集的 pair
    UnionFind uf = new UnionFind(groups.size());
    // 俩俩判断, n 个元素 2 的组合, 共判断 n*(n-1)/2 次
    for (int i = 1; i < groups.size(); i++) {
        Group iGroup = groups.get(i);
        boolean foundJoint = false;
        for (int f = 0; f < i; f++) {
            // f 在 i 的前面, 判断 i 和 f 是否有交集, 没则继续下一个, 有则union
            Group fGroup = groups.get(f);
            if (iGroup.hasJointWith(fGroup)) {
                foundJoint = true;
                if (msgLevel >= 3) {
                    println("Found joint groups: %s and %s".formatted(
                            formatGroupWithUf(iGroup, uf), formatGroupWithUf(fGroup, uf)));
                }
                // 使用 UnionFind 合并
                uf.union(f, i); // trick: 小的参数在前
            }
        }
        if (foundJoint) {
            printGroupsWithUf(4, groups, uf, "-", "After UnionFind [" + i + "] Group");
        }
    }
    // groups index -> group.num 相当于 uf.id[i] = j 的映射关系。
    for (int i = 0; i < groups.size(); i++) {
        groups.get(i).num = uf.find(i);
    }
    if (msgLevel < 4) { // 内部 print 了这里就不显示了
        printGroups(3, groups, "=", "After UnionFind each all");
    }

    // collect result, 上面只是标记，下面这里收集到 Group 集合中。（这里换一收集方式）
    collectResultGroupsV2(groups);
    printGroups(2, groups, "=", "Merged marked groups");
    return uf.getCount();
}

/**
 * 整理收集已标记联结的 Group 合并为大的 Group
 *
 * @see #collectResultGroups
 */
int collectResultGroupsV2(List<Group> groups) {
    int groupCount = 0;
    for (int i = 0; i < groups.size(); i++) {
        Group iGroup = groups.get(i);
        if (iGroup.num == i) {
            iGroup.init();
            groupCount++;
        } else {
            Group nGroup = groups.get(iGroup.num);
            nGroup.init();
            nGroup.add(iGroup);
        }
    }
    return groupCount;
}


Set<Set<Integer>> buildGroupsNumSetSet(List<Group> groups) {
    Map<Integer, Set<Integer>> numToSet = new HashMap<>();
    // num 相同的 index 属于同一集合
    for (int i = 0; i < groups.size(); i++) {
        int num = groups.get(i).num;
        Set<Integer> set = numToSet.get(num);
        if (set == null) {
            set = new HashSet<>();
            numToSet.put(num, set);
        }
        set.add(i);
    }
    Set<Set<Integer>> numSetSet = new HashSet<>(numToSet.values());
    if (msgLevel >= 1) {
        println("{Num -> Set}: " + numToSet);
        println("{NumSets}: " + numSetSet);
        println("-".repeat(80));
    }
    return numSetSet;
}

void printGroupsNumCounterStat(List<Group> groups) {
    if (msgLevel < 0) {
        return;
    }
    Counter counter = new Counter();
    groups.forEach(g -> counter.add(g.num));

    int total = counter.total();
    println("Sets count: %d, pairs total: %d, avg: %.2f".formatted(counter.size(), total, 1.0 * total / counter.size()));
    if (msgLevel >= 1) {
        println("Each Set count stat: " + counter);
    }
    println("Most largest sets: " + counter.mostCommon(10));
    println("-".repeat(80));
}

private void printGroups(int grade, List groups, String ch, String note) {
    if (msgLevel < grade) {
        return;
    }
    printBlockHead(note, ch);
    groups.forEach(IO::println);
    printBlockEnd(note, ch);
}

private void printBlockHead(String note, String ch) {
    if (note != null) {
        String head = "v " + note + " v";
        String padding = ch.repeat(40 - head.length() / 2);
        println(padding + head + padding);
    }
}

private void printBlockEnd(String note, String ch) {
    if (note != null) {
        String head = "^ " + note + " ^";
        String padding = ch.repeat(40 - head.length() / 2);
        println(padding + head + padding);
    } else {
        println(ch.repeat(80));
    }
}

private void printGroupsWithUf(int grade, List<Group> groups, UnionFind uf, String ch, String note) {
    if (msgLevel < grade) {
        return;
    }
    printBlockHead(note, ch);
    for (int i = 0; i < groups.size(); i++) {
        Group g = groups.get(i);
        println(formatGroupWithUf(g, uf));
    }
    printBlockEnd(note, ch);
}

private String formatGroupWithUf(Group g, UnionFind uf) {
    int i = g.origNum;
    return "[%d][%d](%s, %s)".formatted(i, uf.get(i), g.pairA, g.pairB);
}


/**
 * 从文件读取名字 pair 列表, 结构初始化为 Group 列表
 *
 * @param pairsFilePaths null/0 then default {@link #PAIRS_FILE_PATH}
 */
List<Group> readPairs(String... pairsFilePaths) {
    if (pairsFilePaths == null || pairsFilePaths.length == 0) {
        return readPairsOne(PAIRS_FILE_PATH);
    }
    List<Group> pairs = new ArrayList<>();
    for (String pairsFilePath : pairsFilePaths) {
        pairs.addAll(readPairsOne(pairsFilePath));
    }
    return pairs;
}

private List<Group> readPairsOne(String pairsFilePath) {
    List<Group> pairs = new ArrayList<>();
    try (Scanner scanner = new Scanner(new File(pairsFilePath))) {
        int num = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pair = line.split(",");
            if (msgLevel >= 1 && pair[0].equals(pair[1])) {
                println("[%d] two same names in pair(%s)".formatted(num, line));
            }
            Group group = new Group(num, pair[0], pair[1]);
            pairs.add(group);
            num++;
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return pairs;
}


/**
 * 随机生成一些名字 pair, 保存到文件 {@link #PAIRS_FILE_PATH}。
 * <p>
 * 不会覆盖旧文件, 已存在则什么也不做, 删除旧文件才会重新生成。
 */
void genPairs(boolean allowSame) {
    Path pairsPath = Paths.get(PAIRS_FILE_PATH);
    File pairsFile = pairsPath.toFile();
    if (pairsFile.exists()) {
        return;
    }
    initCharset();
    var counter = new Counter();
    try (PrintWriter writer = new PrintWriter(Files.newOutputStream(pairsPath))) {
        for (int i = 0; i < PAIRS_COUNT; i++) {
            String a = genRandomName(), b = genRandomName();
            while (b.equals(a) && !allowSame) {
                b = genRandomName();
            }
            writer.print(a);
            writer.print(',');
            writer.println(b);
            counter.add(a);
            counter.add(b);
        }
        println("Generate pairs successfully @: " + PAIRS_FILE_PATH);
    } catch (IOException e) {
        e.printStackTrace();
    }
    println("with name length: %d, pairsCount: %d。".formatted(NAME_LEN, PAIRS_COUNT));
    println("final: name total: %d, unique count: %d。".formatted(counter.total(), counter.size()));
    println("most common:" + counter.mostCommon(10));
    println("*".repeat(80));
}

private final char[] nameChars = new char[62];

private void initCharset() {
    for (int i = 0; i < 26; i++) {
        nameChars[i] = (char) ('A' + i);
    }
    for (int i = 0; i < 26; i++) {
        nameChars[i + 26] = (char) ('a' + i);
    }
    for (int i = 0; i < 10; i++) {
        nameChars[i + 52] = (char) ('0' + i);
    }
    println("pair names charset: " + new String(nameChars));
}

private String genRandomName() {
    char[] chars = new char[NAME_LEN];
    chars[0] = (char) ('A' + random.nextInt(26));
    for (int i = 1; i < chars.length; i++) {
        chars[i] = nameChars[random.nextInt(nameChars.length)];
    }
    return new String(chars);
}


class Group {
    int num; // 编号
    final int origNum; // 初始编号
    final String pairA, pairB; // 原始 pair 数据
    Set<String> member;
    boolean collected;
    boolean merged;

    Group(int num, String pairA, String pairB) {
        this.num = this.origNum = num;
        this.pairA = pairA;
        this.pairB = pairB;
    }

    void reset() {
        this.num = this.origNum;
        member = null;
        collected = merged = false;
    }

    boolean init() {
        if (collected) {
            return false;
        }
        if (member == null)
            member = new HashSet<>();
        member.add(pairA);
        member.add(pairB);
        return collected = true;
    }

    void add(Group that) {
        this.member.add(that.pairA);
        this.member.add(that.pairB);
    }

    boolean hasJointWith(Group that) {
        return this.pairA.equals(that.pairA) || this.pairA.equals(that.pairB)
                || this.pairB.equals(that.pairA) || this.pairB.equals(that.pairB);
    }

    public String toString() {
        return "[%d][%d](%s, %s)%s".formatted(origNum, num, pairA, pairB, collected ? member : "");
    }
}
