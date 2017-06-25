package algorism.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class MapUtilsTest {

	@Test
	public void testFlattenMap() {
		// Map<String, Object> m = new HashMap<>();
		// m.put("a", 1);
		// Map<String, Object> b = new HashMap<>();
		// b.put("c", 2);
		// b.put("d", new int[] { 3, 4 });
		// m.put("b", b);
		Map<String, Object> m = JSON.parseObject("{\"a\": 1, \"b\": { \"c\": 2, \"d\": [3,4] } }");
		Map<String, Object> o = MapUtils.flattenMap(m);
		System.out.println(JSON.toJSONString(o));
		assertEquals(o.get("b.d"), ((Map<String, Object>) m.get("b")).get("d"));
	}

	@Test
	public void testStoreAndLoadMapArray() {
		Map<String, String>[] mapArray = new Map[] { new HashMap(), new HashMap(), new HashMap() };
		mapArray[0].put("key1\\", "value1");
		mapArray[0].put("ke=y2", "va\nlue2");
		mapArray[2].put("keyA", "val;ueA");
		String text = MapUtils.storeMapArray(mapArray);
		System.out.println(text);
		Map<String, String>[] newMaps = MapUtils.loadMapArray(text);
		for (Map<String, String> map : newMaps) {
			System.out.println(map);
		}
		assertArrayEquals(mapArray, newMaps);
	}
}
