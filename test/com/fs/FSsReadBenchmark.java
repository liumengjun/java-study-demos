package com.fs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Fork(value = 1, jvmArgsPrepend = "-Xmx512m")
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FSsReadBenchmark {

    private static Random random = ThreadLocalRandom.current();

    private static FileSystem hDfs;

    private static String TMP_DIR_NAME = "/tmp/test-files/";

    static {
        try {
            hDfs = FileSystem.get(new URI("hdfs://localhost:9000"), new Configuration());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Benchmark
    public byte[] fsRead() {
        Path tmpDir = Paths.get(TMP_DIR_NAME);
        try {
            List<Path> subPaths = Files.list(tmpDir).collect(Collectors.toList());
            Path toReadPath = subPaths.get(random.nextInt(subPaths.size()));
            return Files.readAllBytes(toReadPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Benchmark
    public byte[] hDfsRead() {
        try {
            org.apache.hadoop.fs.Path remoteDir = new org.apache.hadoop.fs.Path(TMP_DIR_NAME);
            RemoteIterator<LocatedFileStatus> remoteFilesItr = hDfs.listFiles(remoteDir, false);
            List<LocatedFileStatus> remoteFiles = new ArrayList<>();
            while (remoteFilesItr.hasNext()) {
                remoteFiles.add(remoteFilesItr.next());
            }
            LocatedFileStatus toReadFile = remoteFiles.get(random.nextInt(remoteFiles.size()));
            FSDataInputStream hDfsInput = hDfs.open(toReadFile.getPath());
            byte[] bytes = new byte[(int) toReadFile.getLen()];
            hDfsInput.readFully(bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void init() throws IOException {
        // clear TMP_DIR_NAME
        Path tmpDir = Paths.get(TMP_DIR_NAME);
        for (Path p : Files.list(tmpDir).collect(Collectors.toList())) {
            Files.delete(p);
        }
        hDfs.delete(new org.apache.hadoop.fs.Path(TMP_DIR_NAME), true);

        // new files by FSsWriteBenchmark
        FSsWriteBenchmark.TEST_SIZE = 4 * 1024 * 1024;
        FSsWriteBenchmark writeTool = new FSsWriteBenchmark();
        for (int i = 0; i < 100; i++) {
            writeTool.fsWrite();
            writeTool.hDfsWrite();
        }
    }

    public static void main(String[] args) throws RunnerException, IOException {
        System.out.println("init...");
        init();
        System.out.println("init end.");
        Options opt = new OptionsBuilder()
                .include(FSsReadBenchmark.class.getSimpleName())
                .detectJvmArgs()
                .build();

        new Runner(opt).run();
    }
}
