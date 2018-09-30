package com.fs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Fork(value = 1, jvmArgsPrepend = "-Xmx512m")
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FSsWriteBenchmark {

    private static final int TEST_SIZE = 10 * 1024 * 1024;

    private static Random random = ThreadLocalRandom.current();

    private static FileSystem hDfs;

    private static GridFSBucket gridFs;

    static {
        try {
            hDfs = FileSystem.get(new URI("hdfs://localhost:9000"), new Configuration());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        MongoClient mongoClient = MongoClients.create(); // localhost单例
        MongoDatabase db = mongoClient.getDatabase("gridfs");
        gridFs = GridFSBuckets.create(db);
    }

    @Benchmark
    public void fsWrite() {
        String tmpDirName = "/tmp/test-files/";
        Path tmpDir = Paths.get(tmpDirName);
        Path tmpPath = Paths.get(tmpDirName, UUID.randomUUID().toString());
        try {
            Files.createDirectories(tmpDir); // do not use Files.createDirectory(tmpDir);
            Files.write(tmpPath, generateRandomBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void hDfsWrite() {
        String tmpFilename = "/tmp/test-files/" + UUID.randomUUID().toString();
        try {
            FSDataOutputStream out = hDfs.create(new org.apache.hadoop.fs.Path(tmpFilename));
            out.write(generateRandomBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void gridFsWrite() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(generateRandomBytes());
        gridFs.uploadFromStream(UUID.randomUUID().toString(), inputStream);
    }

    private static byte[] generateRandomBytes() {
        byte[] bs = new byte[TEST_SIZE];
        random.nextBytes(bs);
        return bs;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FSsWriteBenchmark.class.getSimpleName())
                .detectJvmArgs()
                .build();

        new Runner(opt).run();
    }

    /**
     * 单点测试，结果：
     * case 5k:
     *   fs >>> gridFs >>>>>>>>>> hDfs
     * case 1m:
     *   fs >>>>> gridFs >>> hDfs
     * case 4m:
     *   fs >>>>> gridFs = hDfs
     * case 5m:
     *   fs >>>>> hDfs >1/5 gridFs
     * case 10m:
     *   fs >>> hDfs >1/2 gridFs
     * case 16m | 32m:
     *   fs >> hDfs >> gridFs
     * case 100m:
     *   fs >1/3 hDfs >>> gridFs
     *
     * PS:
     *  单机上的测试，没有做集群测试。单机上：本地fs总是优于HDFS和GridFS。在小于4M时，单机GridFS优于HDFS，文件越大GridFS表现越差。
     *  集群时，HDFS和GridFS应该优于本地fs，然而小文件不一定。mongodb说，其16M大文件表现优秀，看来不属实啊。TODO：集群环境待测试。
     *  还有其他很多优秀的DFS，但各有各的特点，努力学习中...
     *  而且此处只测试write(写)操作，没有测试Read(读)。GridFs不是FUSE，读GridFS，需要记住其ObjectID，不是很方便。
     */
}
