package chen.LRU;

public interface ILRUCache<K,V> {
	public V get(K key);
	
	public void put(K key, V value);
	
	/**This one is called while we are removing the key Value pair from the cache.
	 * 
	 * @param value
	 */
	public void clean(V value);
}
