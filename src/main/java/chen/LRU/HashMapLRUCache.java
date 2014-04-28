package chen.LRU;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**This implementation is just for Fun. From the performance perspective, it is not better than LinkedHashMap. And also it 
 * bring in more fail points by defining new methods and classes.
 * 
 * @author adam701
 *
 * @param <K>
 * @param <V>
 */

public class HashMapLRUCache<K, V> implements ILRUCache<K, V> {
		private final HashMap<K, ListNode<K, V>> map = new HashMap<>();
		private final ReentrantLock listLock = new ReentrantLock();
		private final CircularDoubleList<K, V> circDoubleList = new CircularDoubleList<>();
		private int maxSize = 10;
		private int currentSize = 0;
		
		static class ListNode<K, V>{
			V value;
			K key;
			ListNode<K, V> prev;
			ListNode<K, V> next;
			
			ListNode(){}
			
			ListNode(K key, V value){
				this.key = key;
				this.value = value;
			}
			
			ListNode(K key, V value, ListNode<K, V> prev, ListNode<K, V> next){
				this.value = value;
				this.key = key;
				this.prev = prev;
				this.next = next;
			}
			
			void removeSelfFromList(){
				this.prev.next = this.next;
				this.next.prev = this.prev;
			}
		}
		
		static class CircularDoubleList<K, V>{
			final ListNode<K, V> anchor = new ListNode<>();
			
			CircularDoubleList(){
				anchor.next = anchor;
				anchor.prev = anchor;
			}
			
			void insertToHead(ListNode<K, V> node){
				node.next = anchor.next;
				node.next.prev = node;
				anchor.next = node;
				node.prev = anchor;
			}
			
			ListNode<K, V> getTail(){
				return anchor.prev;
			}
			
			ListNode<K, V> removeTail(){
				ListNode<K, V> node = anchor.prev;
				node.prev.next = anchor;
				anchor.prev = node.prev;
				return node;
			}
		}
		
		public V get(K key){
			ListNode<K, V> value = map.get(key);
			listLock.lock();
			try{
				value.removeSelfFromList();
				circDoubleList.insertToHead(value);
			}finally{
				listLock.unlock();
			}
			return value.value;
		}
		
		private void removeLast(){
			ListNode<K, V> node;
			node = circDoubleList.removeTail();
			map.remove(node.key);
		}
		
		public void put(K key, V value){
			ListNode<K, V> node = new ListNode<>(key, value);
			listLock.lock();
			try{
				map.put(key, node);
				if(currentSize == maxSize){
					removeLast();
				}else{
					currentSize++;
				}
				circDoubleList.insertToHead(node);
			}finally{
				listLock.lock();
			}
		}

		public void clean(V value) {
			// TODO Auto-generated method stub
			
		}
}
