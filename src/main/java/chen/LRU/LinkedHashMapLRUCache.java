package chen.LRU;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**This implementation of LRUCahce is easy to use and simple. 
 * 
 * @author adam701
 *
 * @param <K>
 * @param <V>
 */

public class LinkedHashMapLRUCache<K extends Comparable<? super K>, V> implements ILRUCache<K, V> {

	private final int maxEntries;
	private final LinkedHashMap<K, V> cache;
	private final ReentrantLock lock = new ReentrantLock();
	
	@SuppressWarnings("serial")
	public LinkedHashMapLRUCache(final int maxEntry){
		this.maxEntries = maxEntry;
		this.cache = new LinkedHashMap<K, V>(maxEntries, 0.75f, true){
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		        if(size() > maxEntries){
		        	clean(eldest.getValue());
		        	return true;
		        }else{
		        	return false;
		        }
		    }
		};
	}
	
	public V get(K key) {
		lock.lock();
		try{
			return cache.get(key);
		}finally{
			lock.unlock();
		}
	}

	public void put(K key, V value) {
		lock.lock();
		try{
			cache.put(key, value);
		}finally{
			lock.unlock();
		}
	}

	
	/**
	 * Can be override so that we can add additional operation after we remove the object.
	 */
	public void clean(V value) {
	}
}
