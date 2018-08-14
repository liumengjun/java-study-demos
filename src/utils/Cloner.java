package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cloner {
    public static Object jsonClone(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return o;
        }
        if (o instanceof Number) {
            return o;
        }
        if (o instanceof Boolean) {
            return o;
        }
        if (o instanceof Map) {
            Map map = new HashMap();
            for (Object kv : ((Map) o).entrySet()) {
                Map.Entry entry = (Map.Entry) kv;
                map.put(entry.getKey(), jsonClone(entry.getValue()));
            }
            return map;
        }
        if (o instanceof Iterable) {
            List list = new ArrayList();
            for (Object ele : (Iterable) o) {
                list.add(jsonClone(ele));
            }
            return list;
        }
        if (o instanceof Serializable) {
            return SerializationUtils.clone((Serializable) o);
        }
        if (o instanceof Cloneable) {
            try {
                return o.getClass().getMethod("clone").invoke(o);
            } catch (Exception e) {
                // throw e; // ignore
            }
        }
        return o;
    }

    public static <T extends Serializable> T deepClone(T o) {
        return SerializationUtils.clone(o);
    }

    @Deprecated
    public static <T> T cloneByGson(T t) {
        Gson gson = new Gson();
        String json = gson.toJson(t);
        return (T) gson.fromJson(json, t.getClass());
    }

    public static <T> T cloneByFastjson(T t) {
        String json = JSON.toJSONString(t, SerializerFeature.WriteMapNullValue);
        return (T) JSON.parseObject(json, t.getClass());
    }

    /**
     * PS:
     * fastjson中，JSONObject实现了Map接口，JSONArray实现了List接口。还有个json-smart工具也是如此
     * org.json和gson中，JSONObject没有实现Map接口，JSONArray实现的是Iterable接口。
     * 另外：
     * gson本身有deepopy()功能。其他的json工具没有。可是gson没有实现Map,List接口，不如fastjson友好
     */
}
