package pl.softech.stackoverflow.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * <a href=
 * "http://stackoverflow.com/questions/23646186/a-java-multimap-which-allows-fast-lookup-of-key-by-value/24966130#24966130"
 * >A Java multimap which allows fast lookup of key by value</a>
 * 
 * @author ssledz
 */
public class MultimapExample<K, V> implements Multimap<K, V> {

	private Multimap<K, V> key2Value = ArrayListMultimap.create();
	private Map<V, K> value2key = Maps.newHashMap();

	public K getKeyByValue(V value) {
		return value2key.get(value);
	}

	@Override
	public int size() {
		return key2Value.size();
	}

	@Override
	public boolean isEmpty() {
		return key2Value.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return key2Value.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return key2Value.containsValue(value);
	}

	@Override
	public boolean containsEntry(Object key, Object value) {
		return key2Value.containsEntry(key, value);
	}

	@Override
	public boolean put(K key, V value) {
		value2key.put(value, key);
		return key2Value.put(key, value);
	}

	@Override
	public boolean remove(Object key, Object value) {
		value2key.remove(value);
		return key2Value.remove(key, value);
	}

	@Override
	public boolean putAll(K key, Iterable<? extends V> values) {
		for (V value : values) {
			value2key.put(value, key);
		}
		return key2Value.putAll(key, values);
	}

	@Override
	public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
		for (Entry<? extends K, ? extends V> e : multimap.entries()) {
			value2key.put(e.getValue(), e.getKey());
		}
		return key2Value.putAll(multimap);
	}

	@Override
	public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
		Collection<V> replaced = key2Value.replaceValues(key, values);
		for (V value : replaced) {
			value2key.remove(value);
		}
		for (V value : values) {
			value2key.put(value, key);
		}
		return replaced;
	}

	@Override
	public Collection<V> removeAll(Object key) {
		Collection<V> removed = key2Value.removeAll(key);
		for (V value : removed) {
			value2key.remove(value);
		}
		return removed;
	}

	@Override
	public void clear() {
		value2key.clear();
		key2Value.clear();
	}

	@Override
	public Collection<V> get(K key) {
		return key2Value.get(key);
	}

	@Override
	public Set<K> keySet() {
		return key2Value.keySet();
	}

	@Override
	public Multiset<K> keys() {
		return key2Value.keys();
	}

	@Override
	public Collection<V> values() {
		return key2Value.values();
	}

	@Override
	public Collection<Entry<K, V>> entries() {
		return key2Value.entries();
	}

	@Override
	public Map<K, Collection<V>> asMap() {
		return key2Value.asMap();
	}

	public static void main(String[] args) {

		MultimapExample<String, String> map = new MultimapExample<>();

		map.put("key1", "value1");
		map.put("key1", "value2");
		map.put("key1", "value3");
		map.put("key1", "value4");
		map.put("key2", "value5");

		System.out.println(map.getKeyByValue("value1"));
		System.out.println(map.getKeyByValue("value5"));

	}

}