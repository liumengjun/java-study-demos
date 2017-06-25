package algorism.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapUtils {

	/**
	 * 展开Map, 只保留一层key/value结构。(Map可能包含多层Map，即value又是Map，新Map的key由多层key用'.'拼接而成)<br>
	 * 如：{ "a": 1, "b": { "c": 2, "d": [3,4] } } => {"a": 1, "b.c": 2, "b.d": [3,4] }
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> flattenMap(Map<String, Object> json) {
		Map<String, Object> flatMap = new HashMap<>();
		// 遍历每一对<key, value>
		for (Map.Entry<String, Object> entry : json.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map) {
				// 如果value是个Map，递归调用该方法。拼接key和该Map的key作为新key
				Map<String, Object> inner = flattenMap((Map) value);
				for (Map.Entry<String, Object> innerEntry : inner.entrySet()) {
					flatMap.put(key + "." + innerEntry.getKey(), innerEntry.getValue());
				}
			} else {
				flatMap.put(key, value);
			}
		}
		return flatMap;
	}

	private static String encode(String str) {
		// 定义 '\' 为转义字符
		str = str.replace("\\", "\\\\"); // '\'即字符，非转义字符
		str = str.replace("=", "\\="); // 转义'='
		str = str.replace(";", "\\;"); // 转义';'
		str = str.replace("\n", "\\\n"); // 转义'\n'
		return str;
	}

	/**
	 * 把Map[]数组转换为字符串，形如"key1=value1;key2=value2\nkeyA=valueA\n"，每行一个Map
	 * 
	 * @param maps
	 * @return
	 */
	public static String storeMapArray(Map<String, String>[] maps) {
		StringBuilder buf = new StringBuilder();
		// 遍历每个map
		for (Map<String, String> map : maps) {
			boolean hasValue = false;
			// 拼接key1=value1;key2=value2
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = encode(entry.getKey());
				String value = encode(entry.getValue());
				buf.append(key).append('=').append(value).append(';');
				hasValue = true;
			}
			if (hasValue) {
				buf.setLength(buf.length() - 1); // 删除最后一个分号';'
			}
			// 结束该map
			buf.append('\n');
		}
		return buf.toString();
	}

	/**
	 * 把形如"key1=value1;key2=value2\nkeyA=valueA\n"字符串（每行一个Map），转换为Map[]数组
	 * 
	 * @param text
	 * @return
	 */
	public static Map<String, String>[] loadMapArray(String text) {
		List<Map<String, String>> mapList = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		String key = "", value;
		boolean todoEscape = false;
		StringBuilder buf = new StringBuilder(); // 收集字符
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\\') { // 检测是否转义
				todoEscape = !todoEscape;
			}
			if (c == '=') {
				if (todoEscape) {
					buf.append(c);
					todoEscape = !todoEscape;
				} else {
					key = buf.toString();
					buf.setLength(0);
				}
			} else if (c == ';') {
				if (todoEscape) {
					buf.append(c);
					todoEscape = !todoEscape;
				} else {
					value = buf.toString();
					buf.setLength(0);
					map.put(key, value);
				}
			} else if (c == '\n') {
				if (todoEscape) {
					buf.append(c);
					todoEscape = !todoEscape;
				} else {
					value = buf.toString();
					buf.setLength(0);
					if (!key.equals("")) {
						// 如何key为""则是空map
						map.put(key, value);
					}
					mapList.add(map);
					// 准备新一行
					map = new HashMap<>();
					key = "";
				}
			} else {
				if (!todoEscape) {
					buf.append(c);
				}
			}
		}
		Map<String, String>[] maps = new Map[mapList.size()];
		return mapList.toArray(maps);
	}

	public static Map<String, String>[] loadMapArrayWithoutEncode(String text) {
		List<Map<String, String>> mapList = new ArrayList<>();
		BufferedReader strReader = new BufferedReader(new StringReader(text));
		try {
			String line;
			while ((line = strReader.readLine()) != null) {
				Map<String, String> map = new HashMap<>();
				if (!line.isEmpty()) {
					String[] items = line.split(";");
					for (String item : items) {
						String[] kv = item.split("=");
						map.put(kv[0], kv[1]);
					}
				}
				// 一行结束，生成一个map
				mapList.add(map);
			}
		} catch (Exception e) {
			// ignore
		} finally {
			try {
				strReader.close();
			} catch (IOException e) {
			}
		}
		Map<String, String>[] maps = new Map[mapList.size()];
		return mapList.toArray(maps);
	}

}
