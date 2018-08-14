package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ClonerTest {

    static JSONObject json;
    static JSONArray jsonArray;

    @Test
    public void test_jsonClone() throws Exception {
        Object jsonCloned = Cloner.jsonClone(json);
        System.out.println(jsonCloned);
        assertFalse(json == jsonCloned);
        assertEquals(json, jsonCloned);

        Object jsonArrayCloned = Cloner.jsonClone(jsonArray);
        System.out.println(jsonArrayCloned);
        assertFalse(jsonArray == jsonArrayCloned);
        assertEquals(jsonArray, jsonArrayCloned);
    }

    @Test
    public void test_deepClone() throws Exception {
        JSONObject jsonCloned = Cloner.deepClone(json);
        System.out.println(jsonCloned);
        assertFalse(json == jsonCloned);
        assertEquals(json, jsonCloned);

        JSONArray jsonArrayCloned = Cloner.deepClone(jsonArray);
        System.out.println(jsonArrayCloned);
        assertFalse(jsonArray == jsonArrayCloned);
        assertEquals(jsonArray, jsonArrayCloned);
    }

    @Ignore
    @Test
    public void test_cloneByGson() {
        JSONObject jsonCloned = Cloner.cloneByGson(json);
        System.out.println(jsonCloned);
        assertFalse(json == jsonCloned);
        assertEquals(json, jsonCloned);
        //        assertTrue(Objects.deepEquals(json, jsonCloned));

        JSONArray jsonArrayCloned = Cloner.cloneByGson(jsonArray);
        System.out.println(jsonArrayCloned);
        assertFalse(jsonArray == jsonArrayCloned);
        assertEquals(jsonArray, jsonArrayCloned);
    }


    @Test
    public void test_cloneByFastjson() {
        JSONObject jsonCloned = Cloner.cloneByFastjson(json);
        System.out.println(jsonCloned);
        assertFalse(json == jsonCloned);
        assertEquals(json, jsonCloned);

        JSONArray jsonArrayCloned = Cloner.cloneByFastjson(jsonArray);
        System.out.println(jsonArrayCloned);
        assertFalse(jsonArray == jsonArrayCloned);
        assertEquals(jsonArray, jsonArrayCloned);
    }


    @BeforeClass
    public static void init() {
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

        jsonArray = JSON.parseArray("[\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": false,\n"
                + "            \"createTime\": 1527937574000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"A0\",\n"
                + "          },\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": false,\n"
                + "            \"createTime\": 1529398329000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"A9\",\n"
                + "          },\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": false,\n"
                + "            \"createTime\": 1530768130000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"A9\",\n"
                + "          },\n"
                + "          {\n"
                + "            \"rejectNewOrderNow\": true,\n"
                + "            \"createTime\": 1533716895000,\n"
                + "            \"orderStatus\": \"REJECTED\",\n"
                + "            \"bills\": [],\n"
                + "            \"id\": \"B1\",\n"
                + "          }\n"
                + "        ]");
    }

}