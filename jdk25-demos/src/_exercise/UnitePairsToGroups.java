// Compact Code Class 没有包, 但是其他类也引用不到？(用反射)🤔😱。若要其他类正常引用需要显示声明 class 和 package
//package _exercise;

import util.Counter;
import util.UnionFind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;

import static java.lang.IO.println;

//class UnitePairsToGroups {

/// 构造 name 的字符集大小, 可调范围\[2, 62], 其他会自动修正, 即\[大写字母+小写字母+数字]
final int NAME_CHARSET_LENGTH = 40;
/// 单个 name 长度, 即字符个数
final int NAME_LEN = 3;
final int PAIRS_COUNT = 40000;
final String PAIRS_FILE_PATH = "temp/pairs.list.txt";
private final Random random = new Random(System.currentTimeMillis());
private char[] nameChars;

/// -1, 0, 1, 2, 3, 4, 5; 数越大输出信息越多, 小于0只输出硬编码的输出信息。
int msgLevel = 1;


/**
 * 生成一些名字 pair, 然后合并有相同名字的 pair 为一组。
 *
 * <p>
 * 调整 NAME_CHARSET_LENGTH, NAME_LEN 和 PAIRS_COUNT, 重新调用 genPairs(), 观察结果。
 * 小数据集验证, 大数据集探索调优。
 * <p>
 * <p>
 * 三种实现方法:
 * <p>
 * {@link #unitePairsToGroups(List)}
 * <p>
 * {@link #unitePairsToGroupsByUnionFind(List)}
 * <p>
 * {@link #unitePairsToGroupsMultiThread}
 * <p>
 * 性能结果见 {@link _exercise.UnitePairsToGroupsBenchmark}
 */
void main() {
    genPairs(true, false, NAME_CHARSET_LENGTH, NAME_LEN, PAIRS_COUNT, null);

    // read
//    final List<Group> groups = readPairs("resources/pairs.list1.txt", "resources/pairs.list2.txt");
//    final List<Group> groups = readPairs("resources/pairs.list3.txt");
    final List<Group> groups = readPairs();
    println("read pairs count: " + groups.size());
    printGroups(3, groups, "~", "Read pairs detail");

    println("*".repeat(40) + "方法1 - UnionFind" + "*".repeat(40)); // 分割线

    // 方法1
    long startTime1 = System.currentTimeMillis();
    int cnt1 = unitePairsToGroupsByUnionFind(groups);
    long elapsedTime1 = System.currentTimeMillis() - startTime1;
    println("Processing groups elapsed time: %dms".formatted(elapsedTime1));
    printGroupsNumCounterStat(groups);
    println("Final remain group count: %d".formatted(cnt1));
    Set<Set<Integer>> numSetSet1 = buildGroupsNumSetSet(groups);
    Set<Set<String>> nameSetSet1 = buildGroupsNameSetSet(groups);

    println("*".repeat(40) + "方法2 - Common" + "*".repeat(40)); // 分割线, 下面换一种方法
    groups.forEach(Group::reset); // reset
    printGroups(5, groups, "~", "After reset");

    // 方法2
    long startTime2 = System.currentTimeMillis();
    int cnt2 = unitePairsToGroups(groups);
    long elapsedTime2 = System.currentTimeMillis() - startTime2;
    println("Processing groups elapsed time: %dms".formatted(elapsedTime2));
    printGroupsNumCounterStat(groups);
    println("Final remain group count: %d".formatted(cnt2));
    println("两个方法最终集合数量结果%s".formatted(cnt1 == cnt2 ? "相同" : "不同"));
    Set<Set<Integer>> numSetSet2 = buildGroupsNumSetSet(groups);
    boolean sameNumSets = Objects.equals(numSetSet1, numSetSet2);
    println("两个方法NumSets结果%s".formatted(sameNumSets ? "相同" : "不同"));
    Set<Set<String>> nameSetSet2 = buildGroupsNameSetSet(groups);
    boolean sameNameSets = Objects.equals(nameSetSet1, nameSetSet2);
    println("两个方法NameSets结果%s".formatted(sameNameSets ? "相同" : "不同"));

    println("*".repeat(40) + "方法3 - Multi-Thread" + "*".repeat(40)); // 分割线, 下面换一种方法
    groups.forEach(Group::reset); // reset

    // 方法3
    long startTime3 = System.currentTimeMillis();
    int cnt3 = unitePairsToGroupsMultiThread(groups, true);
    long elapsedTime3 = System.currentTimeMillis() - startTime3;
    println("Processing groups elapsed time: %dms".formatted(elapsedTime3));
    printGroupsNumCounterStat(groups);
    println("Final remain group count: %d".formatted(cnt3));
    println("方法3:最终集合数量结果%s".formatted(cnt1 == cnt3 ? "相同" : "不同"));
    Set<Set<Integer>> numSetSet3 = buildGroupsNumSetSet(groups);
    boolean sameNumSets3 = Objects.equals(numSetSet1, numSetSet3);
    println("方法3:NumSets结果%s(子线程内会合并掉一些Num)".formatted(sameNumSets3 ? "相同" : "不同"));
    Set<Set<String>> nameSetSet3 = buildGroupsNameSetSet(groups);
    boolean sameNameSets3 = Objects.equals(nameSetSet1, nameSetSet3);
    println("方法3:NameSets结果%s".formatted(sameNameSets3 ? "相同" : "不同"));
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
 *
 * @see #unitePairsToGroupsByUnionFind
 * @see #unitePairsToGroupsMultiThread
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
            if (iGroup.num == fGroup.num || !iGroup.hasJointWith(fGroup)) {
                continue;
            }
            foundJoint = true;
            if (msgLevel >= 3) {
                println("Found joint groups: %s and %s".formatted(iGroup, fGroup));
            }
            // 标记归属, 向前看齐, 设置 Group.num 为前面的 Group 的更小的 Group.num
            // 若 iGroup.num 更小, 则是已经判断得知 iGroup 和更靠前的 Group 有交集。
            if (iGroup.num != fGroup.num) {
                int srcNum = Math.max(iGroup.num, fGroup.num);
                int destNum = Math.min(iGroup.num, fGroup.num);
                for (int a = 0; a <= i; a++) {
                    Group aGroup = groups.get(a);
                    if (aGroup.num == srcNum) {
                        aGroup.num = destNum;
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
        iGroup.doCollect();
        // u 在 i 的后面, 向前看齐合并
        for (int u = i + 1; u < groups.size(); u++) {
            Group uGroup = groups.get(u);
            if (uGroup.num == iGroup.num) {
                iGroup.add(uGroup);
                uGroup.markMerged();
            }
        }
        groupCount++;
    }
    return groupCount;
}

/**
 * 合并有交集的 pair, 转化为同一组。
 *
 * <p>
 * 用{@link UnionFind}实现; 普通遍历版本见{@link #unitePairsToGroups}。
 *
 * @see #unitePairsToGroups
 * @see #unitePairsToGroupsMultiThread
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
            if (uf.get(i) == uf.get(f)) {
                continue;
            }
            if (iGroup.hasJointWith(fGroup)) {
                foundJoint = true;
                if (msgLevel >= 3) {
                    println("Found joint groups: %s and %s".formatted(
                            formatGroupWithUf(iGroup, uf.get(i)), formatGroupWithUf(fGroup, uf.get(f))));
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
        if (iGroup.merged) {
            continue;
        }
        if (iGroup.num == i) {
            iGroup.doCollect();
            groupCount++;
        } else {
            Group nGroup = groups.get(iGroup.num);
            nGroup.doCollect();
            nGroup.add(iGroup);
            iGroup.markMerged();
        }
    }
    return groupCount;
}

/**
 * 合并有交集的 pair, 转化为同一组。
 *
 * <p>
 * 还是通过遍历标记分组实现, 使用多线程。(探索多节点执行逻辑。)
 * 子线程任务使用 UnionFind。
 *
 * @see #unitePairsToGroups
 * @see #unitePairsToGroupsByUnionFind
 */
int unitePairsToGroupsMultiThread(List<Group> groups, boolean subtaskByUF) {
    // groups 分区
    int partCount = Runtime.getRuntime().availableProcessors();
    int partSize = (groups.size() + partCount - 1) / partCount;
    List<List<Group>> parts = new ArrayList<>();
    for (int i = 0; i < groups.size(); i += partSize) {
        parts.add(groups.subList(i, Math.min(i + partSize, groups.size())));
    }
    if (msgLevel >= 1) {
        println("subtasks count: %d, each task has %d elements。".formatted(parts.size(), partSize));
    }
    // 先分批子线程处理, 子线程内会合并一些Group, 保留下来的再在后面汇总
    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (int i = 0; i < parts.size(); i++) {
            final int j = i;
            executor.submit(() -> {
                if (subtaskByUF) {
                    unitePairsToGroupsByUnionFind(parts.get(j));
                } else {
                    unitePairsToGroups(parts.get(j));
                }
            });
        }
    }
    if (msgLevel >= 1) {
        long cnt = groups.stream().filter(g -> g.merged).count();
        println("In subtasks(Sub-Thread), has been merged group count: %d, rate: %.2f%%".formatted(
                cnt, 100.0 * cnt / groups.size()));
    }
    if (subtaskByUF) {
        groups.forEach(g -> {
            // 子线程使用 UnionFind 方法, 需要修正子线程内的 Group.num 为全局的编号
            g.num = g.origNum / partSize * partSize + g.num;
        });
    }
    // 汇总, 整体思路一样, 跳过已经被合并的, 判断共同元素通过集合而不是pair。
    Set<Integer> jointNums = new HashSet<>(groups.size());
    for (int i = partSize; i < groups.size(); i++) {
        Group iGroup = groups.get(i);
        if (iGroup.merged) {
            continue;
        }
        // 首先, 挑选所有与当前 Group 有交集的 Group, 标记为相同的编号
        jointNums.clear();
        jointNums.add(iGroup.num);
        int minNum = iGroup.num;
        for (int f = 0; f < i; f++) {
            Group fGroup = groups.get(f);
            if (fGroup.merged || jointNums.contains(fGroup.num)) {
                continue;
            }
            if (fGroup.anyJointWith(iGroup)) {
                jointNums.add(fGroup.num);
                minNum = Math.min(minNum, fGroup.num);
                if (msgLevel >= 3) {
                    println("Main thread Found joint groups: %s and %s".formatted(iGroup, fGroup));
                }
            }
        }
        // 标记归属, 向前看齐, 设置 Group.num 为最小的 Group.num
        if (minNum != iGroup.num) {
            for (int a = 0; a <= i; a++) {
                Group aGroup = groups.get(a);
                if (jointNums.contains(aGroup.num)) {
                    aGroup.num = minNum;
                }
            }
            printGroups(4, groups, "-", "Main thread After processing [" + i + "] Group");
        }
    }
    if (msgLevel < 4) { // 内部 print 了这里就不显示了
        printGroups(3, groups, "=", "Main thread processed each all");
    }

    // collect result, 上面只是标记，下面这里收集到 Group 集合中
    int groupCount = collectResultGroupsV2(groups);
    printGroups(2, groups, "=", "Main thread Merged marked groups");
    return groupCount;
}


Set<Set<String>> buildGroupsNameSetSet(List<Group> groups) {
    Set<Set<String>> nameSetSet = new HashSet<>();
    for (int i = 0; i < groups.size(); i++) {
        Group g = groups.get(i);
        if (g.merged) {
            continue;
        }
        nameSetSet.add(g.members);
    }
    if (msgLevel >= 2) {
        println("{NameSets}: " + nameSetSet);
        println("-".repeat(80));
    }
    return nameSetSet;
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
    if (msgLevel >= 2) {
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
    if (msgLevel >= 2) {
        println("Each Set count stat: " + counter);
    }
    println("Most largest sets: " + counter.mostCommon(10));
    println("-".repeat(80));
}

private void printGroups(int grade, List<Group> groups, String ch, String note) {
    if (msgLevel < grade) {
        return;
    }
    printBlockHead(note, ch);
    groups.forEach(g -> {
        if (g.merged) {
            return;
        }
        println(g);
    });
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
        if (g.merged) {
            continue;
        }
        println(formatGroupWithUf(g, uf.get(i)));
    }
    printBlockEnd(note, ch);
}

private String formatGroupWithUf(Group g, int ufNum) {
    return "[%d][%d](%s, %s)".formatted(g.origNum, ufNum, g.pairA, g.pairB);
}


/**
 * 从文件读取名字 pair 列表, 结构初始化为 Group 列表
 *
 * @param pairsFilePaths null/0 then default {@link #PAIRS_FILE_PATH}
 */
List<Group> readPairs(String... pairsFilePaths) {
    var counter = new Counter();
    if (pairsFilePaths == null || pairsFilePaths.length == 0) {
        return readPairsOne(PAIRS_FILE_PATH, counter, false, 0);
    }
    List<Group> pairs = new ArrayList<>();
    for (String pairsFilePath : pairsFilePaths) {
        pairs.addAll(readPairsOne(pairsFilePath, counter, true, pairs.size()));
    }
    printPairsNameCounterStat(counter);
    return pairs;
}

private List<Group> readPairsOne(String pairsFilePath, Counter counter, boolean inner, int num) {
    List<Group> pairs = new ArrayList<>();
    try (Scanner scanner = new Scanner(new File(pairsFilePath))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pair = line.split(",");
            if (msgLevel >= 1 && pair[0].equals(pair[1])) {
                println("[%d] two same names in pair(%s)".formatted(num, line));
            }
            Group group = new Group(num, pair[0], pair[1]);
            pairs.add(group);
            counter.add(pair[0]);
            counter.add(pair[1]);
            num++;
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    if (!inner) {
        printPairsNameCounterStat(counter);
    }
    return pairs;
}


/**
 * 随机生成一些名字 pair, 保存到文件 {@link #PAIRS_FILE_PATH}。
 * <p>
 * 不会覆盖旧文件, 已存在则什么也不做, 删除旧文件才会重新生成。
 */
void genPairs(boolean allowSame, boolean overwrite,
              int charsetLen, int nameLen, int pairsCount, String filepath) {
    filepath = (filepath == null || filepath.isBlank()) ? PAIRS_FILE_PATH : filepath;
    Path pairsPath = Paths.get(filepath);
    File pairsFile = pairsPath.toFile();
    if (pairsFile.exists()) {
        if (!overwrite) {
            return;
        }
        println("将要覆盖文件: %s, 重新生成 pairs 数据。".formatted(filepath));
    }
    initCharset(charsetLen);
    var counter = new Counter();
    try (PrintWriter writer = new PrintWriter(Files.newOutputStream(pairsPath))) {
        for (int i = 0; i < pairsCount; i++) {
            String a = genRandomName(nameLen), b = genRandomName(nameLen);
            while (b.equals(a) && !allowSame) {
                b = genRandomName(nameLen);
            }
            writer.print(a);
            writer.print(',');
            writer.println(b);
            counter.add(a);
            counter.add(b);
        }
        println("Generate pairs successfully @: " + filepath);
    } catch (IOException e) {
        e.printStackTrace();
    }
    println("with name length: %d, pairsCount: %d。".formatted(nameLen, pairsCount));
    printPairsNameCounterStat(counter);
}

private void printPairsNameCounterStat(Counter counter) {
    println("final: name total: %d, unique count: %d。".formatted(counter.total(), counter.size()));
    println("most common:" + counter.mostCommon(10));
    println("=".repeat(80));
}

private void initCharset(int len) {
    len = Math.min(Math.max(2, len), 62);
    nameChars = new char[len];
    for (int i = 0; i < 26 && i < len; i++) {
        nameChars[i] = (char) ('A' + i);
    }
    for (int i = 0; i < 26 && i + 26 < len; i++) {
        nameChars[i + 26] = (char) ('a' + i);
    }
    for (int i = 0; i < 10 && i + 52 < len; i++) {
        nameChars[i + 52] = (char) ('0' + i);
    }
    println("pair names charset: " + new String(nameChars));
}

private String genRandomName(int nameLen) {
    char[] chars = new char[nameLen];
    chars[0] = (char) ('A' + random.nextInt(Math.min(nameChars.length, 26))); // 大写字母开头
    for (int i = 1; i < chars.length; i++) {
        chars[i] = nameChars[random.nextInt(nameChars.length)];
    }
    return new String(chars);
}


static class Group {
    int num; // 编号
    final int origNum; // 初始编号
    final String pairA, pairB; // 原始 pair 数据
    Set<String> members;
    boolean collected;
    /// 被合并(吞并)了
    boolean merged;

    Group(int num, String pairA, String pairB) {
        this.origNum = num;
        this.pairA = pairA;
        this.pairB = pairB;
        this.reset();
    }

    void reset() {
        this.num = this.origNum;
        members = new HashSet<>(); // 新建而不是清空
        members.add(pairA);
        members.add(pairB);
        collected = merged = false;
    }

    void doCollect() {
        collected = true;
    }

    /// 标记为被合并(吞并)了
    void markMerged() {
        merged = true;
        members = null;
    }

    void add(Group that) {
        this.members.addAll(that.members);
    }

    /// 通过 pair 判断, 通过集合(Set)判断见[#anyJointWith]
    boolean hasJointWith(Group that) {
        return this.pairA.equals(that.pairA) || this.pairA.equals(that.pairB)
                || this.pairB.equals(that.pairA) || this.pairB.equals(that.pairB);
    }

    /// 通过集合(Set)判断, 若只有两个元素, 性能不如 [#hasJointWith]
    boolean anyJointWith(Group that) {
        Set<String> small = this.members, big = that.members;
        if (small.size() > big.size()) {
            small = big;
            big = this.members;
        }
        for (String name : small) {
            if (big.contains(name)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "[%d][%d](%s, %s)%s".formatted(origNum, num, pairA, pairB, collected ? members : "");
    }
}

//}
