package association;

import java.util.Map;

/**
 * A pairing is a one-to-one association
 * @author Samuel Kolb
 */
public interface Pairing<K, V> extends Relation<K, V>, Map<K, V> {

	/**
	 * Returns the key associated with the given value
	 * @param value	The value
	 * @return	The key previously associated with the given value
	 * 			| this.getValue(return) == value
	 */
	public K getKey(V value);

	/**
	 * Returns the value associated with the given key
	 * @param key	The key
	 * @return	The value previously associated with the given key
	 * 			| this.getKey(return) == key
	 */
	public V getValue(K key);

	/**
	 * Removes the association of the given key
	 * @param key	The key
	 * @return	The associated value or null if no associated key exists
	 * @post	| !new.containsKey(key)
	 * 			| !new.containsValue(old.getValue(key))
	 */
	public V removeKey(K key);

	/**
	 * Removes the association of the given value
	 * @param value	The value
	 * @return	The associated key or null if no associated key exists
	 * @post	| !new.containsValue(value)
	 * 			| !new.containsKey(old.getKey(value))
	 */
	public K removeValue(V value);
}
