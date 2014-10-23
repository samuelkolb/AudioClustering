package association;

import java.util.Collection;
import java.util.Set;

/**
 * Represents a generic relation (can be one-to-one, one-to-many, ...)
 * The first type is referred to as key, the second as value.
 * This allows for simpler formulation, but the relation is effectively two-ways.
 * @author Samuel Kolb
 */
public interface Relation<K, V> {

	/**
	 * Returns whether this relation uses object identity to differentiate between keys
	 * @return	True iff object identity is used for hash-code and equality for keys
	 */
	public boolean usesKeyIdentity();

	/**
	 * Returns whether this relation uses object identity to differentiate between values
	 * @return	True iff object identity is used for hash-code and equality for values
	 */
	public boolean usesValueIdentity();

	/**
	 * Associates the given value and key
	 * @param key	The key
	 * @param value	The value
	 * @post	| new.getValues().contains(value)
	 * 			| new.getKey(value) == key
	 */
	public void associate(K key, V value);

	/**
	 * Returns whether the given key has been associated with a value
	 * @param key	The key
	 * @return	True iff the key has previously been associated with a value
	 */
	public boolean containsKey(K key);

	/**
	 * Returns whether the given value has been associated with a key
	 * @param value	The value
	 * @return	True iff the value has previously been associated with a key
	 */
	public boolean containsValue(V value);

	/**
	 * Returns all keys that have been associated with a value
	 * @return	A set of keys
	 */
	public Set<K> getKeys();

	/**
	 * Returns the number of keys that have been associated with a values
	 * @return	An integer indicating the number of keys
	 */
	public int getKeyCount();

	/**
	 * Returns all values that have been associated with a key
	 * @return	A set of values
	 */
	public Collection<V> getValues();

	/**
	 * Returns the number of values that have been associated with a key
	 * @return	An integer indicating the number of values
	 */
	public int getValueCount();
}
