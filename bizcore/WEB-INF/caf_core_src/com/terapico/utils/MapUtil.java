package com.terapico.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtil {
	public static class MapBuilder {
		private Map<String, Object> mapInstance= null;
		public MapBuilder put(String key, Object value) {
			ensuerMapInstance();
			mapInstance.put(key, value);
			return this;
		}
		public MapBuilder putIf(Object obj, String key, Supplier s) {
			return putIf(obj!=null, key, s);
		}
		public MapBuilder putIf(String key, Object value) {
			return putIf(value!=null, key, value);
		}
		public MapBuilder putIf(boolean shouldPut, String key, Supplier s) {
			return putIf(shouldPut, key, shouldPut?s.get():null);
		}
		public MapBuilder putIf(boolean shouldPut, String key, Object value) {
			if (!shouldPut) {
				return this;
			}
			ensuerMapInstance();
			mapInstance.put(key, value);
			return this;
		}
		private void ensuerMapInstance() {
			if (mapInstance == null) {
				mapInstance = new LinkedHashMap<String, Object>();
			}
		}
		public Map<String, Object> into_map() {
			ensuerMapInstance();
			return mapInstance;
		}
		public <T> Map<String, T> into_map(Class<T> clazz) {
			ensuerMapInstance();
			return (Map<String, T>)mapInstance;
		}
		
	}
	public static class _MapEntryUtil{
		String key;
		Object value;
	}
	public static Object getByPath(Map<String, ? extends Object> dataMap, String keyPath){
		if (keyPath == null){
			return dataMap.get(keyPath);
		}
		String[] keyTokens = keyPath.split("\\.");
		int idx = 0;
		return getByPathOf(dataMap, keyTokens, idx);
	}

	public static Object getByPathOf(Map<String, ? extends Object> dataMap, String[] keyTokens, int idx) {
		if (dataMap == null){
			return null;
		}
		if (idx >= keyTokens.length){
			return null;
		}
		Object objData = getByToken(dataMap, keyTokens[idx]);
		if (idx == keyTokens.length - 1){
			return objData;
		}
		if (!(objData instanceof Map)){
			return null;
		}
		return getByPathOf((Map<String, Object>) objData, keyTokens, idx+1);
	}

	public static void putByPath(Map<String, Object> result, List<String> paths, Object value) {
		if (paths.size() == 1){
			String name = paths.get(0);
			result.put(name, value);
			return;
		}

		String name = paths.remove(0);
		Map<String,Object> subMap = (Map<String, Object>) result.get(name);
		if (subMap == null){
			subMap = new HashMap<>();
			result.put(name, subMap);
		}
		putByPath(subMap, paths, value);
	}

	protected static final Pattern ptnListToken = Pattern.compile("([a-zA-Z0-9_$#]+)\\[(\\d+)\\]");
	protected static Object getByToken(Map<String, ? extends Object> dataMap, String token) {
		Matcher m = ptnListToken.matcher(token);
		if (m.matches()){
			String name = m.group(1);
			int idx = Integer.parseInt( m.group(2));
			Object listData = dataMap.get(name);
			if (listData == null){
				return null;
			}
			if (listData.getClass().isArray()){
				Object[] array = (Object[]) listData;
				if (idx >= array.length){
					return null;
				}
				return array[idx];
			}
			if (listData instanceof List){
				List list = (List) listData;
				if (idx >= list.size()){
					return null;
				}
				return list.get(idx);
			}
		}
		return dataMap.get(token);
	}
	
	@Deprecated
	/**
	 * 被 MapUtil.put(k,v).put(k,v)....put(k,v).into_map(); 取代
	 */
	public static _MapEntryUtil $(String key, Object value) {
		_MapEntryUtil result = new _MapEntryUtil();
		result.key = key;
		result.value = value;
		return result;
	}
	
	@Deprecated
	/**
	 * 被 MapUtil.put(k,v).put(k,v)....put(k,v).into_map(); 取代
	 */
	public static Map<String, Object> newMap(_MapEntryUtil ...entries){
		Map<String, Object> map = new HashMap<String, Object>();
		if (entries != null && entries.length > 0) {
			for(_MapEntryUtil entry : entries) {
				map.put(entry.key, entry.value);
			}
		}
		return map;
	}

	public static MapBuilder put(String key, Object value) {
		return new MapBuilder().put(key, value);
	}
	public static MapBuilder putIf(boolean shouldPut, String key, Object value) {
		return new MapBuilder().putIf(shouldPut, key, value);
	}
	public static MapBuilder putIf(boolean shouldPut, String key, Supplier s) {
		return new MapBuilder().putIf(shouldPut, key, s);
	}
	public static MapBuilder putIf(String key, Object value) {
		return new MapBuilder().putIf(key, value);
	}
	public static MapBuilder putIf(Object obj, String key, Supplier s) {
		return new MapBuilder().putIf(obj, key, s);
	}
	public static <T> Map<String, T> with(String key, T value) {
		Map<String, T> map = new HashMap<>();
		map.put(key, value);
		return map;
	}
}
