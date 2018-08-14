package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Fork(value = 1, jvmArgsPrepend = "-Xmx128m")
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ClonerBenchmark {
    static JSONObject json;

    @Benchmark
    public Object jsonClone() {
        return Cloner.jsonClone(json);
    }

    @Benchmark
    public Object deepClone() {
        return Cloner.deepClone(json);
    }

    @Benchmark
    public Object cloneByGson() {
        return Cloner.cloneByGson(json);
    }

    @Benchmark
    public Object cloneByFastjson() {
        return Cloner.cloneByFastjson(json);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ClonerBenchmark.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .detectJvmArgs()
                .build();

        new Runner(opt).run();
    }

    /**
     * PS:
     * deepClone > cloneByGson > cloneByFastjson > jsonClone
     * 虽然jsonClone性能最高，可是功能局限。
     * deepClone性能最差，而且也限于Serializable对象。
     * fastjson好于gson。有部分原因是测试对象`json`由fastjson生成，主要应该是fastjson确实优化了
     */

    static {
        json = JSON.parseObject("{\n"
                + "        \"code\": \"SUCCESS\",\n"
                + "        \"data\": [\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": false,\n"
                + "            \"createTime\": 1525953066000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"A1\",\n"
                + "          },\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": false,\n"
                + "            \"createTime\": 1532262685000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"A2\",\n"
                + "          },\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": true,\n"
                + "            \"createTime\": 1533716895000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"B1\",\n"
                + "          }\n"
                + "        ],\n"
                + "        \"message\": null\n"
                + "      }");
    }
}
