package sjtu.swenet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Utils {
	public static void main(String[] args){
//		String s = "r";
//		System.out.println(singularize(s));
//		System.out.println(pluralize(s));
	}
	
	public static List<String> readLine(InputStream in) {
		return readLine(in, "utf-8");
	}

	public static List<String> readLine(InputStream in, String encoding) {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, encoding));
			String line = null;
			while ((line = reader.readLine()) != null)
				result.add(line);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static List<String> readLine(File f, String encoding) {
		try {
			return readLine(new FileInputStream(f), encoding);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> readLine(File f) {
		try {
			return readLine(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static <K, V> Map<K, Map<K, V>> addToMap(Map<K, Map<K, V>> map,
			K key1, K key2, V value) {
		if (map == null)
			return null;
		Map<K, V> valueMap = map.get(key1);
		if (valueMap == null)
			valueMap = new HashMap<K, V>();
		valueMap.put(key2, value);
		map.put(key1, valueMap);
		return map;
	}

	public static <T> Map<T, List<T>> addToListMap(Map<T, List<T>> map, T key,
			T value) {
		if (map == null)
			return null;
		if (key == null || value == null)
			return map;
		List<T> set = map.get(key);
		if (set == null)
			set = new ArrayList<T>();
		set.add(value);
		map.put(key, set);
		return map;
	}

	public static <T> Map<T, Map<T, Double>> addToMapWithFreq(
			Map<T, Map<T, Double>> map, T key, T value, Double freq) {
		if (map == null)
			return null;
		Map<T, Double> valueMap = map.get(key);
		if (valueMap == null)
			valueMap = new HashMap<T, Double>();
		valueMap = addToMap(valueMap, value, freq);
		map.put(key, valueMap);
		return map;
	}

	public static <T> Map<T, Double> addToMap(Map<T, Double> map,
			Map<T, Double> toAdd) {
		if (map == null)
			return null;
		if (toAdd == null)
			return map;
		for (Map.Entry<T, Double> entry : toAdd.entrySet()) {
			map = addToMap(map, entry.getKey(), entry.getValue());
		}
		return map;
	}

	public static <T> Map<T, Double> addToMap(Map<T, Double> map, T key,
			Double value) {
		if (map == null)
			return null;
		if (key == null)
			return map;
		if (map.get(key) != null) {
			double temp = map.get(key);
			map.put(key, temp + value);
		} else
			map.put(key, value);
		return map;
	}

	public static <K, V> Map<K, Set<V>> addToMap(Map<K, Set<V>> map, K key,
			V value) {
		if (map == null)
			return null;
		if (key == null || value == null)
			return map;
		Set<V> set = map.get(key);
		if (set == null)
			set = new HashSet<V>();
		set.add(value);
		map.put(key, set);
		return map;
	}

	public static <T> Map<T, Double> addToMap(Map<T, Double> map, T key) {
		if (map == null)
			return null;
		if (key == null)
			return map;
		if (map.get(key) != null) {
			double temp = map.get(key);
			map.put(key, temp + 1d);
		} else
			map.put(key, 1d);
		return map;
	}

	public static <T> Map<T, Double> sortMapByValue(Map<T, Double> oriMap) {
		Map<T, Double> sortedMap = new LinkedHashMap<T, Double>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<T, Double>> entryList = new ArrayList<Map.Entry<T, Double>>(
					oriMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<T, Double>>() {
				public int compare(Entry<T, Double> entry1,
						Entry<T, Double> entry2) {
					Double value1 = entry1.getValue();
					Double value2 = entry2.getValue();

					return value2.compareTo(value1);
				}
			});
			Iterator<Map.Entry<T, Double>> iter = entryList.iterator();
			Map.Entry<T, Double> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(),
						Double.parseDouble(tmpEntry.getValue() + ""));
			}
		}
		return sortedMap;
	}

	public static <T> Map<T, BigDecimal> sortMapByBDValue(
			Map<T, BigDecimal> oriMap) {
		Map<T, BigDecimal> sortedMap = new LinkedHashMap<T, BigDecimal>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<T, BigDecimal>> entryList = new ArrayList<Map.Entry<T, BigDecimal>>(
					oriMap.entrySet());
			Collections.sort(entryList,
					new Comparator<Map.Entry<T, BigDecimal>>() {
						public int compare(Entry<T, BigDecimal> entry1,
								Entry<T, BigDecimal> entry2) {
							BigDecimal value1 = entry1.getValue();
							BigDecimal value2 = entry2.getValue();

							return value2.compareTo(value1);
						}
					});
			Iterator<Map.Entry<T, BigDecimal>> iter = entryList.iterator();
			Map.Entry<T, BigDecimal> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}

	public static <T> Map<T, Double> sortMapByValue(Map<T, Double> oriMap,
			final boolean isDesc) {
		Map<T, Double> sortedMap = new LinkedHashMap<T, Double>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<T, Double>> entryList = new ArrayList<Map.Entry<T, Double>>(
					oriMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<T, Double>>() {
				public int compare(Entry<T, Double> entry1,
						Entry<T, Double> entry2) {
					Double value1 = entry1.getValue();
					Double value2 = entry2.getValue();
					if (isDesc)
						return value2.compareTo(value1);
					else
						return value1.compareTo(value2);
				}
			});
			Iterator<Map.Entry<T, Double>> iter = entryList.iterator();
			Map.Entry<T, Double> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}

	public static <V> Map<Date, V> sortMapByDateKey(Map<Date, V> oriMap,
			final boolean isDesc) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<Date, V> sortedMap = new TreeMap<Date, V>(new Comparator<Date>() {
			public int compare(Date key1, Date key2) {
				if (isDesc)
					return key1.compareTo(key2);
				else
					return key2.compareTo(key1);
			}
		});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	public static <V> Map<String, V> sortMapByStringKey(Map<String, V> oriMap,
			final boolean isDesc) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, V> sortedMap = new TreeMap<String, V>(
				new Comparator<String>() {
					public int compare(String key1, String key2) {
						if (isDesc)
							return key2.compareTo(key1);
						else
							return key1.compareTo(key2);
					}
				});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}
	public static String join( String delimiter, String[] array ) {
		StringBuilder sb = new StringBuilder();
		int counter=0;
		for ( String element : array ) {
			sb.append( counter++>0?delimiter:"" );
			sb.append( element );
		}
		return sb.toString();
	}

	public static String join( String delimiter, List<String> list ) {
		StringBuilder sb = new StringBuilder();
		int counter=0;
		for ( String element : list ) {
			sb.append( counter++>0?delimiter:"" );
			sb.append( element );
		}
		return sb.toString();
	}

	/**
	 * Create a new instance of list which has elements in reverse order
	 * @param list list of String
	 * @return reversed list
	 */
	public static List<String> reverse( List<String> list ) {
		List<String> reversedList = new ArrayList<String>( list );
		Collections.reverse( reversedList );		
		return reversedList;
	}
}
