package association;

import java.util.*;

/**
 * An implementation of Pairing that uses hash-maps
 * @author Samuel Kolb
 */
public class HashPairing<K, V> implements Pairing<K, V> {

	//region Variables
	private final Map<K, V> keyToValue;

	private final Map<V, K> valueToKey;

	private final boolean useKeyIdentity;

	@Override
	public boolean usesKeyIdentity() {
		return useKeyIdentity;
	}

	private final boolean useValueIdentity;

	@Override
	public boolean usesValueIdentity() {
		return useValueIdentity;
	}
	//endregion

	//region Construction

	/**
	 * Creates a copy of the given pairing
	 * @param pairing	The pairing to copy
	 */
	public HashPairing(Pairing<K, V> pairing) {
		this(pairing.usesKeyIdentity(), pairing.usesValueIdentity());
		for(K key : pairing.getKeys())
			associate(key, pairing.getValue(key));
	}

	/**
	 * @effect	| this(true, tue)
	 */
	public HashPairing() {
		this(true, true);
	}

	/**
	 * Constructs a new empty association.
	 * Object identity should be enabled if there might be distinct objects who are equal w.r.t. hash-code and equals
	 * @param useKeyIdentity	If true object identity will be used for hashing keys, else standard hash-code
	 * @param useValueIdentity	If true object identity will be used for hashing values, else standard hash-code
	 */
	public HashPairing(boolean useKeyIdentity, boolean useValueIdentity) {
		this.useKeyIdentity = useKeyIdentity;
		this.useValueIdentity = useValueIdentity;
		keyToValue = useKeyIdentity ? new IdentityHashMap<K, V>() : new HashMap<K, V>();
		valueToKey = useValueIdentity ? new IdentityHashMap<V, K>() : new HashMap<V, K>();
	}
	//endregion

	//region Public methods

	@Override
	public K getKey(V value) {
		return valueToKey.get(value);
	}

	@Override
	public V getValue(K key) {
		return keyToValue.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		try {
			return getValue((K)key);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public V removeKey(K key) {
		V value = keyToValue.remove(key);
		valueToKey.remove(value);
		return value;
	}

	@Override
	public K removeValue(V value) {
		K key = valueToKey.remove(value);
		keyToValue.remove(key);
		return key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		try {
			return removeKey((K)key);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		}
	}

	public boolean remove(Object key, Object value) {
		return false;
	}

	@Override
	public void associate(K key, V value) {
		removeKey(key);
		keyToValue.put(key, value);
		removeValue(value);
		valueToKey.put(value, key);
	}

	@Override
	public boolean containsKey(Object key) {
		return keyToValue.containsKey(key);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public boolean containsValue(Object value) {
		return valueToKey.containsKey(value);
	}

	@Override
	public Set<K> getKeys() {
		return keyToValue.keySet();
	}

	@Override
	public Set<K> keySet() {
		return getKeys();
	}

	@Override
	public int getKeyCount() {
		return keyToValue.size();
	}

	@Override
	public int size() {
		return getKeyCount();
	}

	@Override
	public boolean isEmpty() {
		return getKeyCount() == 0;
	}

	@Override
	public Set<V> getValues() {
		return valueToKey.keySet();
	}

	@Override
	public Collection<V> values() {
		return getValues();
	}

	@Override
	public int getValueCount() {
		return valueToKey.size();
	}

	@Override
	public void clear() {
		keyToValue.clear();
		valueToKey.clear();
	}

	@Override
	public V put(K key, V value) {
		V oldValue = keyToValue.get(key);
		associate(key, value);
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for(K key : map.keySet())
			put(key, map.get(key));
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return keyToValue.entrySet();
	}

	//endregion
}
